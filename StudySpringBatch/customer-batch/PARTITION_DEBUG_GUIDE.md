# Spring Batch Partition 병렬 처리 디버깅 가이드

이 문서는 Spring Batch의 **Partitioner를 이용한 병렬 처리** 동작을 상세히 추적하고 이해하기 위한 가이드입니다.

## 📋 목차
1. [Partition Job 개요](#partition-job-개요)
2. [Partitioner 설명](#partitioner-설명)
3. [병렬 처리 아키텍처](#병렬-처리-아키텍처)
4. [Partition 시나리오](#partition-시나리오)
5. [예상 실행 흐름](#예상-실행-흐름)
6. [디버깅 팁](#디버깅-팁)

---

## 🎯 Partition Job 개요

총 3개의 Partition 디버그 Job을 제공합니다:

### 1. **debugJpaPartitionJob** (JPA 기반, Tier 분할)
- **Partitioner**: `CustomerTierPartitioner`
- **분할 방식**: 고객 등급별 (BRONZE, SILVER, GOLD, VIP)
- **Grid Size**: 4 (4개 파티션)
- **Chunk Size**: 5
- **Thread Pool**: SimpleAsyncTaskExecutor (partition-*)

**파티션별 에러 시나리오**:
| Partition | Tier | 에러 시나리오 | 설명 |
|-----------|------|---------------|------|
| partition_BRONZE | BRONZE | `han.yujin` → Retry → Success | 1회 실패 후 재시도 성공 |
| partition_SILVER | SILVER | `kang.haneul` → Skip | 즉시 Skip |
| partition_GOLD | GOLD | `park.jiwoo` → Filter | null 반환 (필터링) |
| partition_VIP | VIP | 정상 처리 | 에러 없음 |

### 2. **debugMyBatisPartitionJob** (MyBatis 기반, Tier 분할)
- **Partitioner**: `CustomerTierPartitioner`
- **분할 방식**: 고객 등급별
- **Grid Size**: 4
- **Thread Pool**: SimpleAsyncTaskExecutor (mybatis-partition-*)

**파티션별 에러 시나리오**:
| Partition | Tier | 위치 | 에러 시나리오 |
|-----------|------|------|---------------|
| partition_BRONZE | BRONZE | Processor | `lim.jaehyun` → Retry(2회) → Success |
| partition_SILVER | SILVER | Processor | `jung.dohyun` → Retry(3회) → Skip |
| partition_GOLD | GOLD | Writer | `choi.yejin` → Retry → Success |
| partition_VIP | VIP | Writer | `kim.minji` → Retry(3회) → Skip |

### 3. **debugJdbcPartitionJob** (JDBC 기반, Range 분할)
- **Partitioner**: `CustomerRangePartitioner`
- **분할 방식**: 데이터 범위별 (offset/limit)
- **Grid Size**: 4
- **Thread Pool**: SimpleAsyncTaskExecutor (jdbc-partition-*)

**파티션별 에러 시나리오**:
| Partition | Range | 에러 시나리오 |
|-----------|-------|---------------|
| partition_0 | offset:0, limit:~12 | `ahn.taeyang` → Processor Retry → Success |
| partition_1 | offset:~12, limit:~12 | `go.ara` → Processor Retry → Skip |
| partition_2 | offset:~24, limit:~12 | `nam.joohyuk` → Processor Skip |
| partition_3 | offset:~36, limit:~ | `lee.sungkyung` → Processor Filter |

---

## 🔀 Partitioner 설명

### 1. **CustomerTierPartitioner** (Tier 기반)
고객 등급별로 파티션을 생성합니다.

```java
// 파티션 생성 로직
Map<String, ExecutionContext> partitions = new HashMap<>();
String[] tiers = {"BRONZE", "SILVER", "GOLD", "VIP"};

for (String tier : tiers) {
    ExecutionContext context = new ExecutionContext();
    context.putString("tier", tier);
    context.putString("partitionName", "partition_" + tier);
    partitions.put("partition_" + tier, context);
}
```

**장점**:
- 각 등급별 고객 수가 비슷하면 균등한 부하 분산
- 비즈니스 로직상 의미 있는 분할

**단점**:
- 특정 등급에 데이터가 몰려있으면 불균등한 처리

### 2. **CustomerRangePartitioner** (Range 기반)
전체 데이터를 균등하게 범위로 분할합니다.

```java
// 전체 고객 수 조회
Long totalCustomers = jdbcTemplate.queryForObject(
    "SELECT COUNT(*) FROM customers WHERE status = 'ACTIVE'",
    Long.class
);

// 파티션 크기 계산
int partitionSize = (int) Math.ceil((double) totalCustomers / gridSize);

// 각 파티션에 offset/limit 설정
for (int i = 0; i < gridSize; i++) {
    ExecutionContext context = new ExecutionContext();
    context.putInt("offset", i * partitionSize);
    context.putInt("limit", partitionSize);
    partitions.put("partition_" + i, context);
}
```

**장점**:
- 데이터가 균등하게 분배됨
- 처리 시간이 예측 가능

**단점**:
- 비즈니스 로직과 무관한 분할
- 데이터 변경 시 재계산 필요

---

## 🏗️ 병렬 처리 아키텍처

### Manager-Worker 패턴

```
┌─────────────────────────────────────────────────────────────┐
│                      Manager Step                           │
│  - Partitioner를 실행하여 ExecutionContext 생성             │
│  - TaskExecutorPartitionHandler에 Worker Step 실행 위임     │
└─────────────────────────────────────────────────────────────┘
                            │
                            ├──────────────┬──────────────┬──────────────┐
                            ▼              ▼              ▼              ▼
                     ┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐
                     │ Worker 1 │   │ Worker 2 │   │ Worker 3 │   │ Worker 4 │
                     │ Thread-1 │   │ Thread-2 │   │ Thread-3 │   │ Thread-4 │
                     │          │   │          │   │          │   │          │
                     │ Read     │   │ Read     │   │ Read     │   │ Read     │
                     │ Process  │   │ Process  │   │ Process  │   │ Process  │
                     │ Write    │   │ Write    │   │ Write    │   │ Write    │
                     └──────────┘   └──────────┘   └──────────┘   └──────────┘
                            │              │              │              │
                            └──────────────┴──────────────┴──────────────┘
                                           ▼
                            ┌──────────────────────────────┐
                            │    Manager Step 완료         │
                            │  모든 Worker 결과 집계       │
                            └──────────────────────────────┘
```

### Step 구성

1. **Manager Step** (`debugJpaPartitionManagerStep`)
   - Partitioner 실행
   - ExecutionContext 생성 (tier, partitionName 등)
   - PartitionHandler에 Worker 실행 위임

2. **Worker Step** (`debugJpaPartitionWorkerStep`)
   - 실제 데이터 처리 (Read → Process → Write)
   - `@StepScope`로 각 파티션별 독립적인 Bean 생성
   - 각 파티션은 별도 스레드에서 실행

3. **TaskExecutorPartitionHandler**
   - 병렬 실행 관리
   - SimpleAsyncTaskExecutor로 스레드 풀 관리
   - Grid Size만큼 동시 실행

---

## 🎬 Partition 시나리오

### 시나리오 1: JPA Partition Job (Tier 분할)

**실행 흐름**:
```
Manager Step 시작
  ↓
🔀 PARTITIONER: Starting partitioning (gridSize=4)
🔀 PARTITIONER: Created partition 'partition_BRONZE' for tier 'BRONZE'
🔀 PARTITIONER: Created partition 'partition_SILVER' for tier 'SILVER'
🔀 PARTITIONER: Created partition 'partition_GOLD' for tier 'GOLD'
🔀 PARTITIONER: Created partition 'partition_VIP' for tier 'VIP'
🔀 PARTITIONER: Total 4 partitions created
  ↓
4개 Worker Step 병렬 실행 (각각 별도 스레드)
  ↓
╔════════════════════════════════════════════════════════════════╗
║ [debugJpaPartitionJob] 🎯 PARTITION START: partition_BRONZE   ║
║ Thread: partition-1                                            ║
║ Partition #: 0                                                 ║
║ Tier: BRONZE                                                   ║
╚════════════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════════════╗
║ [debugJpaPartitionJob] 🎯 PARTITION START: partition_SILVER   ║
║ Thread: partition-2                                            ║
║ Partition #: 1                                                 ║
║ Tier: SILVER                                                   ║
╚════════════════════════════════════════════════════════════════╝

... (GOLD, VIP 파티션도 동시 시작)

각 파티션별로 Chunk 처리
  ↓
━━━━━━━━━ CHUNK START (partition_BRONZE) ━━━━━━━━━
📖 Reader: Read BRONZE customers
⚙️  Processor: Process each customer
  - han.yujin → TemporaryException
  🔄 Retry: Attempt #1 failed
  ⚙️  Process again → Success!
💾 Writer: Write processed customers
━━━━━━━━━ CHUNK END ━━━━━━━━━

╔════════════════════════════════════════════════════════════════╗
║ [debugJpaPartitionJob] ✅ PARTITION END: partition_BRONZE     ║
║ Thread: partition-1                                            ║
║ Status: COMPLETED                                              ║
║ Read: 10, Write: 9, Skip: 1                                   ║
║ Duration: 1523ms                                               ║
╚════════════════════════════════════════════════════════════════╝

... (모든 파티션 완료)
  ↓
Manager Step 완료 (모든 Worker 결과 집계)
```

### 시나리오 2: MyBatis Partition Job (Writer 에러)

**Writer 에러 발생 시 병렬 처리**:
```
partition_VIP (Thread: mybatis-partition-4)
  ↓
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Reader: Read 5 VIP customers
⚙️  Processor: Process 5 customers (정상)
💾 Writer: Writing 5 customers
  - kim.minji → RetryableCustomerException
  🔄 Retry: Attempt #1 failed
🔴 CHUNK ERROR - Rolling back
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

━━━━━━━━━ CHUNK START (재시도 #1) ━━━━━━━━━
📖 Reader: Read 5 VIP customers (다시 읽기)
⚙️  Processor: Process 5 customers (다시 처리)
💾 Writer: Writing 5 customers
  - kim.minji → RetryableCustomerException
  🔄 Retry: Attempt #2 failed
🔴 CHUNK ERROR - Rolling back
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

━━━━━━━━━ CHUNK START (재시도 #2) ━━━━━━━━━
📖 Reader: Read 5 VIP customers (다시 읽기)
⚙️  Processor: Process 5 customers (다시 처리)
💾 Writer: Writing 5 customers
  - kim.minji → RetryableCustomerException
  🔄 Retry: Attempt #3 failed
🔴 CHUNK ERROR - Rolling back
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

━━━━━━━━━ Item-by-Item 모드 ━━━━━━━━━
💾 Write customer #1 → Success
💾 Write customer #2 (kim.minji) → Error → ⚠️  SKIP
💾 Write customer #3 → Success
💾 Write customer #4 → Success
💾 Write customer #5 → Success
━━━━━━━━━ CHUNK END ━━━━━━━━━
```

**중요**: 다른 파티션(BRONZE, SILVER, GOLD)은 VIP 파티션의 에러와 무관하게 계속 처리됩니다!

### 시나리오 3: JDBC Partition Job (Range 분할)

**균등 분할 예시** (총 48명의 고객):
```
🔀 PARTITIONER: Total ACTIVE customers: 48
🔀 PARTITIONER: Partition size: 12 customers per partition

🔀 PARTITIONER: Created partition 'partition_0' - offset: 0, limit: 12
🔀 PARTITIONER: Created partition 'partition_1' - offset: 12, limit: 12
🔀 PARTITIONER: Created partition 'partition_2' - offset: 24, limit: 12
🔀 PARTITIONER: Created partition 'partition_3' - offset: 36, limit: 12

각 파티션이 동시에 실행:
┌─────────────┬─────────────┬─────────────┬─────────────┐
│ Partition 0 │ Partition 1 │ Partition 2 │ Partition 3 │
│ offset:0    │ offset:12   │ offset:24   │ offset:36   │
│ limit:12    │ limit:12    │ limit:12    │ limit:12    │
│             │             │             │             │
│ Customer    │ Customer    │ Customer    │ Customer    │
│ 1-12        │ 13-24       │ 25-36       │ 37-48       │
└─────────────┴─────────────┴─────────────┴─────────────┘
```

---

## 🔍 예상 실행 흐름

### 병렬 실행 타임라인

```
Time →
0ms    Manager Step 시작
       Partitioner 실행 (파티션 4개 생성)

100ms  ┌─────────────┬─────────────┬─────────────┬─────────────┐
       │ Thread-1    │ Thread-2    │ Thread-3    │ Thread-4    │
       │ BRONZE      │ SILVER      │ GOLD        │ VIP         │
       ├─────────────┼─────────────┼─────────────┼─────────────┤
       │ 🎯 START    │ 🎯 START    │ 🎯 START    │ 🎯 START    │
       │ 🔵 CHUNK    │ 🔵 CHUNK    │ 🔵 CHUNK    │ 🔵 CHUNK    │

500ms  │ 📖 Read     │ 📖 Read     │ 📖 Read     │ 📖 Read     │
       │ ⚙️  Process │ ⚙️  Process │ ⚙️  Process │ ⚙️  Process │
       │ 💾 Write    │ 💾 Write    │ 💾 Write    │ 💾 Write    │
       │ 🟢 DONE     │ 🟢 DONE     │ 🟢 DONE     │ ❌ ERROR    │

1000ms │             │             │             │ 🔄 Retry    │
       │ 🔵 CHUNK    │ 🔵 CHUNK    │ 🔵 CHUNK    │ 🔵 CHUNK    │
       │ ...         │ ...         │ ...         │ 📖 Re-read  │

1500ms │ 🟢 DONE     │ 🟢 DONE     │ 🟢 DONE     │ ⚙️  Re-proc │
       │             │             │             │ 💾 Re-write │
       │ ✅ END      │ ✅ END      │ ✅ END      │ 🟢 DONE     │

2000ms │             │             │             │ ✅ END      │
       └─────────────┴─────────────┴─────────────┴─────────────┘

2100ms Manager Step 완료 (모든 Worker 결과 집계)
```

### 순차 처리 vs 병렬 처리 비교

**순차 처리** (partition 없음):
```
Total Time = BRONZE (500ms) + SILVER (500ms) + GOLD (500ms) + VIP (1500ms)
           = 3000ms
```

**병렬 처리** (4 partitions):
```
Total Time = MAX(BRONZE:500ms, SILVER:500ms, GOLD:500ms, VIP:1500ms)
           = 1500ms (약 50% 성능 향상!)
```

---

## 🚀 실행 방법

```bash
# 1. MySQL 시작
cd StudySpringBatch/customer-batch
docker-compose up -d

# 2. 애플리케이션 실행
./gradlew bootRun

# 3. Partition Job 실행
curl -X POST http://localhost:8080/batch/run/debugJpaPartitionJob
curl -X POST http://localhost:8080/batch/run/debugMyBatisPartitionJob
curl -X POST http://localhost:8080/batch/run/debugJdbcPartitionJob
```

---

## 🧪 디버깅 팁

### 1. Thread 추적
로그에서 Thread 이름을 확인하여 어떤 파티션에서 실행되는지 추적:
```
[PROCESSOR LOGIC][partition_BRONZE][Thread:partition-1] Processing: ...
[PROCESSOR LOGIC][partition_SILVER][Thread:partition-2] Processing: ...
```

### 2. 파티션별 성능 측정
`DetailedPartitionListener`가 각 파티션의 처리 시간을 출력:
```
║ Duration: 1523ms
```

### 3. 동시성 문제 확인
`ConcurrentHashMap`을 사용하여 Thread-safe하게 재시도 횟수 추적:
```java
private final Map<String, Integer> retryAttempts = new ConcurrentHashMap<>();
String key = partitionName + "_" + email; // 파티션별로 구분
```

### 4. Grid Size 조정
```java
private static final int GRID_SIZE = 4; // 파티션 개수 조정
```
- Grid Size ↑ → 더 많은 병렬 처리, 더 많은 스레드
- Grid Size ↓ → 적은 병렬 처리, 적은 오버헤드

### 5. 브레이크포인트 추천
- `CustomerTierPartitioner.partition()` - 파티션 생성 시점
- `DetailedPartitionListener.beforeStep()` - 각 파티션 시작
- `DetailedPartitionListener.afterStep()` - 각 파티션 완료
- Processor/Writer - 병렬 실행 중인 로직

### 6. 로그 필터링
```bash
# 특정 파티션만 보기
grep "partition_BRONZE" application.log

# 특정 스레드만 보기
grep "Thread:partition-1" application.log

# Partitioner 동작만 보기
grep "🔀 PARTITIONER" application.log

# 파티션 시작/종료만 보기
grep "PARTITION START\|PARTITION END" application.log
```

---

## 📚 학습 포인트

### 1. Partition의 이점
- **성능 향상**: 데이터를 분할하여 병렬 처리
- **확장성**: 파티션 수 증가로 처리량 증가
- **독립성**: 각 파티션은 독립적으로 실행 (한 파티션 실패 ≠ 전체 실패)

### 2. Partition 전략
- **Tier 기반**: 비즈니스 로직에 맞는 분할 (등급, 지역, 카테고리 등)
- **Range 기반**: 균등한 데이터 분할 (ID 범위, 날짜 범위 등)
- **Hash 기반**: ID를 해시하여 분할 (추가 구현 필요)

### 3. Thread Safety
- `@StepScope`: 각 파티션마다 독립적인 Bean 생성
- `ConcurrentHashMap`: Thread-safe한 데이터 공유
- 각 파티션은 별도의 트랜잭션

### 4. 성능 고려사항
- Grid Size = 물리적 CPU 코어 수가 적절
- 파티션당 데이터가 너무 적으면 오버헤드 증가
- DB 연결 풀 크기 ≥ Grid Size

### 5. 에러 처리
- 한 파티션의 에러가 다른 파티션에 영향 없음
- 각 파티션은 독립적으로 Retry/Skip 정책 적용
- Manager Step은 모든 Worker 완료를 기다림

---

## ✅ 테스트 체크리스트

- [ ] 파티션이 정확히 생성됨 (로그 확인)
- [ ] 각 파티션이 별도 스레드에서 실행됨 (Thread 이름 확인)
- [ ] 파티션별로 독립적으로 데이터 처리됨
- [ ] 한 파티션의 에러가 다른 파티션에 영향 없음
- [ ] Retry/Skip이 각 파티션에서 독립적으로 동작
- [ ] Writer 에러 시 Chunk 롤백 후 재시도
- [ ] 모든 파티션 완료 후 Job 종료
- [ ] 처리 시간이 순차 처리보다 빠름

---

## 🎓 핵심 개념 정리

| 개념 | 설명 | 예시 |
|------|------|------|
| **Partitioner** | ExecutionContext 생성하여 데이터 분할 | Tier별, Range별 |
| **Manager Step** | 파티션 생성 및 Worker 실행 관리 | debugJpaPartitionManagerStep |
| **Worker Step** | 실제 데이터 처리 (R→P→W) | debugJpaPartitionWorkerStep |
| **Grid Size** | 파티션 개수 (병렬 처리 수) | 4 |
| **@StepScope** | 파티션별 독립 Bean 생성 | Reader, Processor, Writer |
| **TaskExecutor** | 병렬 실행을 위한 스레드 관리 | SimpleAsyncTaskExecutor |
| **Thread Safety** | 동시성 제어 | ConcurrentHashMap |

---

이 가이드를 통해 Spring Batch의 Partition 병렬 처리를 완벽하게 이해할 수 있습니다! 🚀

## 💡 추가 실험

### Grid Size 변경 실험
```java
private static final int GRID_SIZE = 2; // → 2개 파티션으로 변경
private static final int GRID_SIZE = 8; // → 8개 파티션으로 변경
```

성능 차이를 확인해보세요!

### 커스텀 Partitioner 작성
날짜 범위, 지역별, 상태별 등 다양한 기준으로 파티션을 만들어보세요.
