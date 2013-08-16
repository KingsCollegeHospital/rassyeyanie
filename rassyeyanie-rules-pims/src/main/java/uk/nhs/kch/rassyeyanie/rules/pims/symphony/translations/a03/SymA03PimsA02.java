package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a03;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.model.v24.group.ADT_A03_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A03;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class SymA03PimsA02
{
    public void processMessage(@Body ADT_A03 to)
        throws HL7Exception
    {
        this.transform(to.getMSH());
        this.transform(to.getEVN());
        this.transform(to.getPID());
        AbstractGroup zrefParent = getZrefParent(to);
        AbstractSegment zref = (AbstractSegment) zrefParent.get("ZRF");
        this.transform(to.getPV1(), zref);
        zref.clear();
        for (int i = 0; i < to.getDG1Reps(); i++)
        {
            to.getDG1(i).clear();
        }
        
        for (Structure al1 : zrefParent.getAll("AL1"))
        {
            ((AL1) al1).clear();
        }
        
        for (Structure nte : zrefParent.getAll("NTE"))
        {
            ((NTE) nte).clear();
        }
        
        if (zrefParent instanceof ADT_A03_PROCEDURE)
        {
            zrefParent.clear();
        }
    }
    
    private static AbstractGroup getZrefParent(ADT_A03 to)
        throws HL7Exception
    {
        int procedorReps = to.getPROCEDUREReps();
        if (procedorReps > 0)
        {
            ADT_A03_PROCEDURE procedure = to.getPROCEDURE(procedorReps - 1);
            // procedure.insertRepetition("ZRF", 0);
            return procedure;
        }
        else
        {
            return to;
        }
    }
    
    private void transform(EVN evn)
        throws DataTypeException
    {
        evn.getEvn1_EventTypeCode().setValue("A02");
    }
    
    private void transform(MSH msh)
        throws DataTypeException
    {
        msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue("A02");
    }
    
    private void transform(PID pid)
        throws HL7Exception
    {
        pid.getPid1_SetIDPID().clear();
        
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
        
        pid.getPid22_EthnicGroup(0).getCe1_Identifier().setValue("NSP");
        
        for (int i = 25; i <= 30; i++)
        {
            pid.getField(i, 0).clear();
        }
        
    }
    
    private void transform(PV1 pv1, AbstractSegment zref)
        throws HL7Exception
    {
        pv1.getPv11_SetIDPV1().clear();
        pv1.getPv12_PatientClass().setValue("I");
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl1_PointOfCare()
            .setValue(
                pv1
                    .getPv137_DischargedToLocation()
                    .getDld1_DischargeLocation()
                    .getValue());
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
        
        pv1
            .getPv16_PriorPatientLocation()
            .getPl4_Facility()
            .getHd1_NamespaceID()
            .setValue("KCH");
        
        for (int i = 7; i <= 8; i++)
        {
            pv1.getField(i, 0).clear();
        }
        
        XCN referredTo = new XCN(pv1.getMessage());
        ((Varies) zref.getField(7, 0)).setData(referredTo);
        pv1
            .getPv19_ConsultingDoctor(0)
            .getXcn1_IDNumber()
            .setValue(referredTo.getXcn1_IDNumber().getValue());
        
        CE referralBySpecialty = new CE(pv1.getMessage());
        ((Varies) zref.getField(9, 0)).setData(referralBySpecialty);
        
        if (StringUtils.isNotEmpty(referralBySpecialty
            .getCe1_Identifier()
            .getValue()))
        {
            pv1.getPv110_HospitalService().setValue(
                referralBySpecialty.getCe1_Identifier().getValue());
        }
        else
        {
            pv1.getPv110_HospitalService().setValue("NSP");
        }
        
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
        
        pv1.getPv140_BedStatus().setValue("NSP");
        
        for (int i = 45; i <= 52; i++)
        {
            pv1.getField(i, 0).clear();
        }
    }
    
}
