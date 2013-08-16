package uk.nhs.kch.rassyeyanie.common.testing.unit;

import junit.framework.Assert;
import junit.framework.TestSuite;

import org.junit.Test;

import uk.nhs.kch.rassyeyanie.framework.Util;

public class PatternMatchingTest extends TestSuite {
	@Test
	public void PatternMatchingTestReplaceNotDigit() {

		String inputStr;
		String outputStr;

		inputStr = "020 999    9999 02%?.@()[]{}? <>:`!,9999998";

		outputStr = Util.filterStringNumbers(inputStr);

		Assert.assertEquals("020 999 9999 020 9999998", outputStr);
	}
	
	@Test
	public void HashingPatientIdModulo() {
		Assert.assertEquals(2, getGroupId("Z005283^^^^HOSP~^^^^NHS~Z005283^^^^PAS~^^^^EXT", 10));
	}
	
	public int getGroupId(String field, int workerTotal) {
		return field.hashCode() % workerTotal;
	}
}
