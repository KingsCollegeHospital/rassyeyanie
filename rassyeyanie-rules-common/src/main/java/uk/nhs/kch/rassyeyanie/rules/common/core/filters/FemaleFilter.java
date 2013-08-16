package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PID;

public class FemaleFilter
{
    
    public boolean shouldProcessMessage(@Body AbstractMessage body)
        throws HL7Exception
    {
        PID pid = HapiUtil.get(body, PID.class);
        return (StringUtils.startsWithIgnoreCase(pid.getAdministrativeSex().getValue(), "F"));
    }
    
}
