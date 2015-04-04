/***************************
 *       SIMULATION        *
 ***************************/

function simulationDuration() {
   return 500;
}

/***************************
 *       ENVIRONMENT       *
 ***************************/

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
   return 0.9;
}

function crossingOverOperator() {
   // return "OnePointCrossingOverOperator";
   // return "TwoPointCrossingOverOperator";
   return "UniformCrossingOverOperator";
}

function initialGenome(x, y, z) {
   return {
      translationFactor: {
         value: 0.0,
         //mutationFactor: 0,
         //minValue: 0,
         //maxValue: 0,
      },
      growthFactor: {
         value: {x: 0.0, y: 0.0, z: 0.0}
      },
      rotationAngle: {
         value: 0.0
      },
      deviationAngle: {
         value: 0.0
      },

      ploidy: (Math.random() < 0.5) ? 'haploid' : 'diploid',

      haploidFirstChamberRadius: {
         value: 0.0
      },
      diploidFirstChamberRadius: {
         value: 0.0
      },
      wallThicknessFactor: {
         value: 0.0
      },
      minAdultAge: {
         value: 30
      },
      minAdultVolume: {
         value: 0.0
      },
      haploidJuvenileVolumeFactor: {
         value: 0.0
      },
      diploidJuvenileVolumeFactor: {
         value: 0.0
      },
      maxEnergy: {
         value: 0.0
      },
      minEnergy: {
         value: 0.0
      },
      metabolicEffectiveness: {
         value: 0.0
      },
      minMetabolicEffectiveness: {
         value: 0.0
      }
   }
}
