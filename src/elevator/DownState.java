package elevator;

public class DownState implements State {

    private static final ElevatorState state = ElevatorState.DOWN;
    private static State downState;

    private DownState() {
    }

    public static State getInstance() {
        if (downState == null) {
            downState = new DownState();
        }
        return downState;
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
        if (!elevator.downStops.isEmpty() && elevator.downStops.floor(elevator.currentFloor) != null) {
            elevator.goDown(elevator.downStops.floor(elevator.currentFloor));
        } else if (!elevator.upStops.isEmpty() && elevator.upStops.first() < elevator.currentFloor) {
            elevator.goDown(elevator.upStops.first());
        } else {
            elevator.state = UpState.getInstance();
            elevator.goUp(elevator.upStops.first());
        }
    }
}
