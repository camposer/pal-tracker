package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {
    private Map<String, String> env = new HashMap<>();

    public EnvController(
            @Value("${port:NOT SET}") String port,
            @Value("${memory.limit:NOT SET}") String memory,
            @Value("${cf.instance.index:NOT SET}") String instanceIndex,
            @Value("${cf.instance.addr:NOT SET}") String instanceAddr) {
        env.put("PORT", port);
        env.put("MEMORY_LIMIT", memory);
        env.put("CF_INSTANCE_INDEX", instanceIndex);
        env.put("CF_INSTANCE_ADDR", instanceAddr);
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return env;
    }
}
