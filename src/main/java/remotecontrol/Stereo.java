package remotecontrol;

public class Stereo {

    boolean on() {
        System.out.println("LightOn");
        return true;
    }

    boolean off() {
        System.out.println("LightOff");
        return true;
    }

    boolean setCD() {
        System.out.println("CD Set");
        return true;
    }

    boolean setDVD() {
        System.out.println("DVD Set");
        return true;
    }

    boolean setVolume() {
        System.out.println("Volume Set");
        return true;
    }
}
