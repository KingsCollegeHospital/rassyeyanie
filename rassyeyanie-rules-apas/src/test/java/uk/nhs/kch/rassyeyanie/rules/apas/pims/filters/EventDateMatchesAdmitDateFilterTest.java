package uk.nhs.kch.rassyeyanie.rules.apas.pims.filters;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.ADT_A02;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.parser.PipeParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventDateMatchesAdmitDateFilterTest {

	protected PipeParser pipeParser;

	private EventDateMatchesAdmitDateFilter filter;

	private ADT_A02 a02;
	private PV1 pv1;
	private EVN evn;

	@Test
	public void ShouldFailDifferentDateTime() throws Exception {
		this.pv1.parse("PV1|||LICU^Liver ICU||||||HEAND^HEATON Prof ND|||||||||NHS|V1356484|||||||||||||||||||||||||20121203113518|||||");
		this.evn.parse("EVN|A28|20110426151841||||");

		assertFalse(this.filter.shouldProcessMessage(this.a02));
	}

	@Test
	public void ShouldPassSameDateTime() throws Exception {
		this.pv1.parse("PV1|||LICU^Liver ICU||||||HEAND^HEATON Prof ND|||||||||NHS|V1356484|||||||||||||||||||||||||20110426151841|||||");
		this.evn.parse("EVN|A28|20110426151841||||");

		assertTrue(this.filter.shouldProcessMessage(this.a02));
	}

	@Test
	public void ShouldHandleNullDatePV1() throws Exception {
		this.pv1.parse("PV1|||LICU^Liver ICU||||||HEAND^HEATON Prof ND|||||||||NHS|V1356484||||||||||||||||||||||||||||||");
		this.evn.parse("EVN|A28|20110426151841||||");

		assertFalse(this.filter.shouldProcessMessage(this.a02));
	}

	@Test
	public void ShouldHandleNullDateEVN() throws Exception {
		this.pv1.parse("PV1|||LICU^Liver ICU||||||HEAND^HEATON Prof ND|||||||||NHS|V1356484|||||||||||||||||||||||||20110426151841|||||");
		this.evn.parse("EVN|A28|||||");

		assertFalse(this.filter.shouldProcessMessage(this.a02));
	}

	@Before
	public void SetUp() throws DataTypeException {
		this.a02 = new ADT_A02();
		this.a02.getMSH().getEncodingCharacters().setValue("^~\\&");
		this.a02.getMSH().getFieldSeparator().setValue("|");
		this.pv1 = this.a02.getPV1();
		this.evn = this.a02.getEVN();
		this.filter = new EventDateMatchesAdmitDateFilter();
	}

}
