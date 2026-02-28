package id;

import java.util.GregorianCalendar;
import static java.util.GregorianCalendar.*;

public class DateId {
    private int id;
    private static int counter;

    //static 변수 초기화?
    static {
        GregorianCalendar today = new GregorianCalendar();
        int y = today.get(YEAR);
        int m = today.get(MONTH)+1;
        int d = today.get(DATE);

        System.out.printf("오늘은 %d년 %d월 %d일입니다.\n",y,m,d);

        counter = y*1000000 + m*10000 + d*100;
    }
    public DateId() {
        id = ++counter;
    }
    int getId() { return id; }
}