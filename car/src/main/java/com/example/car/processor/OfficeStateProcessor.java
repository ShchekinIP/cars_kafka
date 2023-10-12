package com.example.car.processor;

import com.example.car.provider.CarProvider;
import com.example.common.messages.CarStateMessage;
import com.example.common.messages.OfficeStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("OFFICE_STATE")
@RequiredArgsConstructor
public class OfficeStateProcessor implements MessageProcessor<OfficeStateMessage> {
    private  final MessageConverter messageConverter;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CarProvider carProvider;

    @Override
    public void process(String jsonMessage) {
        carProvider.getCars().forEach(car -> kafkaTemplate.sendDefault(messageConverter.toJson(new CarStateMessage(car))));
    }
}
