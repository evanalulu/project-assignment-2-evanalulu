import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
public class Elevator {
    private int capacity;
    private boolean movingUp; // false = down, true = up
    private int floor;

    private PriorityQueue<Passenger> elevatorUp = new PriorityQueue<>(); // minheap
    private PriorityQueue<Passenger> elevatorDown = new PriorityQueue<>(Collections.reverseOrder()); // maxheap


    public Elevator(int capacity) {
        this.capacity = capacity;
        this.movingUp = true; // up
        this.floor = floor;
    }

    // getters n setters
    public int getCapacity() {
        return capacity;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public PriorityQueue<Passenger> getElevatorUp() {
        return elevatorUp;
    }

    public PriorityQueue<Passenger> getElevatorDown() {
        return elevatorDown;
    }

    /**
     * Removes passengers who have reached their destination floor from the elevator,
     * updating statistics related to their travel time.
     * Checks the elevator's direction and removes passengers from the corresponding queue
     * until no more passengers have reached the specified floor or the elevator is empty.
     *
     * @param floor The floor number to which passengers' destination is checked for removal.
     * @return True if passengers are removed; False if the elevator is empty.
     */
    public boolean removePassenger(int floor) {
        PriorityQueue<Passenger> elevatorPassengers;

        if (movingUp)
            elevatorPassengers = elevatorUp;
        else
            elevatorPassengers = elevatorDown;

        if (elevatorPassengers.isEmpty())
            return false;

        while (!elevatorPassengers.isEmpty() && elevatorPassengers.peek().getDestination() == floor) {
            Passenger passenger = elevatorPassengers.poll();
            int ticksTraveled = passenger.getTimeTraveled();
            Passenger.longestTimeToDest = Math.max(Passenger.longestTimeToDest, ticksTraveled);
            Passenger.shortestTimeToDest = Math.min(Passenger.shortestTimeToDest, ticksTraveled);
            Passenger.totalTime += ticksTraveled;
            Passenger.totalPassengers++;
        }

        return true;
    }

    /**
     * Loads passengers from the provided Deque into the elevator based on its direction.
     * If the elevator is moving up, it loads passengers into the upward-moving elevator queue
     * until it reaches capacity or the provided passenger queue becomes empty.
     * If the elevator is moving down, it loads passengers into the downward-moving elevator queue
     * until it reaches capacity or the provided passenger queue becomes empty.
     *
     * @param passengers The Deque of Passenger objects to load into the elevator.
     */
    public void loadPassenger(Deque<Passenger> passengers) {
        if (movingUp) {
            while (elevatorUp.size() < capacity && !passengers.isEmpty())
                this.elevatorUp.add(passengers.poll());
        } else {
            while (elevatorDown.size() < capacity && !passengers.isEmpty())
                this.elevatorDown.add(passengers.poll());
        }
    }

    /**
     * Increases the tick count for each passenger in the elevator.
     * Iterates through the passengers in the elevator (moving up or down)
     * and increments their ticks traveled by one.
     * Updates the longest time to destination for each passenger in the elevator.
     */
    public void addTick() {
        Iterator<Passenger> iterator;

        if (movingUp) {
            iterator = elevatorUp.iterator();
        } else {
            iterator = elevatorDown.iterator();
        }

        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            int ticksTraveled = passenger.getTimeTraveled();
            passenger.setLongestTimeToDest(ticksTraveled + 1);
        }
    }

}
