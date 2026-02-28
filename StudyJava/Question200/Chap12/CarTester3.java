class CarTester3 {
    public static void main(String[] args){
        Car car1 = new ExCar("W221","KJS",
                            1845,1490,5205,
                            100.0,50.0,10.0);

        car1.move(10,10);
        System.out.println(car1.getX());
        System.out.println(car1.getY());
        System.out.println(((ExCar)car1).getTotalMileage());
    }
}