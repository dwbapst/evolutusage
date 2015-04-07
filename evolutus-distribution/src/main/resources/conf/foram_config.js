
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
