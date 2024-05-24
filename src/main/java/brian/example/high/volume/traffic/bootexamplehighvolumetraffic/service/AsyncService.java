package brian.example.high.volume.traffic.bootexamplehighvolumetraffic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async("taskExecutor")
    public CompletableFuture<String> asyncHello(){
        logger.info("Start asyncMethod");
        try{
            Thread.sleep(1000);
        }catch( InterruptedException e){
            Thread.currentThread().interrupt();
        }
        logger.info("End asyncMethod");

        return CompletableFuture.completedFuture("Hello from Async service");
    }

    public String syncHello(){
        try{
            Thread.sleep(1000);
        }catch( InterruptedException e){
            Thread.currentThread().interrupt();
        }

        return "Hello from Sync service";
    }
}
