package toy;

import battery.Battery;

public class RadioToy {
    public Battery battery;

    public RadioToy(Battery battery) {
        this.battery = battery;
    }
    public void setBattery(Battery battery) {
        this.battery = battery;
    }
}
