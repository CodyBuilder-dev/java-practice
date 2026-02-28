# Atomic 클래스 - 충돌 방지 원리와 실습

## 📚 목차
1. [Atomic 클래스의 충돌 방지 원리](#-atomic-클래스의-충돌-방지-원리)
2. [CAS (Compare-And-Swap) 알고리즘](#-cas-compare-and-swap-알고리즘)
3. [실습 코드](#-실습-코드)
4. [실행 방법](#-실행-방법)

---

## 🔒 Atomic 클래스의 충돌 방지 원리

### 1. CAS (Compare-And-Swap) 알고리즘

Atomic 클래스는 **하드웨어 수준의 CAS 명령어**를 사용합니다:

```
CAS(메모리_주소, 예상값, 새값):
  if (메모리_주소의_실제값 == 예상값):
    메모리_주소에_새값_저장
    return true
  else:
    return false
```

**특징**: 원자적(atomic) 연산 - 중간에 끼어들 수 없는 한 번의 CPU 명령어

---

## 🔄 증가 연산 비교

### 일반 int - Race Condition 발생 ❌

```java
int count = 0;
count++;  // 3단계: 읽기 → 증가 → 쓰기 (중간에 끼어들 수 있음)

Thread1: 읽기(0) → 증가(1) → [Thread2가 끼어듦] → 쓰기(1)
Thread2:            읽기(0) → 증가(1) → 쓰기(1)
결과: 2번 증가했지만 값은 1
```

### AtomicInteger - CAS로 안전 ✅

```java
AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();  // CAS 루프

Thread1: CAS(예상=0, 새값=1) → 성공
Thread2: CAS(예상=0, 새값=1) → 실패 (실제값=1)
         → 재시도 → CAS(예상=1, 새값=2) → 성공
결과: 정확히 2
```

---

## 🛠️ CAS 내부 동작

```java
// AtomicInteger.incrementAndGet() 내부 (의사코드)
public final int incrementAndGet() {
    while (true) {
        int current = get();           // 현재 값 읽기
        int next = current + 1;        // 새 값 계산
        if (compareAndSet(current, next)) {  // CAS 시도
            return next;               // 성공하면 반환
        }
        // 실패하면 while 루프로 재시도
    }
}
```

**Lock-Free 알고리즘**: 락 없이 재시도로 동시성 제어

---

## 📝 실습 코드

### 1. RaceConditionDemo.java - 문제 발생 확인

일반 `int`로 Race Condition이 발생하는 것을 확인합니다.

```java
private int unsafeCounter = 0;

public void increment() {
    unsafeCounter++;  // 충돌 발생!
}
```

**결과**: 10개 스레드 × 1000번 증가 = 10,000 예상이지만 실제로는 값 손실 발생

---

### 2. AtomicCounterDemo.java - Atomic으로 해결

`AtomicInteger`로 안전한 증가 연산을 수행합니다.

```java
private AtomicInteger safeCounter = new AtomicInteger(0);

public void increment() {
    safeCounter.incrementAndGet();  // CAS로 안전!
}
```

**결과**: 정확히 10,000으로 모든 증가가 안전하게 적용됨

---

### 3. AtomicVsLockComparison.java - 성능 비교

세 가지 동시성 제어 방법을 비교합니다:

| 방법 | 안전성 | 성능 | 특징 |
|------|--------|------|------|
| **AtomicInteger** | ✅ 안전 | 🚀 빠름 | Lock-free CAS |
| **synchronized** | ✅ 안전 | 🐌 느림 | Lock contention |
| **volatile** | ❌ 불안전 | 🚀 빠름 | 가시성만 보장, 복합 연산 불안전 |

**핵심 포인트**:
```java
private volatile int counter = 0;
counter++;  // volatile이어도 복합 연산은 안전하지 않음!
```

---

### 4. AtomicCASDemo.java - CAS 동작 원리

CAS의 재시도 메커니즘을 직접 확인합니다.

```java
private void incrementWithCAS() {
    while (true) {
        int current = counter.get();           // 1. 현재 값 읽기
        int next = current + 1;                // 2. 새 값 계산

        if (counter.compareAndSet(current, next)) {
            successCount.incrementAndGet();    // 성공
            break;
        } else {
            retryCount.incrementAndGet();      // 실패 → 재시도
        }
    }
}
```

**출력 예시**:
```
총 CAS 성공: 1000번
총 CAS 재시도: 245번
재시도율: 24.5%
```

---

## 🚀 실행 방법

### 1. 문제 확인 - Race Condition 발생
```bash
cd study
gradle run --args="thread.RaceConditionDemo"
```

또는 IDE에서:
```java
// RaceConditionDemo.java 실행
```

**예상 출력**:
```
=== Race Condition 발생 ===
예상 값: 10000
실제 값: 9847
손실된 증가: 153
❌ Race Condition으로 인해 값이 손실되었습니다!
```

---

### 2. Atomic으로 해결
```bash
gradle run --args="thread.AtomicCounterDemo"
```

**예상 출력**:
```
=== AtomicInteger로 안전한 증가 ===
예상 값: 10000
실제 값: 10000
손실된 증가: 0
✅ CAS 알고리즘으로 모든 증가가 안전하게 적용되었습니다!
```

---

### 3. 성능 비교
```bash
gradle run --args="thread.AtomicVsLockComparison"
```

**예상 출력**:
```
=== 동시성 제어 방법 비교 ===
✅ AtomicInteger: 1000000 / 1000000 (45ms)
✅ synchronized:  1000000 / 1000000 (123ms)
❌ volatile (잘못된 사용): 987654 / 1000000 (38ms)

=== 성능 비교 ===
AtomicInteger: 45ms (기준)
synchronized:  123ms (2.7x 배)
volatile:      38ms (결과 부정확)
```

---

### 4. CAS 동작 원리 확인
```bash
gradle run --args="thread.AtomicCASDemo"
```

**예상 출력**:
```
=== CAS (Compare-And-Swap) 동작 원리 ===

최종 카운터 값: 1000
예상 값: 1000

총 CAS 성공: 1000번
총 CAS 재시도: 245번
재시도율: 24.5%

=== CAS 동작 과정 ===
1. 현재 값(current) 읽기
2. 새 값(next = current + 1) 계산
3. compareAndSet(current, next) 시도:
   - 성공: 메모리 값이 아직 current → next로 변경
   - 실패: 다른 스레드가 이미 변경함 → 1번부터 재시도

✅ 245번의 충돌이 있었지만 CAS로 모두 해결!
```

---

## 🎯 핵심 정리

### Atomic 클래스 사용 시기

✅ **사용해야 할 때**:
- 단순 카운터, 플래그 등 원자적 연산이 필요할 때
- 높은 성능이 필요할 때 (lock-free)
- 단일 변수에 대한 동시성 제어

❌ **사용하지 말아야 할 때**:
- 여러 변수를 동시에 업데이트해야 할 때 → `synchronized` 사용
- 복잡한 비즈니스 로직이 포함될 때 → `Lock` 사용

### volatile vs Atomic vs synchronized

```java
// volatile: 가시성만 보장
private volatile boolean flag = true;  // ✅ 단순 읽기/쓰기
flag++;  // ❌ 복합 연산은 불안전

// Atomic: 가시성 + 원자성 보장
private AtomicInteger count = new AtomicInteger(0);
count.incrementAndGet();  // ✅ 복합 연산도 안전

// synchronized: 가시성 + 원자성 + 복잡한 로직
private int count = 0;
public synchronized void complexOperation() {  // ✅ 모든 경우 안전
    count++;
    // 복잡한 로직...
}
```

---

## 📚 참고 자료

- [Java Concurrency in Practice](https://jcip.net/)
- [AtomicInteger JavaDoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/atomic/AtomicInteger.html)
- [CAS 알고리즘 설명](https://en.wikipedia.org/wiki/Compare-and-swap)

---

## 💡 추가 학습

다음 주제들도 학습해보세요:

1. **ABA Problem**: CAS의 잠재적 문제점
2. **AtomicReference**: 객체 참조에 대한 원자적 연산
3. **LongAdder**: 고경합 환경에서 AtomicLong보다 빠른 대안
4. **VarHandle**: Java 9+의 저수준 원자적 연산

---

**작성일**: 2026-01-25
**테스트 환경**: Java 8+
