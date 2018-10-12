package com.amonge.sandbox.musicartisapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceHealthController {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceHealthController.class);

    @RequestMapping("/ping")
    public String ping() {
        LOG.info("Checking server health");
        return "Service is up and running";
    }
}
