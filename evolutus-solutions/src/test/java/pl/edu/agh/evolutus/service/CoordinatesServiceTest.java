package pl.edu.agh.evolutus.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pl.edu.agh.evolutus.utils.VectorL;

public class CoordinatesServiceTest {

	@Test
	public void should_create_all_coordinates_for_given_grid_size() {
		long sizeX = 10;
		long sizeY = 10;
		long sizeZ = 10;

		List<VectorL> coordinatesList = new ArrayList<>();
		CoordinatesService supplier = new CoordinatesService(sizeX, sizeY, sizeZ);

		for (int i = 0; i < sizeX * sizeY * sizeZ; i++) {
			coordinatesList.add(supplier.createCoordinates());
		}

		assertEquals(sizeX * sizeY * sizeZ, coordinatesList.size());

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				for (int z = 0; z < sizeZ; z++) {
					assertTrue(coordinatesList.contains(new VectorL(x, y, z)));
				}
			}
		}
	}

}