package elevator;

import request.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Elevator implements Runnable {
    protected int currentFloor;
    protected State state;
    protected ConcurrentSkipListSet<Integer> upStops;
    protected ConcurrentSkipListSet<Integer> downStops;
    protected Map<ElevatorState, ConcurrentSkipListSet<Integer>> floorStopsMap;
    protected ConcurrentSkipListSet<Integer> currentStops;
    private boolean running;

    private final static int open = -1000;
    private final static int close = -2000;

    // boolean moving = false;

    public Elevator() {
        this.currentFloor = 0;
        this.state = IdleState.getInstance();
        upStops = new ConcurrentSkipListSet<>();
        downStops = new ConcurrentSkipListSet<>();
        this.floorStopsMap = new ConcurrentHashMap<ElevatorState, ConcurrentSkipListSet<Integer>>();
        this.floorStopsMap.put(ElevatorState.UP, upStops);
        this.floorStopsMap.put(ElevatorState.DOWN, downStops);
        this.currentStops = new ConcurrentSkipListSet<>();
        this.running = false;
    }

    public void handleExternalRequest(ExternalRequest request) {
        synchronized (floorStopsMap) {
            if (Direction.UP.equals(request.getDirection())) {
                upStops.add(request.getFloor());
                System.out.println("Request to go up from floor " + request.getFloor() + " added.");
            }
            if (Direction.DOWN.equals(request.getDirection())) {
                downStops.add(request.getFloor());
                System.out.println("Request to go down from floor " + request.getFloor() + " added.");
            }
            floorStopsMap.notifyAll();
        }
    }

    public void handleInternalRequest(InternalRequest request) {
        int requestedFloor = request.getFloor();
        if (requestedFloor == open && state.equals(IdleState.getInstance())) {
            openGate();
            return;
        }
        if (requestedFloor == close && state.equals(IdleState.getInstance())) {
            closeGate();
            return;
        }

        if (currentFloor < requestedFloor) {
            upStops.add(requestedFloor);
        } else if (currentFloor > requestedFloor) {
            downStops.add(requestedFloor);
        }

    }

    public void openGate() {
        System.out.println("----- Arrived at floor " + currentFloor + ", Opening Gate -----");
    }

    public void closeGate() {
        System.out.println("----- Closing door at floor " + currentFloor + " -----");
    }

    public void move() {
        this.state.move(this);
    }

    protected void goUp(int floor) {
        System.out.println("Going up to floor " + floor);
        getAllIntermediateStops(currentFloor, floor);
        for (int i = currentFloor; i <= floor; i++) {
            System.out.println("Reaching floor " + i);
            this.currentFloor = i;
            if (upStops.contains(i)) {
                openGate();
                synchronized (upStops) {
                    upStops.remove(i);
                }
                closeGate();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void goDown(int floor) {
        System.out.println("Going down to floor " + floor);
        getAllIntermediateStops(currentFloor, floor);
        for (int i = currentFloor; i >= floor; i--) {
            System.out.println("Reaching floor " + i);
            this.currentFloor = i;
            if (downStops.contains(i)) {
                openGate();
                synchronized (downStops) {
                    downStops.remove(i);
                }
                closeGate();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            move();
        }
    }

    private void getAllIntermediateStops(int currentFloor, int nextFloor) {
        if (currentFloor == nextFloor) {
            return;
        }

        int start = currentFloor;
        int next = nextFloor;
        if (currentFloor > nextFloor) {
            start = nextFloor;
            next = currentFloor;
        }

        for (int i = start + 1; i <= next; i++) {
            currentStops.add(i);
        }
    }

    public void stop() {
        this.running = false;
    }
}
