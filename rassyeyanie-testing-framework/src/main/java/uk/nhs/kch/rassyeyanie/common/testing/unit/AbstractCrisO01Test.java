package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.OBR;
import ca.uhn.hl7v2.model.v24.segment.OBX;
import ca.uhn.hl7v2.model.v24.segment.ORC;

public class AbstractCrisO01Test extends AbstractO01Test {
	public AbstractCrisO01Test(String actualSourcePath, String expectedPath) {
		super(actualSourcePath, expectedPath);
	}

	@Test
	public void test_message_to_message_orc_1() throws Exception {
		Structure expectedSegment = getExpected().getGroup("ORDER", 0).get(
				ORC.class);
		Structure actualSegment = getActual().getGroup("ORDER", 0).get(
				ORC.class);
		assertEquals(expectedSegment, actualSegment);
	}

	@Test
	public void test_message_to_message_obr_1() throws Exception {
		Structure expectedSegment = getExpected().getGroup("ORDER", 0).get(
				OBR.class);
		Structure actualSegment = getActual().getGroup("ORDER", 0)
				.getGroup("ORDER_DETAIL", 0).get(OBR.class);
		assertEquals(expectedSegment, actualSegment);
	}

	@Test
	public void test_message_to_message_obx_1_1() throws Exception {
		int ordNo = 0, obNo = 0;
		testObx(ordNo, obNo);
	}

	@Test
	public void test_message_to_message_obx_1_2() throws Exception {
		int ordNo = 0, obNo = 1;
		testObx(ordNo, obNo);
	}

	@Test
	public void test_message_to_message_nte_1_1() throws Exception {
		int x = 0, y = 0;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_2() throws Exception {
		int x = 0, y = 1;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_3() throws Exception {
		int x = 0, y = 2;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_4() throws Exception {
		int x = 0, y = 3;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_5() throws Exception {
		int x = 0, y = 4;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_6() throws Exception {
		int x = 0, y = 5;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_7() throws Exception {
		int x = 0, y = 6;
		testNte(x, y);
	}

	@Test
	public void test_message_to_message_nte_1_8() throws Exception {
		int x = 0, y = 7;
		testNte(x, y);
	}

	private void testObx(int ordNo, int obNo) throws HL7Exception {
		Structure expectedSegment = getExpected().getGroup("ORDER", ordNo)
				.getGroup("OBSERVATION", obNo).get(OBX.class);
		Structure actualSegment = getActual().getGroup("ORDER", ordNo)
				.getGroup("ORDER_DETAIL", ordNo).getGroup("OBSERVATION", obNo)
				.get(OBX.class);
		assertEquals(expectedSegment, actualSegment);
	
	}


	private void testNte(int x, int y) throws HL7Exception {
		Structure expectedSegment = getExpected().getGroup("ORDER", x).get(
				NTE.class, y);
		Structure actualSegment = getActual().getGroup("ORDER", x)
				.getGroup("ORDER_DETAIL", 0).get(NTE.class, y);
		assertEquals(expectedSegment, actualSegment);
	}

}
