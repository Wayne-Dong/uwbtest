package com.ht.service;


import com.google.gson.Gson;
import com.ht.model.TagPosition;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimulateService {


    @PostConstruct
    public void simulate(){
        MqttClient client;
        MqttTopic topic;
        String userName = "root";
        String password = "root";
        float x = 10;
        float y = 10;
        int id = 1;
        int count = 100;
        float length = 70;
        float width = 18;
        float l = 1;
        Gson gson = new Gson();
        TagPosition tagPosition;
        MqttDeliveryToken token;
        //mqtt broker地址
        String broker = "tcp://164.70.6.143:1883";
        String clientId = "UWBTest";
        MqttMessage message = new MqttMessage();
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            System.out.println("正在连接MQTT: " + broker + " ......");
            client.connect(options);
            System.out.println("连接成功！");
            topic = client.getTopic("UWB");

            message.setQos(1);
            message.setRetained(false);
            while (true) {
                for(int i = 0; i < 4; i++){
                    if(i == 0){
                        float t = 0;
                        while(t < length){
                            x = x + l;
                            t = t + l;
                            sendMessage(count, x, y, id, message, topic);
                            Thread.sleep(1000);
                        }

                    }
                    if(i == 1){
                        float t = 0;
                        while(t < width){
                            y = y + l;
                            sendMessage(count, x, y, id, message, topic);
                            t = t + l;
                            Thread.sleep(1000);
                        }


                    }
                    if( i == 2){
                        float t = 0;
                        while(t < length){
                            x = x - l;
                            sendMessage(count, x, y, id, message, topic);
                            t = t + l;
                            Thread.sleep(1000);
                        }


                    }
                    if( i == 3){
                        float t = 0;
                        while(t < width){
                            y = y - l;
                            sendMessage(count, x, y, id, message, topic);
                            t = t + l;
                            Thread.sleep(1000);
                        }
                    }
                }

            }


        }catch (MqttException me) {
            System.out.println("reason" + me.getReasonCode());
            System.out.println("msg" + me.getMessage());
            System.out.println("loc" + me.getLocalizedMessage());
            System.out.println("cause" + me.getCause());
            System.out.println("excep" + me);
            me.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<TagPosition> generate(int id, int count, float x, float y, float radius){
        List<TagPosition> tagPositions = new ArrayList<>();

        return tagPositions;
    }
    private void sendMessage(int count,float x, float y, int id, MqttMessage message, MqttTopic topic){
        for(int j = 0; j <= count; j++){
            TagPosition tagPosition = new TagPosition();
            tagPosition.setTag_id( id + j + "");
            tagPosition.setTag_x(x + "");
            tagPosition.setTag_y(y + "");
            Gson gson = new Gson();
            String tagPositionJson = gson.toJson(tagPosition);
            message.setPayload(tagPositionJson.getBytes());
            try{
                MqttToken token = topic.publish(message);
                token.waitForCompletion();
                System.out.println(tagPositionJson);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
