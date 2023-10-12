package com.example.car.job;

import com.example.car.provider.CarProvider;
import com.example.common.bean.Car;
import com.example.common.bean.RoutePath;
import com.example.common.messages.CarStateMessage;
import com.example.common.processor.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class CarJob {
    private final CarProvider carProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter messageConverter;

    @Scheduled(initialDelay = 0, fixedDelay = 250)
    public void drive() {
        carProvider.getCars()
                .stream()
                .filter(Car::hasRoute)
                .forEach(car -> car.getRoute().getPaths()
                        .stream()
                        .filter(RoutePath::isProgress)
                        .findFirst()
                        .ifPresent(routePath -> {
                            car.calculatePosition(routePath);
                            routePath.addProgress(car.getSpeed());
                            if (routePath.isDone()) {
                                car.setLocation(routePath.getTo().getName());
                            }
                        }));
    }

    @Scheduled(initialDelay = 0, fixedDelay = 1000)
    @Async
    public void notifyState() {
        carProvider.getCars()
                .stream()
                .filter(Car::isBusy)
                .forEach(car -> {
                    Optional<RoutePath> routePath = car.getRoute().getPaths()
                            .stream()
                            .filter(RoutePath::isProgress)
                            .findAny();

                    if (routePath.isEmpty()) {
                        List<RoutePath> paths = car.getRoute().getPaths();
                        car.setLocation(paths.get(paths.size() - 1).getTo().getName());
                        car.setBusy(false);
                    }

                    CarStateMessage message = new CarStateMessage(car);
                    kafkaTemplate.sendDefault(messageConverter.toJson(message));
                });
    }
}
