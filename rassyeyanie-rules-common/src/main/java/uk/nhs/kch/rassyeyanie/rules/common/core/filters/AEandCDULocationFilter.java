package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class AEandCDULocationFilter {

    public boolean shouldProcessMessage(@Body AbstractMessage body)
            throws HL7Exception
        {
            PV1 pv1 = HapiUtil.getWithTerser(body, PV1.class);
            String patientLocation =            		
            		StringUtils.defaultString(pv1
	                    .getAssignedPatientLocation()
	                    .getPointOfCare()
	                    .getValue());
            
           return (patientLocation.equals("A+E") || patientLocation.startsWith("CDU"));
        }

}
