package StudyJava.Chap05;

public class Q5_8 {
    public static void main(String[] args){
        System.out.println("float    int" );
        System.out.println("------------");

        float x = 0;
        for(int i = 0; i<=1000;i++,x+=0.001){
            System.out.printf("%f %f\n",x,(float)i/1000);
        }
    }
}