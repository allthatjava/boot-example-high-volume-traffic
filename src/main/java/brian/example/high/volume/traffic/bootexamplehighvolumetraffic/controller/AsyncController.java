package brian.example.high.volume.traffic.bootexamplehighvolumetraffic.controller;

import brian.example.high.volume.traffic.bootexamplehighvolumetraffic.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncController {

    @Autowired
    private AsyncService service;

    @GetMapping("/sync")
    public String syncHello(){
        return service.syncHello();
    }

    @GetMapping("/async")
    public CompletableFuture<String> asyncHello(){
        return service.asyncHello();
    }
}
