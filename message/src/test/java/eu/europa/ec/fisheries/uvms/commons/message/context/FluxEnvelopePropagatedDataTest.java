package eu.europa.ec.fisheries.uvms.commons.message.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link FluxEnvelopePropagatedData}.
 */
public class FluxEnvelopePropagatedDataTest {
	@Test
	public void testEqualsAndHashCode() {
		FluxEnvelopePropagatedData d1 = new FluxEnvelopePropagatedData("1", "2", "3");
		FluxEnvelopePropagatedData d2 = new FluxEnvelopePropagatedData("1", "2", "3");
		assertTrue(d1.equals(d2));
		assertTrue(d2.equals(d1));
		assertEquals(d1.hashCode(), d2.hashCode());
		FluxEnvelopePropagatedData d3 = new FluxEnvelopePropagatedData("X", "2", "3");
		assertFalse(d1.equals(d3));
		assertFalse(d3.equals(d1));
		FluxEnvelopePropagatedData d4 = new FluxEnvelopePropagatedData("1", "2", "3") {};
		assertFalse(d1.equals(d4));
		FluxEnvelopePropagatedData d5 = new FluxEnvelopePropagatedData("1", "X", "3");
		assertFalse(d1.equals(d5));
		assertFalse(d5.equals(d1));
		FluxEnvelopePropagatedData d6 = new FluxEnvelopePropagatedData("1", "2", "X");
		assertFalse(d1.equals(d6));
		assertFalse(d6.equals(d1));
	}

	@Test
	public void testGetters() {
		FluxEnvelopePropagatedData d = new FluxEnvelopePropagatedData("1", "2", "3");
		assertEquals("1", d.getMessageGuid());
		assertEquals("2", d.getDataflow());
		assertEquals("3", d.getSenderOrReceiver());
	}
}
