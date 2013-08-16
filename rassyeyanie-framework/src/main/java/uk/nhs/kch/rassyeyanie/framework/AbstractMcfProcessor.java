package uk.nhs.kch.rassyeyanie.framework;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;

@Deprecated
public class AbstractMcfProcessor extends AbstractProcessor {

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {

    }

}
