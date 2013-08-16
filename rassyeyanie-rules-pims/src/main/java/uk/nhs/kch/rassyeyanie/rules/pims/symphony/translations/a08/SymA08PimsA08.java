package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a08;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.TS;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.AL1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.util.DeepCopy;

/**
 * @author Rajeev Bera on 17-Aug-12
 * @version 2.4 (the version of the package this class was first added to)
 */
// EC: this translation does not work in live --
// I have made some assumptions about how it would work if it did
// MT: Last meeting between ICT and ED ID agreed A08 to PIMS only allow to go
// through if patient is at CDU
// Updated 14.2.2013
public class SymA08PimsA08
{
    public void processMessage(@Body ADT_A01 to)
        throws HL7Exception
    {
        to.addNonstandardSegment("ZRF");
        Segment zref = null;
        if (to.getPROCEDUREReps() > 0)
        {
            zref = (Segment) to.insertRepetition("ZRF", 0);
        }
        else
        {
            AbstractSegment abstractSegment = (AbstractSegment) to.get("ZRF");
            abstractSegment.clear();
            zref = abstractSegment;
            
        }
        
        this.transform(to.getMSH());
        this.transform(to.getPID());
        this.transformZref(zref, to.getPV1());
        ADT_A01 from = new ADT_A01();
        DeepCopy.copy(to.getPV1(), from.getPV1());
        this.transform(to.getPV1(), from.getPV1());
        
        for (int i = 0; i < to.getDG1Reps(); i++)
        {
            to.getDG1(i).clear();
        }
        
        for (Structure al1 : to.getAll("AL1"))
        {
            ((AL1) al1).clear();
        }
        
        to.addNonstandardSegment("NTE");
        for (Structure nte : to.getAll("NTE"))
        {
            ((NTE) nte).clear();
        }
        
        for (int i = 0; i < to.getPROCEDUREReps(); i++)
        {
            to.getPROCEDURE(i).clear();
        }
    }
    
    private void transformZref(Segment zref, PV1 pv1)
        throws DataTypeException, HL7Exception
    {
        
        CE referralType = new CE(pv1.getMessage());
        ((Varies) zref.getField(1, 0)).setData(referralType);
        referralType.getCe1_Identifier().setValue("EMERG");
        referralType.getCe3_NameOfCodingSystem().clear();
        
        TS dateReceived = new TS(pv1.getMessage());
        ((Varies) zref.getField(2, 0)).setData(dateReceived);
        dateReceived.getTs1_TimeOfAnEvent().setValue(
            pv1.getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().getValue());
        
        for (int i = 3; i <= 14; i++)
        {
            zref.getField(i, 0).clear();
        }
        
        CE referralSource = new CE(pv1.getMessage());
        ((Varies) zref.getField(5, 0)).setData(referralSource);
        referralSource.getCe1_Identifier().clear();
        referralSource.getCe3_NameOfCodingSystem().setValue("DGATE");
        
        CE priority = new CE(pv1.getMessage());
        ((Varies) zref.getField(11, 0)).setData(priority);
        priority.getCe1_Identifier().setValue("R");
        
        CE qual = new CE(pv1.getMessage());
        ((Varies) zref.getField(12, 0)).setData(qual);
        qual.clear();
        
    }
    
    /**
     * This method for translating MSH segment in A08 message from SYM to PIMS.
     * 
     */
    public void transform(MSH msh)
        throws HL7Exception
    {
        msh
            .getMsh3_SendingApplication()
            .getHd1_NamespaceID()
            .setValue("SYMPHONY");
    }
    
    /**
     * This method for translating PID segment in A08 message from SYM to PIMS.
     */
    public void transform(PID pid)
        throws HL7Exception
    {
        
        pid.getPid2_PatientID().getCx5_IdentifierTypeCode().setValue("NHS");
        pid
            .getPid3_PatientIdentifierList(0)
            .getCx5_IdentifierTypeCode()
            .setValue("PAS");
        pid
            .getPatientName(0)
            .getXpn5_PrefixEgDR()
            .setValue(
                StringUtils.upperCase(pid
                    .getPatientName(0)
                    .getXpn5_PrefixEgDR()
                    .getValue()));
        
        pid.getPid16_MaritalStatus().getCe1_Identifier().setValue("NSP");
        pid.getPid17_Religion().getCe1_Identifier().setValue("NSP");
        pid.getPid22_EthnicGroup(0).getCe1_Identifier().setValue("NSP");
        pid.getPid24_MultipleBirthIndicator().setValue("NSP");
    }
    
    /**
     * PV1.
     */
    
    public void transform(PV1 pv1, PV1 copyPv1)
        throws HL7Exception
    {
        pv1.clear(); // Use this to be able to remove the non-standard recursive
                     // value in PV1-39
        
        pv1.getPv11_SetIDPV1().setValue(copyPv1.getPv11_SetIDPV1().getValue());
        pv1.getPv12_PatientClass().setValue("I");
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl1_PointOfCare()
            .setValue(copyPv1.getPv14_AdmissionType().getValue());
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl4_Facility()
            .getHd1_NamespaceID()
            .setValue("KCH");
        
        pv1
            .getPv13_AssignedPatientLocation()
            .getPl9_LocationDescription()
            .setValue(copyPv1.getPv14_AdmissionType().getValue());
        
        DeepCopy.copy(
            copyPv1.getPv114_AdmitSource(),
            pv1.getPv114_AdmitSource());
        
        pv1.getBedStatus().setValue("NSP");
        
        DeepCopy.copy(
            copyPv1.getPv119_VisitNumber(),
            pv1.getPv119_VisitNumber());
        
        pv1.getPv131_BadDebtAgencyCode().setValue("D");
        
        pv1
            .getPv144_AdmitDateTime()
            .getTs1_TimeOfAnEvent()
            .setValue(
                copyPv1
                    .getPv144_AdmitDateTime()
                    .getTs1_TimeOfAnEvent()
                    .getValue());
        
    }
}
