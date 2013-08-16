package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.util.ArrayList;
import java.util.Collection;

import ca.uhn.hl7v2.model.AbstractMessage;

public class FilterTesterFactory
{
    
    private AbstractMessage message;
    private Object filter;
    private boolean expectedOutcome;
    
    private final ArrayList<Object[]> filterTestContexts =
        new ArrayList<Object[]>();
    
    public Collection<Object[]> get()
    {
        return this.filterTestContexts;
    }
    
    public FilterTesterFactory setMessage(AbstractMessage message)
    {
        this.message = message;
        return this;
    }
    
    public FilterTesterFactory setFilter(Object filter)
    {
        this.filter = filter;
        return this;
    }
    
    public FilterTesterFactory setExpectedOutcome(boolean expectedOutcome)
    {
        this.expectedOutcome = expectedOutcome;
        return this;
    }
    
    public FilterTesterFactory add(String name)
    {
        return this.add(name, this.message, this.filter, this.expectedOutcome);
    }
    
    public FilterTesterFactory add(String name, boolean expectedOutcome)
    {
        return this.add(name, this.message, this.filter, expectedOutcome);
    }
    
    public FilterTesterFactory add(String name, AbstractMessage message)
    {
        return this.add(name, message, this.filter, this.expectedOutcome);
    }
    
    public FilterTesterFactory add(String name, Object filter)
    {
        return this.add(name, this.message, filter, this.expectedOutcome);
    }
    
    public FilterTesterFactory add(String name,
                                   AbstractMessage message,
                                   Object filter)
    {
        return this.add(name, message, filter, this.expectedOutcome);
    }
    
    public FilterTesterFactory add(String name,
                                   AbstractMessage message,
                                   Object filter,
                                   boolean expectedOutcome)
    {
        this.filterTestContexts.add(new Object[] { new FilterTester(
            name,
            message,
            filter,
            expectedOutcome) });
        return this;
    }
    
}
