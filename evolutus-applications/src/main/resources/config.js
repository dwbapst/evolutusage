/***************************
 *         SYSTEM          *
 ***************************/

function databaseParameters() {
   return {
      //host: "localhost",
      //port: 27017,
      //inMemory: false
      host: "localhost",
      port: 65432,
      inMemory: true
   };
}

/***************************
 *       SIMULATION        *
 ***************************/

function unitLengthInMeters() {
   return 30;
}

function stepDurationInHours() {
   return 12;
}

function simulationDuration() {
   return 1000 * 24;
}

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

function energyNeededToReproduce() {
   return 10.0;
}

function reproductionProbability() {
   return 0.8;
}

function gametesProduction(chambersCount) {
   return 1000 * chambersCount;
}

function gametesSievingCoefficient() {
   return 0.99;
}

function crossingOverOperator() {
   // return "OnePointCrossingOverOperator";
   // return "TwoPointCrossingOverOperator";
   return "UniformCrossingOverOperator";
}

function initialGenome(x, y, z) {
   return [
      {
         name: "translationFactor",
         value: 0.0,
         //mutationFactor: 0,
         //minValue: 0,
         //maxValue: 0,
      },
      {
         name: "growthFactor",
         value: 0.0
      },
      {
         name: "rotationAngle",
         value: 0.0
      },
      {
         name: "deviationAngle",
         value: 0.0
      },

      {
         name: "haploidFirstChamberRadius",
         value: 0.0
      },
      {
         name: "diploidFirstChamberRadius",
         value: 0.0
      },
      {
         name: "wallThicknessFactor",
         value: 0.0
      },
      {
         name: "minAdultAge",
         value: 30
      },
      {
         name: "minAdultVolume",
         value: 0.0
      },
      {
         name: "haploidJuvenileVolumeFactor",
         value: 0.0
      },
      {
         name: "diploidJuvenileVolumeFactor",
         value: 0.0
      },
      {
         name: "maxEnergy",
         value: 0.0
      },
      {
         name: "minEnergy",
         value: 0.0
      },
      {
         name: "metabolicEffectiveness",
         value: 0.0
      },
      {
         name: "minMetabolicEffectiveness",
         value: 0.0
      }
   ]
}
