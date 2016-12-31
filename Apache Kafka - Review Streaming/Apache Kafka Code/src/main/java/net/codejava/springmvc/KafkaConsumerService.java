package net.codejava.springmvc;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.*;
//import kafka.consumer.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
public class KafkaConsumerService {

	
	public void getMessages()
	{
		 Properties props = new Properties();
	     props.put("bootstrap.servers", "54.197.10.58:9092");
	     props.put("group.id", "test-consumer-group");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("session.timeout.ms", "30000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
	     consumer.subscribe(Arrays.asList("testakay"));
	     int i = 0;
	     while (i <= 1000000) {
	         ConsumerRecords<String, String> records = consumer.poll(100);
	         
	         for (ConsumerRecord<String, String> record : records)
	         {
	        	 if(record.value().length() > 1)
	        	 {
	        		 System.out.println("Received message: "+record.value());
	        		 String[] splits = record.value().toString().split(":");
	        		 System.out.printf("Message: propid = %s, review = %s", splits[0], splits[1]);
	        		 
	        		 try {
	        			 HiveDAO.insertReview(Integer.parseInt(splits[0].trim()), splits[1]);
	        		 }
	        		 catch(Exception e) {
	        			 System.out.println("Error while inserting the data to the hive");
	        		 }
	        	 }
	        	 
	         }
	         i++;
	     }
	     consumer.close();
	}
}
