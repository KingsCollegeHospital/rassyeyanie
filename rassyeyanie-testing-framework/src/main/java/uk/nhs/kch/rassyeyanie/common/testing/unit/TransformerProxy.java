package uk.nhs.kch.rassyeyanie.common.testing.unit;

import uk.nhs.kch.rassyeyanie.framework.AbstractProcessor;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;

public interface TransformerProxy {
    void transform(AbstractProcessor processor, AbstractMessage actual)
	    throws EncodingNotSupportedException, HL7Exception;

}
