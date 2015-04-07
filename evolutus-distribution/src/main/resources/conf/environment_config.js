
function oceanSize() {
   return {x: 10, y: 10, z: 1};
}

function algaeEnergy() {
   return 1.0;
}

function initialForamsCount(x, y, z) {
   if (x == 1 && y == 1) {
      return 100;
   } else if (x == 5 && y == 5) {
      return 50;
   }
   return 0;
}

function initialAlgaeAvailability(x, y, z) {
   return 10.0;
}

function algaeGrowth(insolation) {
   return 1.0 * insolation;
}

function insolation(x, y, z) {
   var surfaceInsolation = 1.0;
   var insolation = surfaceInsolation - 0.5 * z;
   return Math.max(0.0, insolation);
}

function currentDirection(x, y, z) {
   if (x < 2 && y >= 2) {
      return {x: 0, y: -2, z: 0};
   } else if (y < 2 && x <= oceanSize().x - 3) {
      return {x: 2, y: 0, z: 0};
   } else if (x > oceanSize().x - 3 && y <= oceanSize().y - 3) {
      return {x: 0, y: 2, z: 0};
   } else if (y > oceanSize().y - 3) {
      return {x: -2, y: 0, z: 0};
   }
   return {x: 0, y: 0, z: 0};
}

function boundaryConditions() {
//   return "fixed";
   return "periodic";
//   return "mixed";
}
