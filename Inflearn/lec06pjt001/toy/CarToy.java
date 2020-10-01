package toy;

import battery.Battery;
import battery.NormalBattery;

public class CarToy {
    public Battery battery;

    public CarToy(){
        this.battery = new NormalBattery();
    }
}
