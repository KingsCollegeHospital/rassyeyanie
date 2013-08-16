package uk.nhs.kch.rassyeyanie.common.testing.unit;

public class NoMatchingMethodOnProcessorException
    extends Exception
{
    private static final long serialVersionUID = -168654631845685733L;
    private final Object processor;
    
    public NoMatchingMethodOnProcessorException(Object processor)
    {
        this.processor = processor;
    }
    
    @Override
    public String toString()
    {
        return String
            .format(
                "Unable to find method with correct signature to invoke on processor: '%s'.",
                this.processor);
    }
}
