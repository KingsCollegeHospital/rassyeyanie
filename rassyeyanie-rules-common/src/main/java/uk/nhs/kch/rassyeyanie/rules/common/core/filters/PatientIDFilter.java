package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MRG;

public class PatientIDFilter
{
    
    public boolean shouldProcessMessage(@Body AbstractMessage body)
        throws HL7Exception
    {
        
        MRG mrg = HapiUtil.get(body, MRG.class);
        String paientID =
            StringUtils.defaultString(mrg.getMrg1_PriorPatientIdentifierList(0).getCx1_ID().getValue());
        return !paientID.isEmpty();
    }
}
