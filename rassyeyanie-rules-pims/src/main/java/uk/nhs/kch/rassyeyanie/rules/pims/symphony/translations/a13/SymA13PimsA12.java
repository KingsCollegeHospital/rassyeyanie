package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations.a13;

import org.apache.camel.Body;
import org.apache.camel.Header;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;
import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.CE;
import ca.uhn.hl7v2.model.v24.datatype.XCN;
import ca.uhn.hl7v2.model.v24.group.ADT_A01_PROCEDURE;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.util.DeepCopy;

/* Cancel discharge from CDU to ward.  Patient transfer back to CDU. */

public class SymA13PimsA12
{
    public
        void
        dispatchProcessFixture(@Header(HL7AdditionalConstants.HL7_SOURCE_MESSAGE) ADT_A01 from,
                               @Body ADT_A01 to)
            throws HL7Exception
    {
        
        this.changeMessageEventType(to, "A12");
        
        this.tranformPid(to.getPID());
        
        Segment zrf = this.promoteZrfSegment(from);
        
        this.transformZrf(zrf, to.getPV1());
        this.transform(to.getPV1(), zrf);
        
        to.addNonstandardSegment("ZRF");
        Segment toZrf = (Segment) to.insertRepetition("ZRF", 0);
        DeepCopy.copy(zrf, toZrf);
    }
    
    private Segment promoteZrfSegment(ADT_A01 message)
        throws HL7Exception
    {
        
        int countPr1 = message.getPROCEDUREReps();
        if (countPr1 > 0)
        {
            ADT_A01_PROCEDURE lastProcedure =
                message.getPROCEDURE(countPr1 - 1);
            
            lastProcedure.addNonstandardSegment("ZRF");
            Segment zrf = (Segment) lastProcedure.get("ZRF");
            
            return zrf;
        }
        else
        {
            return (Segment) message.get("ZRF");
        }
    }
    
    private void changeMessageEventType(ADT_A01 message, String eventType)
        throws DataTypeException, HL7Exception
    {
        message.getMSH().getMessageType().getTriggerEvent().setValue(eventType);
        message.getEVN().getEventTypeCode().setValue(eventType);
    }
    
    private void tranformPid(PID pid)
        throws HL7Exception
    {
        HapiUtil.clearFieldsToEnd(pid, 23);
    }
    
    private void transform(PV1 pv1, Segment zrf)
        throws HL7Exception
    {
        pv1.getPv12_PatientClass().setValue("I");
        pv1.getPv13_AssignedPatientLocation().clear();
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
        
        pv1.getPv15_PreadmitNumber().clear();
        pv1
            .getPv16_PriorPatientLocation()
            .getPl1_PointOfCare()
            .setValue(pv1.getPv14_AdmissionType().getValue());
        pv1
            .getPv16_PriorPatientLocation()
            .getPl4_Facility()
            .getHd1_NamespaceID()
            .setValue("KCH");
        
        pv1.getPv14_AdmissionType().setValue("21");
        pv1.getPv114_AdmitSource().setValue("19");
        HapiUtil.clearField(pv1, 7);
        HapiUtil.clearField(pv1, 8);
        DeepCopy.copy(
            pv1.getPv19_ConsultingDoctor(0),
            pv1.getPv18_ReferringDoctor(0));
        /*
         * TODO: Is it important? as it's never work in Data-gate
         * pv1.getPv19_ConsultingDoctor(0).getXcn1_IDNumber()
         * .setValue(((Varies) zrf.getField(8, 0)).encode());
         */
        HapiUtil.clearField(pv1, 9);
        
        pv1.getPv110_HospitalService().setValue("AE");
        HapiUtil.clearField(pv1, 12);
        HapiUtil.clearField(pv1, 13);
        HapiUtil.clearFields(pv1, 20, 43);
        pv1.getPv122_CourtesyCode().setValue("NSP");
        pv1.getPv128_InterestCode().setValue("NSP");
        pv1.getPv140_BedStatus().setValue("NSP");
        HapiUtil.clearFieldsToEnd(pv1, 45);
    }
    
    private void transformZrf(Segment zrf, PV1 pv1)
        throws HL7Exception
    {
        zrf.getField(1, 0).parse("EMERG^^DGATE");
        zrf.getField(2, 0).parse(
            pv1.getPv144_AdmitDateTime().getTimeOfAnEvent().getValue());
        
        ((Varies) zrf.getField(5, 0)).setData(new XCN(zrf.getMessage()));
        zrf.getField(5, 0).parse("^^DGATE");
        
        zrf.getField(6, 0).clear();
        zrf.getField(6, 0).parse("EDCDU^^DGATE");
        
        ((Varies) zrf.getField(7, 0)).setData(new XCN(zrf.getMessage()));
        zrf.getField(7, 0).parse("^^DGATE");
        
        ((Varies) zrf.getField(8, 0)).setData(new CE(zrf.getMessage()));
        zrf.getField(8, 0).parse("^^DGATE");
        
        ((Varies) zrf.getField(9, 0)).setData(new CE(zrf.getMessage()));
        zrf.getField(9, 0).parse("^^DGATE");
        
        ((Varies) zrf.getField(10, 0)).setData(new XCN(zrf.getMessage()));
        ((Varies) zrf.getField(11, 0)).setData(new XCN(zrf.getMessage()));
        ((Varies) zrf.getField(12, 0)).setData(new XCN(zrf.getMessage()));
        zrf.getField(10, 0).parse("");
        zrf.getField(11, 0).parse("");
        zrf.getField(12, 0).parse("");
    }
    
}
