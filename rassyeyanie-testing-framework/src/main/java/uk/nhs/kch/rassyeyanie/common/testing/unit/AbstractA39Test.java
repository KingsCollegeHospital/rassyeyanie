package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.junit.Test;

import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;

public class AbstractA39Test extends AbstractHl7Test {

	public AbstractA39Test(String actualSourcePath, String expectedPath) {
		super(actualSourcePath, expectedPath);
	}

	@Test
	public void test_message_to_message_evn() throws Exception {
		segment_tester(EVN.class);
	}

	@Test
	public void test_message_to_message_msh() throws Exception {
		segment_tester(MSH.class);

	}

}
