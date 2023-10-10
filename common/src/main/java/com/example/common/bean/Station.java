package com.example.common.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Station {
    private String name;
    private List<String> cars;
    private int x;
    private int y;

    private void addCar(String carName) {
        if (cars.contains(carName)) {
            cars.set(cars.indexOf(carName), carName);
        } else {
            cars.add(carName);
        }
    }

    private void removeCar(String carName) {
        cars.remove(carName);
    }
}
