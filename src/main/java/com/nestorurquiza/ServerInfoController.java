package com.nestorurquiza;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;

@RestController
public class ServerInfoController {

    @GetMapping("/api/server_info")
    public Map<String, String> getServerInfo() {
        try {
            return Collections.singletonMap("hostname", InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            return Collections.singletonMap("error", "Hostname could not be resolved");
        }
    }
}
