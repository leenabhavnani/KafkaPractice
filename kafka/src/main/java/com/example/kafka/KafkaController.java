package com.example.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping(value="/produce", method= RequestMethod.GET)
    public String produceKafkaMessage() {
        kafkaTemplate.send("messenger", "This message is coming from Producer - Hello Kafka");
        return "Message successfully Produced to Kafka";
    }

    @KafkaListener(topics="messenger", groupId="group-id")
    public void kafkaListener1(String message) {
        System.out.println("Message received from Producer to Consumer");
        System.out.println(message);
    }

}
