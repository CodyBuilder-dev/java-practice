import toy.CarToy;
import toy.RobotToy;
import toy.RadioToy;
import battery.ChargeBattery;
import battery.NormalBattery;

public class MainClass {
    public static void main(String[] args) {
        CarToy cartoy = new CarToy();
        System.out.println(cartoy.battery.getBatteryValue());

        RobotToy robottoy = new RobotToy();
        robottoy.setBattery(new ChargeBattery());
        System.out.println(robottoy.battery.getBatteryValue());
        
        RadioToy radiotoy = new RadioToy(new ChargeBattery());
        System.out.println(radiotoy.battery.getBatteryValue());
    }
}
