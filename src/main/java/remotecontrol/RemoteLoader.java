package remotecontrol;

public class RemoteLoader {
    public static void main(String[] args) {
        RemoteControlWithUndo remote = new RemoteControlWithUndo();

//        Light light = new Light();
//        GarageDoor garageDoor = new GarageDoor();
//        LightOnCommand lightOn = new LightOnCommand(light);
//        LightOffCommand lightOff = new LightOffCommand(light);
//        remote.setCommand(0, lightOn, lightOff);
//        remote.onButtonWasPushed(0);
//        remote.offButtonWasPushed(0);
//        System.out.println(remote);
//        remote.undoButtonWasPushed();

//        CeilingFan ceilingFan = new CeilingFan("LivingRoom");
//        CeilingFanHighCommand ceilingFanHighCommand = new CeilingFanHighCommand(ceilingFan);
//        CeilingFanOffCommand ceilingOffCommand = new CeilingFanOffCommand(ceilingFan);
//        remote.setCommand(0, ceilingFanHighCommand, ceilingOffCommand);
//        remote.onButtonWasPushed(0);
//        remote.offButtonWasPushed(0);
//        System.out.println(remote);
//        remote.undoButtonWasPushed();
//        System.out.println(remote);

        Light light = new Light();
        LightOnCommand lightOn = new LightOnCommand(light);
        LightOffCommand lightOff = new LightOffCommand(light);
        Command[] partyOn = { lightOn };
        Command[] partyOff = { lightOff };
        MacroCommand partyOnMacro = new MacroCommand(partyOn);
        MacroCommand partyOffMacro = new MacroCommand(partyOff);
        remote.setCommand(0, partyOnMacro, partyOffMacro);

        remote.onButtonWasPushed(0);
        remote.offButtonWasPushed(0);
    }
}
