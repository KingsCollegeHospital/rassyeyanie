package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a12;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.model.v24.message.ADT_A09;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class SymA12PimsA11
{
    public void processMessage(@Body ADT_A09 to)
        throws HL7Exception
    {
        this.transform(to.getMSH());
        this.transform(to.getEVN());
        this.transform(to.getPID());
        AbstractSegment zref = (AbstractSegment) to.get("ZRF");
        this.transform(to.getPV1(), zref);
        zref.clear();
    }
    
    private void transform(EVN evn)
        throws DataTypeException
    {
        evn.getEvn1_EventTypeCode().setValue("A11");
    }
    
    private void transform(MSH msh)
        throws DataTypeException
    {
        msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue("A11");
        msh.getMsh16_ApplicationAcknowledgmentType().setValue("AL");
    }
    
    private void transform(PID pid)
        throws HL7Exception
    {
        
        pid
            .getPatientName(0)
            .getXpn5_PrefixEgDR()
            .setValue(
                StringUtils.upperCase(pid
                    .getPatientName(0)
                    .getXpn5_PrefixEgDR()
                    .getValue()));
        
        pid.getField(10, 0).clear();
        
        for (int i = 12; i <= 15; i++)
        {
            pid.getField(i, 0).clear();
        }
        
        pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("NSP");
        
        for (int i = 18; i <= 21; i++)
        {
            pid.getField(i, 0).clear();
        }
        
        for (int i = 25; i <= 30; i++)
        {
            pid.getField(i, 0).clear();
        }
        
    }
    
    private void transform(PV1 pv1, AbstractSegment zref)
        throws HL7Exception
    {
        pv1.getPv12_PatientClass().setValue("I");
        
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl4_Facility()
            .getHd1_NamespaceID()
            .setValue("KCH");
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl9_LocationDescription()
            .setValue(
                pv1
                    .getPv137_DischargedToLocation()
                    .getDld2_EffectiveDate()
                    .getTs1_TimeOfAnEvent()
                    .getValue());
        
        pv1.getPv14_AdmissionType().setValue("21");
        
        for (int i = 6; i <= 8; i++)
        {
            pv1.getField(i, 0).clear();
        }
        
        CE referralBySpecialty = new CE(pv1.getMessage());
        ((Varies) zref.getField(8, 0)).setData(referralBySpecialty);
        
        pv1.getPv110_HospitalService().setValue(
            referralBySpecialty.getCe1_Identifier().getValue());
        
        XCN referredTo = new XCN(pv1.getMessage());
        ((Varies) zref.getField(7, 0)).setData(referredTo);
        pv1
            .getPv117_AdmittingDoctor(0)
            .getXcn1_IDNumber()
            .setValue(referredTo.getXcn1_IDNumber().getValue());
        
        CE admitCategory = new CE(pv1.getMessage());
        ((Varies) zref.getField(12, 0)).setData(admitCategory);
        
        pv1.getPv118_PatientType().setValue(
            admitCategory.getCe1_Identifier().getValue());
        
        pv1.getPv122_CourtesyCode().setValue("NSP");
        
        for (int i = 23; i <= 27; i++)
        {
            pv1.getField(i, 0).clear();
        }
        
        pv1.getPv128_InterestCode().setValue("NSP");
        
        for (int i = 29; i <= 39; i++)
        {
            pv1.getField(i, 0).clear();
        }
        
        pv1.getPv136_DischargeDisposition().setValue("NSP");
        pv1
            .getPv137_DischargedToLocation()
            .getDld1_DischargeLocation()
            .setValue("NSP");
        pv1.getPv138_DietType().getCe1_Identifier().setValue("NA");
        
        pv1.getPv140_BedStatus().setValue("NSP");
        
        for (int i = 45; i <= 49; i++)
        {
            pv1.getField(i, 0).clear();
        }
    }
    
}
