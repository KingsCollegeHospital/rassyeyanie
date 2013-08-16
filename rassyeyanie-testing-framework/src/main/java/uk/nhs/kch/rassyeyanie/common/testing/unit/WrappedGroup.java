package uk.nhs.kch.rassyeyanie.common.testing.unit;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.Structure;

public class WrappedGroup {

	private AbstractGroup abstractGroup;

	public WrappedGroup(Structure structure) {
		this.abstractGroup = (AbstractGroup)structure;
	}
	public WrappedGroup getGroup(String string, int i) throws HL7Exception {
		return new WrappedGroup(abstractGroup.get(string, i));
	}

	public <T extends Structure> Structure get(Class<T> clazz) throws HL7Exception {
		return this.get(clazz, 0);
	}

	public <T extends Structure> Structure get(Class<T> clazz, int index) throws HL7Exception {
		return abstractGroup.get(clazz.getSimpleName(), index);
	}

}
