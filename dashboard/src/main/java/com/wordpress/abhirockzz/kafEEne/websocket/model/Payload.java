package com.wordpress.abhirockzz.kafEEne.websocket.model;

public class Payload {

    private String key;
    private String val;
    private String topic;

    public Payload(String key, String val, String topic) {
        this.key = key;
        this.val = val;
        this.topic = topic;
    }

    public Payload() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "Payload{" + "key=" + key + ", val=" + val + ", topic=" + topic + '}';
    }

}
