# Customer Batch Architecture (DDD 패턴 적용)

## 아키텍처 개요

```
┌──────────────────────────────────────────────────────────┐
│                  Customer Batch                          │
│                                                          │
│  ┌─────────────────────────────────────────────────┐     │
│  │ Application Layer (Batch Jobs)                  │     │
│  │ - DormantCustomerJobConfig                      │     │
│  │ - MarketingConsentExpirationJobConfig           │     │
│  │ - VipTierUpdateJobConfig                        │     │
│  └─────────┬──────────────────────┬────────────────┘     │
│            │                      │                      │
│  ┌─────────▼──────────┐  ┌────────▼──────────┐           │
│  │ Domain Service     │  │ External Clients  │           │
│  │ (customer-core)    │  │ - OrderService    │           │
│  │ -CustomerTierSvc   │  │   Client          │           │
│  └─────────┬──────────┘  └────────┬──────────┘           │
│            │                      │                      │
│  ┌─────────▼──────────────────────▼───────────┐          │
│  │ Domain Model (customer-core)               │          │
│  │ - Customer, MarketingConsent               │          │
│  │ - CustomerTier (Value Object)              │          │
│  └─────────┬──────────────────────────────────┘          │
│            │                                             │
│  ┌─────────▼──────────────────────────────────┐          │
│  │ Infrastructure Layer                       │          │
│  │ - CustomerEntity, MarketingConsentEntity   │          │
│  │ - JPA Repositories                         │          │
│  └────────────────────────────────────────────┘          │
└──────────────────────────────────────────────────────────┘
```

## 핵심 설계 결정

### 1. 도메인 경계 분리

**Before (문제점)**
```java
@Entity
class Customer {
    private BigDecimal totalPurchaseAmount; // ❌ Order 도메인 침범

    public void updateTier() {
        if (totalPurchaseAmount >= ...) { // ❌ 외부 데이터 의존
            tier = GOLD;
        }
    }
}
```

**After (개선)**
```java
// customer-core
class Customer {
    // ✅ totalPurchaseAmount 제거

    public void changeTier(CustomerTier newTier) { // ✅ 단순 상태 변경
        this.tier = newTier;
    }
}

// Domain Service
class CustomerTierService {
    public CustomerTier calculateTier(UUID customerId) {
        // ✅ Order Service에서 구매금액 조회
        CustomerOrderStats stats = orderStatsProvider.getOrderStats(customerId);
        return CustomerTier.fromPurchaseAmount(stats.getTotalAmount());
    }
}
```

### 2. 계층 책임 분리

#### Application Layer (Batch Job)
- **책임**: 배치 실행 흐름 조율
- **역할**: Reader → Processor → Writer 구성

```java
@Bean
public ItemProcessor<CustomerEntity, CustomerEntity> processor() {
    return entity -> {
        Customer customer = entity.toDomain();
        CustomerTier newTier = tierService.calculateTier(customer.getId());
        customer.changeTier(newTier);
        entity.updateFromDomain(customer);
        return entity;
    };
}
```

#### Domain Service Layer
- **책임**: 여러 Aggregate 조율
- **역할**: Customer + Order 데이터 통합

```java
public class CustomerTierService {
    public CustomerTier calculateTier(UUID customerId) {
        CustomerOrderStats stats = orderStatsProvider.getOrderStats(customerId);
        return CustomerTier.fromPurchaseAmount(stats.getTotalAmount());
    }
}
```

#### Domain Model Layer
- **책임**: 비즈니스 규칙 캡슐화
- **역할**: 불변식 유지, 상태 관리

```java
public enum CustomerTier {
    BRONZE(0, 500000),
    SILVER(500000, 1000000),
    // ...

    public static CustomerTier fromPurchaseAmount(BigDecimal amount) {
        // 등급 결정 규칙 캡슐화
    }
}
```

#### Infrastructure Layer
- **책임**: 기술적 구현
- **역할**: JPA 매핑, DB 접근

```java
@Entity
class CustomerEntity {
    public Customer toDomain() { /* Entity → Domain */ }
    public void updateFromDomain(Customer c) { /* Domain → Entity */ }
}
```

### 3. Entity ↔ Domain Model 변환

**변환 이유**
- JPA Entity는 인프라 관심사 (영속성)
- Domain Model은 비즈니스 로직
- 계층 분리를 위해 명확한 변환 필요

**변환 패턴**
```java
// Read: Entity → Domain
Customer domain = entity.toDomain();

// Business Logic: Domain에서 처리
domain.changeTier(newTier);

// Write: Domain → Entity
entity.updateFromDomain(domain);
repository.save(entity);
```

## DB 접근 전략 진화

### 현재 단계: JPA 직접 접근
```
Batch → JPA Repository → H2 Database
```

### 중기 단계: API 호출 추가
```
Batch → Order Service API (구매금액 조회)
      → JPA Repository (상태 변경)
```

### 장기 단계: CQRS 패턴
```
Batch → Read Replica DB (조회)
      → Order Service API (외부 데이터)
      → Event Bus (상태 변경 발행)
```

## 테스트 전략

### Domain Model 테스트
```java
@Test
void 고객_등급_계산_테스트() {
    BigDecimal amount = new BigDecimal("1500000");
    CustomerTier tier = CustomerTier.fromPurchaseAmount(amount);
    assertEquals(CustomerTier.GOLD, tier);
}
```

### Domain Service 테스트
```java
@Test
void 등급_산정_서비스_테스트() {
    // Mock OrderStatsProvider
    when(provider.getOrderStats(any()))
        .thenReturn(new CustomerOrderStats(id, amount, count));

    CustomerTier tier = tierService.calculateTier(customerId);
    assertEquals(CustomerTier.PLATINUM, tier);
}
```

### Batch Job 통합 테스트
```java
@Test
void VIP등급갱신_배치_테스트() {
    JobExecution execution = jobLauncher.run(vipTierUpdateJob, params);
    assertEquals(BatchStatus.COMPLETED, execution.getStatus());
}
```

## 확장 포인트

1. **Order Service 연동**: OrderServiceClient 구현 완성
2. **알림 발송**: NotificationService 연동
3. **이벤트 발행**: 등급 변경시 Domain Event 발행
4. **Read Replica**: 조회 전용 DB 분리
5. **캐싱**: 고객 정보 캐시 레이어 추가
