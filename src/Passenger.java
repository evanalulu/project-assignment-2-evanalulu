import java.util.Random;

class Passenger implements Comparable<Passenger> {
    private int start;
    private int destination;
    private Random random = new Random();
    private int timeTraveled = 0;
    public static int totalPassengers = 0;
    public static int totalTime = 0;

    public static int longestTimeToDest = Integer.MIN_VALUE;
    public static int shortestTimeToDest = Integer.MAX_VALUE;
    public Passenger(int start, int maxFloor) {
        this.start = start;
        this.destination = this.start;
        while (this.destination == this.start) // Ensure the person doesn't start and go to the same floor
            this.destination = random.nextInt(maxFloor + 1);
    }
    public int compareTo(Passenger passenger) {
        return (Integer.compare(this.destination, passenger.destination));
    }

    public int getStart() {
        return this.start;
    }

    public int getDestination() {
        return this.destination;
    }
    public static int getLongestTimeToDest() {
        return longestTimeToDest;
    }

    public int getShortestTimeToDest() {
        return shortestTimeToDest;
    }

    public void setShortestTimeToDest(int ticksTraveled) {
        this.timeTraveled = ticksTraveled;
    }
    public void setLongestTimeToDest(int ticksTraveled) {
        this.timeTraveled = ticksTraveled;
    }

    public int getTimeTraveled() {
        return this.timeTraveled;
    }
}
