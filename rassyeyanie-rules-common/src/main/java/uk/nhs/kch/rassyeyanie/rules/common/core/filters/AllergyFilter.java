package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;


public class AllergyFilter {
/*  This filter is used for A31 to allow only message which contain Allergy */
    public boolean shouldProcessMessage(@Body ADT_A05 a05)
            throws HL7Exception
        {
           
    		int al1Count = a05.getAL1Reps();
            return (al1Count > 0);
        }
}
