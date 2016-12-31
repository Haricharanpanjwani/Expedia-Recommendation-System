package net.codejava.springmvc;
import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.KeeperException;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
public class KafkaProducer {

	private static final String topic = "testakay";
	public static void run(int hotelId, String hotelReview) throws InterruptedException, IOException, KeeperException {
		
		
		Properties properties = new Properties();
		properties.put("metadata.broker.list", "54.197.10.58:9092");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		properties.put("client.id","camus");
		ProducerConfig producerConfig = new ProducerConfig(properties);
		kafka.javaapi.producer.Producer<String, String> producer = new kafka.javaapi.producer.Producer<String, String>(
				producerConfig);
		
		KeyedMessage<String, String> message = null;
		message = new KeyedMessage<String, String>(topic, String.valueOf(hotelId)+":"+hotelReview);
		producer.send(message);
		
//		producer.send(message);
//		producer.send(message);
//		producer.send(message);
//		producer.close();
}
}
