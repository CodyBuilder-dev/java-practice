class StringTester {
    public static void main(String[] args){
        String s1 = null; 
        String s2 = "";
        String s3 = "ABC";
        String s4 = "ABC";
        String s5 = "ABC";
        s5 = "XYZ";

        System.out.println("s1="+s1);
        System.out.println("s2 ="+s2);
        System.out.println("s3 ="+s3);
        System.out.println("s4 ="+s4);
        System.out.println("s5 ="+s5);

        System.out.println("s3와 s4는 같은 문자열 데이터 참조"+
                            ((s3 == s4) ? "하고 있다":"하고 있지 않다"));
    }
}