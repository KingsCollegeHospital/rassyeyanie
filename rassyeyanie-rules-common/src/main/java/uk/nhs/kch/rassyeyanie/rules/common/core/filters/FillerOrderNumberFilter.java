package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.OBR;

/* This filter is used to check if there is value in OBR-4.1
	The default value are:
	- verify true, set to false if want opposite outcome
	-  messageType "O01" is for as order/order update message
		(other type can be is "R01") 
	-  matchValue as below is valid Winpath Lab number.
		user can define any as require even an empty string.
*/

public class FillerOrderNumberFilter {
    private boolean verify = true;
    private String messageType = "O01";
    private String matchValue = "[0-9]{2}[A-Z]{1}[0-9]{7}";
    
    public boolean shouldProcessMessage(@Body AbstractMessage message)
        throws HL7Exception
    {

        boolean outcome = true;   	
        
        if (messageType.equals("O01"))
        	outcome = checkOrderMessage(message);
        else // "R01"
        	outcome = checkResultMessage(message);
        
        
        if (this.verify)
        {
            return (outcome);
        }
        else
        {
            return !(outcome);
        }
        
    }   
    
    
    private boolean checkResultMessage(AbstractMessage message) {
        boolean conformsFiller = true;
        
        ORU_R01 r01 = (ORU_R01) message;
        
        int resultCount = r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
        
        for (int i = 0; i < resultCount && conformsFiller; i++)
        {
            OBR obr = r01.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR();
            String fillerOrderNumber = StringUtils.defaultString(obr
                    .getObr3_FillerOrderNumber()
                    .getEi1_EntityIdentifier()
                    .getValue());
            
            if(fillerOrderNumber.isEmpty())
            {
            	if(matchValue.isEmpty())
            		conformsFiller = true;
            	else
            		conformsFiller = false;
            }
            else                  
            {
            	conformsFiller =
            			this.checkFillerOrderNumberConforms(StringUtils.defaultString(fillerOrderNumber));
            }
        }      
            return (conformsFiller);
		
	}


	private boolean checkOrderMessage(AbstractMessage message) {
        boolean conformsFiller = true;
        
        ORM_O01 o01 = (ORM_O01) message;
        
        int orderCount = o01.getORDERReps();
        
        for (int i = 0; i < orderCount && conformsFiller ; i++)
        {
            OBR obr = o01.getORDER(i).getORDER_DETAIL().getOBR();
            
            String fillerOrderNumber = StringUtils.defaultString(obr
                    .getObr3_FillerOrderNumber()
                    .getEi1_EntityIdentifier()
                    .getValue());
            
            if(fillerOrderNumber.isEmpty())
            {
            	if(matchValue.isEmpty())
            		conformsFiller = true;
            	else
            		conformsFiller = false;
            }
            else                  
            {
            	conformsFiller =
            			this.checkFillerOrderNumberConforms(StringUtils.defaultString(fillerOrderNumber));
            }
        }      
            return (conformsFiller);

	}

	
	private boolean checkFillerOrderNumberConforms(String fillerOrderNumber)
    {
        return fillerOrderNumber.matches(matchValue);
    }  
	
	
    public String getMessageType()
    {
        return this.messageType;
    }
    
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getMatchValue()
    {
        return this.matchValue;
    }
    
    public void setMatchValue(String matchValue)
    {
        this.matchValue = matchValue;
    }
    
    public boolean getVerify()
    {
        return this.verify;
    }
    
    public void setVerify(boolean verify)
    {
        this.verify = verify;
    }
}
