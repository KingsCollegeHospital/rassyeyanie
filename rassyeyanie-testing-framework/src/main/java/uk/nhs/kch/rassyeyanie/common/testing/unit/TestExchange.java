package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.spi.UnitOfWork;

public class TestExchange implements Exchange {

    private Message in;
    private Message out;

    @Override
    public ExchangePattern getPattern() {
	return null;
    }

    @Override
    public void setPattern(ExchangePattern pattern) {
    }

    @Override
    public Object getProperty(String name) {
	return null;
    }

    @Override
    public Object getProperty(String name, Object defaultValue) {
	return null;
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {
	return null;
    }

    @Override
    public <T> T getProperty(String name, Object defaultValue, Class<T> type) {
	return null;
    }

    @Override
    public void setProperty(String name, Object value) {
    }

    @Override
    public Object removeProperty(String name) {
	return null;
    }

    @Override
    public Map<String, Object> getProperties() {
	return null;
    }

    @Override
    public boolean hasProperties() {
	return false;
    }

    @Override
    public Message getIn() {
	if (this.in == null) {
	    this.in = new DefaultMessage();
	}

	return this.in;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getIn(Class<T> type) {
	return (T) this.in;
    }

    @Override
    public void setIn(Message in) {
	this.in = in;
    }

    @Override
    public Message getOut() {
	if (this.out == null) {
	    // in camel the output would be the input *if* the input is null
	    this.out = this.in;
	}

	return this.out;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOut(Class<T> type) {
	return (T) this.out;
    }

    @Override
    public boolean hasOut() {
	return this.out != null;
    }

    @Override
    public void setOut(Message out) {
	this.out = out;
    }

    @Override
    public Exception getException() {
	return null;
    }

    @Override
    public <T> T getException(Class<T> type) {
	return null;
    }

    @Override
    public void setException(Throwable t) {
    }

    @Override
    public boolean isFailed() {
	return false;
    }

    @Override
    public boolean isTransacted() {
	return false;
    }

    @Override
    public Boolean isExternalRedelivered() {
	return null;
    }

    @Override
    public boolean isRollbackOnly() {
	return false;
    }

    @Override
    public CamelContext getContext() {
	return null;
    }

    @Override
    public Exchange copy() {
	return null;
    }

    @Override
    public Endpoint getFromEndpoint() {
	return null;
    }

    @Override
    public void setFromEndpoint(Endpoint fromEndpoint) {
    }

    @Override
    public String getFromRouteId() {
	return null;
    }

    @Override
    public void setFromRouteId(String fromRouteId) {
    }

    @Override
    public UnitOfWork getUnitOfWork() {
	return null;
    }

    @Override
    public void setUnitOfWork(UnitOfWork unitOfWork) {
    }

    @Override
    public String getExchangeId() {
	return null;
    }

    @Override
    public void setExchangeId(String id) {
    }

    @Override
    public void addOnCompletion(Synchronization onCompletion) {
    }

    @Override
    public boolean containsOnCompletion(Synchronization onCompletion) {
	return false;
    }

    @Override
    public void handoverCompletions(Exchange target) {
    }

    @Override
    public List<Synchronization> handoverCompletions() {
	return null;
    }
}
