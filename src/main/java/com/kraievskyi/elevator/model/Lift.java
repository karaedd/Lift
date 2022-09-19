package com.kraievskyi.elevator.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Lift {
    public static final int MAX_PASSENGERS = 5;
    private int[] passengersOnLift = new int[MAX_PASSENGERS];

    private int currentFloor = 1;
    private int maxFloor;
    private boolean direction = true;

    public Lift() {
    }

    public Lift(int maxFloor) {
        this.maxFloor = maxFloor;
    }

    public int increaseCurrentFloor() {
        currentFloor++;
        return currentFloor;
    }

    public int decreaseCurrentFloor() {
        currentFloor--;
        return currentFloor;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int passenger : passengersOnLift) {
            if (passenger != 0) {
                result
                        .append(passenger)
                        .append(" ");
            }
        }
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }
}
