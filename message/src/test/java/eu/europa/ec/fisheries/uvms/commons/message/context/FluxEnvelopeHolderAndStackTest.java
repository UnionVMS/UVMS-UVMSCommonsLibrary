package eu.europa.ec.fisheries.uvms.commons.message.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link FluxEnvelopeHolderAndStack}.
 */
public class FluxEnvelopeHolderAndStackTest {

	private FluxEnvelopeHolderAndStack sut;

	@Before
	public void before() {
		sut = new FluxEnvelopeHolderAndStack();
	}

	@Test
	public void testGetFromEmpty() {
		assertNull(sut.get());
	}

	@Test
	public void testPushNull() {
		sut.push(null);
		assertNull(sut.get());
	}

	@Test
	public void testPushGetAndPop() {
		FluxEnvelopePropagatedData d1 = new FluxEnvelopePropagatedData("1", "2", "3");
		sut.push(d1);
		assertEquals(d1, sut.get());
		assertEquals(d1, sut.get());
		FluxEnvelopePropagatedData d2 = new FluxEnvelopePropagatedData("1", "2", "X");
		sut.push(d2);
		assertEquals(d2, sut.get());
		assertEquals(d2, sut.get());
		assertEquals(d2, sut.pop());
		assertEquals(d1, sut.get());
		assertEquals(d1, sut.pop());
		assertNull(sut.get());
	}

	@Test
	public void testWithContext() {
		try {
			FluxEnvelopePropagatedData d1 = new FluxEnvelopePropagatedData("1", "2", "3");
			sut.withContext(d1, ctx -> {
				assertEquals(d1, ctx);
				assertEquals(d1, sut.get());
				throw new RuntimeException("this is for testing");
			});
		} catch (RuntimeException re) {
			assertEquals("this is for testing", re.getMessage());
		}
		assertNull(sut.get());
	}
}
