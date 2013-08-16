package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.junit.Test;

import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class AbstractO01Test extends AbstractHl7Test {

	public AbstractO01Test(String actualSourcePath, String expectedPath) {
		super(actualSourcePath, expectedPath);
	}

	@Test
	public void test_message_to_message_msh() throws Exception {
		segment_tester(MSH.class);

	}

	@Test
	public void test_message_to_message_pid() throws Exception {
		segment_tester(PID.class);
	}

	@Test
	public void test_message_to_message_pv1() throws Exception {
		segment_tester(PV1.class);
	}

}
