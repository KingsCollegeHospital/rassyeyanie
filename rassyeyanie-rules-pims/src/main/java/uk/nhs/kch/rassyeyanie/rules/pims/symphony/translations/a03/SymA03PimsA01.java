package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a03;

import org.apache.camel.Body;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.group.ADT_A03_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.message.ADT_A03;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.util.DeepCopy;

public class SymA03PimsA01
{
    
    public void transform(MSH msh)
        throws HL7Exception
    {
        
        msh.getMsh3_SendingApplication().getNamespaceID().setValue("SYMPHONY");
        msh.getMessageType().getTriggerEvent().setValue("A01");
        msh.getMsh13_SequenceNumber().clear();
        msh.getMsh14_ContinuationPointer().clear();
        msh.getMsh17_CountryCode().clear();
        int i = 0;
        while (i < msh.getMsh18_CharacterSetReps())
        {
            msh.getMsh18_CharacterSet(i).clear();
            i++;
        }
        msh.getMsh19_PrincipalLanguageOfMessage().clear();
        
    }
    
    public void transform(EVN evn)
        throws HL7Exception
    {
        evn.getEventTypeCode().setValue("A01");
    }
    
    public void transform(PID pid)
        throws HL7Exception
    {
        pid.getPid2_PatientID().getCx5_IdentifierTypeCode().setValue("NHS");
        pid.getPid19_SSNNumberPatient().clear();
        pid.getPid20_DriverSLicenseNumberPatient().clear();
        int i = 0;
        while (i < pid.getPid21_MotherSIdentifierReps())
        {
            pid.getPid21_MotherSIdentifier(i).clear();
            i++;
        }
        pid.getPid24_MultipleBirthIndicator().clear();
        
    }
    
    public void transform(PV1 pv1, Segment zref, EVN evn)
        throws HL7Exception
    {
        
        ADT_A01 a01 = new ADT_A01();
        MSH mshSegment = a01.getMSH();
        mshSegment.getFieldSeparator().setValue("|");
        mshSegment.getEncodingCharacters().setValue("^~\\&");
        PV1 tempPv1 = a01.getPV1();
        ca.uhn.hl7v2.util.DeepCopy.copy(pv1, tempPv1);
        pv1.clear();
        pv1.getPv11_SetIDPV1().setValue(tempPv1.getPv11_SetIDPV1().encode());
        pv1.getPv14_AdmissionType().setValue(
            tempPv1.getPv14_AdmissionType().encode());
        pv1.getPv110_HospitalService().setValue(
            zref.getField(9, 0).encode().toString());
        pv1.getPv114_AdmitSource().setValue(
            tempPv1.getPv114_AdmitSource().encode());
        
        pv1.getPv19_ConsultingDoctor(0).parse(
            zref.getField(7, 0).encode().toString().replace("^^DGATE", ""));
        pv1.getPv12_PatientClass().setValue("I");
        String dischargeLocation =
            tempPv1.getPv137_DischargedToLocation().encode();
        pv1.getPv18_ReferringDoctor(0).parse(
            tempPv1.getPv19_ConsultingDoctor(0).encode());
        pv1.getPv13_AssignedPatientLocation().parse(
            dischargeLocation.split("\\^")[0] + "^^^KCH^^^^^" +
                dischargeLocation.split("\\^")[1]);
        pv1.getPv117_AdmittingDoctor(0).parse(
            tempPv1.getPv117_AdmittingDoctor(0).encode());
        pv1.getPv118_PatientType().parse(
            tempPv1.getPv118_PatientType().encode());
        pv1.getPv119_VisitNumber().parse(
            tempPv1.getPv119_VisitNumber().encode());
        pv1.getPv122_CourtesyCode().setValue("NSP");
        pv1.getPv128_InterestCode().parse(
            tempPv1.getPv128_InterestCode().encode());
        pv1.getPv128_InterestCode().setValue("NSP");
        pv1.getPv140_BedStatus().setValue("NSP");
        pv1.getPv144_AdmitDateTime().parse(
            tempPv1.getPv145_DischargeDateTime(0).encode());
        CE ce = new CE(zref.getMessage());
        ((Varies) zref.getField(1, 0)).setData(ce);
        ce.getCe1_Identifier().setValue("EMERG");
        
        ce.getCe3_NameOfCodingSystem().setValue("DGATE");
        zref.getField(2, 0).parse(
            tempPv1.getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().getValue());
        
        zref.getField(5, 0).parse(zref.getField(5, 0).encode() + "^^DGATE");
        zref.getField(8, 0).parse(zref.getField(9, 0).encode());
        zref.getField(9, 0).parse(zref.getField(9, 0).encode() + "^^DGATE");
        zref.getField(11, 0).parse("R^^DGATE");
        ((Varies) zref.getField(12, 0)).setData(new ST(zref.getMessage()));
        zref.getField(12, 0).clear();
        
    }
    
    public void dispatchProcessFixture(@Body ADT_A03 to)
        throws HL7Exception
    {
        AbstractGroup zrefParent = getZrefParent(to);
        AbstractSegment zref = (AbstractSegment) zrefParent.get("ZRF");
        
        this.transform(to.getPV1(), zref, to.getEVN());
        this.transform(to.getEVN());
        this.transform(to.getMSH());
        this.transform(to.getPID());
        
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
            to.addNonstandardSegment("ZRF");
            Segment zrf = (Segment) to.insertRepetition("ZRF", 0);
            DeepCopy.copy(zref, zrf);
            to.insertRepetition("ZRF", 0);
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
    
}
