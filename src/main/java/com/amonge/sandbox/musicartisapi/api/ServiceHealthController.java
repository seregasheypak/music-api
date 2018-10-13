package com.amonge.sandbox.musicartisapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceHealthController {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceHealthController.class);

    // what code it returns 200?
    // what if we smoke-test remote services here also?
    // what if return json which is easy to parse
    @RequestMapping("/ping")
    public String ping() {
        LOG.info("Checking server health");
        return "Service is up and running";
    }

    @RequestMapping("/ping")
    public String stats() {
        LOG.info("Checking stats");
        return "hm... count of OK / FAIL calls to each service after last restart?";
    }


}
