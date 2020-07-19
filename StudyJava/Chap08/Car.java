package StudyJava.Chap08;

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
}
