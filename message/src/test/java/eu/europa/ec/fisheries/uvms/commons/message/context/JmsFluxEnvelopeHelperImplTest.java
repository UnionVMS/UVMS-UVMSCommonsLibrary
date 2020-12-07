package eu.europa.ec.fisheries.uvms.commons.message.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Message;

import java.time.ZonedDateTime;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageRuntimeException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link JmsFluxEnvelopeHelperImpl}.
 */
public class JmsFluxEnvelopeHelperImplTest {

	private static final String VALUE_FR = "sender_or_receiver";
	private static final String VALUE_DF = "dataflow";
	private static final String VALUE_GUID = "the guid";
	private static final ZonedDateTime VALUE_RECEPTION = ZonedDateTime.parse("2019-12-03T10:15:30Z", FluxEnvelopePropagatedData.RECEPTION_FORMATTER);

	private JmsFluxEnvelopeHelperImpl sut;

	@Before
	public void before() {
		sut = new JmsFluxEnvelopeHelperImpl();
	}

	@Test
	public void testExtractWithAllNull() throws JMSException {
		Message message = mock(Message.class);
		when(message.getStringProperty(anyString())).thenReturn(null);
		assertNull(sut.extract(message));
	}

	@Test
	public void testExtract() throws JMSException {
		Message message = mock(Message.class);
		when(message.getStringProperty("FLUX_FR")).thenReturn(VALUE_FR);
		FluxEnvelopePropagatedData result = sut.extract(message);
		assertNotNull(result);
		assertEquals(VALUE_FR, result.getSenderOrReceiver());
		assertNull(result.getMessageGuid());
		assertNull(result.getDataflow());
		when(message.getStringProperty("FLUX_DF")).thenReturn(VALUE_DF);
		when(message.getStringProperty("FLUX_GUID")).thenReturn(VALUE_GUID);
		result = sut.extract(message);
		assertEquals(VALUE_FR, result.getSenderOrReceiver());
		assertEquals(VALUE_DF, result.getDataflow());
		assertEquals(VALUE_GUID, result.getMessageGuid());
	}

	@Test
	public void testExtractWithException() throws JMSException {
		Message message = mock(Message.class);
		when(message.getStringProperty(anyString())).thenThrow(JMSException.class);
		try {
			sut.extract(message);
			fail("should throw");
		} catch (MessageRuntimeException expected) {
			// expected
		}
	}

	@Test
	public void testSetHeadersWithNull() {
		Message message = mock(Message.class);
		sut.setHeaders(null, message);
		verifyNoMoreInteractions(message);
	}

	@Test
	public void testSetHeadersWithMessageGuid() throws JMSException {
		Message message = mock(Message.class);
		FluxEnvelopePropagatedData data = new FluxEnvelopePropagatedData(VALUE_GUID, null, null, null);
		sut.setHeaders(data, message);
		verify(message).setStringProperty("FLUX_GUID", VALUE_GUID);
		verifyNoMoreInteractions(message);
	}

	@Test
	public void testSetHeadersWithDataflow() throws JMSException {
		Message message = mock(Message.class);
		FluxEnvelopePropagatedData data = new FluxEnvelopePropagatedData(null, VALUE_DF, null, null);
		sut.setHeaders(data, message);
		verify(message).setStringProperty("FLUX_DF", VALUE_DF);
		verifyNoMoreInteractions(message);
	}

	@Test
	public void testSetHeadersWithSenderOrReceiver() throws JMSException {
		Message message = mock(Message.class);
		FluxEnvelopePropagatedData data = new FluxEnvelopePropagatedData(null, null, VALUE_FR, null);
		sut.setHeaders(data, message);
		verify(message).setStringProperty("FLUX_FR", VALUE_FR);
		verifyNoMoreInteractions(message);
	}

	@Test
	public void testSetHeadersWithReceptionDateTime() throws JMSException {
		Message message = mock(Message.class);
		FluxEnvelopePropagatedData data = new FluxEnvelopePropagatedData(null, null, null, VALUE_RECEPTION);
		sut.setHeaders(data, message);
		verify(message).setStringProperty("FLUX_receptionDateTime", "2019-12-03T10:15:30Z");
		verifyNoMoreInteractions(message);
	}

	@Test
	public void testSetHeadersWithException() throws JMSException {
		Message message = mock(Message.class);
		doThrow(JMSException.class).when(message).setStringProperty(anyString(), anyString());
		FluxEnvelopePropagatedData data = new FluxEnvelopePropagatedData(null, null, VALUE_FR, null);
		try {
			sut.setHeaders(data, message);
			fail("should throw");
		} catch (MessageRuntimeException expected) {
			// expected
		}
	}
}
