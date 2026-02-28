class Car {
    private String name;
    private String number;
    private int width;
    private int height;
    private int length;
    private double x;
    private double y;
    private double tank;
    private double fuel;
    private double sfc;

    Car(String name,String number, int width,int height, int length,
        double tank, double fuel, double sfc){
        this.name = name;
        this.number= number;
        this.width = width;
        this.height = height;
        this.length = length;
        this.tank = tank;
        this.fuel =fuel;
        this.sfc = sfc;
    }

    double getX(){ return this.x; }
    double getY(){ return this.y; }
    double getFuel(){ return this.fuel; }

    void getSpec() {
        System.out.println(this.name);
        System.out.println(this.number);
        System.out.println(this.width);
        System.out.println(this.height);
        System.out.println(this.length);
        System.out.println(this.tank);
        System.out.println(this.fuel);
        System.out.println(this.sfc);
    }

    boolean move(double dx, double dy){
        double dist = Math.sqrt(dx * dx + dy * dy);
        double f = dist/this.sfc;

        if( f > this.fuel) {
            return false;
        }
        else {
            this.fuel -= f;
            this.x += dx;
            this.y += dy;
            return true;
        }
    }
    
    void refuel(double df){
        this.fuel += df;
    }

    void putSpec(){
        System.out.println("이름 : "+ this.name);
        System.out.println("전폭 : "+ this.width);
        System.out.println("전고 : "+ this.height);
        System.out.println("전장 : "+ this.length);
    }
}


class ExCar extends Car {
    private double totalMileage;

    ExCar(String name,String number, int width,int height, int length,
        double tank, double fuel, double sfc){
        super(name,number,width,height,length,tank,fuel,sfc);
        this.totalMileage = 0.0;

    }

    public double getTotalMileage() {
        return this.totalMileage;
    }
 
    public void putSpec() {
        super.putSpec();
        System.out.printf("총 주행 거리 : %.2f km\n",this.totalMileage);
    }
    public static void main(String[] args){
        ExCar myCar = new ExCar("W221","6852",1846,1490,5205,100.0,0.0,10.0);

        System.out.printf("현재위치 : %.2f,%.2f\n",myCar.getX(),myCar.getY());
        System.out.printf("남은 연료 : %.2f\n",myCar.getFuel());
        myCar.putSpec();

    }
}
