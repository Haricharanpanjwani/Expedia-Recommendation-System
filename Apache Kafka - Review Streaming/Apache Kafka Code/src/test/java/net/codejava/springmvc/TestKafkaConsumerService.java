package net.codejava.springmvc;

public class TestKafkaConsumerService {

	public static void main(String[] args)
	{
		KafkaConsumerService ksf = new KafkaConsumerService();
		ksf.getMessages();
	}
}
