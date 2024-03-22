package org.example;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MyProducer
{
    private static final String TOPIC = "events";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    private static final Logger log = LoggerFactory.getLogger(MyProducer.class);

    public static void main(String[] args) throws JsonProcessingException
    {
        TransactionInfo transactionInfo = new TransactionInfo("Mohammed" , "Tartous" , 150.24);
        boolean x = logTransaction(transactionInfo , TOPIC);
    }

    public static boolean logTransaction(TransactionInfo transactionInfo , String topic)
    {

        log.info("Registering A Transaction for Mr/Ms : " + transactionInfo.getName());
        ObjectMapper objectMapper = new ObjectMapper();

        String value = null;
        try
        {
            value = objectMapper.writeValueAsString(transactionInfo);
        }
        catch (JsonProcessingException e)
        {
            log.error("Transaction for Mr/Ms : " + transactionInfo.getName() + " Could Not Be Processed To Json");
            return false;
        }

        Producer<Long, String> kafkaProducer = createKafkaProducer(BOOTSTRAP_SERVERS);

        long timeStamp = System.currentTimeMillis();

        ProducerRecord<Long , String> record = new ProducerRecord<>(topic , timeStamp ,value);

        RecordMetadata recordMetadata = null;
        try
        {
            recordMetadata = kafkaProducer.send(record).get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            log.error("Could Not Send Data to Kafka , Or Could Not Get Meta-Data");
            return false;
        }


        log.info((String.format("Record for Mr/Ms : %s -  with (key: %s, value: %s), " +
                        "was sent to (partition: %d, offset: %d)",
                transactionInfo.getName(),record.key(),
                record.value(), recordMetadata.partition(),
                recordMetadata.offset())));

        System.out.println((String.format("Record for Mr/Ms : %s -  with (key: %s, value: %s), " +
                        "was sent to (partition: %d, offset: %d)",
                transactionInfo.getName(),record.key(),
                record.value(), recordMetadata.partition(),
                recordMetadata.offset())));

        kafkaProducer.flush();
        kafkaProducer.close();

        return true;

    }

    public static Producer<Long, String> createKafkaProducer(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "events-producer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(properties);
    }

}
