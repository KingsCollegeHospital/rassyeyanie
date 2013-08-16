package uk.nhs.kch.rassyeyanie.framework.processor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.nhs.kch.rassyeyanie.framework.processor.MessageSelectionAdapter;

/**
 */
public class MessageSelectionAdapterTest {

    private MessageSelectionAdapter adapter;

    @Mock private Processor mockProcessorOne;
    @Mock private Processor mockProcessorTwo;
    @Mock private Exchange mockExchange;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        List<Processor> processorList = Arrays.asList(mockProcessorOne, mockProcessorTwo);

        adapter = new MessageSelectionAdapter();
        adapter.setProcessorList(processorList);
    }

    @Test
    public void testAdapterDefersToProcessor() throws Exception {
        adapter.process(mockExchange);

        verify(mockProcessorOne).process(mockExchange);
        verify(mockProcessorTwo).process(mockExchange);
    }

    @Test
    public void testAdapterWithNoProcessors() throws Exception {
        MessageSelectionAdapter localAdapter = new MessageSelectionAdapter();
        localAdapter.process(mockExchange);

        verifyNoMoreInteractions(mockExchange);
    }
}
