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

 time                                     - time in hours from the beginning of the simulation

 currentEnvState {                        - environment state in the previous step of simulation, mind you that in time = 0,
   position: { x, y, z },                   some fields may not be initialized (insolation, algaeEnergy etc.)
   insolation,
   algaeEnergy,
   algaeGrowth,
   algaeAvailability,
   currentDirection: { x, y, z }
 }
 */
function algaeEnergy(time, currentEnvState) {
   return 1.0;
}

function algaeGrowth(time, currentEnvState) {
   return 1.0 * insolation(time, currentEnvState);
}

function insolation(time, currentEnvState) {
   var surfaceInsolation = 1.0;
   var insolation = surfaceInsolation - 0.01 * currentEnvState.position.z;
   return Math.max(0.0, insolation);
}

function currentDirection(time, currentEnvState) {
   return {x: 50, y: 50, z: 0};
}

