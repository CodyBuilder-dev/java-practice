# build.gradle 동작 & 정책 문서

이 문서는 `study/build.gradle` 파일의 현재 동작 방식과 설계/정책을 정리합니다. 팀원이나 CI에서 빌드 동작을 예측하고 문제를 진단하는 데 사용하세요.

> 정보: 본 문서의 예시 경로는 아래의 플레이스홀더를 사용합니다.
>
> - <REPO_ROOT> : 저장소 루트디렉토리 (예: /home/you/my-repo 또는 C:/work/my-repo)
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
  - 목적: `custom-annotation-processor/src/main/java`의 프로세서 소스를 임시 클래스디렉토리(`build/tmp/processor-classes`)에 컴파일.
  - 실행 조건(onlyIf): 로컬 모듈 디렉토리 `<REPO_ROOT>/custom-annotation-processor`가 존재하고, mavenLocal에 `me.codybuilder:custom-annotation-processor:1.0-SNAPSHOT` JAR이 없는 경우에만 실행.
  - 출력: `build/tmp/processor-classes` (및 리소스의 META-INF/services가 있으면 복사)
  - 사용처: `packageProcessorJar` 또는 `processAnnotations`에서 임시 클래스 디렉토리를 annotationProcessorPath로 사용.

- `packageProcessorJar` (Jar)
  - 목적: `build/tmp/processor-classes`를 `build/tmp/processor-temp.jar`로 패키지.
  - 조건: `compileProcessorSources`와 같은 onlyIf 조건(모듈 존재 && mavenLocal JAR 부재).
  - 부가작업: `META-INF/Services`(대문자) -> `META-INF/services`(소문자) 정규화 및 모듈 리소스의 서비스 파일 복사.

- `processAnnotations` (JavaCompile)
  - 목적: annotation processors만 실행해서 생성된 소스(예: .aj, .java)를 생성한다. javac에 `-proc:only`로 호출.
  - classpath: `sourceSets.main.compileClasspath + configurations.annotationProcessor`
  - options.annotationProcessorPath: `files(build/tmp/processor-classes)` + `configurations.annotationProcessor`(기본)
  - 컴파일러 인자: `-s <build/generated/sources/annotationProcessor/java/main>`, `-proc:only`.
  - 안전장치: `compilerArgs`에서 `-processorpath`/`--processor-path`가 있으면 구성/실행 시 제거(Gradle 9 요구사항).
  - 프로세서 탐지 로직: (아래 "processAnnotations - 자세한 흐름" 참고)

- `compileAjc` (ant iajc)
  - 목적: AspectJ ajc로 weaving(compile-time weaving) 수행.
  - 의존성: `processResources` 및 `processAnnotations`.
  - 동작: `sourceSets.main.java`와 annotation-generated source 디렉토리를 sourceroot로 전달.
  - 사전처리: 실행 전에 generated source 디렉토리(`build/generated/sources/annotationProcessor/main` 및 `.../java/main`)를 생성하여 "디렉토리 없음" 경고 회피.

2) `processAnnotations` - 자세한 흐름 (구성/실행 단계로 분리)

아래는 `processAnnotations` 태스크가 Gradle에서 동작하는 순서를 가능한 한 그대로 표현한 것이며, 디버깅/확장 시 참고하세요.

A. 구성(configuration) 단계 (Gradle이 빌드 스크립트를 평가할 때)
- 태스크 등록: `tasks.register('processAnnotations', JavaCompile)`으로 JavaCompile 타입이 등록됩니다.
- 초기 옵션 설정:
  - `options.release.set(17)`로 소스/타깃 버전을 고정합니다.
  - `options.compilerArgs`에 `-s <apGenDir>` 및 `-proc:only`를 추가합니다. (여기서 `apGenDir`는 `build/generated/sources/annotationProcessor/java/main`)
  - `options.annotationProcessorPath`에 기본값으로 `files(build/tmp/processor-classes)` 및 `configurations.annotationProcessor`를 설정합니다.
