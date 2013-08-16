package uk.nhs.kch.rassyeyanie.framework;

import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.group.ADT_A05_INSURANCE;
import ca.uhn.hl7v2.model.v24.group.ADT_A05_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.model.v24.segment.ACC;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.DB1;
import ca.uhn.hl7v2.model.v24.segment.DG1;
import ca.uhn.hl7v2.model.v24.segment.DRG;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.GT1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NK1;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.PV2;
import ca.uhn.hl7v2.model.v24.segment.ROL;
import ca.uhn.hl7v2.model.v24.segment.UB1;
import ca.uhn.hl7v2.model.v24.segment.UB2;

@Deprecated
public class AbstractA05Processor extends AbstractProcessor {
    public void transform(ACC acc, ACC clonedAcc) throws HL7Exception {
    }

    public void transformAl1s(List<AL1> al1s) throws HL7Exception {
    }

    public void transformDb1s(List<DB1> db1s) throws HL7Exception {
    }

    public void transformDg1s(List<DG1> dg1s) throws HL7Exception {
    }

    public void transform(DRG drg, DRG clonedDrg) throws HL7Exception {
    }

    public void transform(EVN evn, EVN clonedEvn) throws HL7Exception {

    }

    public void transformGt1s(List<GT1> gt1s) throws HL7Exception {
    }

    public void transformInsurances(List<ADT_A05_INSURANCE> insurances)
	    throws HL7Exception {
    }

    public void transform(MSH msh, MSH clonedMsh) throws HL7Exception {

    }

    public void transformNk1s(List<NK1> nk1s) throws HL7Exception {

    }

    public void transformObxs(List<OBX> obxs) throws HL7Exception {
    }

    public void transform(PD1 pd1, PD1 clonedPd1) throws HL7Exception {

    }

    public void transform(PID pid, PID clonedPid) throws HL7Exception {

    }

    public void transformProcedures(List<ADT_A05_PROCEDURE> procedures)
	    throws HL7Exception {
    }

    public void transform(PV1 pv1, PV1 clonedPv1) throws HL7Exception {

    }

    public void transform(PV2 pv2, PV2 clonedPv2) throws HL7Exception {

    }

    public void transformRols(List<ROL> rols) throws HL7Exception {
    }

    public void transform(UB1 ub1, UB1 clonedUb1) throws HL7Exception {
    }

    public void transform(UB2 ub2, UB2 clonedUb2) throws HL7Exception {
    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
	    throws HL7Exception {
	ADT_A05 message = (ADT_A05) workingMessage;
	ADT_A05 clonedMessage = new ADT_A05();

	this.transform(message.getACC(),
		this.copySegment(message.getACC(), clonedMessage.getACC()));
	this.transform(message.getDRG(),
		this.copySegment(message.getDRG(), clonedMessage.getDRG()));
	this.transform(message.getEVN(),
		this.copySegment(message.getEVN(), clonedMessage.getEVN()));
	this.transform(message.getMSH(),
		this.copySegment(message.getMSH(), clonedMessage.getMSH()));
	this.transform(message.getPD1(),
		this.copySegment(message.getPD1(), clonedMessage.getPD1()));
	this.transform(message.getPID(),
		this.copySegment(message.getPID(), clonedMessage.getPID()));
	this.transform(message.getPV1(),
		this.copySegment(message.getPV1(), clonedMessage.getPV1()));
	this.transform(message.getPV2(),
		this.copySegment(message.getPV2(), clonedMessage.getPV2()));
	this.transform(message.getUB1(),
		this.copySegment(message.getUB1(), clonedMessage.getUB1()));
	this.transform(message.getUB2(),
		this.copySegment(message.getUB2(), clonedMessage.getUB2()));

	this.transformAl1s(HapiUtil.getAll(workingMessage, AL1.class));
	this.transformDb1s(HapiUtil.getAll(workingMessage, DB1.class));
	this.transformDg1s(HapiUtil.getAll(workingMessage, DG1.class));
	this.transformGt1s(HapiUtil.getAll(workingMessage, GT1.class));
	this.transformInsurances(HapiUtil.getAll(workingMessage, "INSURANCE",
		ADT_A05_INSURANCE.class));
	this.transformNk1s(HapiUtil.getAll(workingMessage, NK1.class));
	this.transformObxs(HapiUtil.getAll(workingMessage, OBX.class));
	this.transformProcedures(HapiUtil.getAll(workingMessage, "PROCEDURE",
		ADT_A05_PROCEDURE.class));
	this.transformRols(HapiUtil.getAll(workingMessage, ROL.class));
    }

}
