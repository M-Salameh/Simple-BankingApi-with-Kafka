package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AccountManagerKafkaConsumer
{
    private static final String TOPIC = "VALID_TOPIC";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    static final Logger log = LoggerFactory.getLogger(AccountManagerKafkaConsumer.class);

    public static void main(String[] args)
    {
        String consumerGroup = "defaultConsumerGroup";
        if (args.length == 1)
        {
            consumerGroup = args[0];
        }

        System.out.println("Consumer is part of consumer group " + consumerGroup);

        Consumer<Long, String> kafkaConsumer = createKafkaConsumer(BOOTSTRAP_SERVERS, consumerGroup);

        consumeMessages(TOPIC, kafkaConsumer);
    }

    public static void consumeMessages(String topic, Consumer<Long, String> kafkaConsumer)
    {
        ObjectMapper objectMapper = new ObjectMapper();

        kafkaConsumer.subscribe(Collections.singletonList(topic));

        while (true)
        {
            ConsumerRecords<Long, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            if (consumerRecords.isEmpty()) continue;

            for (ConsumerRecord<Long , String> record : consumerRecords)
            {
                String stringJson = record.value();
                System.out.println("key : " + record.key());
                System.out.println("Received : " + stringJson);
                try
                {
                    TransactionInfo transactionInfo = objectMapper.readValue(stringJson , TransactionInfo.class);
                    WritingHelper writingHelper = new WritingHelper(transactionInfo);
                    writingHelper.writeToLog();
                    boolean x = DataBaseHandler.editAmount(transactionInfo.getName() , transactionInfo.getAmount());
                    if (!x)
                    {
                        log.warn(transactionInfo.getName() + " : Balance not Enough to Withdraw Amount");
                    }
                    System.out.println(writingHelper);
                }
                catch (JsonProcessingException e)
                {
                    System.out.println("Error Parsing");
                    System.out.println(e.getMessage());
                    log.error("Could Not Parse Json in Transaction");
                }
            }

            kafkaConsumer.commitAsync();
        }
    }

    public static Consumer<Long, String> createKafkaConsumer(String bootstrapServers, String consumerGroup)
    {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new KafkaConsumer<>(properties);
    }
}