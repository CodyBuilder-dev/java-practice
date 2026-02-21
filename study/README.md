# study 모듈

이 디렉토리는 학습용 `study` 모듈 소스와 빌드 스크립트를 포함합니다.

## Build / Annotation Processing 정책 요약
이 저장소는 `custom-annotation-processor`라는 별도 모듈(또는 mavenLocal의 artifact)을 통해 annotation processing을 수행하고, 그 결과로 생성된 AspectJ 파일을 AspectJ 컴파일 단계에서 위빙합니다.

주요 정책 요약:
- 우선순위: `mavenLocal`에 processor JAR이 있으면 이를 사용합니다. 그렇지 않으면 로컬 모듈(`<REPO_ROOT>/custom-annotation-processor`)을 컴파일/패키지해서 사용합니다.
- 불필요한 빌드 방지: 로컬 모듈이 없거나 `mavenLocal`에 이미 JAR이 있으면 `compileProcessorSources`와 `packageProcessorJar` 작업은 건너뜁니다.
- Gradle 9 규약 준수: `-processorpath`를 `compilerArgs`에 직접 넣지 않고 `options.annotationProcessorPath`를 사용합니다.
- 생성된 sources는 `build/generated/sources/annotationProcessor/java/main`에 위치하며, AspectJ(`compileAjc`)는 이 경로를 sourceroot로 참고합니다.

자세한 동작, 진단 포인트, CI 동작 방식 및 권장 개선 사항은 `BUILD-GRADLE-POLICY.md`를 참고하세요.