# Spring Batch Retry 디버깅 가이드

이 문서는 Spring Batch의 **Retry(재시도)** 동작을 상세히 추적하고 이해하기 위한 가이드입니다.

## 📋 목차
1. [Retry Job 개요](#retry-job-개요)
2. [Retry vs Skip 차이점](#retry-vs-skip-차이점)
3. [예외 클래스 설명](#예외-클래스-설명)
4. [Retry 시나리오](#retry-시나리오)
5. [예상 실행 흐름](#예상-실행-흐름)
6. [디버깅 팁](#디버깅-팁)

---

## 🎯 Retry Job 개요

총 3개의 Retry 디버그 Job을 제공합니다:

### 1. **debugJpaRetryJob** (JPA 기반)
- **Chunk Size**: 5
- **Retry Limit**: 3
- **Skip Limit**: 10
- **Retry 대상 예외**: `TemporaryException`, `RetryableCustomerException`
- **Skip 대상 예외**: `SkippableCustomerException`, `RetryableCustomerException` (재시도 후)

**에러 시나리오**:
| 이메일 | 위치 | 동작 | 설명 |
|--------|------|------|------|
| `kim.minji` | Processor | Retry → Success | 1회 실패 후 재시도 성공 |
| `lee.seojun` | Processor | Retry → Skip | 3회 재시도 후 Skip |
| `choi.yejin` | Processor | Skip (No Retry) | 즉시 Skip |
| `jung.dohyun` | Processor | Filter | null 반환 (필터링) |
| `park.jiwoo` | Processor | Retry → Fail | 재시도 후 Job 실패 (주석 처리됨) |

### 2. **debugMyBatisRetryJob** (MyBatis 기반)
- **Chunk Size**: 5
- **Retry Limit**: 3
- **Skip Limit**: 10

**에러 시나리오**:
| 이메일 | 위치 | 동작 | 설명 |
|--------|------|------|------|
| `oh.minseok` | Processor | Retry → Success | 2회 실패 후 3번째 성공 |
| `shin.daeun` | Processor | Retry → Skip | 3회 재시도 후 Skip |
| `baek.joonho` | Writer | Retry → Success | 1회 실패 후 재시도 성공 |
| `kwon.nayeon` | Writer | Retry → Skip | 3회 재시도 후 Skip |

### 3. **debugJdbcRetryJob** (JDBC 기반)
- **Chunk Size**: 5
- **Retry Limit**: 3
- **Skip Limit**: 10

**에러 시나리오**:
| 이메일 | 위치 | 동작 | 설명 |
|--------|------|------|------|
| `kang.haneul` | Processor | Retry → Success | 1회 실패 후 재시도 성공 |
| `yoon.soobin` | Processor | Retry → Skip | 3회 재시도 후 Skip |
| `lim.jaehyun` | Processor | Skip (No Retry) | 즉시 Skip |
| `han.yujin` | Processor | Filter | null 반환 (필터링) |
| `song.eunchae` | Writer | Retry → Success | 1회 실패 후 재시도 성공 |
| `seo.hyunwoo` | Writer | Retry → Skip | 3회 재시도 후 Skip |

---

## 🔄 Retry vs Skip 차이점

### Retry (재시도)
- **목적**: 일시적인 오류를 극복하기 위해 같은 작업을 다시 시도
- **대상**: 네트워크 타임아웃, DB 락, 일시적인 리소스 부족 등
- **동작**: 설정된 재시도 횟수만큼 같은 아이템을 다시 처리
- **결과**: 재시도 성공 시 정상 처리, 모든 재시도 실패 시 Skip 또는 Job 실패

### Skip (건너뛰기)
- **목적**: 특정 아이템의 오류를 무시하고 다음 아이템으로 진행
- **대상**: 데이터 검증 실패, 비즈니스 룰 위반 등
- **동작**: 문제가 있는 아이템을 건너뛰고 계속 진행
- **결과**: 해당 아이템은 처리되지 않음

### 조합 사용
```java
.faultTolerant()
.retry(TemporaryException.class)       // 1. 먼저 재시도
.retryLimit(3)
.skip(TemporaryException.class)        // 2. 재시도 실패 시 Skip
.skipLimit(10)
```

**처리 순서**:
1. 예외 발생
2. Retry 대상인가? → Yes → 재시도 (최대 3회)
3. 재시도 성공? → Yes → 정상 처리
4. 재시도 실패? → Skip 대상인가? → Yes → Skip
5. Skip도 안되면? → Job 실패

---

## 📦 예외 클래스 설명

### 1. **TemporaryException**
- **용도**: 일시적인 오류 시뮬레이션
- **동작**: Retry 대상 (재시도 하면 성공 가능)
- **예시**: 첫 시도는 실패, 재시도 시 성공

### 2. **RetryableCustomerException**
- **용도**: 재시도 가능하지만 계속 실패하는 케이스
- **동작**: Retry 대상 + Skip 대상 (재시도 후 Skip)
- **예시**: 3회 재시도 후 모두 실패 → Skip

### 3. **SkippableCustomerException**
- **용도**: 즉시 건너뛰기
- **동작**: Retry 없이 바로 Skip
- **예시**: 데이터 검증 실패 등

### 4. **NonSkippableCustomerException**
- **용도**: Job을 실패시켜야 하는 치명적 오류
- **동작**: Retry도 Skip도 안됨 → Job 실패
- **예시**: 시스템 오류, 설정 오류 등

---

## 🎬 Retry 시나리오

### 시나리오 1: Processor에서 Retry 후 성공
**대상**: `kim.minji` (JPA), `oh.minseok` (MyBatis), `kang.haneul` (JDBC)

**동작**:
```
1회 시도 → TemporaryException 발생
  ↓
🔄 Retry: onError() - Attempt #1 failed
  ↓
2회 시도 → 성공!
  ↓
🔄 Retry: close() - Item processed successfully after 2 attempt(s)
```

**로그**:
```
⚙️  Processor: beforeProcess() - Processing customer: kim.minji@example.com
  [PROCESSOR LOGIC] Processing customer: kim.minji@example.com (Attempt #1)
  [PROCESSOR LOGIC] 'kim.minji' - First attempt, throwing TemporaryException
❌ Processor: onProcessError() - Error processing item
🔄 Retry: onError() - Attempt #1 failed with exception: TemporaryException
🔄 Retry: Will retry this item (attempt #1 -> #2)

⚙️  Processor: beforeProcess() - Processing customer: kim.minji@example.com
  [PROCESSOR LOGIC] Processing customer: kim.minji@example.com (Attempt #2)
  [PROCESSOR LOGIC] 'kim.minji' - Retry successful!
⚙️  Processor: afterProcess() - Item #1 processed successfully
🔄 Retry: close() - Item processed successfully after 2 attempt(s)
```

### 시나리오 2: Processor에서 Retry 후 Skip
**대상**: `lee.seojun` (JPA), `shin.daeun` (MyBatis), `yoon.soobin` (JDBC)

**동작**:
```
1회 시도 → RetryableCustomerException
  ↓
2회 시도 → RetryableCustomerException
  ↓
3회 시도 → RetryableCustomerException
  ↓
재시도 한도 초과 → Skip
  ↓
⚠️  SKIP in PROCESS
```

**로그**:
```
⚙️  Processor: beforeProcess() - Processing customer: lee.seojun@example.com
  [PROCESSOR LOGIC] Processing customer: lee.seojun@example.com (Attempt #1)
❌ Processor: onProcessError() - Error processing item
🔄 Retry: onError() - Attempt #1 failed with exception: RetryableCustomerException
🔄 Retry: Will retry this item (attempt #1 -> #2)

⚙️  Processor: beforeProcess() - Processing customer: lee.seojun@example.com
  [PROCESSOR LOGIC] Processing customer: lee.seojun@example.com (Attempt #2)
❌ Processor: onProcessError() - Error processing item
🔄 Retry: onError() - Attempt #2 failed with exception: RetryableCustomerException
🔄 Retry: Will retry this item (attempt #2 -> #3)

⚙️  Processor: beforeProcess() - Processing customer: lee.seojun@example.com
  [PROCESSOR LOGIC] Processing customer: lee.seojun@example.com (Attempt #3)
❌ Processor: onProcessError() - Error processing item
🔄 Retry: onError() - Attempt #3 failed with exception: RetryableCustomerException
🔄 Retry: Maximum retry attempts reached. Will skip or fail.
🔄 Retry: close() - Failed after 3 attempt(s). Final exception: RetryableCustomerException
⚠️  SKIP #1 in PROCESS - Item: lee.seojun@example.com - Exception: RetryableCustomerException
```

### 시나리오 3: Writer에서 Retry 후 성공
**대상**: `baek.joonho` (MyBatis), `song.eunchae` (JDBC)

**동작**:
```
Chunk 처리 → Writer 시작
  ↓
1회 쓰기 시도 → TemporaryException
  ↓
🔴 CHUNK ERROR - Rolling back
  ↓
━━━━━━━━━ CHUNK START (재시도) ━━━━━━━━━
  ↓
Reader 다시 읽기 → Processor 다시 처리 → Writer 다시 쓰기
  ↓
2회 쓰기 시도 → 성공!
```

**중요**: Writer 에러는 **전체 Chunk를 롤백**하고 처음부터 다시 시작합니다!

**로그**:
```
💾 Writer: beforeWrite() - About to write 5 items
  [WRITER LOGIC] Writing customer: baek.joonho@example.com (Attempt #1)
  [WRITER LOGIC] 'baek.joonho' - First attempt, throwing TemporaryException
❌ Writer: onWriteError() - Error writing items
🔄 Retry: onError() - Attempt #1 failed with exception: TemporaryException
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[debugMyBatisRetryJob] 🔵 CHUNK START (재시도)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

📖 Reader: afterRead() - Successfully read item #1: ...
... (모든 아이템 다시 읽기)

⚙️  Processor: beforeProcess() - Processing customer: ...
... (모든 아이템 다시 처리)

💾 Writer: beforeWrite() - About to write 5 items
  [WRITER LOGIC] Writing customer: baek.joonho@example.com (Attempt #2)
  [WRITER LOGIC] 'baek.joonho' - Retry successful on attempt #2!
💾 Writer: afterWrite() - Successfully wrote 5 items

━━━━━━━━━ CHUNK END - Committed successfully ━━━━━━━━━
```

### 시나리오 4: Writer에서 Retry 후 Skip
**대상**: `kwon.nayeon` (MyBatis), `seo.hyunwoo` (JDBC)

**동작**:
```
Chunk 처리 → Writer 계속 실패 (3회)
  ↓
Spring Batch가 자동으로 Item-by-Item 처리 모드로 전환
  ↓
각 아이템을 개별적으로 처리
  ↓
문제 있는 아이템만 Skip
```

**로그**:
```
💾 Writer: beforeWrite() - About to write 5 items
❌ Writer: onWriteError() - Error writing items
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (재시도 #1) ━━━━━━━━━
... (전체 다시 처리)
❌ Writer: onWriteError() - Error writing items
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (재시도 #2) ━━━━━━━━━
... (전체 다시 처리)
❌ Writer: onWriteError() - Error writing items
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (Item-by-Item 모드) ━━━━━━━━━
💾 Writer: beforeWrite() - About to write 1 item (아이템 #1)
💾 Writer: afterWrite() - Successfully wrote 1 item

💾 Writer: beforeWrite() - About to write 1 item (아이템 #2 - 문제 있는 아이템)
❌ Writer: onWriteError() - Error writing item
⚠️  SKIP #1 in WRITE - Item: kwon.nayeon@example.com

💾 Writer: beforeWrite() - About to write 1 item (아이템 #3)
💾 Writer: afterWrite() - Successfully wrote 1 item
...
```

### 시나리오 5: 즉시 Skip (Retry 없음)
**대상**: `choi.yejin` (JPA), `lim.jaehyun` (JDBC)

**동작**:
```
SkippableCustomerException 발생
  ↓
Retry 대상이 아님
  ↓
즉시 Skip
```

**로그**:
```
⚙️  Processor: beforeProcess() - Processing customer: choi.yejin@example.com
  [PROCESSOR LOGIC] 'choi.yejin' - Throwing SkippableCustomerException (no retry)
❌ Processor: onProcessError() - Error processing item
⚠️  SKIP #1 in PROCESS - Item: choi.yejin@example.com - Exception: SkippableCustomerException
```

### 시나리오 6: 필터링 (Retry/Skip 아님)
**대상**: `jung.dohyun` (JPA), `han.yujin` (JDBC)

**동작**:
```
Processor가 null 반환
  ↓
Writer에 전달되지 않음
  ↓
정상적인 필터링 (에러 아님)
```

---

## 🔍 예상 실행 흐름

### 정상 케이스 (재시도 없음)
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Read (5개)
⚙️ Process (5개 → 5개)
💾 Write (5개)
━━━━━━━━━ CHUNK END ━━━━━━━━━
```

### Processor Retry 성공 케이스
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Read (5개)

⚙️ Process item #1 → Error
🔄 Retry: Attempt #1 failed
⚙️ Process item #1 again → Success!
🔄 Retry: close() - successful after 2 attempts

⚙️ Process item #2~5 → Success

💾 Write (5개)
━━━━━━━━━ CHUNK END ━━━━━━━━━
```

### Processor Retry 후 Skip 케이스
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Read (5개)

⚙️ Process item #1 → Error
🔄 Retry: Attempt #1 failed
⚙️ Process item #1 again → Error
🔄 Retry: Attempt #2 failed
⚙️ Process item #1 again → Error
🔄 Retry: Attempt #3 failed
🔄 Retry: Maximum attempts reached
⚠️  SKIP #1 in PROCESS

⚙️ Process item #2~5 → Success

💾 Write (4개) ← 1개 Skip됨
━━━━━━━━━ CHUNK END ━━━━━━━━━
```

### Writer Retry 성공 케이스
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Read (5개)
⚙️ Process (5개)
💾 Write (5개) → Error
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (재시도) ━━━━━━━━━
📖 Read (5개) ← 다시 읽기
⚙️ Process (5개) ← 다시 처리
💾 Write (5개) → Success!
━━━━━━━━━ CHUNK END ━━━━━━━━━
```

### Writer Retry 후 Skip 케이스
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Read (5개)
⚙️ Process (5개)
💾 Write (5개) → Error
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (재시도 #1) ━━━━━━━━━
📖 Read (5개)
⚙️ Process (5개)
💾 Write (5개) → Error
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (재시도 #2) ━━━━━━━━━
📖 Read (5개)
⚙️ Process (5개)
💾 Write (5개) → Error
🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ Item-by-Item 모드 시작 ━━━━━━━━━
💾 Write (1개) → Success
💾 Write (1개) → Error → ⚠️  SKIP
💾 Write (1개) → Success
💾 Write (1개) → Success
💾 Write (1개) → Success
━━━━━━━━━ CHUNK END ━━━━━━━━━
```

---

## 🚀 실행 방법

```bash
# 1. MySQL 컨테이너 시작
cd StudySpringBatch/customer-batch
docker-compose up -d

# 2. 애플리케이션 실행
./gradlew bootRun

# 3. Retry Job 실행
curl -X POST http://localhost:8080/batch/run/debugJpaRetryJob
curl -X POST http://localhost:8080/batch/run/debugMyBatisRetryJob
curl -X POST http://localhost:8080/batch/run/debugJdbcRetryJob
```

---

## 🧪 디버깅 팁

### 1. 브레이크포인트 추천 위치
- `DetailedRetryListener.onError()` - 재시도 실패 시점
- `DetailedRetryListener.close()` - 재시도 완료 시점
- `Processor` 내부 - 재시도 카운터 확인
- `Writer` 내부 - Writer 재시도 동작 확인

### 2. 재시도 횟수 확인
각 Job Config에는 `retryAttempts` Map이 있어 재시도 횟수를 추적합니다:
```java
int attemptCount = retryAttempts.getOrDefault(email, 0) + 1;
retryAttempts.put(email, attemptCount);
```

### 3. Retry Limit 조정
```java
private static final int RETRY_LIMIT = 3; // 이 값을 변경
```

### 4. 로그 필터링
```bash
# Retry 관련 로그만 보기
grep "🔄 Retry" application.log

# 특정 고객 추적
grep "kim.minji" application.log
```

### 5. 치명적 오류 테스트
`NonSkippableCustomerException` 주석을 해제하면 Job 실패를 테스트할 수 있습니다:
```java
// DebugJpaRetryJobConfig.java
if (email.contains("park.jiwoo")) {
    throw new NonSkippableCustomerException("Fatal error for: " + email);
}
```

---

## 📚 학습 포인트

### 1. Retry 처리 순서
```
예외 발생
  ↓
Retry 대상 예외인가?
  ↓ Yes
재시도 (최대 retryLimit번)
  ↓
성공? → 정상 처리
  ↓ No
Skip 대상 예외인가?
  ↓ Yes
Skip
  ↓ No
Job 실패
```

### 2. Writer Retry의 특수성
- Writer 에러 발생 → **전체 Chunk 롤백**
- Reader부터 다시 시작 (Read → Process → Write)
- 재시도 후에도 실패 → Item-by-Item 모드로 전환
- 문제 있는 아이템만 Skip

### 3. Retry vs Skip 우선순위
1. Retry 먼저 시도
2. Retry 실패 시 Skip 확인
3. Skip도 안되면 Job 실패

### 4. Transaction 경계
- Processor Retry: Item 단위 (트랜잭션 롤백 없음)
- Writer Retry: Chunk 단위 (트랜잭션 롤백)

### 5. 성능 고려사항
- Retry는 비용이 큼 (특히 Writer Retry)
- Retry Limit을 너무 크게 설정하지 말 것
- 일시적 오류만 Retry 대상으로 설정

---

## ✅ 테스트 체크리스트

- [ ] Processor Retry 성공: 재시도 후 정상 처리됨
- [ ] Processor Retry 후 Skip: 최대 재시도 후 Skip됨
- [ ] Writer Retry 성공: Chunk 롤백 후 재시도 성공
- [ ] Writer Retry 후 Skip: Item-by-Item 모드로 Skip
- [ ] 즉시 Skip: Retry 없이 바로 Skip
- [ ] 필터링: null 반환 시 Writer에 전달 안됨
- [ ] Retry 횟수 추적: 로그에서 Attempt # 확인
- [ ] Transaction 롤백: Writer 에러 시 Chunk 롤백 확인

---

## 🎓 핵심 개념 정리

| 개념 | 설명 | 예시 |
|------|------|------|
| **Retry** | 일시적 오류를 극복하기 위한 재시도 | 네트워크 타임아웃 → 재시도 |
| **Skip** | 문제 있는 아이템을 건너뛰기 | 잘못된 데이터 → Skip |
| **Retry Limit** | 최대 재시도 횟수 | 3회 |
| **Skip Limit** | 최대 Skip 가능 횟수 | 10회 |
| **Chunk Rollback** | Writer 에러 시 전체 Chunk 롤백 | Write 실패 → Read부터 다시 |
| **Item-by-Item** | Writer 재시도 실패 시 개별 처리 | 5개 중 1개만 Skip |

---

이 가이드를 통해 Spring Batch의 Retry 메커니즘을 완벽하게 이해할 수 있습니다! 🚀
