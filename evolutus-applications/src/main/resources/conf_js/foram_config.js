// ---- FORAM ----

function rand() {
   return Math.random();
}

function reproductionType() {
   return "sexual";
   //return "sexual_asexual";
}

function foramActiveMotion() {
   return false;
}

function initialEnergy() {
   return 10.0;
}

/**
 Each kernel gets three parameters:

 envState {
   oxygen,
   temperature,
   salinity,
   algaeAvailability,
   insolation,
   ph,
   position: { x, y, z },
   algaeEnergy,
   algaeGrowth,
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
   foramActiveMotion,
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
   return 1000.0;
}

function growthProbability(envState, foramState, time) {
   return 0.8;
}

function chambersLimit(envState, foramState, time) {
   return 10;
}

function energyNeededToReproduce(envState, foramState, time) {
   return 2000;
}

function reproductionProbability(envState, foramState, time) {
   return 0.9;
}

function gametesProduction(envState, foramState, time) {
   return 0.4 * foramState.shell.volumeShell;
}

function gametesSievingCoefficient(envState, foramState, time) {
   return 0.999;
}

function raduisOfFoodCollecting(envState, foramState, time) {
   return 0.5; // 20 cm
}

function crossingOverOperator(envState, foramState, time) {
   // return "OnePointCrossingOverOperator";
   // return "TwoPointCrossingOverOperator";
   return "UniformCrossingOverOperator";
}

function globalMutationProbability(envState, foramState, time) {
   return 0.0;
}

//planktic foraminifera can move actively only in z direction
//bentic foraminfera can move in any direction
function foramActiveSpeed(envState, foramState, time) {
   return {x: 0.001, y: 0.001, z: 0.001};
}

//for planktic foraminifera motion energy is always 0
//bentic foraminifera always need energy to move
function activeMotionEnergyCostPerChamberPerMeter(envState, foramState, time) {
   return {up: 0.05, down: 0.1, horizontally: 0.07};
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
   var energyEnough           = foramState.energy > energyNeededToReproduce(envState, foramState, time);
   var reproductionProbable   = rand() > reproductionProbability(envState, foramState, time);

   return oldEnough && energyEnough && reproductionProbable && !isInHibernationState(envState, foramState, time);
}

function canCreateChamber(envState, foramState, time) {
   var energyEnough        = foramState.energy > energyNeededForGrowth(envState, foramState, time);
   var notTooManyChambers  = foramState.shell.chambersCount <= chambersLimit(envState, foramState, time);
   var growthProbable      = rand() > growthProbability(envState, foramState, time);

   return energyEnough && notTooManyChambers && growthProbable && !isInHibernationState(envState, foramState, time);
}

function canMigrate(envState, foramState, time) {
   // benthic forams cannot move when in hibernation
   return !(foramState.foramActiveMotion && isInHibernationState(envState, foramState, time));
}

// -------------------------------------------------------------------------

/**
 * position {  x, y, z }  - position in meters
 */
function initialGenome(position) {
   return [
      {
         name: "translationFactor",
         value: 0.15,
         mutationProbability: 0.7,
         mutationRate: 0.1,
         minValue: -1.0,
         maxValue: 1.0
      },
      {
         name: "growthFactor",
         value: 1.1,
         mutationProbability: 0.7,
         mutationRate: 0.1,
         minValue: 1.0,
         maxValue: 2.0
      },
      {
         name: "rotationAngle",
         value: 0.0,
         mutationProbability: 0.7,
         mutationRate: 0.5,
         minValue: -180.0,
         maxValue: 180.0
      },
      {
         name: "deviationAngle",
         value: 0.0,
         mutationProbability: 0.7,
         mutationRate: 0.5,
         minValue: -180.0,
         maxValue: 180.0
      },

      {
         name: "haploidFirstChamberRadius",
         value: 40.0,
         mutationProbability: 0.7,
         mutationRate: 0.05,
         minValue: 30.0,
         maxValue: 100.0
      },
      {
         name: "diploidFirstChamberRadius",
         value: 10.0,
         mutationProbability: 0.7,
         mutationRate: 0.05,
         minValue: 5.0,
         maxValue: 30.0
      },
      {
         name: "wallThicknessFactor",
         value: 0.1,
         mutationProbability: 0.7,
         mutationRate: 0.01,
         minValue: 0.01,
         maxValue: 0.5
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
         name: "foodCollectingRate",
         value: 0.01,
         mutationProbability: 0.2,
         mutationRate: 0.001,
         minValue: 0.001,
         maxValue: 0.1
      },
      {
         name: "energyDemandPerChamberPerHour",
         value: 0.02,
         mutationProbability: 0.2,
         mutationRate: 0.1,
         minValue: 0.001,
         maxValue: 0.9
      },
      {
         name: "minEnergy",
         value: 0.0
      },
      {
         name: "chamberGrowthCostFactor",
         value: 0.01,
         mutationProbability: 0.2,
         mutationRate: 0.1,
         minValue: 0.001,
         maxValue: 0.9
      },
      {
         name: "metabolicEffectiveness",
         value: 0.8,
         mutationProbability: 0.2,
         mutationRate: 0.001,
         minValue: 0.1,
         maxValue: 0.9
      },
      {
         name: "minMetabolicEffectiveness",
         value: 0.01
      },
      {
         name: "hibernationEnergyLevel",
         value: 5
      },
      {
         name: "hibernationEnergyConsumptionPerHour",
         value: 0.01
      }
   ]
}
