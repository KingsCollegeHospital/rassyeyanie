package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a03;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.TS;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.model.v24.group.ADT_A03_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A03;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class SymA03PimsA03
{
    public void processMessage(@Body ADT_A03 to)
        throws HL7Exception
    {
        this.transform(to.getPID());
        AbstractGroup zrefParent = getZrefParent(to);
        AbstractSegment zref = (AbstractSegment) zrefParent.get("ZRF");
        this.transform(to.getPV1(), zref, to.getEVN());
        
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
        
        this.transformZref(zref, to.getPV1());
        
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
            return procedure;
        }
        else
        {
            return to;
        }
    }
    
    private void transformZref(Segment zref, PV1 pv1)
        throws DataTypeException, HL7Exception
    {
        CE referralType = new CE(pv1.getMessage());
        ((Varies) zref.getField(1, 0)).setData(referralType);
        referralType.getCe1_Identifier().setValue("EMERG");
        referralType.getCe3_NameOfCodingSystem().setValue("DGATE");
        
        TS dateReceived = new TS(pv1.getMessage());
        ((Varies) zref.getField(2, 0)).setData(dateReceived);
        dateReceived.getTs1_TimeOfAnEvent().setValue(
            pv1.getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().getValue());
        
        for (int i = 3; i <= 4; i++)
        {
            zref.getField(i, 0).clear();
        }
        
        CE referralSource = new CE(pv1.getMessage());
        ((Varies) zref.getField(5, 0)).setData(referralSource);
        referralSource.getCe1_Identifier().clear();
        referralSource.getCe3_NameOfCodingSystem().setValue("DGATE");
        
        XCN referredBy = new XCN(pv1.getMessage());
        ((Varies) zref.getField(6, 0)).setData(referredBy);
        referredBy.getXcn1_IDNumber().setValue("EDCDU");
        referredBy.getXcn3_GivenName().setValue("DGATE");
        
        XCN referredTo = new XCN(pv1.getMessage());
        ((Varies) zref.getField(7, 0)).setData(referredTo);
        referredTo.getXcn3_GivenName().setValue("DGATE");
        
        CE referralBySpecialty = new CE(pv1.getMessage());
        ((Varies) zref.getField(9, 0)).setData(referralBySpecialty);
        referralBySpecialty.getCe3_NameOfCodingSystem().setValue("DGATE");
        
        CE referralByOrg = new CE(pv1.getMessage());
        ((Varies) zref.getField(8, 0)).setData(referralByOrg);
        referralByOrg.getCe1_Identifier().setValue(
            referralBySpecialty.getCe1_Identifier().getValue());
        referralByOrg.getCe3_NameOfCodingSystem().setValue("DGATE");
        
        CE priority = new CE(pv1.getMessage());
        ((Varies) zref.getField(11, 0)).setData(priority);
        priority.getCe1_Identifier().setValue("R");
        
        zref.getField(12, 0).clear();
    }
    
    private void transform(PV1 pv1, Segment zref, EVN evn)
        throws HL7Exception
    {
        pv1.getPv12_PatientClass().setValue("I");
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl4_Facility()
            .getHd1_NamespaceID()
            .setValue("KCH");
        
        for (int i = 6; i < 9; i++)
            pv1.getField(i, 0).clear();
        
        CE hospitalService = new CE(pv1.getMessage());
        ((Varies) zref.getField(9, 0)).setData(hospitalService);
        
        pv1.getPv110_HospitalService().setValue(
            hospitalService.getCe1_Identifier().getValue());
        
        CE careProvider = new CE(pv1.getMessage());
        ((Varies) zref.getField(7, 0)).setData(careProvider);
        
        pv1
            .getPv117_AdmittingDoctor(0)
            .getXcn1_IDNumber()
            .setValue(careProvider.getCe1_Identifier().getValue());
        
        CE admitCategory = new CE(pv1.getMessage());
        ((Varies) zref.getField(12, 0)).setData(admitCategory);
        
        pv1.getPv118_PatientType().setValue(
            admitCategory.getCe1_Identifier().getValue());
        
        for (int i = 20; i <= 40; i++)
        {
            pv1.getField(i, 0).clear();
        }
        
        pv1.getPv122_CourtesyCode().setValue("NSP");
        pv1.getPv128_InterestCode().setValue("1");
        pv1.getPv136_DischargeDisposition().setValue("CL");
        pv1
            .getPv137_DischargedToLocation()
            .getDld1_DischargeLocation()
            .setValue("U");
        pv1.getPv138_DietType().getCe1_Identifier().setValue("EMERG");
        pv1.getPv140_BedStatus().setValue("NSP");
        
    }
    
    private void transform(PID pid)
        throws HL7Exception
    {
        pid.getPid4_AlternatePatientIDPID(0).clear();
        pid.getPid6_MotherSMaidenName(0).clear();
        pid.getPid10_Race(0).clear();
        
        pid
            .getPatientName(0)
            .getPrefixEgDR()
            .setValue(
                StringUtils.upperCase(pid
                    .getPatientName(0)
                    .getPrefixEgDR()
                    .getValue()));
        
        for (int i = 12; i <= 15; i++)
        {
            pid.getField(i, 0).clear();
        }
        
        for (int i = 18; i <= 21; i++)
        {
            pid.getField(i, 0).clear();
        }
        
        for (int i = 25; i <= 38; i++)
        {
            pid.getField(i, 0).clear();
        }
        
    }
    
}
