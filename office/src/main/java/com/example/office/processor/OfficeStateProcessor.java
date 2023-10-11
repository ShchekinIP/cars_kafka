package com.example.office.processor;

import com.example.common.messages.OfficeStateMessage;
import com.example.common.messages.StationStateMessage;
import com.example.common.processor.MessageConverter;
import com.example.common.processor.MessageProcessor;
import com.example.office.provider.StationsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("OFFICE_STATE")
@RequiredArgsConstructor
public class OfficeStateProcessor implements MessageProcessor<OfficeStateMessage> {
    private final MessageConverter messageConverter;
    private final StationsProvider stationsProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void process(String jsonMessage) {
        stationsProvider.getStations().forEach(station -> {
            kafkaTemplate.sendDefault(messageConverter.toJson(new StationStateMessage(station)));
        });

    }
}
