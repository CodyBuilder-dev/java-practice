/**
 * 프로그램의 이전 실행 날짜 및 시간, 종료 날짜 및 시간 표시 프로그램
 * (처음 실행시에는 처음 실행을 알리는 문자열 표시)
 */

 import java.io.*;
 import java.util.GregorianCalendar;
 import static java.util.GregorianCalendar.*;

 class LastTime {
     static void init() {
         BufferedReader br = null;

         try {
            // BufferedReader 객체에 파일 불러오기 
            br = new BufferedReader(new FileReader("LastTime.txt"));
            String time = br.readLine();
            System.out.println("이전 시간은 " + time + "였습니다.");
         }
         catch (IOException e){ // LastTime.txt가 없는 경우
             System.out.println("이 프로그램을 실행하는 것은 처음입니다.");
         }
         finally {
             if(br != null){
                 try {
                     br.close();
                 } 
                 catch(IOException e) {
                     System.out.println("파일 닫기 실패");
                 }
             }
         }
     }

    static void term() {
        FileWriter fw = null;

        try {
            fw = new FileWriter("LastTime.txt");
            GregorianCalendar c = new GregorianCalendar();
            fw.write((String.format("%04d년 %02d월 %02d일 %02d시 %02d분 %02d초",
                    c.get(YEAR),c.get(MONTH)+1,c.get(DATE),
                    c.get(HOUR_OF_DAY),c.get(MINUTE),c.get(SECOND))));
        }
        catch (IOException e) {
            System.out.println("오류 발생!!");
        }
        finally {
            if(fw != null) {
                try {
                    fw.close();
                }
                catch (IOException e) {
                    System.out.println("파일 닫기 실패");
                }
            }
        }
    }

    public static void main(String[] args) {
        init();
        term();
    }
 }