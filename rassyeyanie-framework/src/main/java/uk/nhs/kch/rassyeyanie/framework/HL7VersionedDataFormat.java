package uk.nhs.kch.rassyeyanie.framework;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class HL7VersionedDataFormat
    implements DataFormat
{
    
    private static PipeParser createVersionedParser(String version,
                                                    boolean validate)
    {
        CanonicalModelClassFactory canonicalModelClassFactory =
            new CanonicalModelClassFactory(version);
        PipeParser pipeParser = new PipeParser(canonicalModelClassFactory);
        if (!validate)
        {
            pipeParser.setValidationContext(new NoValidation());
        }
        return pipeParser;
    }
    
    private boolean validate;
    
    private String version;
    
    public HL7VersionedDataFormat()
    {
        this.validate = true;
        this.version = "2.4";
    }
    
    public String getVersion()
    {
        return this.version;
    }
    
    public boolean isValidate()
    {
        return this.validate;
    }
    
    @Override
    public void marshal(Exchange exchange,
                        Object body,
                        OutputStream outputStream)
        throws Exception
    {
        Message message =
            ExchangeHelper
                .convertToMandatoryType(exchange, Message.class, body);
        PipeParser pipeParser =
            createVersionedParser(this.version, this.validate);
        String encoded = pipeParser.encode(message);
        outputStream.write(encoded.getBytes());
    }
    
    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    @Override
    public Object unmarshal(Exchange exchange, InputStream inputStream)
        throws Exception
    {
        String body =
            ExchangeHelper.convertToMandatoryType(
                exchange,
                String.class,
                inputStream);
        PipeParser pipeParser =
            createVersionedParser(this.version, this.validate);
        
        body = body.replace('\n', '\r');
        Message message = pipeParser.parse(body);
        
        // add MSH fields as message out headers
        Terser terser = new Terser(message);
        exchange.getOut().setHeader(
            HL7Constants.HL7_SENDING_APPLICATION,
            terser.get("MSH-3"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_SENDING_FACILITY,
            terser.get("MSH-4"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_RECEIVING_APPLICATION,
            terser.get("MSH-5"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_RECEIVING_FACILITY,
            terser.get("MSH-6"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_TIMESTAMP,
            terser.get("MSH-7"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_SECURITY,
            terser.get("MSH-8"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_MESSAGE_TYPE,
            terser.get("MSH-9-1"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_TRIGGER_EVENT,
            terser.get("MSH-9-2"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_MESSAGE_CONTROL,
            terser.get("MSH-10"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_PROCESSING_ID,
            terser.get("MSH-11"));
        exchange.getOut().setHeader(
            HL7Constants.HL7_VERSION_ID,
            terser.get("MSH-12"));
        return message;
    }
}
