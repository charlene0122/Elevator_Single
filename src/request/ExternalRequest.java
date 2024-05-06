package request;

import exception.InvalidRequestException;

public class ExternalRequest extends Request {
    private Direction direction;

    public ExternalRequest(int floor, int direction) {
        if (direction != 1 && direction != 2) {
            throw new InvalidRequestException("Invalid external request for direction!");
        }
        this.floor = floor;
        this.direction = direction == 1 ? Direction.UP : Direction.DOWN;
    }

    public Direction getDirection() {
        return direction;
    }
}
