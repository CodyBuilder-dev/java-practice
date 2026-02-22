package StudyJava.Chap08;

class Human2 {

    Human2(String n,int h,int w){ //생성자
        //this 키워드는 써도 되고 안써도 되는데 python과 통일성을 위해 쓰겠음
        this.name=n;this.height=h;this.weight=w;
        //name=n;height=h;weight=w;
    }

    public static void main(String[] args){        
        Human2 h = new Human2("길동",170,60);
        System.out.println(h);

        //자기 클래스 안의 멤버라서 호출 가능
        System.out.println(h.name); 
        System.out.println(h.height);
        System.out.println(h.weight);

        System.out.println(h.getName());
        System.out.println(h.getHeight());
        System.out.println(h.getWeight());

        h.setName("춘향");
        System.out.println(h.name);

        //자기 클래스 안의 멤버라서 변경 가능
        h.name = "사또";
        System.out.println(h.name);
    }

    String getName() { return name; }
    static int getHeight() { return gildong.height; }
    int getWeight() { return weight; }

    void setName(String n) { name = n; }


    // 멤버변수 선언 위치는 중요하지 않음
    private String name; 
    private int height;
    private int weight;
        
}

public class HumanTester2 {
    public static void main(String[] args){        
        Human2 h = new Human2("길동",170,60);
        System.out.println(h);

        //다른 클래스 내의 멤버변수라 접근 불가능
        //System.out.println(h.name); 
        //System.out.println(h.height);
        //System.out.println(h.weight);

        
        System.out.println(h.getName());
        System.out.println(h.getHeight());
        System.out.println(h.getWeight());

        h.setName("춘향");
        System.out.println(h.getName());

        //h.name = "사또";
        //System.out.println(h.name);
    }
}