- 구성 시 의존성 결정:
  - `moduleDir.exists()`(즉, `<REPO_ROOT>/custom-annotation-processor` 존재) && `!m2Jar.exists()`(사용자 로컬 mavenLocal에 JAR이 없음) 이면 `dependsOn 'compileProcessorSources'` 및 `dependsOn 'packageProcessorJar'`를 추가합니다.
  - 위 조건이 아니면 태스크는 로컬 빌드 의존 없이 동작하도록 남습니다.
- 구성 시 `compilerArgs` 내부에 `-processorpath` 같은 금지된 플래그가 있으면 제거.

B. 실행(execution / doFirst) 단계 (태스크가 실제로 실행될 때)
- 목표: annotation processors를 javac에 제공해 javac가 생성물을 만들도록 함.
- 동작 순서 (줄 단위로 요약):
  1. `configurations.annotationProcessor`를 강제로 해석(resolve)하여 의존성 아티팩트(jar)를 가져옵니다. (mavenLocal 포함)
  2. `procFiles` 라는 리스트를 만들고, 다음 우선순위로 processor 제공원을 수집합니다:
     - `configurations.annotationProcessor`에서 제공되는 파일들
     - `build/tmp/processor-temp.jar` (packageProcessorJar 결과)
     - `<REPO_ROOT>/custom-annotation-processor/build/classes/java/main` (로컬 모듈의 컴파일 결과)
     - `<REPO_ROOT>/custom-annotation-processor/build/libs/*.jar` (모듈의 빌드 산출물)
     - mavenLocal의 snapshot JAR (`m2Jar`)
     - `build/tmp/processor-classes` (compileProcessorSources의 임시 컴파일 디렉토리)
  3. `procFiles`가 비어있으면(아무것도 못찾으면) 자동으로 로컬 모듈을 `gradlew.bat build`로 시도해 빌드해보고 생성물(build/classes, build/libs)이 생기면 다시 procFiles에 추가합니다. (단, gradlew.bat가 없으면 시도 안함)
  4. 각 디렉토리/아카이브에 대해 `META-INF/services/javax.annotation.processing.Processor` 파일이 없으면 모듈의 리소스에 있는 서비스 파일을 복사해서 보강합니다(여전히 없으면 다음 폴백).
  5. 모든 procFiles를 `options.annotationProcessorPath`에 추가하고, `classpath`에도 병합합니다.
  6. 서비스 파일(서비스 로더 방식)이 전혀 없는 경우에는, procFiles 내부를 훑어 `*Processor.class`를 검색하여 발견된 클래스명을 컴파일러에 `-processor <클래스명,...>`로 강제 전달하는 폴백 로직을 동작시킵니다. 이 동작은 마지막 수단이며, 잘못된 클래스명을 넣으면 컴파일 실패를 일으킬 수 있습니다.
  7. 최종적으로 `options.annotationProcessorPath`와 `classpath`가 설정되면 javac를 실행해 processors만 동작하도록(`-proc:only`) 생성물을 `apGenDir`로 출력합니다.

C. 산출물
- `build/generated/sources/annotationProcessor/java/main` (일반적으로 javac가 `-s`로 작성한 디렉토리)
- (조건부) `build/tmp/processor-temp.jar` — 로컬 모듈을 컴파일 후 패키징한 임시 jar
- 콘솔 로그: 다양한 상태 메시지가 찍힘(아래 "예시 로그" 참고)

3) 우선순위/결정 규칙 (정리)
- 우선순위(높 -> 낮):
  1. `configurations.annotationProcessor`로부터 직접 제공되는 artifacts (프로젝트 의존성에 선언된 processor)
  2. 임시로 패키지된 `build/tmp/processor-temp.jar` (로컬 모듈을 컴파일/패키지했을 때)
  3. 로컬 모듈의 `build/classes/java/main` (직접 컴파일한 클래스 디렉토리)
  4. 로컬 모듈의 `build/libs/*.jar`
  5. mavenLocal에 있는 snapshot JAR (`~/.m2/.../custom-annotation-processor-1.0-SNAPSHOT.jar`)
  6. `build/tmp/processor-classes` (compileProcessorSources의 결과)

- 참고: `options.annotationProcessorPath`는 `-processorpath` 플래그 대신 사용해야 합니다(Gradle 9 규약). 스크립트는 `compilerArgs`에서 `-processorpath`를 자동으로 제거합니다.

