package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.junit.Test;

import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NK1;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PDA;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.PV2;




public class AbstractA38Test extends AbstractHl7Test {

	public AbstractA38Test(String actualSourcePath, String expectedPath) {
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

		@Test
		public void test_message_to_message_pda() throws Exception {
			segment_tester(PDA.class);
		}

		@Test
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
		public void test_message_to_message_al1_1() throws Exception {
			testStructureByIndex(0, AL1.class);
		}

		@Test
		public void test_message_to_message_al1_2() throws Exception {
			testStructureByIndex(1, AL1.class);
		}


		@Test
		public void test_message_to_message_nk1_1() throws Exception {
			testStructureByIndex(0, NK1.class);
		}

		@Test
		public void test_message_to_message_nk1_2() throws Exception {
			testStructureByIndex(1, NK1.class);

		}

	


}