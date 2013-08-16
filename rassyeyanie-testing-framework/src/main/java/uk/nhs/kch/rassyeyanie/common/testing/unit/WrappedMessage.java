package uk.nhs.kch.rassyeyanie.common.testing.unit;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractMessage;

public class WrappedMessage {

	private AbstractMessage abstractMessage;

	public WrappedMessage(AbstractMessage abstractMessage) {
		this.setAbstractMessage(abstractMessage);
	}

	public AbstractMessage getAbstractMessage() {
		return abstractMessage;
	}

	public void setAbstractMessage(AbstractMessage abstractMessage) {
		this.abstractMessage = abstractMessage;
	}

	public WrappedGroup getGroup(String string, int i) throws HL7Exception {
		return 
			new WrappedGroup(
					(AbstractGroup)
						getAbstractMessage()
							.get(string, i));
	}

}
