package com.trong.consumer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trong.consumer.ConsumerApplication;
import com.trong.consumer.consumer.SseController;
import com.trong.consumer.model.LibraryEvent;
import com.trong.consumer.repository.LibraryEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class LibraryEventService {

    @Autowired
    ObjectMapper jsonService;

    @Autowired
    LibraryEventRepository libraryEventRepository;

    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws IOException {
        LibraryEvent libraryEvent = jsonService.readValue(consumerRecord.value(), LibraryEvent.class);

        switch (libraryEvent.getLibraryEventType()){
            case NEW:
                save(libraryEvent);
                break;
            case UPDATE:
                update(libraryEvent);
                break;
            default:
                log.info("Unknown Library Event Type");
        }
    }



    private void save(LibraryEvent libraryEvent) throws IOException {
        String data = jsonService.writeValueAsString(libraryEvent);
        libraryEvent.getBook().setLibraryEvent(libraryEvent);
        libraryEventRepository.save(libraryEvent);
        log.info("Saved Success {}", libraryEvent);

        for(SseEmitter emitter: SseController.emitterList){
            emitter.send(SseEmitter.event().name("library-events").data(data));
        }
    }

    private void update(LibraryEvent libraryEvent){

        if(libraryEvent.getLibraryEventId() == null){
            throw new IllegalArgumentException("Library Event ID is Missing");
        }

        Optional<LibraryEvent> libraryEventOptional = libraryEventRepository.findById(libraryEvent.getLibraryEventId());

        if(libraryEventOptional.isPresent()){
            libraryEvent.getBook().setLibraryEvent(libraryEvent);
            libraryEventRepository.save(libraryEvent);
            log.info("Updated Success {}", libraryEvent);
        } else {
            throw new IllegalArgumentException("Library Event Not Found");
        }
    }
}
