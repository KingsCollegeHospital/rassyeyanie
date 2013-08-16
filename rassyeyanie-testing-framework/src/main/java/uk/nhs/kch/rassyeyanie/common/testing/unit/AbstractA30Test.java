package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.junit.Test;

import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MRG;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PID;

public class AbstractA30Test extends AbstractHl7Test {

	public AbstractA30Test(String actualSourcePath, String expectedPath) {
		super(actualSourcePath, expectedPath);
	}

	@Test
	public void test_message_to_message_evn() throws Exception {
		segment_tester(EVN.class);
	}

	@Test
	public void test_message_to_message_mrg() throws Exception {
		segment_tester(MRG.class);

	}

	@Test
	public void test_message_to_message_msh() throws Exception {
		segment_tester(MSH.class);

	}

	@Test
	public void test_message_to_message_pd1() throws Exception {
		segment_tester(PD1.class);
	}

	@Test
	public void test_message_to_message_pid() throws Exception {
		segment_tester(PID.class);
	}

}
