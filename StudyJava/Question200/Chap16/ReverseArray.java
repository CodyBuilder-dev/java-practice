import java.util.Scanner;

class ReverseArray1 {
    // 배열 요소 swap
    static void swap(int[] a,int idx1, int idx2){
        int t = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = t;
    }

    static void reverse(int[] a) {
        try {
            for (int i = 0; i < a.length/2 ; ++i)
                swap(a,i,a.length-i); //예외가 숨겨진 구문
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.print("요소 수 : ");
        int num = sc.nextInt();
        int[] x = new int[num];

        for (int i = 0 ; i< num ; ++i) {
            System.out.print("x["+i+"]:");
            x[i] = sc.nextInt();
        }

        reverse(x);

        for (int i = 0 ; i< num ; ++i) {
            System.out.println("x["+i+"]:"+x[i]);
        }
    }
}


class ReverseArray2 {
    // 배열 요소 swap
    static void swap(int[] a,int idx1, int idx2){
        int t = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = t;
    }

    static void reverse(int[] a) {
        try {
            for (int i = 0; i < a.length/2 ; ++i)
                swap(a,i,a.length-i); //예외가 숨겨진 구문
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("reverse의 버그",e);
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.print("요소 수 : ");
        int num = sc.nextInt();
        int[] x = new int[num];

        for (int i = 0 ; i< num ; ++i) {
            System.out.print("x["+i+"]:");
            x[i] = sc.nextInt();
        }

        try {
            reverse(x);

            for (int i = 0 ; i< num ; ++i) {
                System.out.println("x["+i+"]:"+x[i]);
            }
        }
        catch (RuntimeException e) {
            System.out.println("예외:"+e);
            System.out.println("예외의 원인:"+e.getCause());
        }
    }
}