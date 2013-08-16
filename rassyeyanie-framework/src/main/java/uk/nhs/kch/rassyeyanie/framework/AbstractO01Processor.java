package uk.nhs.kch.rassyeyanie.framework;

import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.group.ORM_O01_ORDER;
import ca.uhn.hl7v2.model.v24.group.ORM_O01_PATIENT;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

@Deprecated
public class AbstractO01Processor extends AbstractProcessor {
    public void transform(MSH msh, MSH clonedMsh) throws HL7Exception {

    }

    public void transformNtes(List<NTE> ntes) throws HL7Exception {

    }

    public void transformOrders(List<ORM_O01_ORDER> orders) throws HL7Exception {
    }

    public void transformPatients(List<ORM_O01_PATIENT> patient)
	    throws HL7Exception {

    }

    public void transform(PID pid, PID clonedPid) throws HL7Exception {
    }

    public void transform(PV1 pv1, PV1 clonedPv1) throws HL7Exception {

    }

    public void transform(PD1 pd1, PD1 clonedPd1) throws HL7Exception {

    }

    public void transformAl1s(List<AL1> al1s) throws HL7Exception {
	for (AL1 al1 : al1s) {
	    al1.clear();
	}
    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {
	ORM_O01 message = (ORM_O01) workingMessage;
	ORM_O01 clonedMessage = new ORM_O01();

	this.transform(message.getMSH(),
		this.copySegment(message.getMSH(), clonedMessage.getMSH()));

	this.transform(message.getPATIENT().getPID(), this.copySegment(message
		.getPATIENT().getPID(), clonedMessage.getPATIENT().getPID()));

	this.transform(
		message.getPATIENT().getPATIENT_VISIT().getPV1(),
		this.copySegment(message.getPATIENT().getPATIENT_VISIT()
			.getPV1(), clonedMessage.getPATIENT()
			.getPATIENT_VISIT().getPV1()));

	this.transform(message.getPATIENT().getPD1(), this.copySegment(message
		.getPATIENT().getPD1(), clonedMessage.getPATIENT().getPD1()));

	this.transformAl1s(HapiUtil.getAll(message.getPATIENT(), AL1.class));

	/*
	 * this.transformNtes(HapiUtil.getAll(workingMessage, NTE.class));
	 * this.transformOrders(HapiUtil.getGroupAll(message.getORDER(),
	 * ORM_O01_ORDER.class));
	 * this.transformPatients(HapiUtil.getAll(workingMessage,
	 * ORM_O01_PATIENT.class));
	 */
    }

}
