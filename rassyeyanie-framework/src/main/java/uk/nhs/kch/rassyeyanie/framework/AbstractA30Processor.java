package uk.nhs.kch.rassyeyanie.framework;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.message.ADT_A30;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MRG;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PID;

@Deprecated
public class AbstractA30Processor extends AbstractProcessor {
    public void transform(EVN evn, EVN clonedEvn) throws HL7Exception {

    }

    public void transform(MRG mrg, MRG clonedMrg) throws HL7Exception {
    }

    public void transform(MSH msh, MSH clonedMsh) throws HL7Exception {

    }

    public void transform(PD1 pd1, PD1 clonedPd1) throws HL7Exception {

    }

    public void transform(PID pid, PID clonedPid) throws HL7Exception {

    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {
	ADT_A30 message = (ADT_A30) workingMessage;
	ADT_A30 clonedMessage = new ADT_A30();

	this.transform(message.getEVN(),
		this.copySegment(message.getEVN(), clonedMessage.getEVN()));
	this.transform(message.getMSH(),
		this.copySegment(message.getMSH(), clonedMessage.getMSH()));
	this.transform(message.getMRG(),
		this.copySegment(message.getMRG(), clonedMessage.getMRG()));
	this.transform(message.getPD1(),
		this.copySegment(message.getPD1(), clonedMessage.getPD1()));
	this.transform(message.getPID(),
		this.copySegment(message.getPID(), clonedMessage.getPID()));
    }
}
