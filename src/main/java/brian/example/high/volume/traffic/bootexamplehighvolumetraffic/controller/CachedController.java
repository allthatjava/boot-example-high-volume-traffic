package brian.example.high.volume.traffic.bootexamplehighvolumetraffic.controller;

import brian.example.high.volume.traffic.bootexamplehighvolumetraffic.service.CachedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CachedController {

    @Autowired
    private CachedService service;

    @GetMapping("/cached")
    public String cached(){
        return service.getHello();
    }
}
