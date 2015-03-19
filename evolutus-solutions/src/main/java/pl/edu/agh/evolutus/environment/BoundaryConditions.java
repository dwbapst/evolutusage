package pl.edu.agh.evolutus.environment;

import pl.edu.agh.evolutus.utils.VectorL;

public enum BoundaryConditions {
	FIXED {
		@Override
		public VectorL keepVectorWithinBounds(VectorL vector, VectorL bounds) {
			if (vector.x >= bounds.x) {
				vector = new VectorL(bounds.x - 1, vector.y, vector.z);
			}
			if (vector.y >= bounds.y) {
				vector = new VectorL(vector.x, bounds.y - 1, vector.z);
			}
			if (vector.z >= bounds.z) {
				vector = new VectorL(vector.x, vector.y, bounds.z - 1);
			}

			if (vector.x < 0) {
				vector = new VectorL(0, vector.y, vector.z);
			}
			if (vector.y < 0) {
				vector = new VectorL(vector.x, 0, vector.z);
			}
			if (vector.z < 0) {
				vector = new VectorL(vector.x, vector.y, 0);
			}
			return vector;
		}
	},

	PERIODIC {
		@Override
		public VectorL keepVectorWithinBounds(VectorL vector, VectorL bounds) {
			if (vector.x >= bounds.x) {
				vector = new VectorL(0, vector.y, vector.z);
			}
			if (vector.y >= bounds.y) {
				vector = new VectorL(vector.x, 0, vector.z);
			}
			if (vector.z >= bounds.z) {
				vector = new VectorL(vector.x, vector.y, 0);
			}

			if (vector.x < 0) {
				vector = new VectorL(bounds.x - 1, vector.y, vector.z);
			}
			if (vector.y < 0) {
				vector = new VectorL(vector.x, bounds.y - 1, vector.z);
			}
			if (vector.z < 0) {
				vector = new VectorL(vector.x, vector.y, bounds.z - 1);
			}
			return vector;
		}
	},

	MIXED {
		@Override
		public VectorL keepVectorWithinBounds(VectorL vector, VectorL bounds) {
			if (vector.x >= bounds.x) {
				vector = new VectorL(bounds.x - 1, vector.y, vector.z);
			}
			if (vector.y >= bounds.y) {
				vector = new VectorL(vector.x, 0, vector.z);
			}
			if (vector.z >= bounds.z) {
				vector = new VectorL(vector.x, vector.y, 0);
			}

			if (vector.x < 0) {
				vector = new VectorL(0, vector.y, vector.z);
			}
			if (vector.y < 0) {
				vector = new VectorL(vector.x, bounds.y - 1, vector.z);
			}
			if (vector.z < 0) {
				vector = new VectorL(vector.x, vector.y, bounds.z - 1);
			}
			return vector;
		}
	};

	public abstract VectorL keepVectorWithinBounds(VectorL vector, VectorL bounds);

	public static BoundaryConditions fromString(String string) {
		return BoundaryConditions.valueOf(string.toUpperCase());
	}
}
