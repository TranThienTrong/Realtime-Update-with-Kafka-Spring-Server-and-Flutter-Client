package com.trong.producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trong.producer.model.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Component
@Slf4j
public class LibraryEventProducer {

    private static final String TOPIC = "library-events";

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Autowired
    ObjectMapper jsonService;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String data = jsonService.writeValueAsString(libraryEvent);


        List<Header> recordHeader = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        ProducerRecord<Integer, String> record = new ProducerRecord<>(TOPIC, null, key, data, recordHeader);

        var listenableFuture = kafkaTemplate.send(record);

        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error: {}", ex.getMessage());

                try {
                    throw ex;
                } catch (Throwable throwable) {
                    log.error(throwable.getMessage());
                }
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.info("Message sent success for key: {}, value: {}, partition: {}", key, data, result.getRecordMetadata().partition());
            }
        });
    }

}
