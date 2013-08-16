package uk.nhs.kch.rassyeyanie.framework;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.util.DeepCopy;
import ca.uhn.hl7v2.util.SegmentFinder;
import ca.uhn.hl7v2.util.Terser;

public class HapiUtil
{
    
    @SuppressWarnings("unchecked")
    public static <T extends Structure> T get(AbstractMessage abstractMessage,
                                              Class<T> clazz)
        throws HL7Exception
    {
        
        return (T) abstractMessage.get(clazz.getSimpleName());
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Structure> T get(AbstractGroup abstractGroup,
                                              Class<T> clazz)
        throws HL7Exception
    {
        
        return (T) abstractGroup.get(clazz.getSimpleName());
    }
    
    public static <T extends Structure> List<T>
        getAll(AbstractMessage abstractMessage, Class<T> clazz)
            throws HL7Exception
    {
        
        return getAll(abstractMessage, clazz.getSimpleName(), clazz);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Structure> List<T>
        getAll(AbstractMessage abstractMessage, String name, Class<T> clazz)
            throws HL7Exception
    {
        
        List<T> retVal = new ArrayList<>();
        for (Structure next : abstractMessage.getAll(name))
        {
            retVal.add((T) next);
        }
        return Collections.unmodifiableList(retVal);
    }
    
    public static <T extends Structure> List<T>
        getAll(AbstractGroup abstractMessage, Class<T> clazz)
            throws HL7Exception
    {
        
        return getAll(abstractMessage, clazz.getSimpleName(), clazz);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Structure> List<T>
        getAll(AbstractGroup abstractGroup, String name, Class<T> clazz)
            throws HL7Exception
    {
        
        List<T> retVal = new ArrayList<T>();
        for (Structure next : abstractGroup.getAll(name))
        {
            retVal.add((T) next);
        }
        return Collections.unmodifiableList(retVal);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Structure> T
        getWithTerser(AbstractMessage workingMessage, Class<T> clazz)
            throws HL7Exception
    {
        Terser terser = new Terser(workingMessage);
        SegmentFinder finder = terser.getFinder();
        
        Segment segment = finder.findSegment(clazz.getSimpleName(), 0);
        return (T) segment;
    }
    
    public static <T extends Segment> T copySegment(T originalSegment,
                                                    T clonedSegment)
        throws HL7Exception
    {
        DeepCopy.copy(originalSegment, clonedSegment);
        return clonedSegment;
    }
    
    public static void clearField(Segment segment, int index)
        throws HL7Exception
    {
        segment.getField(index, 0).clear();
    }
    
    public static void
        clearFields(Segment segment, int startIndex, int endIndex)
            throws HL7Exception
    {
        for (int index = startIndex; index < endIndex; index++)
        {
            clearField(segment, index);
        }
    }
    
    public static void clearFieldsToEnd(Segment segment, int startIndex)
        throws HL7Exception
    {
        int count = segment.numFields();
        clearFields(segment, startIndex, count);
    }
    
    public static AbstractMessage createEmptyMessage(AbstractMessage message,
                                                     String version)
        throws HL7Exception
    {
        CanonicalModelClassFactory canonicalModelClassFactory =
            new CanonicalModelClassFactory(version);
        String className = getClassName(message);
        
        AbstractMessage result = null;
        
        try
        {
            Class<? extends Message> messageClass =
                canonicalModelClassFactory.getMessageClass(
                    className,
                    version,
                    false);
            if (messageClass == null)
                throw new ClassNotFoundException(
                    "Can't find message class in current package list: " +
                        className);
            
            Constructor<? extends Message> constructor =
                messageClass
                    .getConstructor(new Class[] { ModelClassFactory.class });
            result =
                (AbstractMessage) constructor
                    .newInstance(new Object[] { canonicalModelClassFactory });
        }
        catch (Exception e)
        {
            throw new HL7Exception(
                "Couldn't create Message object of type " + className,
                HL7Exception.UNSUPPORTED_MESSAGE_TYPE,
                e);
        }
        return result;
    }
    
    private static String getClassName(AbstractMessage message)
        throws HL7Exception
    {
        // return
        MSH msh = (MSH) message.get("MSH");
        return String.format("%s_%s", msh
            .getMsh9_MessageType()
            .getMsg1_MessageType()
            .getValue(), msh
            .getMsh9_MessageType()
            .getMsg2_TriggerEvent()
            .getValue());
    }
}
