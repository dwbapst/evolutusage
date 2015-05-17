package pl.edu.agh.evolutus.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import pl.edu.agh.evolutus.environment.BoundaryConditions;

public class Position extends VectorL {

	public Position(VectorL vectorL) {
		this(vectorL.x, vectorL.y, vectorL.z);
	}

	public Position(Number x, Number y, Number z) {
		super(x, y, z);
	}

	public List<Position> getTheSameLevelNeighborhood() {
		return Lists.newArrayList(
				this,
				new Position(x + 1, y, z),
				new Position(x, y + 1, z),
				new Position(x - 1, y, z),
				new Position(x, y - 1, z)
		);
	}

	public List<Position> getTheSameLevelNeighborhood(VectorL oceanSize, BoundaryConditions boundaryConditions) {
		return getTheSameLevelNeighborhood()
				.stream()
				.map(position -> boundaryConditions.keepVectorWithinBounds(position, oceanSize))
				.map(Position::new)
				.collect(Collectors.toList());
	}

}
