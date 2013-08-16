package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;
import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.util.DeepCopy;

public class CommonSymphonyPimsAdt
{
    
    @Autowired
    private ConfigurationService configurationService;
    
    public static final String SYM_PIMS_SEX = "SYM_PIMS_SEX";
    
    public
        void
        processMessage(@Header(HL7AdditionalConstants.HL7_SOURCE_MESSAGE) ADT_A01 from,
                       @Body ADT_A01 to)
            throws HL7Exception
    {
        DeepCopy.copy(from.getMSH(), to.getMSH());
        DeepCopy.copy(from.getEVN(), to.getEVN());
        DeepCopy.copy(from.getPID(), to.getPID());
        
        this.transformMsh(to.getMSH());
        this.transformPid(to.getPID());
        DeepCopy.copy(from.getPV1(), to.getPV1());
    }
    
    public void transformMsh(MSH msh)
        throws HL7Exception
    {
        msh.getSendingApplication().getNamespaceID().setValue("SYMPHONY");
        msh.getContinuationPointer().clear();
    }
    
    public void transformPid(PID pid)
        throws HL7Exception
    {
        if (StringUtils.isNotEmpty(pid.getPatientID().getID().getValue()))
        {
            pid.getPatientID().getIdentifierTypeCode().setValue("NHS");
        }
        pid.getPatientIdentifierList(0).getAssigningAuthority().clear();
        pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("PAS");
        String title =
            StringUtils.defaultString(pid
                .getPatientName(0)
                .getPrefixEgDR()
                .getValue());
        
        if (!title.isEmpty())
        {
            pid
                .getPatientName(0)
                .getPrefixEgDR()
                .setValue(StringUtils.capitalize(title.toLowerCase()));
        }
        int contextId =
            this.configurationService.findContextIdByName(SYM_PIMS_SEX);
        
        String sex =
            this.configurationService.findValue(contextId, StringUtils
                .defaultString(pid.getAdministrativeSex().getValue()));
        
        pid.getAdministrativeSex().setValue(sex);
        pid.getPid24_MultipleBirthIndicator().clear();
    }
}
