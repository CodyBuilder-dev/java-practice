class CarTester1 {
    public static void main(String[] args){
        Car car1 = new Car("W140","KMS",
                            1885,1490,5220,
                            95.0,0.0,15.0);
        ExCar car2 = new ExCar("W221","KJS",
                            1845,1490,5205,
                            100.0,0.0,10.0);

        Car x;

        //x = car1;
        x = car2;

        System.out.println(x.getTotalMileage());

    }
}