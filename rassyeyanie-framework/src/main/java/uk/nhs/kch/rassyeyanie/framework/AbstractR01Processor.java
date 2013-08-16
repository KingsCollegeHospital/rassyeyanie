package uk.nhs.kch.rassyeyanie.framework;

import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.DSC;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
//ORU_R01_PATIENT_RESULT

@Deprecated
public class AbstractR01Processor extends AbstractProcessor {
    public void transform(DSC dsc, DSC clonedDsc) throws HL7Exception {
    }

    public void transform(MSH msh, MSH clonedMsh) throws HL7Exception {

    }

    public void transformResults(List<ORU_R01_PATIENT_RESULT> results)
	    throws HL7Exception {

    }
    
    public ORU_R01 cloneIncomingMessage(AbstractMessage workingMessage) throws HL7Exception
    {
    	ORU_R01 message = (ORU_R01)workingMessage;
    	ORU_R01 clonedMessage = new ORU_R01();
    	this.transform(message.getDSC(),
    		this.copySegment(message.getDSC(), clonedMessage.getDSC()));
    	this.transform(message.getMSH(),
    		this.copySegment(message.getMSH(), clonedMessage.getMSH()));
    	return message;
    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {
	
    ORU_R01 message = cloneIncomingMessage((ORU_R01)workingMessage);
	this.transformResults(HapiUtil.getAll(message,
			ORU_R01_PATIENT_RESULT.class));

    }

	public void transformPID(PID pid, PID clonedPID) throws HL7Exception {
		
	}

}
