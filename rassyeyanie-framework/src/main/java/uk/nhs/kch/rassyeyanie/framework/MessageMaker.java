package uk.nhs.kch.rassyeyanie.framework;

import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.group.ORM_O01_ORDER;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class MessageMaker
{
    static final long serialVersionUID = 5661330224405262470L;
    
    private final AbstractMessage message;
    private final MSH msh;
    private final PV1 pv1;
    
    private MessageMaker(AbstractMessage message)
        throws HL7Exception
    {
        this.message = message;
        this.msh = HapiUtil.getWithTerser(this.message, MSH.class);
        this.msh.getFieldSeparator().setValue("|");
        this.msh.getEncodingCharacters().setValue("^~\\&");
        this.pv1 = HapiUtil.getWithTerser(message, PV1.class);
    }
    
    public static MessageMaker for_ORM_O01()
        throws HL7Exception
    {
        MessageMaker mm = new MessageMaker(new ORM_O01());
        return mm;
    }
    
    public static MessageMaker for_ADT_A01()
        throws HL7Exception
    {
        MessageMaker mm = new MessageMaker(new ADT_A01());
        return mm;
    }
    
    public static MessageMaker for_ADT_A13()
        throws HL7Exception
    {
        MessageMaker mm = new MessageMaker(new ADT_A01());
        return mm;
    }
    
    public AbstractMessage getMessage()
    {
        return this.message;
    }
    
    public MessageMaker receivingApplication(String value)
        throws DataTypeException
    {
        this.msh.getReceivingApplication().getNamespaceID().setValue(value);
        return this;
    }
    
    public MessageMaker sendingFacility(String value)
        throws DataTypeException
    {
        this.msh.getSendingFacility().getNamespaceID().setValue(value);
        return this;
    }
    
    public MessageMaker admissionType(String value)
        throws DataTypeException
    {
        this.pv1.getAdmissionType().setValue(value);
        return this;
    }
    
    public MessageMaker assignedPatientLocation(String value)
        throws DataTypeException
    {
        this.pv1.getAssignedPatientLocation().getPointOfCare().setValue(value);
        return this;
    }
    
    public MessageMaker priorPatientLocation(String value)
        throws DataTypeException
    {
        this.pv1.getPriorPatientLocation().getPointOfCare().setValue(value);
        return this;
    }
    
    public MessageMaker dischargeDisposition(String value)
        throws DataTypeException
    {
        this.pv1.getDischargeDisposition().setValue(value);
        return this;
    }
    
    public MessageMaker visitNumber(String value)
        throws DataTypeException
    {
        this.pv1.getVisitNumber().getID().setValue(value);
        return this;
    }
    
    public OrderMaker Order()
    {
        return new OrderMaker(this);
    }
    
    private void add(OrderMaker orderMaker)
        throws HL7Exception
    {
        ORM_O01 ormMessage = ((ORM_O01) this.message);
        int index = ormMessage.getORDERReps();
        ormMessage.insertRepetition("ORDER", index);
        orderMaker.populateLocation(ormMessage);
        orderMaker.populateOrder(ormMessage.getORDER(index));
        
    }
    
    public class OrderMaker
    {
        private final MessageMaker parent;
        private String placersField1Value;
        private String orderControlValue;
        private String priorityValue;
        private String locationValue;
        
        public OrderMaker(MessageMaker parent)
        {
            this.parent = parent;
        }
        
        public OrderMaker placersField1(String value)
        {
            this.placersField1Value = value;
            return this;
        }
        
        public OrderMaker orderControl(String value)
        {
            this.orderControlValue = value;
            return this;
        }
        
        public OrderMaker priority(String value)
        {
            this.priorityValue = value;
            return this;
        }
        
        public OrderMaker location(String value)
        {
            this.locationValue = value;
            return this;
        }
        
        public MessageMaker end()
            throws HL7Exception
        {
            this.parent.add(this);
            return this.parent;
        }
        
        public void populateLocation(ORM_O01 ormMessage)
            throws DataTypeException
        {
            if (!StringUtils.isEmpty(this.locationValue))
            {
                ormMessage
                    .getPATIENT()
                    .getPATIENT_VISIT()
                    .getPV1()
                    .getPv13_AssignedPatientLocation()
                    .getPointOfCare()
                    .setValue(this.locationValue);
            }
        }
        
        public void populateOrder(ORM_O01_ORDER order)
            throws HL7Exception
        {
            order
                .getORDER_DETAIL()
                .getOBR()
                .getPlacerField1()
                .setValue(this.placersField1Value);
            
            if (!StringUtils.isEmpty(this.orderControlValue))
                order
                    .getORC()
                    .getOrderControl()
                    .setValue(this.orderControlValue);
            
            if (!StringUtils.isEmpty(this.priorityValue))
            {
                if (order.getORC().getQuantityTimingReps() == 0)
                    order.getORC().insertQuantityTiming(0);
                order
                    .getORC()
                    .getQuantityTiming(0)
                    .getPriority()
                    .setValue(this.priorityValue);
            }
            
        }
    }
    
}
