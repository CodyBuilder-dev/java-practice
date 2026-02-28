class WearableTester{
    public static void main(String[] args){
        Wearable[] a = new Wearable[2];

        // 인터페이스형 변수도 클래스 인스턴스 할당 가능(다형성)
        a[0] = new Headphone();
        a[1] = new WearableComputer();

        for (int i = 0; i <a.length; ++i)
            a[i].putOn();
        for (int i = 0; i <a.length; ++i)
            a[i].putOff();
    }
}