// 진짜 이것만 있으면 어플리케이션이 완성된다?!
// 심지어 문법도 파이썬이랑 비슷하다 (자바+파이썬 느낌)
@RestController
class HeloController {
    @RequestMapping("/")
    def hello() {
        return "Hello Spring Boot"
    }
}