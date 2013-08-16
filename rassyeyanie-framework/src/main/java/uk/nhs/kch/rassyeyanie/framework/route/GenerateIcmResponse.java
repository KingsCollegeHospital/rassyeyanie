package uk.nhs.kch.rassyeyanie.framework.route;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Simple exception processor that takes any exceptions caught by the exchange and sends the
 * text back as the body.
 */
public class GenerateIcmResponse implements Processor {

	private static final byte ACK = 0x06;
	private static final byte NACK = 0x15;
	
	private byte acknowledgement;

	private GenerateIcmResponse(byte ack){
		this.acknowledgement = ack;
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.getOut().setBody( new byte[] { acknowledgement } );	
	}

	public static Processor Ack() {
		return new GenerateIcmResponse(ACK);
	}

	public static Processor Nack() {
		return new GenerateIcmResponse(NACK);
	}

}
