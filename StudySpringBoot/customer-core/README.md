# Customer Core Module

고객 도메인의 핵심 비즈니스 로직을 담은 공유 모듈

## 목적

Customer Service와 Customer Batch가 도메인 모델과 비즈니스 로직을 공유하기 위한 모듈입니다.

## 포함 내용

### 1. Domain Model
- `Customer`: 고객 Aggregate Root
- `MarketingConsent`: 마케팅 동의 Entity

### 2. Value Object
- `CustomerTier`: 고객 등급 및 승급 규칙
- `CustomerStatus`: 고객 상태 (Enum)
- `MarketingChannel`: 마케팅 채널 (Enum)

### 3. Domain Service
- `CustomerTierService`: 고객 등급 산정 로직 (여러 도메인 조율)

### 4. DTO
- `CustomerOrderStats`: Order Service로부터 받을 데이터 구조

## DDD 설계 원칙

### 도메인 분리
```
Customer 도메인: 고객 정보, 상태, 등급
Order 도메인: 구매 이력, 구매 금액
```

**구매 금액(totalPurchaseAmount)은 Order 도메인의 책임**
- Customer 도메인에서 제거
- Order Service API를 통해 조회

### 계층 분리

```
┌─────────────────────────────────────┐
│ Application Layer                   │
│ - Batch Job, REST Controller        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│ Domain Service (customer-core)      │
│ - CustomerTierService               │
│ - 여러 도메인 데이터 조율             │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│ Domain Model (customer-core)        │
│ - Customer Entity                   │
│ - CustomerTier Value Object         │
│ - 비즈니스 규칙 캡슐화                │
└─────────────────────────────────────┘
               │
┌──────────────▼──────────────────────┐
│ Infrastructure Layer                │
│ - JPA Entity (CustomerEntity)       │
│ - Repository                        │
│ - External API Client               │
└─────────────────────────────────────┘
```

## 사용 예시

### Domain Model 사용
```java
// 도메인 모델의 비즈니스 로직 활용
Customer customer = entity.toDomain();
customer.convertToDormant();  // 도메인 규칙 적용
entity.updateFromDomain(customer);
```

### Value Object 사용
```java
// 등급 규칙 적용
BigDecimal purchaseAmount = new BigDecimal("1500000");
CustomerTier tier = CustomerTier.fromPurchaseAmount(purchaseAmount);
// tier = GOLD
```

### Domain Service 사용
```java
// 여러 도메인 조율
CustomerTier tier = tierService.calculateTier(customerId);
// Order Service에서 구매금액 조회 후 등급 계산
```

## 의존성

- 최소한의 의존성만 포함
- Spring Framework (Domain Service용, Optional)
- JPA/Hibernate 의존성 없음 (순수 도메인)

## 빌드

```bash
cd StudySpringBoot/customer-core
../../gradlew.bat build
```

## 사용하는 모듈

- `customer` (Customer Service)
- `customer-batch` (Customer Batch)
