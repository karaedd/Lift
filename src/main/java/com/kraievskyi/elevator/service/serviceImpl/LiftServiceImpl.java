package com.kraievskyi.elevator.service.serviceImpl;

import static com.kraievskyi.elevator.model.Lift.MAX_PASSENGERS;

import com.kraievskyi.elevator.model.Building;
import com.kraievskyi.elevator.model.Lift;
import com.kraievskyi.elevator.service.LiftService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class LiftServiceImpl implements LiftService {
    private static final int RANDOM_FLOORS = 5 + (int) (Math.random() * ((20 - 5) + 1));
    private static final Random RANDOM = new Random();
    private static final int STEPS = 3;
    private final Building building;
    private final Lift lift;

    public LiftServiceImpl() {
        this.building = new Building(RANDOM_FLOORS);
        this.lift = new Lift(RANDOM_FLOORS);
        fillTheFloorWithPassengers();
        start();
    }

    public void go() {
        correctDirection();
        int nextFloor;
        if (checkIfLiftFull()) {
            nextFloor = lift.isDirection() ? lift.increaseCurrentFloor() : lift.decreaseCurrentFloor();
        } else nextFloor = goToAnotherFloorIfLiftFull();
        lift.setCurrentFloor(nextFloor);
    }

    public void correctDirection() {
        if (lift.getCurrentFloor() == 1) {
            lift.setDirection(true);
        } else if (lift.getCurrentFloor() == lift.getMaxFloor()) {
            lift.setDirection(false);
        }
    }

    public boolean checkIfLiftFull() {
        int[] passengers = lift.getPassengersOnLift();
        boolean isLiftFull = true;
        for (int i = 0; i < MAX_PASSENGERS; i++)
            if (passengers[i] == 0) {
                isLiftFull = false;
                break;
            }
        return !isLiftFull;
    }

    public boolean checkIfLiftEmpty() {
        for (int i : lift.getPassengersOnLift())
            if (i != 0) return false;
        return true;
    }

    public void addPassenger(int passengerFloor) {
        int[] passengers = lift.getPassengersOnLift();
        for (int i = 0; i < MAX_PASSENGERS; i++)
            if (passengers[i] == 0) {
                passengers[i] = passengerFloor;
                return;
            }
    }

    public int removePassengers() {
        int[] passengers = lift.getPassengersOnLift();
        int removedPassengersCount = 0;
        for (int i = 0; i < MAX_PASSENGERS; i++)
            if (passengers[i] == lift.getCurrentFloor()) {
                passengers[i] = 0;
                removedPassengersCount++;
            }
        return removedPassengersCount;
    }

    public int goToAnotherFloorIfLiftFull() {
        int result = 0;
        if (lift.isDirection()) {
            int maxFloor = lift.getMaxFloor();
            int min = maxFloor + 1;
            for (int i : lift.getPassengersOnLift())
                if (i != 0 && i < min) {
                    min = i;
                    result = (min != maxFloor + 1) ? min : 0;
                }
        } else {
            int max = 0;
            for (int i : lift.getPassengersOnLift())
                if (i > max) {
                    max = i;
                    result = max;
                }
        }
        return result;
    }

    public void start() {
        for (int i = 1; i <= STEPS; i++) {
            int addedPassengers = 0;
            int removedPassengers = removePassengersFromLift();
            if (checkIfLiftEmpty()) {
                lift.setDirection(directionAtTheRequestOfTheMajority());
                addedPassengers = addPassengersToLift();
            }
            if (removedPassengers == 0 && addedPassengers == 0) {
                i--;
            } else {
                createRandomPassengers(removedPassengers);
                info(i, removedPassengers, addedPassengers);
            }
            go();
        }
    }

    public int addPassengersToLift() {
        correctDirection();
        List<Integer>[] passengersOnFloor = building.getPassengersOnFloor();
        ArrayList<Integer> indexesToDelete = new ArrayList<>();
        for (int i = 0; i < passengersOnFloor[lift.getCurrentFloor() - 1].size() && checkIfLiftFull(); i++) {
            if (lift.isDirection()) {
                if (passengersOnFloor[lift.getCurrentFloor() - 1].get(i) > lift.getCurrentFloor()) {
                    indexesToDelete.add(i);
                    addPassenger(
                            passengersOnFloor[lift.getCurrentFloor() - 1].get(i));
                }
            } else {
                if (passengersOnFloor[lift.getCurrentFloor() - 1].get(i) < lift.getCurrentFloor()) {
                    indexesToDelete.add(i);
                    addPassenger(
                            passengersOnFloor[lift.getCurrentFloor() - 1].get(i));
                }
            }
        }
        if (indexesToDelete.size() > 0) {
            passengersOnFloor[lift.getCurrentFloor() - 1].subList(0, indexesToDelete.size()).clear();
        }

        return indexesToDelete.size();
    }


    public int removePassengersFromLift() {
        return removePassengers();
    }


    public void fillTheFloorWithPassengers() {
        List<Integer>[] passengersOnFloor = new List[building.getFloors()];
        for (int i = 0; i < building.getFloors(); i++) {
            passengersOnFloor[i] = randomQuantityPassengers(i + 1);
            building.setPassengersOnFloor(passengersOnFloor);
        }
    }

    public List<Integer> randomQuantityPassengers(int currentFloor) {
        ArrayList<Integer> floor = new ArrayList<>();
        int passOnTheFloor = (int) (Math.random() * 10);
        for (int i = 1; i < passOnTheFloor; i++) {
            floor.add(createRandomPassenger(currentFloor));
        }
        return floor;
    }

    public int createRandomPassenger(int currentFloor) {
        int randomPassenger = currentFloor;
        while (randomPassenger == currentFloor)
            randomPassenger = RANDOM.nextInt(building.getFloors()) + 1;

        return randomPassenger;
    }

    public void createRandomPassengers(int count) {
        List<Integer>[] passengersOnFloor = building.getPassengersOnFloor();
        for (int j = 0; j < count; j++)
            passengersOnFloor[lift.getCurrentFloor() - 1].add(
                    createRandomPassenger(lift.getCurrentFloor()));
    }

    public boolean directionAtTheRequestOfTheMajority() {
        List<Integer>[] passengersOnFloor = building.getPassengersOnFloor();
        if (lift.getCurrentFloor() == 1) {
            return true;
        } else if (lift.getCurrentFloor() == building.getFloors()) {
            return false;
        } else {
            int peopleWhoWantRise = 0;
            for (int i = 0; i < passengersOnFloor[lift.getCurrentFloor() - 1].size(); i++)
                if (passengersOnFloor[lift.getCurrentFloor() - 1].get(i) > lift.getCurrentFloor())
                    peopleWhoWantRise++;

            return passengersOnFloor[lift.getCurrentFloor() - 1].size() - peopleWhoWantRise < peopleWhoWantRise;
        }
    }

    public String toString() {
        List<Integer>[] passengersOnFloor = building.getPassengersOnFloor();
        StringBuilder result = new StringBuilder();
        for (int i = building.getFloors() - 1; i >= 0; i--) {
            if (lift.getCurrentFloor() != i + 1)
                result.append(i + 1)
                        .append(" floor: ")
                        .append(passengersOnFloor[i].toString())
                        .append("\n");
            else
                result
                        .append(i + 1)
                        .append(" floor: ")
                        .append(passengersOnFloor[i].toString())
                        .append(" Lift:{")
                        .append(lift)
                        .append("}\n");
        }
        return result.toString();
    }

    public void info(int step, int removedPassengers, int addedPassengers) {
        System.out.println("********************** Step " + step + " **********************");
        System.out.print(this);
        System.out.println("Leaved: " + removedPassengers + " Entered: " + addedPassengers + "\n");
    }
}
