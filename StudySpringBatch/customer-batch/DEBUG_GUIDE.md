# Spring Batch 디버깅 가이드

이 문서는 Spring Batch의 Reader/Processor/Writer 처리 순서와 Listener, Skip 동작을 테스트하기 위한 가이드입니다.

## 📋 목차
1. [디버그 Job 개요](#디버그-job-개요)
2. [Listener 설명](#listener-설명)
3. [실행 방법](#실행-방법)
4. [예상 실행 흐름](#예상-실행-흐름)
5. [에러 시나리오](#에러-시나리오)

---

## 🎯 디버그 Job 개요

총 3개의 디버그 Job을 제공합니다:

### 1. **debugJpaJob** (JPA 기반)
- **Reader**: JpaPagingItemReader
- **Chunk Size**: 5
- **Skip Limit**: 10
- **에러 시나리오**:
  - `shin.daeun@example.com` → SkippableException (Processor에서 Skip)
  - `baek.joonho@example.com` → null 반환 (필터링)

### 2. **debugMyBatisJob** (MyBatis 기반)
- **Reader**: MyBatisPagingItemReader
- **Chunk Size**: 5
- **Skip Limit**: 10
- **에러 시나리오**:
  - `kwon.nayeon@example.com` → SkippableException (Processor에서 Skip)
  - `ahn.taeyang@example.com` → null 반환 (필터링)
  - `hong.gildong@example.com` → SkippableException (Writer에서 Skip)

### 3. **debugJdbcJob** (JDBC 기반)
- **Reader**: JdbcPagingItemReader
- **Chunk Size**: 5
- **Skip Limit**: 10
- **에러 시나리오**:
  - `seo.hyunwoo@example.com` → SkippableException (Processor에서 Skip)
  - `moon.chaewon@example.com` → null 반환 (필터링)

---

## 🎧 Listener 설명

각 Job에 다음 Listener들이 적용되어 있습니다:

### 1. **DetailedChunkListener**
Chunk의 시작/종료/에러를 추적합니다.
```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[JobName] 🔵 CHUNK START
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

### 2. **DetailedItemReadListener**
각 아이템 읽기 전후를 추적합니다.
```
[JobName] 📖 Reader: beforeRead() - Attempting to read item #1
[JobName] 📖 Reader: afterRead() - Successfully read item #1: CustomerEntity{...}
[JobName] ❌ Reader: onReadError() - Error reading item
```

### 3. **DetailedItemProcessListener**
각 아이템 처리 전후를 추적합니다.
```
[JobName] ⚙️  Processor: beforeProcess() - Processing item: CustomerEntity{...}
[JobName] ⚙️  Processor: afterProcess() - Item #1 processed successfully
[JobName] ⚙️  Processor: afterProcess() - Item #2 filtered out (returned null)
[JobName] ❌ Processor: onProcessError() - Error processing item
```

### 4. **DetailedItemWriteListener**
Chunk 단위 쓰기 전후를 추적합니다.
```
[JobName] 💾 Writer: beforeWrite() - About to write 5 items
[JobName] 💾 Writer: afterWrite() - Successfully wrote 5 items
[JobName] ❌ Writer: onWriteError() - Error writing items
```

### 5. **DetailedSkipListener**
Skip 이벤트를 추적합니다.
```
[JobName] ⚠️  SKIP #1 in PROCESS - Item: ... - Exception: SkippableCustomerException
[JobName] ⚠️  SKIP #2 in WRITE - Item: ... - Exception: SkippableCustomerException
```

---

## 🚀 실행 방법

### 1. MySQL 컨테이너 시작
```bash
cd StudySpringBatch/customer-batch
docker-compose up -d
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. Job 실행 (REST API 또는 수동 실행)
```bash
# JPA Job
curl -X POST http://localhost:8080/batch/run/debugJpaJob

# MyBatis Job
curl -X POST http://localhost:8080/batch/run/debugMyBatisJob

# JDBC Job
curl -X POST http://localhost:8080/batch/run/debugJdbcJob
```

---

## 📊 예상 실행 흐름

### 정상 케이스 (에러 없음)
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Reader: beforeRead() - Attempting to read item #1
📖 Reader: afterRead() - Successfully read item #1: Customer{email=...}
📖 Reader: beforeRead() - Attempting to read item #2
📖 Reader: afterRead() - Successfully read item #2: Customer{email=...}
...
📖 Reader: beforeRead() - Attempting to read item #5
📖 Reader: afterRead() - Successfully read item #5: Customer{email=...}
📖 Reader: beforeRead() - Attempting to read item #6
📖 Reader: afterRead() - Read returned null (end of chunk)

⚙️  Processor: beforeProcess() - Processing item: Customer{email=...}
⚙️  Processor: afterProcess() - Item #1 processed successfully
⚙️  Processor: beforeProcess() - Processing item: Customer{email=...}
⚙️  Processor: afterProcess() - Item #2 processed successfully
...
⚙️  Processor: beforeProcess() - Processing item: Customer{email=...}
⚙️  Processor: afterProcess() - Item #5 processed successfully

💾 Writer: beforeWrite() - About to write 5 items
💾 Writer: afterWrite() - Successfully wrote 5 items

━━━━━━━━━ CHUNK END - Committed successfully ━━━━━━━━━
```

### Skip 케이스 (Processor에서 에러)
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Reader: afterRead() - Successfully read item #1: Customer{email=shin.daeun@...}
...

⚙️  Processor: beforeProcess() - Processing item: Customer{email=shin.daeun@...}
❌ Processor: onProcessError() - Error processing item: SkippableCustomerException
⚠️  SKIP #1 in PROCESS - Item: Customer{email=shin.daeun@...} - Exception: SkippableCustomerException

⚙️  Processor: beforeProcess() - Processing item: Customer{email=...}
⚙️  Processor: afterProcess() - Item #2 processed successfully
...

💾 Writer: beforeWrite() - About to write 4 items (5 - 1 skipped)
💾 Writer: afterWrite() - Successfully wrote 4 items

━━━━━━━━━ CHUNK END - Committed successfully ━━━━━━━━━
```

### 필터링 케이스 (Processor가 null 반환)
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
📖 Reader: afterRead() - Successfully read item #1: Customer{email=baek.joonho@...}
...

⚙️  Processor: beforeProcess() - Processing item: Customer{email=baek.joonho@...}
⚙️  Processor: afterProcess() - Item #1 filtered out (returned null)

⚙️  Processor: beforeProcess() - Processing item: Customer{email=...}
⚙️  Processor: afterProcess() - Item #2 processed successfully
...

💾 Writer: beforeWrite() - About to write 4 items (5 - 1 filtered)
💾 Writer: afterWrite() - Successfully wrote 4 items

━━━━━━━━━ CHUNK END - Committed successfully ━━━━━━━━━
```

### Writer에서 에러 케이스
```
━━━━━━━━━ CHUNK START ━━━━━━━━━
... (Reader, Processor 정상 진행)

💾 Writer: beforeWrite() - About to write 5 items
❌ Writer: onWriteError() - Error writing items: SkippableCustomerException
⚠️  SKIP #1 in WRITE - Item: Customer{email=hong.gildong@...} - Exception: SkippableCustomerException

🔴 CHUNK ERROR - Rolling back

━━━━━━━━━ CHUNK START (재시도) ━━━━━━━━━
... (Chunk가 롤백되고 item-by-item으로 재시도)
```

---

## 🔍 에러 시나리오 상세

### 1. Processor Skip (SkippableCustomerException)
**대상**: `shin.daeun`, `kwon.nayeon`, `seo.hyunwoo`

**동작**:
1. Reader가 정상적으로 읽음
2. Processor에서 예외 발생
3. SkipListener의 `onSkipInProcess()` 호출
4. 해당 아이템은 Writer에 전달되지 않음
5. 나머지 아이템들은 정상 처리

**로그**:
```
⚙️  Processor: beforeProcess() - Processing customer: shin.daeun@example.com
  [PROCESSOR LOGIC] Found 'shin.daeun' - throwing SkippableCustomerException
❌ Processor: onProcessError() - Error processing item
⚠️  SKIP #1 in PROCESS - Item: ... - Exception: SkippableCustomerException
```

### 2. Processor Filter (null 반환)
**대상**: `baek.joonho`, `ahn.taeyang`, `moon.chaewon`

**동작**:
1. Reader가 정상적으로 읽음
2. Processor가 null 반환
3. Writer에 전달되지 않음 (Skip과 다름, 에러가 아닌 정상 처리)

**로그**:
```
⚙️  Processor: beforeProcess() - Processing customer: baek.joonho@example.com
  [PROCESSOR LOGIC] Found 'baek.joonho' - filtering out (returning null)
⚙️  Processor: afterProcess() - Item filtered out (returned null)
```

### 3. Writer Skip (SkippableCustomerException)
**대상**: `hong.gildong`

**동작**:
1. Reader, Processor 정상 처리
2. Writer에서 예외 발생
3. **전체 Chunk 롤백**
4. Spring Batch가 자동으로 item-by-item 재시도
5. 문제 있는 아이템만 Skip

**로그**:
```
💾 Writer: beforeWrite() - About to write 5 items
  [WRITER LOGIC] Found 'hong.gildong' - throwing SkippableCustomerException
❌ Writer: onWriteError() - Error writing items
⚠️  SKIP #1 in WRITE - Item: ... - Exception: SkippableCustomerException
🔴 CHUNK ERROR - Rolling back
━━━━━━━━━ CHUNK START (재시도) ━━━━━━━━━
```

---

## 🎓 학습 포인트

### 1. Chunk 처리 순서
```
Chunk Start
→ Read (chunk size만큼 반복)
  → beforeRead()
  → 실제 읽기
  → afterRead()
→ Process (읽은 아이템들에 대해)
  → beforeProcess()
  → 실제 처리
  → afterProcess()
→ Write (처리된 아이템들을 한 번에)
  → beforeWrite()
  → 실제 쓰기
  → afterWrite()
→ Chunk End (커밋)
```

### 2. Skip vs Filter
- **Skip**: 예외가 발생했지만 허용된 예외라서 건너뜀 (에러 카운트)
- **Filter**: Processor가 의도적으로 null을 반환해서 걸러냄 (정상 처리)

### 3. Writer 에러의 특수성
- Writer에서 에러 발생 시 전체 Chunk 롤백
- Spring Batch가 자동으로 item-by-item 재처리
- 문제 있는 아이템만 Skip하고 나머지는 재처리

### 4. Transaction 경계
- 각 Chunk가 하나의 트랜잭션
- Chunk 성공 시 커밋, 실패 시 롤백
- Skip은 예외를 처리하고 트랜잭션은 커밋

---

## 🧪 테스트 체크리스트

- [ ] 정상 케이스: 모든 아이템이 성공적으로 처리됨
- [ ] Processor Skip: 특정 이메일에서 예외 발생 후 Skip
- [ ] Processor Filter: 특정 이메일이 null로 필터링
- [ ] Writer Skip: Writer에서 예외 발생 후 재시도 및 Skip
- [ ] Chunk 경계: Chunk 단위로 커밋되는지 확인
- [ ] Listener 순서: 각 Listener가 올바른 순서로 호출되는지 확인
- [ ] Skip Limit: Skip 횟수가 제한을 초과하면 Job 실패하는지 확인

---

## 📝 추가 디버깅 팁

### 1. 브레이크포인트 설정 위치
- `DetailedItemReadListener.afterRead()`
- `DetailedItemProcessListener.beforeProcess()`
- `DetailedItemWriteListener.beforeWrite()`
- `DetailedSkipListener.onSkipInProcess()`

### 2. 로그 레벨 조정
```yaml
logging:
  level:
    com.example.customerbatch.listener: DEBUG  # 더 자세한 로그
```

### 3. Chunk Size 조정
- 작은 Chunk Size (예: 2-3)로 설정하면 동작을 더 명확히 관찰 가능
- Job Config에서 `CHUNK_SIZE` 상수 변경

### 4. Skip Limit 테스트
- Skip Limit을 낮게 설정 (예: 2)하고 여러 에러 발생시켜 Job 실패 확인
