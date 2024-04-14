package org.example;

import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class UserNotificationKafkaConsumer
{
    private static final String TOPIC = "SUSPICIOUS_TOPIC";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    static final Logger log = LoggerFactory.getLogger(UserNotificationKafkaConsumer.class);

    public static void main(String[] args)
    {
        String consumerGroup = "defaultConsumerGroup";
        if (args.length == 1)
        {
            consumerGroup = args[0];
        }

        System.out.println("Consumer is part of consumer group " + consumerGroup);

        Consumer<Long, TransactionInfo> kafkaConsumer = createKafkaConsumer(BOOTSTRAP_SERVERS, consumerGroup);

        consumeMessages(TOPIC, kafkaConsumer);
    }

    public static void consumeMessages(String topic, Consumer<Long, TransactionInfo> kafkaConsumer)
    {

        kafkaConsumer.subscribe(Collections.singletonList(topic));

        while (true)
        {
            ConsumerRecords<Long, TransactionInfo> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            if (consumerRecords.isEmpty()) continue;

            for (ConsumerRecord<Long , TransactionInfo> record : consumerRecords)
            {
                TransactionInfo transactionInfo = record.value();
                System.out.println("key : " + record.key());
                System.out.println("Received : " + transactionInfo);
                WritingHelper writingHelper = new WritingHelper(transactionInfo);
                writingHelper.writeToLog();
                DataBaseHandler.notifyUser(transactionInfo.getName());
                System.out.println(writingHelper);
            }
            kafkaConsumer.commitAsync();
        }
    }

    public static Consumer<Long, TransactionInfo> createKafkaConsumer(String bootstrapServers, String consumerGroup)
    {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        //properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TransactionInfo.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new KafkaConsumer<>(properties);
    }
}