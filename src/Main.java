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

    private static void printResults() {
        System.out.printf("Total passengers: %d\n", Passenger.totalPassengers);
        System.out.printf("Total Ticks Traveled: %d\n", Passenger.totalTime);
        System.out.printf("Average Ticks Traveled: %f\n", ((float) Passenger.totalTime / Passenger.totalPassengers));
        System.out.printf("Minimum Ticks Traveled: %d\n", Passenger.shortestTimeToDest);
        System.out.printf("Maximum Ticks Traveled: %d\n", Passenger.longestTimeToDest);
    }
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

    private static void printFloorStatus(Floor floor) {
        System.out.printf("Floor: %d\n", floor.getFloors());
        System.out.printf("Moving up: %d\n", floor.getPassengersUp().size());
        System.out.printf("Moving down: %d\n\n", floor.getPassengersDown().size());
    }

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

    private static void updateElevatorDirection(Elevator elevator, int numOfFloors) {
        if (elevator.getFloor() == numOfFloors - 1) {
            elevator.setMovingUp(false);
        } else if (elevator.getFloor() == 0) {
            elevator.setMovingUp(true);
        }
    }

}