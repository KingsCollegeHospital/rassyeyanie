package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.camel.Processor;

import ca.uhn.hl7v2.model.AbstractMessage;

public class TestContextFactory<T extends AbstractMessage> {

	ArrayList<Object[]> testContexts = new ArrayList<Object[]>();

	private String inputTemplate;
	private String outputTemplate;
	private MessageInstanceFactory<T> messageInstanceFactory;
	private Callable<Processor[]> processorMaker;

	public TestContextFactory() {
		this.testContexts = new ArrayList<Object[]>();
		// return empty processor list when tester doesn't pass one in
		this.processorMaker = new ProcessorMaker();
	}

	public interface MessageInstanceFactory<T> {
		T get();
	}

	public TestContextFactory<T> setInputTemplate(String inputTemplate) {
		this.inputTemplate = inputTemplate;
		return this;
	}

	public TestContextFactory<T> setOutputTemplate(String outputTemplate) {
		this.outputTemplate = outputTemplate;
		return this;
	}

	public TestContextFactory<T> setMessageInstanceFactory(
			MessageInstanceFactory<T> messageInstanceFactory) {
		this.messageInstanceFactory = messageInstanceFactory;
		return this;
	}

	public TestContextFactory<T> setProcessorMaker(
			Callable<Processor[]> processorMaker) {
		this.processorMaker = processorMaker;
		return this;
	}

	public static class ProcessorMaker implements Callable<Processor[]> {
		@Override
		public Processor[] call() {
			return new Processor[] {};
		}
	}

	public TestContextFactory<T> add(String messageId) throws Exception {
		if (this.messageInstanceFactory != null) {
			this.testContexts.add(new Object[] { new TestContext<T>(String
					.format(this.inputTemplate, messageId), String.format(
					this.outputTemplate, messageId),
					this.messageInstanceFactory.get(), this.processorMaker
							.call()) });
		} else {
			this.testContexts.add(new Object[] { new TestContext<T>(String
					.format(this.inputTemplate, messageId), String.format(
					this.outputTemplate, messageId), null, this.processorMaker
					.call()) });

		}

		return this;
	}

	public ArrayList<Object[]> getTestContexts() {
		return this.testContexts;
	}
}
