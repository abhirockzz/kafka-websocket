package com.wordpress.abhirockzz.kafEEne.websocket;

import com.wordpress.abhirockzz.kafEEne.websocket.model.Payload;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

@Stateless
public class Consumer {

    private KafkaConsumer<String, String> kConsumer;
    private static final String CONSUMER_GROUP = "test-group";
    private final AtomicBoolean stopped = new AtomicBoolean(false);

    @PostConstruct
    public void init() {

        String kafkaCluster = System.getenv().getOrDefault("KAFKA_BROKER", "192.168.99.100:9092");
        System.out.println("Kafka Cluster " + kafkaCluster);

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        kConsumer = new KafkaConsumer<>(consumerProps);
        kConsumer.subscribe(Arrays.asList("topic-1", "topic-2"));

        System.out.println("init() complete");

    }

    @PreDestroy
    public void close() {
        if (stopped.get()) {
            System.out.println("Consumer already stopped");
        } else {
            if (kConsumer != null) {
                stopped.set(true);
                kConsumer.wakeup();
                System.out.println("Invoked Consumer wakeup() from thread " + Thread.currentThread().getName());
            }
        }

    }

    @Inject
    Event<Payload> event;

    public void consume() {
        System.out.println("Consumer loop wil be triggered...");
        try {
            while (!stopped.get()) {

                ConsumerRecords<String, String> records = kConsumer.poll(Integer.MAX_VALUE);
                for (ConsumerRecord<String, String> record : records) {
                    Payload payload = new Payload(record.key(), record.value(), record.topic());
                    event.fire(payload);
                    System.out.println("Event for payload " + payload + " fired.....");
                }

            }
        } catch (WakeupException e) {
            System.out.println("Consumer loop interrupted from thread " + Thread.currentThread().getName());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            kConsumer.close();
            System.out.println("Consumer shutdown");
        }

    }
}
