package uk.nhs.kch.rassyeyanie.framework.route;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * Simple exception processor that takes any exceptions caught by the exchange and sends the
 * text back as the body.
 */
public class SimpleExceptionProcessor implements Processor {

    private static final String NACK_RESPONSE = "MSH|^~\\&|FUSE|SERVICEMIX|ERR|ERR|%1$s||ACK||P|2.3||||\n" +
                                                "MSA|AE||HL7 NACK created";

    @Override
    public void process(Exchange exchange) {
        Date now = new Date();

        String date = DateFormatUtils.format(now, "yyyyMMddhhmmss");
        String nack = String.format(NACK_RESPONSE, date);

        exchange.getOut().setBody(nack);
    }
}
