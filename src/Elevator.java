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
    public boolean addPerson(Passenger passenger) {
        boolean isBelowCapacity = elevatorUp.size() < capacity;
        boolean isAboveCapacity = elevatorDown.size() < capacity;
        if (isBelowCapacity && isAboveCapacity) {
            if (this.movingUp)
                elevatorUp.add(passenger);
            else
                elevatorDown.add(passenger);
            return true;
        }
        return false;
    }

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
            Passenger.totalTime += ticksTraveled;
            Passenger.totalPassengers++;
        }

        return true;
    }

    public void loadPassenger(Deque<Passenger> passengers) {
        if (movingUp) {
            while (elevatorUp.size() < capacity && !passengers.isEmpty())
                this.elevatorUp.add(passengers.poll());
        } else {
            while (elevatorDown.size() < capacity && !passengers.isEmpty())
                this.elevatorDown.add(passengers.poll());
        }
    }

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
