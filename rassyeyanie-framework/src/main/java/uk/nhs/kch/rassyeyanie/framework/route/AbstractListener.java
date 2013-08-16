package uk.nhs.kch.rassyeyanie.framework.route;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Extend this class and implement the createRoute method to create different
 * kinds of listeners.
 */
public abstract class AbstractListener
    extends AbstractRouteBuilder
{
    
    private List<ListenerConfig> listeners;
    
    protected abstract void createRoute(ListenerConfig listenerConfig);
    
    @Autowired
    public void setListeners(List<ListenerConfig> listeners)
    {
        this.listeners = Collections.unmodifiableList(listeners);
    }
    
    public List<ListenerConfig> getListeners()
    {
        return this.listeners;
    }
    
    @Override
    public void configure()
    {
        
        for (ListenerConfig listenerConfig : this.listeners)
        {
            this.createRoute(listenerConfig);
        }
    }
}
