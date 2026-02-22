# Customer Batch Service

고객 도메인 배치 작업 모듈

## 기능

### 1. 휴면 회원 전환 배치 (DormantCustomerJob)
- **목적**: 90일 이상 로그인하지 않은 활성 회원을 휴면 회원으로 전환
- **실행 주기**: 매일 새벽 2시 (cron: `0 0 2 * * ?`)
- **처리 방식**: Chunk-oriented processing (chunk size: 10)

## 아키텍처 설계

### 1. 코드 중복 해결 전략
```
현재: 모델 복제 (독립성 우선)
향후: customer-core 모듈 분리 및 공유
```

### 2. DB 접근 전략
```
현재 단계: JPA 직접 접근 (Read/Write)
  ├─ Read: RepositoryItemReader (JPA)
  └─ Write: Repository.saveAll()

중기 단계: Read-Only Repository + API Write
  ├─ Read: JPA @Transactional(readOnly=true)
  └─ Write: REST API 호출 (CustomerServiceClient)

성숙 단계: CQRS 패턴
  ├─ Read: Batch 전용 Read Replica DB
  └─ Write: REST API or Event 발행
```

## 실행 방법

### Gradle로 실행
```bash
cd StudySpringBatch/customer-batch
../../gradlew.bat bootRun
```

### 수동 배치 실행 (테스트)
```bash
curl -X POST http://localhost:8080/batch/dormant-customer
```

## 설정

### application.yml
- `spring.batch.job.enabled=false`: 자동 실행 방지 (스케줄러로 제어)
- `customer.service.url`: Customer Service API URL
- H2 콘솔: http://localhost:8080/h2-console

## 테스트 데이터

초기화시 자동으로 4명의 테스트 고객 생성:
- Alice: 활성 (최근 로그인)
- Bob: 활성 → 휴면 전환 대상 (100일 전)
- Charlie: 활성 → 휴면 전환 대상 (95일 전)
- David: 이미 휴면

## 확장 계획

1. **다른 배치 Job 추가**
   - VIP 등급 갱신
   - 마케팅 동의 만료 처리

2. **알림 연동**
   - 휴면 전환시 Notification Service API 호출

3. **공유 모듈 분리**
   - customer-core 모듈 생성
   - 도메인 모델 공유

4. **DB 접근 전략 고도화**
   - Read Replica 분리
   - CQRS 패턴 적용
