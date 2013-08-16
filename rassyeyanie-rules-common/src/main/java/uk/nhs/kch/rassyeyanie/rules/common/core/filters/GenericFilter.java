package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;

public abstract class GenericFilter
{
    
    protected boolean verifyMode = true;
    protected List<String> values = new ArrayList<String>();
    
    public void setVerifyMode(boolean verifyMode)
    {
        this.verifyMode = verifyMode;
    }
    
    public void setValues(List<String> values)
    {
        this.values = values;
    }
    
    public boolean shouldProcessMessage(@Body AbstractMessage message)
        throws HL7Exception
    {
        String messageValue = this.getValueFromMessage(message);
        
        if (this.values.isEmpty())
            return this.verifyMode;
        
        if (messageValue != null)
            for (String value : this.values)
                if (messageValue.matches(value))
                    return this.verifyMode;
        
        return !this.verifyMode;
    }
    
    protected abstract String getValueFromMessage(AbstractMessage message)
        throws HL7Exception;
    
}
