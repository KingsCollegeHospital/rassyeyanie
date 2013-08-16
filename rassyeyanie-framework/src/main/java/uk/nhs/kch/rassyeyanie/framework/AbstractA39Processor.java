package uk.nhs.kch.rassyeyanie.framework;

import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.group.ADT_A39_PATIENT;
import ca.uhn.hl7v2.model.v24.message.ADT_A39;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;

@Deprecated
public class AbstractA39Processor extends AbstractProcessor {

    public void transform(EVN evn, EVN clonedEvn) throws HL7Exception {

    }

    public void transformPatient(List<ADT_A39_PATIENT> patients)
	    throws HL7Exception {
    }

    public void transform(MSH msh, MSH clonedMsh) throws HL7Exception {

    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {
	ADT_A39 message = (ADT_A39) workingMessage;
	ADT_A39 clonedMessage = new ADT_A39();

	this.transform(message.getEVN(),
		this.copySegment(message.getEVN(), clonedMessage.getEVN()));
	this.transform(message.getMSH(),
		this.copySegment(message.getMSH(), clonedMessage.getMSH()));
	this.transformPatient(HapiUtil.getAll(message, "PATIENT",
		ADT_A39_PATIENT.class));

    }

}
