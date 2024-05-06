package elevator;

public class UpState implements State {

    private static final ElevatorState state = ElevatorState.UP;
    private static State upState;

    private UpState() {
    }

    public static State getInstance() {
        if (upState == null) {
            upState = new UpState();
        }
        return upState;
    }

    @Override
    public ElevatorState getState() {
        return state;
    }

    @Override
    public void move(Elevator elevator) {
        if (elevator.upStops.isEmpty() && elevator.downStops.isEmpty()) {
            elevator.state = IdleState.getInstance();
            return;
        }
        if (!elevator.upStops.isEmpty() && elevator.upStops.ceiling(elevator.currentFloor) != null) {
            elevator.goUp(elevator.upStops.ceiling(elevator.currentFloor));
        } else if (!elevator.downStops.isEmpty() && elevator.downStops.first() > elevator.currentFloor) {
            elevator.goUp(elevator.downStops.first());
        } else {
            elevator.state = DownState.getInstance();
            elevator.goDown(elevator.downStops.first());
        }
    }

}
