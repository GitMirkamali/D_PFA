package tsp_pfa.model;


import tsp_pfa.Starter;
import tsp_pfa.Utils;

import java.util.ArrayList;
import java.util.List;

public class PopulationModel {


    List<ChromosomeModel> chromosomeModelList = new ArrayList<>();

    public void generateRandomPopulation(int populationSize,  CityModel[] cities, int  lowerBound,  int upperBound) {
        for( int i = 0 ; i < populationSize; i++) {
            ChromosomeModel chromosomeModel = generateRandomChromosome(cities, i,  lowerBound, upperBound);
            chromosomeModelList.add(chromosomeModel);
        }
    }
    public void generateSamplePopulation(int populationSize,  CityModel[] cities) {


      /*      int [] chromosome = new int[cities.length+1] ;
            chromosome[0] = 1 ;
            chromosome[1] = 3 ;
            chromosome[2] = 4 ;
            chromosome[3] = 5 ;
            chromosome[4] = 2 ;
            chromosome[5] = 1 ;
            chromosomeModelList.add(new ChromosomeModel(chromosome, 0));






        int [] chromosome3 = new int[cities.length+1] ;
        chromosome3[0] = 1 ;
        chromosome3[1] = 3 ;
        chromosome3[2] = 5 ;
        chromosome3[3] = 4 ;
        chromosome3[4] = 2 ;
        chromosome3[5] = 1 ;
        chromosomeModelList.add(new ChromosomeModel(chromosome3, 3));


        int [] chromosome1 = new int[cities.length+1] ;
            chromosome1[0] = 1 ;
            chromosome1[1] = 5 ;
            chromosome1[2] = 4 ;
            chromosome1[3] = 2 ;
            chromosome1[4] = 3 ;
            chromosome1[5] = 1 ;
            chromosomeModelList.add(new ChromosomeModel(chromosome1, 1));



        int [] chromosome2 = new int[cities.length+1] ;
        chromosome2[0] = 1 ;
        chromosome2[1] = 5 ;
        chromosome2[2] = 3 ;
        chromosome2[3] = 4 ;
        chromosome2[4] = 2 ;
        chromosome2[5] = 1 ;
        chromosomeModelList.add(new ChromosomeModel(chromosome2, 2));
        int [] chromosome4 = new int[cities.length+1] ;

            chromosome4[0] = 1 ;
            chromosome4[1] = 4 ;
            chromosome4[2] = 2 ;
            chromosome4[3] = 3 ;
            chromosome4[4] = 5 ;
            chromosome4[5] = 1 ;
            chromosomeModelList.add(new ChromosomeModel(chromosome4, 4));

*/
    }

    public void setChromosomeModelList(List<ChromosomeModel> chromosomeModelList) {
        this.chromosomeModelList.clear();;
        this.chromosomeModelList.addAll(chromosomeModelList);
    }

    public List<ChromosomeModel> getChromosomeModelList() {
        return chromosomeModelList;
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        for (ChromosomeModel chromosomeModel : chromosomeModelList) {
            data.append(chromosomeModel.toString() + "\n");
        }
        return data.toString();
    }

    public ChromosomeModel generateRandomChromosome(CityModel[] cities, int index, int  lowerBound,  int upperBound) {
        int [] chromosome = new int[cities.length+1] ;
        for(int i = 0; i < chromosome.length; i++) {
            if(i==0) {
                chromosome[i] = cities[0].getNumber();
            }
            else if(i == Starter.chromosomeSize) chromosome[i] = chromosome[0];
            else chromosome[i] = Utils.getRandomGene(cities, chromosome);
        }
        double position = Utils.generateRandomNumber(lowerBound, upperBound);
        return new ChromosomeModel(chromosome, index, position);
    }




}
