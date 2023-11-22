import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();

        // Check if a property file is provided as a command-line argument
        if (args.length > 0) {
            // Load properties from the provided file
            try (FileInputStream input = new FileInputStream(args[0])) {
                properties.load(input);
            } catch (IOException e) {
                System.err.println("Error loading property file: " + e.getMessage());
            }
        } else {
            // Setting default keys and property vals
            properties.setProperty("structures", "linked");
            properties.setProperty("floors", "32");
            properties.setProperty("passengers", "0.03");
            properties.setProperty("elevators", "1");
            properties.setProperty("elevatorCapacity", "10");
            properties.setProperty("duration", "500");
        }

        String structures = properties.getProperty("structures");
        int floors = Integer.parseInt(properties.getProperty("floors"));
        float passengers = Float.parseFloat(properties.getProperty("passengers"));
        int elevators = Integer.parseInt(properties.getProperty("elevators"));
        int elevatorCapacity = Integer.parseInt(properties.getProperty("elevatorCapacity"));
        int duration = Integer.parseInt(properties.getProperty("duration"));

        Floor[] floor = new Floor[floors];
        for (int i = 0; i < floors; i++)
            floor[i] = new Floor(i, structures);

        Elevator[] elevator = new Elevator[elevators];
        for (int i = 0; i < elevators; i++)
            elevator[i] = new Elevator(elevatorCapacity);

        for (int i = 0; i < duration; i++)
            processElevatorAndFloors(elevator, floor, floors, passengers);

        printResults();
    }

    /**
     * Prints the statistical results related to passenger travel ticks.
     * Prints total passengers, total ticks traveled, average ticks traveled per passenger,
     * minimum ticks traveled to destination, and maximum ticks traveled to destination.
     */
    private static void printResults() {
        System.out.printf("Total passengers: %d\n", Passenger.totalPassengers);
        System.out.printf("Total Ticks Traveled: %d\n", Passenger.totalTime);
        System.out.printf("Average Ticks Traveled: %f\n", ((float) Passenger.totalTime / Passenger.totalPassengers));
        System.out.printf("Minimum Ticks Traveled: %d\n", Passenger.shortestTimeToDest);
        System.out.printf("Maximum Ticks Traveled: %d\n", Passenger.longestTimeToDest);
    }

    /**
     * Simulates the movement and processing of elevators and floors.
     *
     * @param elevator            Array of Elevator objects.
     * @param floors              Array of Floor objects.
     * @param numOfFloors         Total number of floors in the building.
     * @param passengerArrivalRate Rate of passenger arrival at each floor.
     */
    private static void processElevatorAndFloors(Elevator[] elevator, Floor[] floors, int numOfFloors, float passengerArrivalRate) {
        for (Floor floor : floors) {
            if (Math.random() < passengerArrivalRate)
                floor.addPassenger(new Passenger(floor.getFloors(), numOfFloors - 1));

            floor.incrementTickCount();
        }

        for (Elevator singleElevator : elevator) {
            singleElevator.addTick();

            for (Floor floor : floors) {
                printFloorStatus(floor);
            }

            manageElevatorMovement(singleElevator, floors, numOfFloors);
        }
    }

    /**
     * Prints the status of a specific floor including its number, count of passengers moving up, and count of passengers moving down.
     *
     * @param floor The Floor object for which the status is to be printed.
     */
    private static void printFloorStatus(Floor floor) {
        System.out.printf("Floor: %d\n", floor.getFloors());
        System.out.printf("Moving up: %d\n", floor.getPassengersUp().size());
        System.out.printf("Moving down: %d\n\n", floor.getPassengersDown().size());
    }

    /**
     * Manages the movement of the elevator based on its current direction.
     * It loads and unloads passengers, updates the elevator's direction, and manages its movement accordingly.
     *
     * @param elevator    The Elevator object whose movement is to be managed.
     * @param floors      Array of Floor objects representing the building's floors.
     * @param numOfFloors Total number of floors in the building.
     */
    private static void manageElevatorMovement(Elevator elevator, Floor[] floors, int numOfFloors) {
        if (elevator.isMovingUp()) {
            manageElevatorMovingUp(elevator, numOfFloors);
        } else {
            manageElevatorMovingDown(elevator, numOfFloors);
        }

        elevator.removePassenger(elevator.getFloor());

        if (elevator.isMovingUp()) {
            elevator.loadPassenger(floors[elevator.getFloor()].getPassengersUp());
        } else {
            elevator.loadPassenger(floors[elevator.getFloor()].getPassengersDown());
        }

        updateElevatorDirection(elevator, numOfFloors);
    }

    /**
     * Manages the movement of the elevator when moving upward.
     * It determines the next floor to move to based on elevator's current state and passengers' destinations.
     * Handles the movement logic for the elevator when it's moving upwards.
     *
     * @param elevator    The Elevator object to manage its upward movement.
     * @param numOfFloors Total number of floors in the building.
     */
    private static void manageElevatorMovingUp(Elevator elevator, int numOfFloors) {
        if (elevator.getElevatorUp().isEmpty()) {
            if (elevator.getFloor() + 5 <= numOfFloors - 1) {
                elevator.setFloor(elevator.getFloor() + 5);
            } else {
                elevator.setFloor(numOfFloors - 1);
            }
        } else {
            Passenger root = elevator.getElevatorUp().peek();
            if (root.getDestination() - elevator.getFloor() > 5) {
                elevator.setFloor(elevator.getFloor() + 5);
            } else {
                elevator.setFloor(root.getDestination());
            }
        }
    }

    /**
     * Manages the movement of the elevator when moving downward.
     * It determines the next floor to move to based on elevator's current state and passengers' destinations.
     * Handles the movement logic for the elevator when it's moving downwards.
     *
     * @param elevator    The Elevator object to manage its downward movement.
     * @param numOfFloors Total number of floors in the building.
     */
    private static void manageElevatorMovingDown(Elevator elevator, int numOfFloors) {
        if (elevator.getElevatorDown().isEmpty()) {
            if (elevator.getFloor() - 5 >= 0) {
                elevator.setFloor(elevator.getFloor() - 5);
            } else {
                elevator.setFloor(0);
            }
        } else {
            Passenger root = elevator.getElevatorDown().peek();
            if (elevator.getFloor() - root.getDestination() > 5) {
                elevator.setFloor(elevator.getFloor() - 5);
            } else {
                elevator.setFloor(root.getDestination());
            }
        }
    }

    /**
     * Updates the direction of the elevator based on its current floor and the total number of floors in the building.
     * If the elevator reaches the top floor, it sets the direction to downward.
     * If the elevator reaches the ground floor, it sets the direction to upward.
     *
     * @param elevator    The Elevator object to update its direction.
     * @param numOfFloors Total number of floors in the building.
     */
    private static void updateElevatorDirection(Elevator elevator, int numOfFloors) {
        if (elevator.getFloor() == numOfFloors - 1) {
            elevator.setMovingUp(false);
        } else if (elevator.getFloor() == 0) {
            elevator.setMovingUp(true);
        }
    }

}