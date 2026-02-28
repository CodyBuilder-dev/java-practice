# compareAndSet(current, next) 동작 원리 Deep Dive

## 🎯 목차
1. [compareAndSet 메서드 시그니처](#1-compareandsset-메서드-시그니처)
2. [동작 과정 상세 분석](#2-동작-과정-상세-분석)
3. [하드웨어 수준의 원자성](#3-하드웨어-수준의-원자성)
4. [Java에서 네이티브 코드로](#4-java에서-네이티브-코드로)
5. [실제 CPU 명령어](#5-실제-cpu-명령어)
6. [왜 원자적인가?](#6-왜-원자적인가)
7. [시각적 이해](#7-시각적-이해)
8. [실습 코드](#8-실습-코드)

---

## 1. compareAndSet 메서드 시그니처

```java
public final boolean compareAndSet(int expect, int update)
```

**파라미터:**
- `expect`: 예상하는 현재 값 (expected value)
- `update`: 변경하려는 새로운 값 (new value)

**반환값:**
- `true`: 성공 (메모리 값이 expect와 같아서 update로 변경됨)
- `false`: 실패 (메모리 값이 expect와 달라서 변경하지 않음)

---

## 2. 동작 과정 상세 분석

### 단계별 동작

```java
AtomicInteger counter = new AtomicInteger(10);

// Thread A 실행
boolean result = counter.compareAndSet(10, 11);
```

**내부에서 일어나는 일:**

```
┌─────────────────────────────────────────────────────┐
│ 1. expect = 10, update = 11 로 메서드 호출          │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│ 2. 메모리 주소에서 실제 값(actual) 읽기             │
│    actual = memory[counter의_주소]                  │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│ 3. 비교: actual == expect ?                         │
│    10 == 10 ? → YES                                 │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│ 4. 메모리에 새 값 쓰기                               │
│    memory[counter의_주소] = update (11)             │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│ 5. return true (성공)                               │
└─────────────────────────────────────────────────────┘
```

### 핵심: 2~4단계가 **한 번의 원자적 CPU 명령어**로 실행됨!

---

## 3. 하드웨어 수준의 원자성

### 일반적인 증가 연산 (원자적이지 않음)

```java
counter++;  // 3개의 별도 명령어
```

**CPU 레벨에서:**
```assembly
LOAD  R1, [counter]    ; 1. 메모리 → 레지스터 (읽기)
ADD   R1, R1, 1        ; 2. 레지스터에서 증가
STORE [counter], R1    ; 3. 레지스터 → 메모리 (쓰기)
```

**문제점:**
```
Thread A: LOAD(10)  ADD(11)  [Thread B 끼어듦]  STORE(11)
Thread B:              LOAD(10)  ADD(11)  STORE(11)
결과: 11 (하나 손실!)
```

---

### CAS 연산 (원자적)

```java
compareAndSet(10, 11)  // 1개의 원자적 명령어
```

**CPU 레벨에서:**
```assembly
CMPXCHG [counter], R_expect, R_update
; Compare-And-Exchange: 한 번에 실행!
```

**의사 코드:**
```c
// 하드웨어가 보장: 이 블록은 절대 중단되지 않음
atomic {
    actual = *memory_address;
    if (actual == expect) {
        *memory_address = update;
        return true;
    } else {
        return false;
    }
}
```

---

## 4. Java에서 네이티브 코드로

### Java 레벨
```java
AtomicInteger counter = new AtomicInteger(0);
counter.compareAndSet(0, 1);
```

### JDK 내부 (Unsafe 클래스)
```java
public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(
        this,           // 객체
        valueOffset,    // 필드의 메모리 오프셋
        expect,         // 예상 값
        update          // 새 값
    );
}
```

### Unsafe (JNI 네이티브 메서드)
```cpp
// hotspot/src/share/vm/prims/unsafe.cpp
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(
    JNIEnv *env, jobject unsafe,
    jobject obj, jlong offset,
    jint expect, jint update))
{
    oop p = JNIHandles::resolve(obj);
    jint* addr = (jint *)index_oop_from_field_offset_long(p, offset);

    return (jint)(Atomic::cmpxchg(update, addr, expect)) == expect;
}
UNSAFE_END
```

### 최종 하드웨어 명령어 (x86-64)
```assembly
; Intel x86-64 CMPXCHG 명령어
lock cmpxchg [memory_address], new_value

; 동작:
; 1. EAX 레지스터에 expect 값 로드
; 2. [memory_address]와 EAX 비교
; 3. 같으면: [memory_address] = new_value, ZF=1
; 4. 다르면: EAX = [memory_address], ZF=0
; 5. lock 접두사로 다른 CPU 접근 차단
```

---

## 5. 실제 CPU 명령어

### x86-64 아키텍처

```assembly
; compareAndSet(10, 11) 실행 시

mov eax, 10              ; expect 값을 EAX에 로드
mov ebx, 11              ; update 값을 EBX에 로드
lock cmpxchg [rcx], ebx  ; 원자적 비교-교환
                         ; rcx = counter의 메모리 주소
                         ; lock 접두사 = 메모리 버스 잠금
```

**LOCK 접두사의 역할:**
1. **메모리 버스 잠금**: 다른 CPU가 해당 메모리에 접근 못함
2. **캐시 무효화**: 모든 CPU 캐시를 동기화
3. **원자성 보장**: 명령어가 완전히 끝날 때까지 중단 불가

### ARM 아키텍처 (LL/SC)

```assembly
; ARM은 Load-Link/Store-Conditional 사용

retry:
    ldrex r0, [r1]        ; Load-Exclusive: 메모리 값 읽고 예약
    cmp   r0, r2          ; expect와 비교
    bne   fail            ; 다르면 실패
    strex r3, r4, [r1]    ; Store-Exclusive: 예약 유효하면 쓰기
    cmp   r3, #0          ; 성공 여부 확인
    bne   retry           ; 실패하면 재시도
    b     success
fail:
    mov   r0, #0          ; false 반환
    bx    lr
success:
    mov   r0, #1          ; true 반환
    bx    lr
```

---

## 6. 왜 원자적인가?

### 멀티코어 환경에서의 동작

```
      CPU 0                    CPU 1
        │                        │
        ├─ LOCK CMPXCHG [addr]   │
        │  ┌─────────────┐       │
        │  │ 버스 잠금    │◄──────┼─── 접근 차단!
        │  │ 읽기+비교+   │       │
        │  │ 쓰기 완료    │       │
        │  └─────────────┘       │
        │                        │
        ├─ 잠금 해제             │
        │                        ├─ 이제 접근 가능
```

**보장 사항:**
1. **읽기-비교-쓰기가 분리 불가능**
2. **다른 CPU가 중간에 끼어들 수 없음**
3. **캐시 일관성 프로토콜(MESI)로 모든 CPU 동기화**

---

## 7. 시각적 이해

### 시나리오: 2개 스레드가 동시에 증가

```
초기 상태: counter = 5

Thread A                          Thread B
   │                                 │
   ├─ current = counter.get()       │
   │  current = 5                    │
   │                                 ├─ current = counter.get()
   │                                 │  current = 5
   │                                 │
   ├─ next = current + 1             │
   │  next = 6                       │
   │                                 ├─ next = current + 1
   │                                 │  next = 6
   │                                 │
   ├─ compareAndSet(5, 6)            │
   │  ┌────────────────────┐         │
   │  │ LOCK 획득          │         │
   │  │ actual = 5         │         │
   │  │ 5 == 5? YES        │         │
   │  │ counter = 6        │         │
   │  │ LOCK 해제          │         │
   │  │ return true        │         │
   │  └────────────────────┘         │
   │  ✅ 성공!                       │
   │                                 │
   │                                 ├─ compareAndSet(5, 6)
   │                                 │  ┌────────────────────┐
   │                                 │  │ LOCK 획득          │
   │                                 │  │ actual = 6 (A가 변경함!)
   │                                 │  │ 6 == 5? NO         │
   │                                 │  │ 변경하지 않음      │
   │                                 │  │ LOCK 해제          │
   │                                 │  │ return false       │
   │                                 │  └────────────────────┘
   │                                 │  ❌ 실패!
   │                                 │
   │                                 ├─ 재시도 (while loop)
   │                                 ├─ current = counter.get()
   │                                 │  current = 6
   │                                 ├─ next = 7
   │                                 ├─ compareAndSet(6, 7)
   │                                 │  ✅ 성공!
   │                                 │
   ▼                                 ▼

최종 결과: counter = 7 (정확함!)
```

---

## 8. 실습 코드

### 예제 1: compareAndSet 직접 사용

```java
package thread;

import java.util.concurrent.atomic.AtomicInteger;

public class CASDetailedDemo {
    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger(10);

        System.out.println("=== compareAndSet 동작 과정 ===\n");

        // 시나리오 1: 성공하는 경우
        System.out.println("초기 값: " + counter.get());
        System.out.println("compareAndSet(10, 20) 실행...");
        boolean result1 = counter.compareAndSet(10, 20);
        System.out.println("결과: " + result1);
        System.out.println("현재 값: " + counter.get());
        System.out.println("→ 예상값(10)과 실제값(10)이 같아서 20으로 변경 성공!\n");

        // 시나리오 2: 실패하는 경우
        System.out.println("compareAndSet(10, 30) 실행...");
        boolean result2 = counter.compareAndSet(10, 30);
        System.out.println("결과: " + result2);
        System.out.println("현재 값: " + counter.get());
        System.out.println("→ 예상값(10)과 실제값(20)이 달라서 변경 실패!\n");

        // 시나리오 3: 올바른 예상값으로 재시도
        System.out.println("compareAndSet(20, 30) 실행...");
        boolean result3 = counter.compareAndSet(20, 30);
        System.out.println("결과: " + result3);
        System.out.println("현재 값: " + counter.get());
        System.out.println("→ 예상값(20)과 실제값(20)이 같아서 30으로 변경 성공!");
    }
}
```

### 예제 2: CAS 충돌 상황 시뮬레이션

```java
package thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;

public class CASCollisionDemo {

    private static AtomicInteger counter = new AtomicInteger(0);
    private static AtomicInteger successCount = new AtomicInteger(0);
    private static AtomicInteger failureCount = new AtomicInteger(0);

    static class Worker implements Runnable {
        private final String name;
        private final CountDownLatch startLatch;

        public Worker(String name, CountDownLatch startLatch) {
            this.name = name;
            this.startLatch = startLatch;
        }

        @Override
        public void run() {
            try {
                // 모든 스레드가 동시에 시작하도록 대기
                startLatch.await();
            } catch (InterruptedException e) {
                return;
            }

            // 정확히 한 번의 증가 시도 (재시도 없음)
            int current = counter.get();
            int next = current + 1;

            // 약간의 지연으로 충돌 유도
            try {
                Thread.sleep(0, 100);  // 100 나노초
            } catch (InterruptedException e) {
                // ignore
            }

            boolean success = counter.compareAndSet(current, next);

            if (success) {
                successCount.incrementAndGet();
                System.out.println(name + ": ✅ 성공 (예상=" + current +
                                 ", 새값=" + next + ")");
            } else {
                failureCount.incrementAndGet();
                System.out.println(name + ": ❌ 실패 (예상=" + current +
                                 ", 실제=" + counter.get() + ")");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        Thread[] threads = new Thread[threadCount];

        System.out.println("=== CAS 충돌 시뮬레이션 ===\n");
        System.out.println(threadCount + "개 스레드가 동시에 compareAndSet 실행\n");

        // 스레드 생성
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new Worker("Thread-" + i, startLatch));
            threads[i].start();
        }

        // 모든 스레드 동시 시작
        Thread.sleep(100);  // 스레드가 모두 준비될 때까지 대기
        startLatch.countDown();  // 시작 신호!

        // 완료 대기
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("\n=== 결과 ===");
        System.out.println("최종 counter 값: " + counter.get());
        System.out.println("성공한 스레드: " + successCount.get());
        System.out.println("실패한 스레드: " + failureCount.get());
        System.out.println("\n💡 실패한 스레드는 다른 스레드가 먼저 값을 변경해서 예상값이 맞지 않았습니다!");
        System.out.println("   실제 프로그램에서는 while 루프로 재시도합니다.");
    }
}
```

### 예제 3: CAS vs synchronized 동작 비교

```java
package thread;

import java.util.concurrent.atomic.AtomicInteger;

public class CASvsSynchronizedMechanism {

    // CAS 방식
    static class CASCounter {
        private AtomicInteger value = new AtomicInteger(0);

        public void increment() {
            while (true) {
                int current = value.get();        // 1. 읽기
                int next = current + 1;           // 2. 계산

                if (value.compareAndSet(current, next)) {  // 3. CAS
                    System.out.println(Thread.currentThread().getName() +
                                     ": CAS 성공 (" + current + " → " + next + ")");
                    return;  // 성공
                } else {
                    System.out.println(Thread.currentThread().getName() +
                                     ": CAS 실패, 재시도 (예상=" + current +
                                     ", 실제=" + value.get() + ")");
                    // while 루프로 재시도
                }
            }
        }

        public int get() {
            return value.get();
        }
    }

    // synchronized 방식
    static class SyncCounter {
        private int value = 0;

        public synchronized void increment() {
            System.out.println(Thread.currentThread().getName() +
                             ": Lock 획득, 증가 시작");
            value++;
            System.out.println(Thread.currentThread().getName() +
                             ": 증가 완료 (" + value + "), Lock 해제");
        }

        public synchronized int get() {
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CAS 방식 (Lock-Free) ===\n");
        CASCounter casCounter = new CASCounter();

        Thread t1 = new Thread(() -> casCounter.increment(), "CAS-Thread-1");
        Thread t2 = new Thread(() -> casCounter.increment(), "CAS-Thread-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("\n최종 값: " + casCounter.get());
        System.out.println("→ 실패 시 재시도로 최종 성공!\n");

        System.out.println("=".repeat(50));
        System.out.println("\n=== synchronized 방식 (Lock-Based) ===\n");

        SyncCounter syncCounter = new SyncCounter();

        Thread t3 = new Thread(() -> syncCounter.increment(), "Sync-Thread-1");
        Thread t4 = new Thread(() -> syncCounter.increment(), "Sync-Thread-2");

        t3.start();
        t4.start();
        t3.join();
        t4.join();

        System.out.println("\n최종 값: " + syncCounter.get());
        System.out.println("→ Lock으로 순차 실행, 대기 시간 발생!");
    }
}
```

---

## 핵심 정리

### compareAndSet의 3가지 핵심

1. **원자성 (Atomicity)**
   - 읽기-비교-쓰기가 하나의 CPU 명령어로 실행
   - 중간에 다른 스레드가 끼어들 수 없음

2. **Lock-Free**
   - 실제 락을 걸지 않음
   - 실패 시 재시도로 해결
   - 데드락 불가능

3. **하드웨어 지원**
   - CPU의 CMPXCHG (x86) 또는 LL/SC (ARM) 명령어 사용
   - LOCK 접두사로 메모리 버스 잠금
   - 캐시 일관성 프로토콜로 멀티코어 동기화

### 의사 코드 요약

```
compareAndSet(expect, update):
    원자적으로 {
        actual = memory[address]
        if actual == expect:
            memory[address] = update
            return true
        else:
            return false
    }
```

이게 바로 **하드웨어가 보장하는 마법**입니다! 🎩✨
