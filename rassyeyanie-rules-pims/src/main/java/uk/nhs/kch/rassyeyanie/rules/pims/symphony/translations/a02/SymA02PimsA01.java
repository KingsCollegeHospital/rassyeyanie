package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a02;

import org.apache.camel.Body;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.message.ADT_A02;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class SymA02PimsA01
{
    public void transform(EVN evn)
        throws HL7Exception
    {
        evn.getEventTypeCode().setValue("A01");
    }
    
    public void transform(MSH msh)
        throws HL7Exception
    {
        msh.getMessageType().getTriggerEvent().setValue("A01");
    }
    
    public void dispatchProcessFixture(@Body ADT_A02 to)
        throws HL7Exception
    {
        Segment zref = (Segment) to.get("ZRF");
        this.transform(to.getPV1(), zref, to.getEVN());
        this.transformZref(zref);
        this.transform(to.getEVN());
        this.transform(to.getMSH());
        this.transform(to.getPID());
    }
    
    private void transform(PID pid)
        throws DataTypeException
    {
        pid.getPatientID().getIdentifierTypeCode().setValue("NHS");
    }
    
    private void transformZref(Segment zref)
        throws HL7Exception
    {
        CE ce = new CE(zref.getMessage());
        ((Varies) zref.getField(1, 0)).setData(ce);
        ce.getCe1_Identifier().setValue("EMERG");
        
        ce.getCe3_NameOfCodingSystem().setValue("DGATE");
    }
    
    private void transform(PV1 pv1, Segment zref, EVN evn)
        throws HL7Exception
    {
        pv1.getPv12_PatientClass().setValue("I");
        for (int i = 6; i < 9; i++)
            pv1.getField(i, 0).clear();
        pv1
            .getPv19_ConsultingDoctor(0)
            .getXcn1_IDNumber()
            .setValue(zref.getField(7, 0).encode().split("\\^")[0]);
        pv1.getPv110_HospitalService().setValue(
            zref.getField(9, 0).encode().split("\\^")[0]);
        pv1.getPv122_CourtesyCode().setValue("NSP");
        pv1.getPv128_InterestCode().setValue("NSP");
        zref.getField(2, 0).parse(
            pv1.getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().getValue());
        pv1
            .getPv144_AdmitDateTime()
            .getTs1_TimeOfAnEvent()
            .setValueToSecond(
                evn
                    .getEvn2_RecordedDateTime()
                    .getTimeOfAnEvent()
                    .getValueAsCalendar());
        pv1.getPv150_AlternateVisitID().clear();
        
        zref.getField(5, 0).parse(zref.getField(5, 0).encode() + "^^DGATE");
        zref.getField(8, 0).parse(zref.getField(9, 0).encode());
        zref.getField(11, 0).parse("R^^DGATE");
        ((Varies) zref.getField(12, 0)).setData(new ST(zref.getMessage()));
        zref.getField(12, 0).clear();
    }
}
