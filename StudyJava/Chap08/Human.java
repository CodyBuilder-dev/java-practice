package StudyJava.Chap08;

public class Human {
    String name;
    int height;
    int weight;


    public static void main(String[] args){ //자기자신에 main을 넣어 테스트 가능
        Human gildong = new Human();
        //Human gildong = new Human("길동",180,60); // 생성자 없으면 인자넣는게 오히려 에러
        Human chulsu = new Human();


        chulsu.name = "철수";
        chulsu.height = 166;
        chulsu.weight = 72;

        System.out.println(gildong.name);
        System.out.println(gildong.height);
        System.out.println(gildong.weight);

        System.out.println(chulsu.name);
        System.out.println(chulsu.height);
        System.out.println(chulsu.weight);
    }
}