4) 예시 로그 (디버깅에 유용한 출력 메시지)
- `processAnnotations: begin - printing current annotationProcessor configuration files: <파일목록>`
- `processAnnotations: current tmp processor-classes exists=<true|false>`
- `Using packaged processor jar: <.../build/tmp/processor-temp.jar>`
- `Found processor classes directory: <.../custom-annotation-processor/build/classes/java/main>`
- `Found processor jars in build/libs: <...>`
- `Found processor jar in mavenLocal: <...>`
- `Detected processor classes, forcing -processor: <com.example.MyProcessor>`
- `No processor artifacts available; annotation processing will run with no processors.`

이 메시지들을 --info 또는 --debug로 실행하면 더 상세한 상황을 확인할 수 있습니다.

5) 흔한 문제와 해결
- 문제가: "Cannot specify -processorpath ..." 또는 Gradle 9 관련 경고
  - 원인: 누군가(또는 다른 플러그인)가 `compilerArgs`에 `-processorpath`를 넣음.
  - 해결: 스크립트는 구성/실행 시 `-processorpath`를 제거하고 `options.annotationProcessorPath`에 값을 설정합니다. 만약 다른 스크립트/플러그인이 계속 추가한다면 해당 플러그인 설정을 수정하세요.

- 문제가: processor 미탐지(생성물 없음)
  - 원인: mavenLocal JAR 없음 && 로컬 모듈도 없음 또는 서비스 파일(META-INF/services/...)이 누락됨.
  - 해결: 로컬 모듈을 추가하거나 `custom-annotation-processor`를 빌드해서 mavenLocal에 publish 혹은 모듈의 build output을 생성하세요. 또한 `META-INF/services/javax.annotation.processing.Processor`가 올바른 경로인지 확인하세요(대소문자 주의).

- 문제가: 잘못된 `-processor` 강제 지정으로 인한 컴파일 실패
  - 원인: 서비스 파일 없을 때 스크립트의 폴백 로직이 `*Processor.class`를 찾아 `-processor`를 강제로 지정함.
  - 해결: 서비스 파일을 올바르게 제공하거나, 강제 지정된 클래스명이 실제 프로세서인지 확인. 필요하면 이 폴백을 임시로 주석 처리하세요.

6) 디버깅/검증 절차(권장 명령 예시, Windows PowerShell)
- 현재 `processAnnotations`만 실행해 로그/생성물 확인:
```
# study 디렉토리에서
cd <REPO_ROOT>/study
./gradlew.bat clean processAnnotations --info
# 로그에서 위 예시 로그 메시지를 확인하고, build/generated/sources/annotationProcessor/java/main 디렉토리를 확인
```

- 로컬 모듈(있을 때) 강제 빌드 및 태스크 단위 실행:
```
# custom-annotation-processor 빌드
cd <REPO_ROOT>/custom-annotation-processor
./gradlew.bat build

# study에서 compileProcessorSources/packageProcessorJar만 실행
cd <REPO_ROOT>/study
./gradlew.bat compileProcessorSources packageProcessorJar --info
```

- 프로세서 JAR을 mavenLocal에 올려서 사용하려면:
```
cd <REPO_ROOT>/custom-annotation-processor
./gradlew.bat publishToMavenLocal
```

7) 안전 및 권장사항
- 가능한 경우 서비스 로더 방식(META-INF/services/javax.annotation.processing.Processor)을 제공하세요. 이는 가장 안정적인 processor 탐지 방식입니다.
- `-processor` 강제 지정은 마지막 수단으로 사용하고, CI/팀 환경에서는 서비스 파일을 정비하는 편이 더 안전합니다.
- 스크립트가 자동 빌드(`gradlew.bat build`)를 시도하는 것은 편리하지만, 네트워크/환경에 따라 실패할 수 있으므로 CI에서는 미리 processor를 publish하거나 subproject로 구성하는 것을 권장합니다.

8) 입력/출력/성공 기준(요약)
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

---

위 항목들을 `processAnnotations` 관련 디버깅, CI 설정, 로컬 개발자 가이드에 복사/참조하셔도 됩니다. 원하시면 이 문서에 "간단 체크리스트"(예: 로컬에서 문제 발생 시 1~5 단계로 해결) 항목을 추가해 드리겠습니다.
