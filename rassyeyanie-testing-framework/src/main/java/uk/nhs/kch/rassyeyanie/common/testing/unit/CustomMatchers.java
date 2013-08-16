package uk.nhs.kch.rassyeyanie.common.testing.unit;

import org.apache.commons.lang.StringEscapeUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomMatchers {

	public static Matcher<String> contains(final String value)
	{
		return new BaseMatcher<String>(){
			
			public boolean matches(Object actual) {
				return actual.toString().contains(value);
			}
			
			public void describeTo(Description description) {
				description.appendText("contains '" + StringEscapeUtils.escapeJava(value) + "'");
			}
		};
	}
	
	public static Matcher<String> notContains(final String value)
	{
		return new BaseMatcher<String>(){

			public boolean matches(Object actual) {
				return !actual.toString().contains(value);
			}

			public void describeTo(Description description) {
				description.appendText("doesn't contain '" + StringEscapeUtils.escapeJava(value) + "'");
			}
		};
	}
	
	
}
