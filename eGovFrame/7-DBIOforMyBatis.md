# 전자정부 프레임워크 분투기 - 7. DBIO(MyBatis)
## 준비
### 프로젝트 생성
이번에는 Web이 아니라 Core 프로젝트다. 물론 이게 무엇인지 설명은 없다. 일단 하라는 대로 설치를 해 본다.  

### Mapper Configuration 파일 생성
Package Explorer 우클릭 > New > mapper configuration 으로 New Mapper Configuration을 생성한다. Config 파일 저장 경로는, 프로젝트 루트 폴더 아래에 sample_config.xml로 일단 해 둔다. 

### Mapper 파일 생성
Package Explorer 우클릭 > New > mapper 으로 New Mapper를 생성한다. Config 파일 저장 경로는, 프로젝트 루트 폴더 아래에 sample_map.xml로 일단 해 둔다.  

### Mapper 파일에 Result Map 작성
(적용하는 방법만을 설명해 주고 Result Map의 개념에 대해서는 설명하지 않는다. MyBatis를 찾아가면서 공부해야 한다.)  
해당 sample_map.xml 파일을 우클릭 > Open With > Mapper editor로 연 후, 좌측 Mapper Tree에서 ResultMap 우클릭 > Add resultMap을 선택한다.

Result Map에서 뭔지는 모르지만 Name과 Column을 선택한다.

### Mapper 파일에 Query 작성
(마찬가지로 Mapper에 Query가 어떻게 적용되는지는 설명해주지 않는다.)  
좌측 Mapper Tree에서 Query 우클릭 > Add Select Query를 선택한다.

### Query 테스트
안돼잖아!
