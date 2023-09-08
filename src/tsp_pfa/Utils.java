package tsp_pfa;

import jdk.nashorn.internal.ir.IdentNode;
import tsp_pfa.model.ChromosomeModel;
import tsp_pfa.model.CityModel;
import tsp_pfa.model.PopulationModel;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {


    public static double getDistance(CityModel cityModel1, CityModel cityModel2) {
        return  Math.round(Math.sqrt(
                        Math.pow((cityModel2.getX()  - cityModel1.getX()), 2) +
                        Math.pow((cityModel2.getY()  - cityModel1.getY()), 2)
                ));
    }

    public static void printDistanceArray(double [][] array) {

        System.out.print(  " \t\t");
        for(int i = 0 ; i < array.length; i++) {
            System.out.print((i+1) + "\t\t");

        }
        System.out.println("");
         for(int i = 0 ; i < array.length; i++) {
             System.out.print((i+1) + "\t\t");
            for(int j = 0 ; j < array.length; j++) {

                System.out.print(array[i][j] + "\t\t");
            }
            System.out.println("");
        }
    }

    public static int getRandomGene(CityModel[] cities, int [] chromosome) {
        int start = cities[0].getNumber();
        int end = cities[cities.length-1].getNumber();
        Random rand = new Random();
        int range = end - start + 1;
        while(true) {
            final int random = rand.nextInt(range) + 1;
            if(Arrays.stream(chromosome).noneMatch(i -> i == random))
                return random;
        }
    }

    /**
     *
     * @param chromosome
     * @return index of duplicated gene
     */
    public static int findDuplicateGenes(int [] chromosome) {
       for(int i = 0 ; i < chromosome.length; i++) {
           for(int j = 0 ; j < chromosome.length; j++) {
               if(i != j &&  chromosome[i] == chromosome[j]  && chromosome[i] != 1)
                  return  i ;
           }
       }
       return -1;
    }

    /**
     *
     * @param fullChromosome
     * @param newChromosome
     * @return duplicated gene
     */
    public static int findMissingGenes(int [] fullChromosome, int[] newChromosome) {
       for(int i = 0 ; i < fullChromosome.length; i++) {
           boolean exists = false;
           for(int j = 0 ; j < newChromosome.length; j++) {
               if(fullChromosome[i] == newChromosome[j]) exists= true;
           }
           if(!exists && fullChromosome[i] != 1) return fullChromosome[i];
       }
       return -1;
    }

    /**
     * min ham jozve javab mitone bashe
     * max ham jozve javab mitone bashe
     * @param from
     * @param to
     * @return
     */
    public static int generateRandomNumber(int from, int to) {
        return  ThreadLocalRandom.current().nextInt(from, to + 1);
    }
    public static double generateRandomDoubleNumber(int from, int to) {
        return  ThreadLocalRandom.current().nextDouble(from, to + 1);
    }

    public static int rouletteWheel(List<ChromosomeModel> chromosomeModels) {

        int summaryOfFitness = 0 ;
        for(ChromosomeModel  chromosomeModel : chromosomeModels) {
            summaryOfFitness+= chromosomeModel.getFitness(false);
        }

        double [] normalize = new double[chromosomeModels.size()];
        double [] cp = new double[chromosomeModels.size()];
        for(int i = 0 ; i < normalize.length; i++) {
            ChromosomeModel  chromosomeModel = chromosomeModels.get(i);
            normalize[i] = (chromosomeModel.getFitness(false) / summaryOfFitness);
        }
        double tmpCp = 0 ;
        for(int i = 0 ; i < normalize.length; i++) {
            tmpCp += normalize[i];
            cp[i]  = tmpCp;
        }
        double rand = Math.random();
        for(int i = 0  ; i < cp.length; i++) {
            if(rand < cp[i]) return i;

        }
        return 0;
    }
    public static void sortByFitness(List<ChromosomeModel> chromosomeModels) {
         chromosomeModels.sort((o1, o2) -> (int) (o1.getFitness(true) - o2.getFitness(true)));
    }

    public static void printArray(int[] array) {
        System.out.println(Arrays.toString(array));
    }

    public static double myRandom(double min, double max) {
        Random r = new Random();
        double rand = (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
        while(rand == 2)
            rand = (r.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
        return rand;
    }

    public static int getChromosomePositionByIndex(List<ChromosomeModel> list , int index) {
        int len = list.size();
        for(int i = 0 ; i < len ; i++) {
            if(list.get(i).getIndex() == index) return i;
        }
        return 0;
    }


    /**
     * print inline message
     * @param generation
     */
    public static void printProgress(String generation) {
        String string = '\r' +  generation;
        System.out.print(string);
    }



    public static void renderChart(double A , double E, int populationSize, int generationSize, ChromosomeModel finalChromosome, CityModel[] cities , List<Double> lastLeadersFitness , List<Double> totalFitness) {

        int len = finalChromosome.getChromosome().length;

        StringBuilder data = new StringBuilder("const data = [ \n");
        for( int i = 0 ; i < len ; i++) {
            for(int j = 0 ; j < cities.length; j++) {
                if(finalChromosome.getChromosome()[i] == cities[j].getNumber()) {
                    data.append("\t\n{x:").append(cities[j].getX()).append(",y:").append(cities[j].getY()).append(", details:'").append(cities[j].getNumber()).append("'}");
                    if(i < len-1)
                        data.append(",\n");

                }
            }
        }
        data.append("\n]");

        data.append(";\n");
        data.append("const A = ").append(A);
        data.append(";\n");
        data.append("const E = ").append(E);
        data.append(";\n");
        data.append("const populationSize = ").append(populationSize);
        data.append(";\n");
        data.append("const generationSize = ").append(generationSize);
        data.append(";\n");
        data.append("const fitness = ").append(finalChromosome.getFitness(true));


        data.append("\n");
        data.append("\n");

        data.append("const fitnessList = [");

        int fitnessLen = lastLeadersFitness.size();
        for( int i = 0 ; i < fitnessLen ; i++) {
            Double fitness = lastLeadersFitness.get(i);
            data.append(fitness);
            if(i < fitnessLen-1) {
                data.append(",\n");
            }
        }

        data.append("]");


        data.append("\n");
        data.append("\n");

        data.append("const totalFitness = [");

        int totalFitnessLen = totalFitness.size();
        for( int i = 0 ; i < totalFitnessLen ; i++) {
            int fitness = totalFitness.get(i).intValue();

            data.append(fitness);
            if(i < fitnessLen-1) {
                data.append(",\n");
            }
        }

        data.append("]");

        //System.out.println(data);
        writeDataToJavascriptFile(data.toString());
       displayOutput();
    }

    public static void displayOutput() {
        File htmlFile = new File("Charts/index.html");
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeDataToJavascriptFile(String data) {
        try {
            FileWriter myWriter = new FileWriter("Charts/data.js");
            myWriter.write(data);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
           // System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    public static void terminate(String message) {
        System.out.println(message);
        System.exit(0);
    }

    public static void findProblemInChromosome(ChromosomeModel chromosome) {

      //  System.out.println("================= findProblemInChromosome ========================");
        //System.out.println(chromosome.toString());
        Boolean problem = false;
        for(int i = 1 ; i <= 280; i++) {
            int repeat = 0 ;

            for(int j : chromosome.getChromosome()) {
                if(j == i) repeat++;
            }
            if(repeat != 1 && (i != 1)) {
               // System.out.println("we have problem for " + i + " repeat: " + repeat);
               // System.exit(0);
            }

        }



    }



    /**
     * calculate distance between leader and first neighbor
     * equation 2.5
     * @param populationModel
     * @param leader
     * @return
     */
    public static double getDistanceBetweenTwoChromosome(PopulationModel populationModel , ChromosomeModel leader){
        int index = leader.getIndex();
         if(index == 0)
            index = populationModel.getChromosomeModelList().size();



        ChromosomeModel neighbor = populationModel.getChromosomeModelList().get(index -1);

        return Math.abs(leader.getFitness() - neighbor.getFitness());
    }
}
