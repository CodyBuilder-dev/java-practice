class PrintAscii {
    public static void main(String[] args){
        for (char i = 0x21; i<= 0x7E ; i++)
            System.out.printf("%c %04x\n",i,(int)i);
        //유니코드 확장 동작방식 :해당 유니코드 확장을 컴파일단계에서 치환
        System.out.println("ABC\u000aDEF"); //컴파일에러
        /*System.out.println("ABC
        DEF") 로 치환되기 때문*/
        
    }
}