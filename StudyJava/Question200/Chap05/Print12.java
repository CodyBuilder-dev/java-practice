package StudyJava.Chap05;

public class Print12 {
    public static void main(String[] args){
        System.out.println(0b11);
        System.out.println(012);
        System.out.println(12);
        System.out.println(0x12);
    
        long a = 23143234234124141L;
        //long b = 23234234131341351; //에러 발생. long타입은 반드시 접미사 L 필수
        System.out.println(a);
        //System.out.println(b);
    }
}