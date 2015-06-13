// ---- ENVIRONMENT ----

function rand(maxValue) {
   return Math.round((Math.random() * maxValue))
}

// initialization-only kernels

function oceanSize() {
   return {x: 300, y: 300, z: 100};
}

function initialForamsCount(x, y, z) {
   var count = rand(10);
   return count;
}

function initialAlgaeAvailability(x, y, z) {
   return 5.0;
}

function boundaryConditions() {
//   return "fixed";
   return "periodic";
//   return "mixed";
}

// other kernels

/**
 Each kernel gets two parameters:

 time          - time in hours from the beginning of the simulation
 envStates     - array of environment states in the previous step of the cell and its neighbors:
                 envStates[0] - current cell
                 envStates[1] - front neighbor (x + 1, y,     z    )
                 envStates[2] - right neighbor (x,     y + 1, z    )
                 envStates[3] - upper neighbor (x,     y,     z + 1)
                 envStates[4] - back  neighbor (x - 1, y,     z    )
                 envStates[5] - left  neighbor (x,     y - 1, z    )
                 envStates[6] - lower neighbor (x,     y,     z - 1)

                 elements of this array may have null values if current cell is on the edge of the grid (eg. if cell lies on
                 the right edge then it has no right neighbor)


 each element of envStates[] array has following structure:

 envState {                        - mind you that in time = 0, some fields may not be initialized (insolation, algaeEnergy etc.)
   position: { x, y, z },
   insolation,
   algaeEnergy,
   algaeGrowth,
   algaeAvailability,
   currentDirection: { x, y, z }
 }
 */
function algaeEnergy(time, envStates) {
   return 1.0;
}

function algaeGrowth(time, envStates) {
   return 1.0 * insolation(time, envStates);
}

function insolation(time, envStates) {
   var surfaceInsolation = 1.0;
   var insolation = surfaceInsolation - 0.01 * envStates[0].position.z;
   return Math.max(0.0, insolation);
}

function currentDirection(time, envStates) {
   return {x: 50, y: 50, z: 0};
}

