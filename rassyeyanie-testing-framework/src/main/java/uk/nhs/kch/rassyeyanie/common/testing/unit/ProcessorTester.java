package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ca.uhn.hl7v2.model.AbstractMessage;

public class ProcessorTester
{
    private final String name;
    private final AbstractMessage message;
    private final Object[] processors;
    
    public ProcessorTester(String name,
                           AbstractMessage message,
                           Object[] processors)
    {
        this.name = name;
        this.message = message;
        this.processors = processors;
    }
    
    @Override
    public String toString()
    {
        return this.getName();
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public AbstractMessage getMessage()
    {
        return this.message;
    }
    
    public Object[] getFilter()
    {
        return this.processors;
    }
    
    public void performTest()
        throws Exception
    {
        for (Object processor : this.processors)
        {
            new ProcessorInvoker(processor, this.message).Invoke();
        }
    }
    
    private class ProcessorInvoker
    {
        private final Object processor;
        private final AbstractMessage message;
        private boolean invoked = false;
        
        public ProcessorInvoker(Object processor, AbstractMessage message)
        {
            this.processor = processor;
            this.message = message;
        }
        
        public void Invoke()
            throws Exception
        {
            this.invokeProcessor(this.processor, this.message);
            
            if (!this.invoked)
                throw new NoMatchingMethodOnProcessorException(this.processor);
        }
        
        private void invokeProcessor(Object processor, AbstractMessage message)
            throws IllegalAccessException, InvocationTargetException
        {
            Method[] methods = processor.getClass().getMethods();
            for (Method method : methods)
                if (method.getReturnType() == void.class)
                    this.invokeMethod(processor, method, message);
        }
        
        public void invokeMethod(Object processor,
                                 Method method,
                                 AbstractMessage message)
            throws IllegalAccessException, InvocationTargetException
        {
            @SuppressWarnings("rawtypes")
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 &&
                parameterTypes[0] == AbstractMessage.class)
            {
                method.invoke(processor, message);
                this.invoked = true;
            }
        }
    }
}
