package com.takeaway.eventservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class EventServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(EventServiceApplication.class, args);
    }
}
