package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableConfigServer
public class DemoApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	/*public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}*/

    public static void main(String[] args) {

        Map<String, String> systemvars = System.getenv();
        systemvars
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e2, LinkedHashMap::new))
                .forEach((u,v) -> LOGGER.info(u + "---->" + v));

        int a = 0b10100000000000000000000000000000;
    }

}
