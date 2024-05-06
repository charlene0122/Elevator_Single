package elevator;

public interface State {
    ElevatorState getState();
    void move(Elevator elevator);
}

