public interface Wearable {
    void putOn(); // interface 내의 메서드는 자동으로 public abstract
    void putOff();
}

// 인터페이스 구현
class Headphone implements Wearable {
    int volume = 0;

    public void putOn(){
        System.out.println("헤드폰을 착용했습니다.");
    }

    public void putOff(){
        System.out.println("헤드폰을 벗었습니다.");
    }

    public void setVolume(int volume){
        this.volume = volume;
        System.out.println("볼륨을 "+volume+"로 변경했습니다.");
    }

}

class WearableComputer implements Wearable {
    public void putOn() {
        System.out.println("컴퓨터를 실행했습니다.");
    }

    public void putOff() {
        System.out.println("컴퓨터를 종료했습니다.");
    }

    public void reset() {
        System.out.println("컴퓨터를 재시작했습니다.");
    }
}