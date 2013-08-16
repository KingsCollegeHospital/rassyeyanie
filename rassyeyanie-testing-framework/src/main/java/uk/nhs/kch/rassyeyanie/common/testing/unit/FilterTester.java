package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ca.uhn.hl7v2.model.AbstractMessage;

public class FilterTester
{
    private final String name;
    private final AbstractMessage message;
    private final Object filter;
    private final boolean expectedOutcome;
    private boolean actualOutcome;
    
    public FilterTester(String name,
                        AbstractMessage message,
                        Object filter,
                        boolean expectedOutcome)
    {
        this.name = name;
        this.message = message;
        this.filter = filter;
        this.expectedOutcome = expectedOutcome;
    }
    
    @Override
    public String toString()
    {
        return this.getName();
    }
    
    public static FilterTesterFactory getFactory()
    {
        return new FilterTesterFactory();
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public AbstractMessage getMessage()
    {
        return this.message;
    }
    
    public Object getFilter()
    {
        return this.filter;
    }
    
    public boolean getExpectedOutcome()
    {
        return this.expectedOutcome;
    }
    
    public boolean isExpectedOutcome()
    {
        return this.actualOutcome == this.expectedOutcome;
    }
    
    public void performTest()
        throws NoSuchMethodException, SecurityException,
        IllegalAccessException, IllegalArgumentException,
        InvocationTargetException
    {
        // Method method =
        // this.filter.getClass().getMethod("perform", AbstractMessage.class);
        Method[] methods = this.filter.getClass().getMethods();
        for (Method method : methods)
        {
            if (method.getReturnType() == boolean.class)
            {
                @SuppressWarnings("rawtypes")
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 &&
                    parameterTypes[0] == AbstractMessage.class)
                {
                    this.actualOutcome =
                        (boolean) method.invoke(this.filter, this.message);
                }
            }
        }
    }
}
