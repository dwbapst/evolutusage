/***************************
 *      ENVIRONMENT        *
 ***************************/

function oceanSize() {
   return {x: 10, y: 10, z: 2}
}

function algaeEnergy() {
   return 1.0;
}

function initialForamsCount(x, y, z) {
   var count = 100 - 10 * z;
   return count > 0 ? count : 0;
}

function initialAlgaeAvailability(x, y, z) {
   return 100.0;
}

function algaeGrowth(insolation) {
   return 10.0 * insolation;
}

function insolation(x, y, z) {
   var surfaceInsolation = 1.0;
   var insolation = surfaceInsolation - 0.5 * z;
   return Math.max(0.0, insolation);
}

function currentDirection(x, y, z) {
   return {x: 3, y: 2, z: 0}
}

function currentStrength(x, y, z) {
   return 1.0;
}
