package com.wordpress.abhirockzz.kafEEne.websocket.producer;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Producer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Producer.class.getName());
    private KafkaProducer<String, String> kafkaProducer = null;

    public Producer() {
        LOGGER.log(Level.INFO, "Kafka Producer running in thread {0}", Thread.currentThread().getName());

        Properties kafkaProps = new Properties();

        String kafkaCluster = System.getenv().get("KAFKA_BROKER");
        LOGGER.log(Level.INFO, "Kafka cluster {0}", kafkaCluster);

        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "0");

        this.kafkaProducer = new KafkaProducer<>(kafkaProps);

    }

    @Override
    public void run() {
        try {
            produce();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * produce messages
     *
     * @throws Exception
     */
    private void produce() throws Exception {

        try {
            while (true) {
                
                kafkaProducer.send(record("topic-1"));
                kafkaProducer.send(record("topic-2"));
                
                Thread.sleep(5000);

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Producer thread was interrupted");
        } finally {
            kafkaProducer.close();

            LOGGER.log(Level.INFO, "Producer closed");
        }

    }
    static Random rnd = new Random();
    private ProducerRecord<String, String> record(String topic) {
        
        String key = "key-" + rnd.nextInt(10);
        String value = "value-" + rnd.nextInt(10);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);;
        return record;
    }

}
