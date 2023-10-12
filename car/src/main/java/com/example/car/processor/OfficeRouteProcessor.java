package com.example.car.processor;

import com.example.car.provider.CarProvider;
import com.example.common.bean.Route;
import com.example.common.messages.OfficeRouteMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("OFFICE_ROUTE")
@RequiredArgsConstructor
public class OfficeRouteProcessor implements MessageProcessor<OfficeRouteMessage> {
    private final MessageConverter messageConverter;
    private final CarProvider carProvider;

    @Override
    public void process(String jsonMessage) {
        OfficeRouteMessage message = messageConverter.extractMessage(jsonMessage, OfficeRouteMessage.class);
        Route route = message.getRoute();
        carProvider.getCars().stream()
                .filter(car -> car.noBusy() && route.getCarName()
                        .equals(car.getName()))
                .findFirst()
                .ifPresent(car -> {
                    car.setRoute(route);
                    car.setBusy(true);
                    car.setLocation(null);
                });
    }
}
