package uk.nhs.kch.rassyeyanie.framework;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.group.OMG_O19_ORDER;
import ca.uhn.hl7v2.model.v24.group.OMG_O19_PATIENT;
import ca.uhn.hl7v2.model.v24.message.OMG_O19;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;

import java.util.List;

@Deprecated
public class AbstractO19Processor extends AbstractProcessor {
    public void transform(MSH msh, MSH clonedMsh) throws HL7Exception {

    }

    public void transformAl1s(List<AL1> al1s) throws HL7Exception {
    }

    public void transformNtes(List<NTE> ntes) throws HL7Exception {

    }

    public void transformOrders(List<OMG_O19_ORDER> orders) throws HL7Exception {
    }

    public void transformPatient(List<OMG_O19_PATIENT> patient)
	    throws HL7Exception {

    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {
	OMG_O19 message = (OMG_O19) workingMessage;
	OMG_O19 clonedMessage = new OMG_O19();

	this.transform(message.getMSH(),
		this.copySegment(message.getMSH(), clonedMessage.getMSH()));
	this.transformNtes(HapiUtil.getAll(workingMessage, NTE.class));
	this.transformOrders(HapiUtil.getAll(workingMessage,
		OMG_O19_ORDER.class));
	this.transformPatient(HapiUtil.getAll(workingMessage,
		OMG_O19_PATIENT.class));

    }

}
