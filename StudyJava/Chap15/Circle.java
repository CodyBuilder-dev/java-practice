class Circle {
    public static void main(String[] args){
        // 문자열로 들어온 커맨드라인 인수를 double 타입으로 변환
        double r = Double.parseDouble(args[0]);
        
        System.out.println(r*r);
    }
}