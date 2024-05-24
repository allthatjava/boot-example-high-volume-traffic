package brian.example.high.volume.traffic.bootexamplehighvolumetraffic.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachedService {

    @Cacheable(value="cached-single")
    public String getHello(){
        System.out.println("Fetching and cache the data:"+System.currentTimeMillis());
        try{
            Thread.sleep(1000);
        }catch( InterruptedException e){
            Thread.currentThread().interrupt();
        }

        return "Cached Hello";
    }
}
