package com.example.office.processors;

import com.example.common.bean.Car;
import com.example.common.bean.Route;
import com.example.common.bean.Station;
import com.example.common.messages.CarStateMessage;
import com.example.common.messages.StationStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import com.example.office.provider.CarsProvider;
import com.example.office.provider.StationsProvider;
import com.example.office.service.WaitingRoutesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("CAR_STATE")
@RequiredArgsConstructor
public class CarStateProcessor implements MessageProcessor<CarStateMessage> {
    private final MessageConverter messageConverter;
    private final WaitingRoutesService waitingRoutesService;
    private final CarsProvider carsProvider;
    private final StationsProvider stationsProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    public void process(String jsonMessage) {
        CarStateMessage message = messageConverter.extractMessage(jsonMessage, CarStateMessage.class);
        Car car = message.getCar();
        Optional<Car> previous = carsProvider.getCarByName(car.getName());
        Station station = stationsProvider.getStationByName(car.getLocation());

        carsProvider.addCar(car);
        if (previous.isPresent() && car.hasRoute() && previous.get().hasRoute()) {
            Route route = car.getRoute();
            waitingRoutesService.remove(route);
        }

        if (previous.isEmpty() || !car.isBusy() && previous.get().isBusy()) {
            station.addCar(car.getName());
            kafkaTemplate.sendDefault(messageConverter.toJson(new StationStateMessage(station)));
        }
    }
}
