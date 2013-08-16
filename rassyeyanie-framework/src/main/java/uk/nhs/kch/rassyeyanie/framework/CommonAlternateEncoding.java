package uk.nhs.kch.rassyeyanie.framework;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.datatype.ST;
import ca.uhn.hl7v2.model.v24.segment.MSH;

public class CommonAlternateEncoding extends AbstractProcessor {

    private static void insertEncodingCharactersToMSH(MSH msh)
            throws HL7Exception {
        ST encodingChars = msh.getEncodingCharacters();
        encodingChars.setValue("^~\\,");

    }

    public void transform(MSH msh) throws HL7Exception {
        insertEncodingCharactersToMSH(msh);
    }

    @Override
    protected void dispatchProcessFixture(AbstractMessage workingMessage)
            throws HL7Exception {
        this.transform(HapiUtil.get(workingMessage, MSH.class));

    }

}
