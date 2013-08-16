package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PID;

public class NotMaleFilter
extends GenericFilter
{
    @Override
    protected String getValueFromMessage(AbstractMessage message)
        throws HL7Exception
    {
        PID pid = HapiUtil.getWithTerser(message, PID.class);
        return pid.getPid8_AdministrativeSex().getValue();
    }
    
}
