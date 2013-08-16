package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Test code for NullTransformer class.
 */
@Deprecated
public class NullTransformerTest {

    private NullTransformer transformer;

    @Mock private Exchange mockExchange;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        transformer = new NullTransformer();
    }

    @Test
    public void testExchangeIsNotAltered() {

        transformer.process(mockExchange);

        verifyNoMoreInteractions(mockExchange);
    }
}
