# build.gradle 동작 & 정책 문서

이 문서는 `study/build.gradle` 파일의 현재 동작 방식과 설계/정책을 정리합니다. 팀원이나 CI에서 빌드 동작을 예측하고 문제를 진단하는 데 사용하세요.

> 정보: 본 문서의 예시 경로는 아래의 플레이스홀더를 사용합니다.
>
> - <REPO_ROOT> : 저장소 루트 디렉토리 (예: /home/you/my-repo 또는 C:/work/my-repo)
> - <USER_HOME> : 사용자 홈 디렉토리 (예: ~/.m2 은 <USER_HOME>/.m2 로 표현)

요약
- 목적: 프로젝트의 annotation processing과 AspectJ 빌드 파이프라인을 Gradle 9+ 환경에서 안정적으로 동작시키는 것.
- 핵심 정책:
  - `custom-annotation-processor` 모듈이 로컬에 있지 않거나 mavenLocal(예: <USER_HOME>/.m2) 에 이미 JAR이 있으면 로컬 소스의 컴파일/패키징을 건너뜁니다.
  - Gradle 9의 제한(예: `-processorpath` 을 compilerArgs에 직접 넣을 수 없음)에 맞춰 `options.annotationProcessorPath`를 사용합니다.
  - Annotation processor가 생성한 소스는 `build/generated/sources/annotationProcessor/...`에 놓고, javac에는 `-s <dir>`로 전달합니다.
  - `options.release=17`로 도구체인(Java toolchain)과 컴파일러 옵션을 일치시켜 processor의 소스 버전 경고를 제거합니다.

위치
- 대상 파일: `study/build.gradle`
- 문서 파일: `study/BUILD-GRADLE-POLICY.md` (이 파일)

상세 동작

1) 주요 Task들
- `compileProcessorSources` (JavaCompile)
  - 목적: `custom-annotation-processor/src/main/java`의 프로세서 소스를 임시 클래스 디렉토리(`build/tmp/processor-classes`)에 컴파일.
  - 실행 조건(onlyIf): 로컬 모듈 디렉토리 `<REPO_ROOT>/custom-annotation-processor`가 존재하고, mavenLocal에 `me.codybuilder:custom-annotation-processor:1.0-SNAPSHOT` JAR이 없는 경우에만 실행.
  - 출력: `build/tmp/processor-classes` (and copied META-INF/services if present)
  - 사용처: `packageProcessorJar` 또는 `processAnnotations`에서 임시 클래스 디렉토리를 annotationProcessorPath로 사용.

- `packageProcessorJar` (Jar)
  - 목적: `build/tmp/processor-classes`를 `build/tmp/processor-temp.jar`로 패키지.
  - 조건: `compileProcessorSources`와 같은 onlyIf 조건(모듈 존재 && mavenLocal JAR 부재).
  - 부가작업: `META-INF/Services`(대문자) -> `META-INF/services`(소문자) 정규화 및 모듈 리소스의 서비스 파일 복사.

- `processAnnotations` (JavaCompile)
  - 목적: annotation processors만 실행해서 생성된 소스(예: .aj)를 만든다. `-proc:only` 모드.
  - classpath: `sourceSets.main.compileClasspath + configurations.annotationProcessor`
  - options.annotationProcessorPath: `files(build/tmp/processor-classes)` + `configurations.annotationProcessor` (기본)
  - 컴파일러 인자: `-s <build/generated/sources/annotationProcessor/java/main>`, `-proc:only`.
  - 안전장치: `compilerArgs`에서 `-processorpath`/`--processor-path`가 있으면 구성/실행 시 제거(Gradle 9 요구사항).
  - 프로세서 탐지 로직:
    1. 우선 `configurations.annotationProcessor`(mavenLocal 포함)에 있는 아티팩트를 사용.
    2. packaged jar(`build/tmp/processor-temp.jar`)가 있으면 `options.annotationProcessorPath`에 추가.
    3. 임시 컴파일 클래스 디렉토리나 `custom-annotation-processor/build/libs`에 jar가 있으면 사용.
    4. 서비스 파일(META-INF/services/javax.annotation.processing.Processor)이 없어도 `*Processor.class`를 검색해 발견된 클래스명을 `-processor`로 강제 지정하는 폴백 로직 존재(단, 주의해서 사용).

- `compileAjc` (ant iajc)
  - 목적: AspectJ ajc로 weaving(compile-time weaving) 수행.
  - 의존성: `processResources` 및 `processAnnotations`.
  - 동작: `sourceSets.main.java`와 annotation-generated source 디렉토리를 sourceroot로 전달.
  - 사전처리: 실행 전에 generated source 디렉토리(`build/generated/sources/annotationProcessor/main` 및 `.../java/main`)를 생성하여 "디렉토리 없음" 경고 회피.

