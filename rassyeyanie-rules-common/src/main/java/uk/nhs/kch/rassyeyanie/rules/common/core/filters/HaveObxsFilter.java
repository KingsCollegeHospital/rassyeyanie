package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.group.ORU_R01_ORDER_OBSERVATION;

/* This filter is used to stop the broken messages without OBXs through */
public class HaveObxsFilter {

    public boolean shouldProcessMessage(@Body ORU_R01 r01)
            throws HL7Exception
        {
            
            boolean obxExisten = true;


            int resultCount = r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
            
            for (int i = 0; i < resultCount && obxExisten ; i++)
            {
            	ORU_R01_ORDER_OBSERVATION order_observation = r01.getPATIENT_RESULT().getORDER_OBSERVATION(i);
                obxExisten =
                    this.checkObxExisten(order_observation);
            }
            
                return (obxExisten);

        }
        
        private boolean checkObxExisten(ORU_R01_ORDER_OBSERVATION order_observation)
        {
            return (order_observation.getOBSERVATIONReps()> 0);
        }      

}
