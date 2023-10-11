package com.example.office.job;

import com.example.common.bean.Car;
import com.example.common.bean.Route;
import com.example.common.bean.RoutePath;
import com.example.common.bean.Station;
import com.example.common.messages.OfficeRouteMessage;
import com.example.common.messages.StationStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.office.provider.CarsProvider;
import com.example.office.provider.StationsProvider;
import com.example.office.service.PathService;
import com.example.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class RouteDistributeJob {
    private final PathService pathService;
    private final CarsProvider carsProvider;
    private final WaitingRoutesService waitingRoutesService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter messageConverter;
    private final StationsProvider stationsProvider;

    @Scheduled(initialDelay = 500, fixedDelay = 2000)
    private void distribute() {
        waitingRoutesService.routes()
                .stream()
                .filter(Route::isNotAssigned)
                .forEach(route -> {
                    String start = route.getPaths().get(0).getFrom().getName();
                    carsProvider.getCars().stream()
                            .filter(car -> start.equals(car.getLocation()) && car.noBusy())
                            .findFirst()
                            .ifPresent(car -> setRouteToCar(route, car));
                    if (route.isNotAssigned()) {
                        carsProvider.getCars()
                                .stream()
                                .filter(Car::noBusy)
                                .findFirst()
                                .ifPresent(car -> {
                                    String currentLocation = car.getLocation();
                                    if (!currentLocation.equals(start)) {
                                        RoutePath routePath = pathService.makePath(currentLocation, start);
                                        route.getPaths().add(0, routePath);
                                    }
                                    setRouteToCar(route, car);
                                });
                    }
                });
    }

    private void setRouteToCar(Route route, Car car) {
        route.setCarName(car.getName());
        Station station = stationsProvider.findStationAndRemoveCar(car.getName());
        car.setLocation(null);
        kafkaTemplate.sendDefault(messageConverter.toJson(new OfficeRouteMessage(route)));
        kafkaTemplate.sendDefault(messageConverter.toJson(new StationStateMessage(station)));
    }
}
