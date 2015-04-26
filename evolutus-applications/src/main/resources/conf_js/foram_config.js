
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

function globalMutationProbability() {
   return 0.0;
}

function initialGenome(x, y, z) {
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
