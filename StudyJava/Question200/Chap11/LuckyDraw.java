package StudyJava.Chap11;

import java.util.GregorianCalendar;
import java.util.Random;


public class LuckyDraw {
    public static void main(String[] args){
        GregorianCalendar today = new GregorianCalendar();
        int y = today.get(GregorianCalendar.YEAR);
        int m = today.get(GregorianCalendar.MONTH) + 1; //0~11로 표현하므로 +1해줘야 현재월
        int d = today.get(GregorianCalendar.DATE);

        // GregorianCalender 없으면 컴파일에러
        // 이유 : YEAR,MONTH,DATE가 STATIC 멤버이므로
        // int y = today.get(YEAR);
        // int m = today.get(MONTH) + 1;
        // int d = today.get(DATE);

        System.out.printf("오늘은 %d년 %d월 %d일입니다.\n",y,m,d);

        Random rand = new Random();
        int k = rand.nextInt(10);
        System.out.println("오늘의 운세는 ");
        switch(k){
            case 0:                 System.out.println("대길"); break;
            case 1: case 2: case 3: System.out.println("길"); break;
            case 4: case 5: case 6: System.out.println("중길"); break;
            case 7: case 8:         System.out.println("소길"); break;
            case 9:                 System.out.println("흉"); break;
        }

    }
}