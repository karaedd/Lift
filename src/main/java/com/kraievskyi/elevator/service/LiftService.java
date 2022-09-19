package com.kraievskyi.elevator.service;

import java.util.List;

public interface LiftService {
    void start(); //starts the elevator cycle

    void go(); //moves the elevator

    boolean directionAtTheRequestOfTheMajority(); //chooses where the elevator will go for most people

    void correctDirection(); //

    boolean checkIfLiftFull(); //checks whether elevator full

    boolean checkIfLiftEmpty(); //checks whether elevator empty

    void addPassenger(int passengerFloor); //adds passenger to lift

    int removePassengers(); //removing passenger from floor

    int goToAnotherFloorIfLiftFull(); //moving lift to another floor if lift is ful

    int addPassengersToLift(); //adds passenger to lift

    int removePassengersFromLift(); //removing passenger from lift and makes some checks before

    void fillTheFloorWithPassengers(); //fill the floor with passengers

    List<Integer> randomQuantityPassengers(int currentFloor); //create random quantity of passengers

    int createRandomPassenger(int currentFloor); //create random passenger

    void createRandomPassengers(int count); //create random passengers on the floor instead of who leaved the lift

    void info(int step, int removedPassengers, int addedPassengers); //get info
}