2) 정책 요약(언제 무엇을 실행할지)
- mavenLocal에 processor JAR이 있으면: 로컬 모듈의 컴파일/패키징(`compileProcessorSources`, `packageProcessorJar`)은 건너뜀. `processAnnotations`는 mavenLocal의 JAR을 사용.
- 로컬 모듈이 있고 mavenLocal에 JAR이 없으면: `compileProcessorSources` → `packageProcessorJar` → `processAnnotations` 흐름으로 프로세서를 준비하고 실행.
- 모듈도 없고 mavenLocal에도 없으면: `processAnnotations`는 "No processor artifacts available" 경고를 남기고 processor 없이 실행(실질적으로 아무 것도 생성되지 않을 수 있음).

3) 로그/진단 포인트
- build.log에서 찾아볼 키워드:
  - `Using packaged processor jar:` — packageProcessorJar 결과 사용
  - `Found processor jar in mavenLocal:` — mavenLocal 사용
  - `processAnnotations: options.annotationProcessorPath=` — 실제 annotationProcessorPath 확인
  - `Detected processor classes, forcing -processor:` — 서비스 파일이 없을 때 동적 감지 폴백
  - `Skipping compileProcessorSources dependency:` — 구성 시점에 compileProcessorSources를 건너뛴 이유
  - `Normalized META-INF/Services -> META-INF/services` — 서비스 파일 경로 정규화

4) 입력/출력 / 성공 기준
- Inputs:
  - 소스: `src/main/java`, `custom-annotation-processor/src/main/java` (선택적)
  - 구성: `configurations.annotationProcessor` 아티팩트(외부 의존성), mavenLocal (예: <USER_HOME>/.m2)
- Outputs:
  - annotation-generated sources: `build/generated/sources/annotationProcessor/java/main` (예: .aj, .java)
  - packaged processor jar: `build/tmp/processor-temp.jar` (조건부)
  - woven classes (compileAjc 결과): `build/classes/java/main` 등
- 성공 기준:
  - annotation processors가 발견되어 생성 소스가 생성되거나(특정 processor가 제공된 경우), 빌드가 실패하지 않음.
  - 삽입된 안전장치로 인해 Gradle 9 규약 위반(예: `-processorpath` in compilerArgs)이 발생하지 않음.

5) 에러 모드(흔한 원인과 해결)
- "Cannot specify -processorpath ..." 오류
  - 원인: compilerArgs에 `-processorpath`가 포함됨.
  - 해결: 스크립트에서 `options.annotationProcessorPath`를 사용하도록 되어 있으므로, `compilerArgs`에 `-processorpath`가 남아있다면 제거되었는지 확인.

- processor 미탐지(생성물 없음)
  - 원인: mavenLocal JAR 없음 + 로컬 모듈 없음 또는 서비스 파일 누락
  - 조치: 로컬 모듈을 사용하려면 `<REPO_ROOT>/custom-annotation-processor` 모듈이 workspace에 있어야 하며, 해당 모듈을 빌드(또는 `./gradlew publishToMavenLocal` 등으로 mavenLocal에 올림)해야 함. 서비스 파일 경로(META-INF/services/javax.annotation.processing.Processor)가 올바른지 확인.

- 서비스 파일이 `META-INF/Services`로 잘못 들어간 경우
  - 현재 스크립트는 이를 `META-INF/services`로 정규화하지만, 모듈 빌드 스크립트에서 리소스 경로(대소문자)를 올바르게 고쳐 빌드하는 것이 권장됨.

6) 테스트 방법(시나리오별 명령)
- (A) mavenLocal에 JAR이 있고 모듈이 없어도 동작 확인
```powershell
# mavenLocal에 JAR이 있는 상태에서
cd <REPO_ROOT>/study
./gradlew.bat clean processAnnotations --info
# 로그에서 "Found processor jar in mavenLocal" 확인
```

- (B) 로컬 모듈에서 컴파일+패키지 후 사용
```powershell
# 1) (옵션) custom-annotation-processor 빌드 또는 publishToMavenLocal
cd <REPO_ROOT>/custom-annotation-processor
./gradlew.bat build
# 또는 publishToMavenLocal 필요 시 실행
# 2) study에서 annotation processing 실행
cd <REPO_ROOT>/study
./gradlew.bat clean processAnnotations --info
# 로그에서 packageProcessorJar/compileProcessorSources 실행 확인
```

- (C) 강제로 로컬 빌드가 필요할 때
```powershell
# compileProcessorSources 직접 실행
cd <REPO_ROOT>/study
./gradlew.bat compileProcessorSources --info
```
