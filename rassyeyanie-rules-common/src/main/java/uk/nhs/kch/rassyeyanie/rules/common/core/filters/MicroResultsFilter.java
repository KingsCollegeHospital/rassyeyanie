package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.OBR;

public class MicroResultsFilter
{
    
    private boolean verify = true;
    
    public boolean shouldProcessMessage(@Body ORU_R01 r01)
        throws HL7Exception
    {
        
        boolean conformsFiller = true;
        boolean conformsService = true;
        
        int resultCount = r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
        
        for (int i = 0; i < resultCount && conformsFiller && conformsService; i++)
        {
            OBR obr = r01.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR();
            conformsFiller =
                this.checkFillerOrderNumberConforms(obr
                    .getObr3_FillerOrderNumber()
                    .getEi1_EntityIdentifier()
                    .getValue());
            conformsService =
                this.checkDiagnosticServiceConforms(obr
                    .getObr24_DiagnosticServSectID()
                    .getValue());
        }
        
        if (this.verify)
        {
            return (conformsFiller && conformsService);
        }
        else
        {
            return !(conformsFiller && conformsService);
        }
        
    }
    
    private boolean checkFillerOrderNumberConforms(String fillerOrderNumber)
    {
    	
        return (StringUtils.isNumeric(StringUtils.left(fillerOrderNumber, 2)) &&
                (StringUtils.mid(fillerOrderNumber, 2, 1).equals("M")||
                		StringUtils.mid(fillerOrderNumber, 2, 1).equals("U"))
                && StringUtils.isNumeric(StringUtils.mid(fillerOrderNumber, 3, 7)));
    }
    
    private boolean checkDiagnosticServiceConforms(String diagnosticService)
    {
        return (diagnosticService.equals("MB"));
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
