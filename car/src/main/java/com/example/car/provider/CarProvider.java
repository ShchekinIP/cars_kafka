package com.example.car.provider;

import com.example.common.bean.Car;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "application")
public class CarProvider {
    private final List<Car> cars = new ArrayList<>();

}
