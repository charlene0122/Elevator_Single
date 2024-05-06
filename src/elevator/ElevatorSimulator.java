package elevator;

public class ElevatorSimulator {
    public static void main(String[] args) {
        ElevatorSystem system = ElevatorSystem.getInstance();
        Elevator elevator = system.getElevator();

        Thread elevatorThread = new Thread(elevator);
        Thread elevatorSystemThread = new Thread(system);

        elevatorThread.start();
        elevatorSystemThread.start();
    }

}
