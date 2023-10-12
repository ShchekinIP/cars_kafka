package com.example.car.processor;

import com.example.common.messages.CarStateMessage;
import com.example.common.processor.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("CAR_STATE")
public class CarStateProcessor implements MessageProcessor<CarStateMessage> {
    @Override
    public void process(String jsonMessage) {
        //IGNORE
    }
}
