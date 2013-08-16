package uk.nhs.kch.rassyeyanie.rules.common.pims.translations.a08;

import java.util.List;

import uk.nhs.kch.rassyeyanie.framework.AbstractA08Processor;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.NK1;

public class PimsA08AllA08 extends AbstractA08Processor {

	@Override
	public void transformAl1s(List<AL1> al1s) {
		for (AL1 al1 : al1s)
			al1.clear();
	}

	@Override
	public void transformNk1s(List<NK1> nk1s) {
		for (NK1 nk1 : nk1s)
			nk1.clear();
	}

}
