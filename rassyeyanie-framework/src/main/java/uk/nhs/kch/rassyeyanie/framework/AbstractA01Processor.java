package uk.nhs.kch.rassyeyanie.framework;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.group.ADT_A01_INSURANCE;
import ca.uhn.hl7v2.model.v24.group.ADT_A01_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.*;

import java.util.List;

@Deprecated
public class AbstractA01Processor extends AbstractProcessor {
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

	public void transformInsurances(List<ADT_A01_INSURANCE> insurances)
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

	public void transform(PDA pda, PDA clonedPda) throws HL7Exception {
	}

	public void transform(PID pid, PID clonedPid) throws HL7Exception {

	}

	public void transformProcedures(List<ADT_A01_PROCEDURE> procedures)
			throws HL7Exception {
	}

	public void transform(PV1 pv1, PV1 clonedPv1) throws HL7Exception {

	}

	public void transform(PV2 pv2, PV2 clonedPv2) throws HL7Exception {

	}

	public void transformRols(List<ROL> rols) throws HL7Exception {
	}

	public void transform(UB1 ub1, UB1 clonedDb1) throws HL7Exception {
	}

	public void transform(UB2 ub2, UB2 clonedUb2) throws HL7Exception {
	}

	@Override
	protected void dispatchProcessFixture(AbstractMessage workingMessage)
			throws HL7Exception {
		ADT_A01 message = (ADT_A01) workingMessage;
		ADT_A01 clonedMessage = new ADT_A01();

		this.transform(message.getACC(),
				this.copySegment(message.getACC(), clonedMessage.getACC()));

		this.transformInsurances(HapiUtil.getAll(message, "INSURANCE",
				ADT_A01_INSURANCE.class));
		this.transformProcedures(HapiUtil.getAll(message, "PROCEDURE",
				ADT_A01_PROCEDURE.class));
		this.transformAl1s(HapiUtil.getAll(message, AL1.class));
		this.transformDb1s(HapiUtil.getAll(message, DB1.class));
		this.transformDg1s(HapiUtil.getAll(message, DG1.class));
		this.transform(message.getDRG(),
				this.copySegment(message.getDRG(), clonedMessage.getDRG()));
		this.transform(message.getEVN(),
				this.copySegment(message.getEVN(), clonedMessage.getEVN()));
		this.transformGt1s(HapiUtil.getAll(message, GT1.class));
		this.transform(message.getMSH(),
				this.copySegment(message.getMSH(), clonedMessage.getMSH()));
		this.transformNk1s(HapiUtil.getAll(message, NK1.class));
		this.transformObxs(HapiUtil.getAll(message, OBX.class));
		this.transform(message.getPD1(),
				this.copySegment(message.getPD1(), clonedMessage.getPD1()));
		this.transform(message.getPDA(),
				this.copySegment(message.getPDA(), clonedMessage.getPDA()));
		this.transform(message.getPID(),
				this.copySegment(message.getPID(), clonedMessage.getPID()));
		this.transform(message.getPV1(),
				this.copySegment(message.getPV1(), clonedMessage.getPV1()));
		this.transform(message.getPV2(),
				this.copySegment(message.getPV2(), clonedMessage.getPV2()));
		this.transformRols(HapiUtil.getAll(message, ROL.class));
		this.transform(message.getUB1(),
				this.copySegment(message.getUB1(), clonedMessage.getUB1()));
		this.transform(message.getUB2(),
				this.copySegment(message.getUB2(), clonedMessage.getUB2()));
	}

}
