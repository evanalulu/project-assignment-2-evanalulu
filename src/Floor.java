import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Iterator;

public class Floor {
    private String structure;
    private int floors;
    private Deque<Passenger> passengerUp;
    private Deque<Passenger> passengerDown;
    public Floor(int floors, String structure) {
        this.floors = floors;
        if (structure == "linked") {
            passengerUp = new LinkedList<Passenger>();
            passengerDown = new LinkedList<Passenger>();
        } else {
            passengerUp = new ArrayDeque<Passenger>();
            passengerDown = new ArrayDeque<Passenger>();
        }
    }

    public void addPassenger(Passenger passenger) {
        if (passenger.getStart() > passenger.getDestination())
            passengerDown.add(passenger);
        else
            passengerUp.add(passenger);
    }

    public int getFloors() {
        return floors;
    }

    public Deque<Passenger> getPassengersDown() {
        return passengerDown;
    }

    public Deque<Passenger> getPassengersUp() {
        return passengerUp;
    }

    /**
     * Increments the travel tick count for each passenger in the elevator,
     * for both upward and downward directions.
     * Iterates through the passengers in the elevator moving up and moving down
     * and increments their ticks traveled by one.
     * Updates the longest time to destination for each passenger.
     */
    public void incrementTickCount() {
        Iterator<Passenger> upIterator;
        Iterator<Passenger> downIterator;
        upIterator = passengerUp.iterator();
        downIterator = passengerDown.iterator();

        while (upIterator.hasNext()) {
            Passenger passenger = upIterator.next();
            passenger.setLongestTimeToDest(passenger.getTimeTraveled() + 1);
        }

        while (downIterator.hasNext()) {
            Passenger passenger = downIterator.next();
            passenger.setLongestTimeToDest(passenger.getTimeTraveled() + 1);
        }
    }
}
