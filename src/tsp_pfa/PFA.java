package tsp_pfa;

import tsp_pfa.model.ChromosomeModel;

import java.util.List;

public class PFA {


    public static int findMovement(double alpha, double beta) {
        return 0;
    }

    public static ChromosomeModel getRandomChromosome(int min, List<ChromosomeModel> chromosomeModels, ChromosomeModel [] exceptions) {
        double randomFitness = -1;
        int random = Utils.generateRandomNumber(min , chromosomeModels.size()-1);
        ChromosomeModel chromosomeModel = chromosomeModels.get(random);


        while(true) {
            boolean repetitive = false;
            for(int i = 0 ; i < exceptions.length; i++) {
                if(exceptions[i].getFitness(false) == chromosomeModel.getFitness(false)) {
                    random = Utils.generateRandomNumber(0 , chromosomeModels.size()-1);
                    chromosomeModel = chromosomeModels.get(random);
                    repetitive = true;
                }
            }

            if(!repetitive) break;

        }
        return chromosomeModel;
    }

}
