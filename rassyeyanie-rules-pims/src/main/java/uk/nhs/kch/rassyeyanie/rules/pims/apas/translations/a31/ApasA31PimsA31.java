package uk.nhs.kch.rassyeyanie.rules.pims.apas.translations.a31;

import org.apache.camel.Body;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.model.v24.segment.PD1;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class ApasA31PimsA31
{
    public void processMessage(@Body ADT_A05 to)
        throws HL7Exception
    {
        this.transform(to.getPD1());
        this.transform(to.getPV1());
    }
    
    public void transform(PD1 pd1)
    {
        pd1.clear();
    }
    
    public void transform(PV1 pv1)
        throws HL7Exception
    {
        pv1.clear();
        pv1.getPv11_SetIDPV1().setValue("1");
        pv1.getPv12_PatientClass().setValue("R");
    }
}
