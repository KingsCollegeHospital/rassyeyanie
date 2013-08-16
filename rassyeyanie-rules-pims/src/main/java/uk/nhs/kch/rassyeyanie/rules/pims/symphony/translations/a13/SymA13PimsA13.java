package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a13;

import org.apache.camel.Body;
import org.apache.camel.Header;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;
import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.group.ADT_A01_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

/* "SYM_A13_PIMS_A13" "Cancel discharge from CDU")*/

public class SymA13PimsA13
{
    //
    public
        void
        dispatchProcessFixture(@Header(HL7AdditionalConstants.HL7_SOURCE_MESSAGE) ADT_A01 from,
                               @Body ADT_A01 to)
            // (@Body ADT_A01 message)
            throws HL7Exception
    {
        
        this.tranformPid(to.getPID());
        
        Segment zrf = this.promoteZrfSegment(from);
        
        this.transform(to.getPV1(), zrf);
        
    }
    
    private Segment promoteZrfSegment(ADT_A01 message)
        throws HL7Exception
    {
        
        int countPr1 = message.getPROCEDUREReps();
        if (countPr1 > 0)
        {
            ADT_A01_PROCEDURE lastProcedure =
                message.getPROCEDURE(countPr1 - 1);
            
            Segment zrf = (Segment) lastProcedure.get("ZRF");
            
            return zrf;
        }
        else
        {
            return (Segment) message.get("ZRF");
        }
    }
    
    private void tranformPid(PID pid)
        throws HL7Exception
    {
        
        HapiUtil.clearFieldsToEnd(pid, 17);
        pid.getPid17_Religion().getCe1_Identifier().setValue("NSP");
        pid.getPid22_EthnicGroup(0).getCe1_Identifier().setValue("NSP");
        pid.getPid24_MultipleBirthIndicator().setValue("NSP");
    }
    
    private void transform(PV1 pv1, Segment zrf)
        throws HL7Exception
    {
        pv1.getPv12_PatientClass().setValue("I");
        pv1.getPv13_AssignedPatientLocation().clear();
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl1_PointOfCare()
            .setValue(pv1.getPv14_AdmissionType().getValue());
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl4_Facility()
            .getHd1_NamespaceID()
            .setValue("KCH");
        pv1.getPv14_AdmissionType().setValue("21");
        pv1.getPv15_PreadmitNumber().clear();
        pv1.getPv16_PriorPatientLocation().clear();
        
        pv1.getPv114_AdmitSource().setValue("19");
        
        pv1.getPv110_HospitalService().clear();
        pv1.getPv117_AdmittingDoctor(0).clear();
        pv1.getPv118_PatientType().clear();
        
        pv1.getPv122_CourtesyCode().setValue("NSP");
        pv1.getPv128_InterestCode().setValue("NSP");
        pv1.getPv136_DischargeDisposition().clear();
        pv1.getPv136_DischargeDisposition().setValue("NSP");
        
        pv1
            .getPv137_DischargedToLocation()
            .getDld1_DischargeLocation()
            .setValue("NSP");
        
        pv1.getPv138_DietType().getCe1_Identifier().setValue("NA");
        pv1.getPv139_ServicingFacility().clear();
        pv1.getPv140_BedStatus().setValue("NSP");
    }
    
}
