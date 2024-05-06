package elevator;

import java.util.Scanner;

import request.*;

public class ElevatorSystem implements Runnable {

    private Elevator elevator;
    private static ElevatorSystem system;
    private boolean running;
    private final Scanner scanner;

    private ElevatorSystem() {
        this.elevator = new Elevator();
        this.running = false;
        this.scanner = new Scanner(System.in);
    }

    public static ElevatorSystem getInstance() {
        if (system == null) {
            system = new ElevatorSystem();
        }
        return system;
    }

    public Elevator getElevator() {
        return this.elevator;
    }

    @Override
    public void run() {
        this.running = true;
        while (this.running) {
            System.out.println("Are you going up or down? Press 1 for up and 2 for down: ");
            int direction = scanner.nextInt();
            System.out.println("Enter the floor you want to go to: ");
            int nextFloor = scanner.nextInt();
            ExternalRequest request = new ExternalRequest(nextFloor, direction);
            handleExternalRequest(request);
        }
    }

    public void stop() {
        this.running = false;
    }

    public void handleExternalRequest(ExternalRequest request) {
        this.elevator.handleExternalRequest(request);
    }
}
