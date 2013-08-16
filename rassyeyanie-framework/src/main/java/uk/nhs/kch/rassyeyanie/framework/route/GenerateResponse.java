package uk.nhs.kch.rassyeyanie.framework.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import ca.uhn.hl7v2.model.AbstractMessage;

public class GenerateResponse implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		AbstractMessage body = exchange.getIn().getBody(AbstractMessage.class);

		// return the response as plain string
		exchange.getOut().setBody(body.generateACK());

	}
}
