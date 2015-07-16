// ---- FORAM ----

function rand() {
   return Math.random();
}

function initialEnergy() {
   return 1.0;
}

/**
 Each kernel gets three parameters:

 envState {
   position: { x, y, z },
   insolation,
   algaeEnergy,
   algaeGrowth,
   algaeAvailability,
   currentDirection: { x, y, z }
 }

 foramState {
   genotype: {
     translationFactor: double[3],       - value of each gene is an array of 3 elements: [effective value, value from A chromosome, value B]
     growthFactor: double[3],
     rotationAngle: double[3],
     deviationAngle: double[3],
     ...
   },
   isBenthic,
   energy,
   age,
   shell: {
     firstChamberRadius,
     lastChamberRadius,
     chambersCount,
     volume
   }
 }

 time - time in hours from the beginning of the simulation
 */

function energyNeededForGrowth(envState, foramState, time) {
   return 40.0;
}

function growthProbability(envState, foramState, time) {
   return 0.8;
}

function chambersLimit(envState, foramState, time) {
   return 20;
}

function energyNeededToReproduce(envState, foramState, time) {
   return 300.0;
}

function reproductionProbability(envState, foramState, time) {
   return 0.8;
}

function gametesProduction(envState, foramState, time) {
   return 1000 * foramState.shell.chambersCount;
}

function gametesSievingCoefficient(envState, foramState, time) {
   return 0.9999;
}

function raduisOfFoodCollecting(envState, foramState, time) {
   return 0.02; // 2 cm
}

function crossingOverOperator(envState, foramState, time) {
   // return "OnePointCrossingOverOperator";
   // return "TwoPointCrossingOverOperator";
   return "UniformCrossingOverOperator";
}

function globalMutationProbability(envState, foramState, time) {
   return 0.0;
}

// -------------------------------------------------------------------------

function shouldDie(envState, foramState, time) {
   return foramState.energy < foramState.genotype.minEnergy[0];
}

function isInHibernationState(envState, foramState, time) {
   return foramState.energy < foramState.genotype.hibernationEnergyLevel[0];
}

function canReproduce(envState, foramState, time) {
   var oldEnough              = foramState.age >= foramState.genotype.minAdultAge[0];
   var energyEnough           = foramState.energy >= energyNeededToReproduce(envState, foramState, time);
   var reproductionProbable   = rand() < reproductionProbability(envState, foramState, time);

   return oldEnough && energyEnough && reproductionProbable && !isInHibernationState(envState, foramState, time);
}

function canCreateChamber(envState, foramState, time) {
   var energyEnough        = foramState.energy >= energyNeededForGrowth(envState, foramState, time);
   var notTooManyChambers  = foramState.shell.chambersCount <= chambersLimit(envState, foramState, time);
   var growthProbable      = rand() < growthProbability(envState, foramState, time);

   return energyEnough && notTooManyChambers && growthProbable && !isInHibernationState(envState, foramState, time);
}

function canMigrate(envState, foramState, time) {
   // benthic forams cannot move when in hibernation
   return !(foramState.isBenthic && isInHibernationState(envState, foramState, time));
}

// -------------------------------------------------------------------------

/**
 * position {  x, y, z }  - position in meters
 */
function initialGenome(position) {
   return [
      {
         name: "translationFactor",
         value: 0.0,
         //mutationProbability: 0,
         //mutationRate: 0,
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
         name: "maxEnergyPerChamber",
         value: 60.0
      },
      {
         name: "maxEnergyCollectingPerChamberPerHour",
         value: 0.5
      },
      {
         name: "energyDemandPerChamberPerHour",
         value: 0.2
      },
      {
         name: "minEnergy",
         value: 0.0
      },
      {
         name: "chamberGrowthCostFactor",
         value: 0.5
      },
      {
         name: "metabolicEffectiveness",
         value: 0.0
      },
      {
         name: "minMetabolicEffectiveness",
         value: 0.0
      },
      {
         name: "hibernationEnergyLevel",
         value: 1
      },
      {
         name: "hibernationEnergyConsumptionPerHour",
         value: 0.01
      }
   ]
}
