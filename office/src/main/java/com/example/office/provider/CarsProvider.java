package com.example.office.provider;

import com.example.common.bean.Car;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class CarsProvider {
    private final List<Car> cars = new ArrayList<>();
    private final Lock lock = new ReentrantLock(true);

    public Optional<Car> getCarByName(String carName) {
        return cars.stream()
                .filter(car -> car.getName().equals(carName))
                .findFirst();
    }

    public List<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        try {
            lock.lock();
            Optional<Car> carByName = getCarByName(car.getName());
            if (carByName.isPresent()) {
                int index = cars.indexOf(carByName.get());
                cars.set(index, car);
            } else {
                cars.add(car);
            }
        } finally {
            lock.unlock();
        }
    }
}
