package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

public class MyProducer
{
    private static final String TOPIC = "transaction";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    private static final Logger log = LoggerFactory.getLogger(MyProducer.class);

    public static boolean logTransaction(TransactionInfo transactionInfo)
    {
        log.info("Registering A Transaction for Mr/Ms : " + transactionInfo.getName());
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.convertValue(transactionInfo , JsonNode.class);

        ObjectNode jsonTransactionInfo = (ObjectNode) jsonNode;

        Producer<Long, ObjectNode> kafkaProducer = createKafkaProducer(BOOTSTRAP_SERVERS);

        long timeStamp = System.currentTimeMillis();

        ProducerRecord<Long , ObjectNode> record = new ProducerRecord<>(TOPIC , timeStamp ,jsonTransactionInfo);

        RecordMetadata recordMetadata = null;
        try {
            recordMetadata = kafkaProducer.send(record).get();
        } catch (InterruptedException | ExecutionException e)
        {
            log.error("Could Not Send Data to Kafka , Or Could Not Get Meta-Data");
            return false;
        }


        log.info((String.format("Record for Mr/Ms : %s -  with (key: %s, value: %s), " +
                        "was sent to (partition: %d, offset: %d)",
                transactionInfo.getName(),record.key(),
                record.value(), recordMetadata.partition(),
                recordMetadata.offset())));


        kafkaProducer.flush();
        kafkaProducer.close();

        return true;

    }

    public static Producer<Long, ObjectNode> createKafkaProducer(String bootstrapServers)
    {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "events-producer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new KafkaProducer<>(properties);
    }

}
