
/***************************
 *       ENVIRONMENT       *
 ***************************/

function rand(maxValue) {
   return Math.round((Math.random() * maxValue))
}

function oceanSize() {
   return {x: 300, y: 300, z: 100};
}

function algaeEnergy() {
   return 1.0;
}

function initialForamsCount(x, y, z) {
   var count = rand(10);
   return count;
}

function initialAlgaeAvailability(x, y, z) {
   return 5.0;
}

function algaeGrowth(insolation) {
   return 1.0 * insolation;
}

function insolation(x, y, z) {
   var surfaceInsolation = 1.0;
   var insolation = surfaceInsolation - 0.01 * z;
   return Math.max(0.0, insolation);
}

function currentDirection(x, y, z) {
   return {x: 50, y: 50, z: 0};
}

function boundaryConditions() {
//   return "fixed";
   return "periodic";
//   return "mixed";
}

