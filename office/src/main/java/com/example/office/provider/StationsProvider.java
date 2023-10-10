package com.example.office.provider;

import com.example.common.bean.RoutePoint;
import com.example.common.bean.Station;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Getter
@Component
@ConfigurationProperties(prefix = "application")
public class StationsProvider {
    private final List<Station> stations = new ArrayList<>();

    public Station findStationAndRemoveCar(String carName) {
        AtomicReference<Station> res = new AtomicReference<>();
        stations.stream()
                .filter(station -> station.getCars().contains(carName))
                .findFirst()
                .ifPresent(station -> {
            station.removeCar(carName);
            res.set(station);
        });
        return res.get();
    }

    public Station getStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.getName().equals(stationName))
                .findFirst()
                .orElse(new Station());
    }

    public RoutePoint getRoutePoint(String stationName) {
        return new RoutePoint(getStationByName(stationName));
    }

}
