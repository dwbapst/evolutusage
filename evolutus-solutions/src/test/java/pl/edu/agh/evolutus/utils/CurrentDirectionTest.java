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
		assertTrue(currentDirection.getVectorComponents().containsKey(new Vector(1, 0, 0)));

		currentDirection = new CurrentDirection(1, 1, 0);
		assertEquals(2, currentDirection.getVectorComponents().size());
		assertTrue(currentDirection.getVectorComponents().containsKey(new Vector(1, 0, 0)));
		assertTrue(currentDirection.getVectorComponents().containsKey(new Vector(0, 1, 0)));

		currentDirection = new CurrentDirection(-4, 17, -9);
		assertEquals(3, currentDirection.getVectorComponents().size());
		assertTrue(currentDirection.getVectorComponents().containsKey(new Vector(-1, 0, 0)));
		assertTrue(currentDirection.getVectorComponents().containsKey(new Vector(0, 1, 0)));
		assertTrue(currentDirection.getVectorComponents().containsKey(new Vector(0, 0, -1)));
		assertEquals(0.133333, currentDirection.getVectorComponents().get(new Vector(-1, 0, 0)), 0.000001);
		assertEquals(0.566667, currentDirection.getVectorComponents().get(new Vector(0, 1, 0)), 0.000001);
		assertEquals(0.300000, currentDirection.getVectorComponents().get(new Vector(0, 0, -1)), 0.000001);
	}

	@Test
	public void testGetTargetCoordinates() throws Exception {
		CurrentDirection currentDirection = new CurrentDirection(-4, 17, 0);
		Vector poolSize = new Vector(5, 5, 5);

		Map<Vector, Double> targetCoordinates = currentDirection.getTargetCoordinateProbabilities(new Vector(3, 3, 3), poolSize);
		assertEquals(2, targetCoordinates.size());
		assertTrue(targetCoordinates.containsKey(new Vector(2, 3, 3)));
		assertTrue(targetCoordinates.containsKey(new Vector(3, 4, 3)));
		assertEquals(0.190476, targetCoordinates.get(new Vector(2, 3, 3)), 0.000001);
		assertEquals(0.809524, targetCoordinates.get(new Vector(3, 4, 3)), 0.000001);

		targetCoordinates = currentDirection.getTargetCoordinateProbabilities(new Vector(0, 4, 3), poolSize);
		assertEquals(2, targetCoordinates.size());
		assertTrue(targetCoordinates.containsKey(new Vector(4, 4, 3)));
		assertTrue(targetCoordinates.containsKey(new Vector(0, 0, 3)));
		assertEquals(0.190476, targetCoordinates.get(new Vector(4, 4, 3)), 0.000001);
		assertEquals(0.809524, targetCoordinates.get(new Vector(0, 0, 3)), 0.000001);
	}
}