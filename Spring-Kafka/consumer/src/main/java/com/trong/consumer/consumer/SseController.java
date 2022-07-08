package com.trong.consumer.consumer;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SseController {

    public static List<SseEmitter> emitterList = new CopyOnWriteArrayList<>();

    // Method for Client Subscription
    @CrossOrigin
    @RequestMapping(value = "/subscribe")
    public SseEmitter subscribe(){
        System.out.println("Client Connected");
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        sseEmitter.onCompletion(() -> {
            synchronized (this.emitterList) {
                this.emitterList.remove(sseEmitter);
            }
        });


        // Put context in a map
        emitterList.add(sseEmitter);


        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (IOException e){
            e.printStackTrace();
        }
        emitterList.add(sseEmitter);
        return sseEmitter;
    }


    // Method for dispatching Event to All Clients
    @PostMapping(value = "/dispatchEvent")
    public void dispatchEventToClients(@RequestParam String news) throws IOException {
        for(SseEmitter emitter: emitterList){
            emitter.send(SseEmitter.event().name("news").data(news));
        }

    }

}
