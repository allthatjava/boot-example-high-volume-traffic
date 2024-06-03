package brian.example.high.volume.traffic.bootexamplehighvolumetraffic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class AsyncService {

    @Autowired
    private ThreadPoolTaskExecutor executor;

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async("taskExecutor")
    public CompletableFuture<String> asyncHello(){
        logger.info("Active Count:{},Queue size:{}, Pool Size:{}",executor.getActiveCount(),executor.getQueueSize(),executor.getPoolSize());
        try{
            Thread.sleep(1000);
        }catch( InterruptedException e){
            Thread.currentThread().interrupt();
        }

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
