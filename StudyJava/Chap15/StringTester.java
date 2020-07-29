class StringTester {
    public static void main(String[] args){
        String s1 = null;
        String s2 = "";
        String s3 = "ABC";
        String s4 = "ABC";
        String s5 = "ABC"; 
        s5 = "XYZ";
        String s6 = new String("ABC");

        System.out.println("s1 = "+s1);
        System.out.println("s2 = "+s2);
        System.out.println("s3 = "+s3);
        System.out.println("s4 = "+s4);
        System.out.println("s5 = "+s5);

        System.out.println("s3과 s4는 같은 인스턴스를 참조"+
                            ((s3==s4) ? "하고 있다": "안하고 있다"));

        System.out.println("s3과 s6는 같은 인스턴스를 참조"+
                            ((s3==s6) ? "하고 있다": "안하고 있다"));

        //문자열의 intern : 내용이 같은데 인스턴스는 다른 문자열을 같은 인스턴스로 통일
        String s7 = "ABC" + s5;
        String s8 = "ABC" + s5;

        System.out.println("s7과 s8는 같은 인스턴스를 참조"+
                            ((s7==s8) ? "하고 있다": "안하고 있다"));

        s7 = s7.intern();
        System.out.println("s7과 s8는 같은 인스턴스를 참조"+
        ((s7==s8) ? "하고 있다": "안하고 있다"));

        s8 = s8.intern();
        System.out.println("s7과 s8는 같은 인스턴스를 참조"+
        ((s7==s8) ? "하고 있다": "안하고 있다"));


    }
}