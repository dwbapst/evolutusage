/***************************
 *       SIMULATION        *
 ***************************/

function simulationDuration() {
   return 1000;
}

/***************************
 *       ENVIRONMENT       *
 ***************************/

function oceanSize() {
   return {x: 10, y: 10, z: 2};
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
   return {x: 3, y: 2, z: 0};
}

function currentStrength(x, y, z) {
   return 1.0;
}

/***************************
 *          FORAM          *
 ***************************/

function initialEnergy() {
   return 5.0;
}

function energyCapacity(chambersCount) {
   return 1.1 * chambersCount;
}

function energyDemand(chambersCount) {
   return 0.2 * chambersCount;
}

function chamberGrowthEnergyCost(chambersCount) {
   return 0.5 * energyCapacity(chambersCount);
}

function energyNeededForGrowth() {
   return 10.0;
}

function growthProbability() {
   return 0.8;
}

function chambersLimit() {
   return 20;
}

function newBornLimit() {
   return 9;
}

function energyNeededToReproduce() {
   return 10.0;
}

function reproductionProbability() {
   return 0.8;
}
