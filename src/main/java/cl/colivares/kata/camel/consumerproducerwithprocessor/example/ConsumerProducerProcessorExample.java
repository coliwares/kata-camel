package cl.colivares.kata.camel.consumerproducerwithprocessor.example;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ConsumerProducerProcessorExample {
	public static void main(String[] args) throws Exception {
		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("direct:start").process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {

						String message = exchange.getIn().getBody(String.class);
						message = message + " from PROCESSOR";
						exchange.getOut().setBody(message);
					}
				}).to("seda:end");
			}
		});

		context.start();

		ProducerTemplate producer = context.createProducerTemplate();
		producer.sendBody("direct:start", "HEllo everyone");

		ConsumerTemplate consumer = context.createConsumerTemplate();
		String response = consumer.receiveBody("seda:end", String.class);
		System.out.println(response);

	}
}