package uk.nhs.kch.rassyeyanie.framework.processor;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.commons.lang.StringUtils;

public class InvalidEnvelopeFormatter implements DataFormat {

	@Override
	public void marshal(Exchange exchange, Object graph, OutputStream stream)
			throws Exception {
		String body = this
				.formatMessage(exchange.getIn().getBody(String.class));

		byte[] bodyBytes = body.getBytes();
		stream.write(bodyBytes);

	}

	@Override
	public Object unmarshal(Exchange exchange, InputStream stream)
			throws Exception {
		return this.formatMessage(exchange.getIn().getBody(String.class));
	}

	public String formatMessage(String message) {

		if (!StringUtils.isEmpty(message)) {
			message = this.replaceAllOfChar(message, (char) 11);
			// message = this.replaceAllOfChar(message, (char) 13);
			message = this.replaceAllOfChar(message, (char) 28);

			// message = this.replaceAllOfChar(message, ">".charAt(0));
		}
		return message;
	}

	private String replaceAllOfChar(String message, char chr) {

		return message.replace(String.valueOf(chr).toString(), "");
	}

}
