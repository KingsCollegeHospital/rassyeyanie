package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.junit.Test;

import ca.uhn.hl7v2.model.v24.segment.DB1;
import ca.uhn.hl7v2.model.v24.segment.DG1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.PV2;

public class AbstractA09Test extends AbstractHl7Test {

	public AbstractA09Test(String actualSourcePath, String expectedPath) {
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

	@Test
	public void test_message_to_message_pd1() throws Exception {
		segment_tester(PD1.class);
	}

	public void test_message_to_message_pid() throws Exception {
		segment_tester(PID.class);
	}

	@Test
	public void test_message_to_message_pv1() throws Exception {
		segment_tester(PV1.class);
	}

	@Test
	public void test_message_to_message_pv2() throws Exception {
		segment_tester(PV2.class);
	}

	@Test
	public void test_message_to_message_db1_1() throws Exception {
		testStructureByIndex(0, DB1.class);
	}

	@Test
	public void test_message_to_message_db1_2() throws Exception {
		testStructureByIndex(1, DB1.class);
	}

	@Test
	public void test_message_to_message_dg1_1() throws Exception {
		testStructureByIndex(0, DG1.class);
	}

	@Test
	public void test_message_to_message_dg1_2() throws Exception {
		testStructureByIndex(1, DG1.class);
	}

	@Test
	public void test_message_to_message_obx_1() throws Exception {
		testStructureByIndex(0, OBX.class);
	}

	@Test
	public void test_message_to_message_obx_2() throws Exception {
		testStructureByIndex(1, OBX.class);

	}

}
