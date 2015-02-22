package pl.edu.agh.evolutus.utils;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class CurrentDirectionTest {

	@Test
	public void testGetVectorComponents() throws Exception {
		CurrentDirection currentDirection = new CurrentDirection(0, 0, 0);
		assertEquals(0, currentDirection.getVectorComponents().size());

		currentDirection = new CurrentDirection(1, 0, 0);
		assertEquals(1, currentDirection.getVectorComponents().size());
		assertTrue(currentDirection.getVectorComponents().containsKey(new VectorL(1, 0, 0)));

		currentDirection = new CurrentDirection(1, 1, 0);
		assertEquals(2, currentDirection.getVectorComponents().size());
		assertTrue(currentDirection.getVectorComponents().containsKey(new VectorL(1, 0, 0)));
		assertTrue(currentDirection.getVectorComponents().containsKey(new VectorL(0, 1, 0)));

		currentDirection = new CurrentDirection(-4, 17, -9);
		assertEquals(3, currentDirection.getVectorComponents().size());
		assertTrue(currentDirection.getVectorComponents().containsKey(new VectorL(-1, 0, 0)));
		assertTrue(currentDirection.getVectorComponents().containsKey(new VectorL(0, 1, 0)));
		assertTrue(currentDirection.getVectorComponents().containsKey(new VectorL(0, 0, -1)));
		assertEquals(0.133333, currentDirection.getVectorComponents().get(new VectorL(-1, 0, 0)), 0.000001);
		assertEquals(0.566667, currentDirection.getVectorComponents().get(new VectorL(0, 1, 0)), 0.000001);
		assertEquals(0.300000, currentDirection.getVectorComponents().get(new VectorL(0, 0, -1)), 0.000001);
	}

	@Test
	public void testGetTargetCoordinates() throws Exception {
		CurrentDirection currentDirection = new CurrentDirection(-4, 17, 0);
		VectorL poolSize = new VectorL(5, 5, 5);

		Map<VectorL, Double> targetCoordinates = currentDirection.getTargetCoordinateProbabilities(new VectorL(3, 3, 3), poolSize);
		assertEquals(2, targetCoordinates.size());
		assertTrue(targetCoordinates.containsKey(new VectorL(2, 3, 3)));
		assertTrue(targetCoordinates.containsKey(new VectorL(3, 4, 3)));
		assertEquals(0.190476, targetCoordinates.get(new VectorL(2, 3, 3)), 0.000001);
		assertEquals(0.809524, targetCoordinates.get(new VectorL(3, 4, 3)), 0.000001);

		targetCoordinates = currentDirection.getTargetCoordinateProbabilities(new VectorL(0, 4, 3), poolSize);
		assertEquals(2, targetCoordinates.size());
		assertTrue(targetCoordinates.containsKey(new VectorL(4, 4, 3)));
		assertTrue(targetCoordinates.containsKey(new VectorL(0, 0, 3)));
		assertEquals(0.190476, targetCoordinates.get(new VectorL(4, 4, 3)), 0.000001);
		assertEquals(0.809524, targetCoordinates.get(new VectorL(0, 0, 3)), 0.000001);
	}
}