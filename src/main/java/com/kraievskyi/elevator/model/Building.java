package com.kraievskyi.elevator.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class Building {

    private List<Integer>[] passengersOnFloor;
    private int floors;

    public Building() {
    }

    public Building(int floors) {
        this.floors = floors;
    }
}
