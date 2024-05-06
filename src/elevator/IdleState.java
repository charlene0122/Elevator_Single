package elevator;

public class IdleState implements State {
    private static final ElevatorState state = ElevatorState.IDLE;
    private static State idleState;

    private IdleState() {
    }

    public static State getInstance() {
        if (idleState == null) {
            idleState = new IdleState();
        }
        return idleState;
    }

    @Override
    public ElevatorState getState() {
        return state;
    }

    @Override
    public void move(Elevator elevator) {
        synchronized (elevator.floorStopsMap) {
            while (elevator.upStops.isEmpty() && elevator.downStops.isEmpty()) {
                System.out.println("Elevator is idle. Waiting for requests...");
                try {
                    elevator.floorStopsMap.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!elevator.upStops.isEmpty()) {
                elevator.state = UpState.getInstance();
            } else if (!elevator.downStops.isEmpty()) {
                elevator.state = DownState.getInstance();
            }
        }
    }
}
