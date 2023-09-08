package tsp_pfa;

import tsp_pfa.model.ChromosomeModel;
import tsp_pfa.model.CityModel;
import tsp_pfa.model.PopulationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Starter {


    /**
     * initial random population size
     */
    public static final int populationSize = 10; //input

    /**
     * max iteration size
     */
    public static int maxLoop = 100;//input
    public static int chromosomeSize       = 0;
    /**
     * 2d matrix of distance of each nodes
     */
    public static  double [][] distanceArray;

    /**
     * number of cities
     */
    public static int maxShift = 0 ;

    /**
     * max iteration size (temp variable)
     */
    public static int k = 0 ;

    public static void main(String [] args) throws CloneNotSupportedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to TSP PFA");
        System.out.println("we are going to solve TSP problem with PFA algorithm");
        System.out.println("Please enter K > 2 ");





      try {
            k = Integer.parseInt(scanner.nextLine());

            if(k < 2) {
                System.out.println(" ! K > 2 ");
                System.exit(0);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("K is invalid");
            System.exit(0);
        }

        maxLoop = k;

        /**
         * read all cities from tsp file
         */
        // CityModel[] cities =  new DataProvider().readData("a280.tsp");
        CityModel[] cities =  new DataProvider().readData("a280.tsp");


        /**
         * generate distance array
         */
        distanceArray = new double[cities.length][cities.length];



        for(int i = 0 ; i < cities.length; i++) {
            for(int j = 0 ; j < cities.length; j++) {
                if(i == j )  distanceArray[i][j] = 0 ;
                else if(i>j) {
                    distanceArray[i][j] =  distanceArray[j][i];
                }
                else {
                    distanceArray[i][j] = Utils.getDistance(cities[i], cities[j]);
                }
            }
        }


        int lowerBound= 2 ;
        int upperBound= 280 ;
        chromosomeSize = cities.length;
        maxShift = cities.length;

        /**
         * generate initial random population
         */
        PopulationModel populationModel = new PopulationModel();
        populationModel.generateRandomPopulation(populationSize, cities, -100, 100);




        int varNum=cities.length;

        int N=280;

        double A = 1;
        double eps = 0;

        List<Double> lastLeadersFitness = new ArrayList<>();
        List<Double> totalFitness = new ArrayList<>();

        /**
         * sort all members by fitness ascending
         */
        Utils.sortByFitness(populationModel.getChromosomeModelList());

        /**
         * select the first member as Leader
         */
        ChromosomeModel Leader = populationModel.getChromosomeModelList().get(0);
        ChromosomeModel lastLeader = Leader;






        for( int it = 0 ; it < maxLoop; it++) {


             System.out.println(" ======== iteration: " + it + " ======== ");

            /**
             * equation 2.3
             */
            double alpha =  Utils.myRandom(1 , 2);
            /**
             * equation 2.3
             */
            double beta  =  Utils.myRandom(1 , 2);

            /**
             * equation 2.5
             */
            double u1=  Utils.myRandom(-1 , 1);

            /**
             * equation 2.6
             */
            double u2=  Utils.myRandom(-1 , 1);



            /**
             * equation 2.4
             */
            double r3  =  Utils.myRandom(0 , 1);

            ChromosomeModel newLeader = (ChromosomeModel) Leader.clone();

            //shift chromosome
            //Leader.Position+2.*r3.*(OldLeader.Position-Leader.Position)+A;
            /**
             * equation 2.4
             */
            newLeader.shiftLeaderChromosome(r3, lastLeader, Leader, A , upperBound);
            /**
             * update fitness after update
             */
            newLeader.updateFitness();
           // System.out.println("Leader check");

            /**
             * debug purposes
             */
            Utils.findProblemInChromosome(newLeader);

          //  System.out.println("Leader old: " +Leader.getFitness() + " new : " +newLeader.getFitness()  );


            if(newLeader.getFitness() < Leader.getFitness()) {
                lastLeader = Leader;
                Leader = newLeader;
            }


            for(int i = 2 ; i<populationSize-1; i++) {


                ChromosomeModel newSolution = (ChromosomeModel) populationModel.getChromosomeModelList().get(i).clone();
                ChromosomeModel oldSolution = (ChromosomeModel) populationModel.getChromosomeModelList().get(i-1).clone();
                /**
                 * equation 2.3
                 * update member
                 */
                newSolution.shiftMemberChromosome(alpha, beta, varNum, Leader, populationModel.getChromosomeModelList().get(i), populationModel.getChromosomeModelList().get(i-1), eps, lowerBound, upperBound);

                /**
                 * update fitness after update
                 */
                newSolution.updateFitness();

                /**
                 * debug purposes
                 */
                Utils.findProblemInChromosome(newSolution);


             //    System.out.println("old: " +populationModel.getChromosomeModelList().get(i).getFitness() + " new : " +newSolution.getFitness()  );
                //if(newSolution.getFitness() < populationModel.getChromosomeModelList().get(i).getFitness()) {
                    populationModel.getChromosomeModelList().set(i, newSolution)   ;
                //}

                /**
                 * compare updated fitness with leader fitness
                 */
                if(newSolution.getFitness() < Leader.getFitness()) {
                    lastLeader = Leader;
                    Leader =    populationModel.getChromosomeModelList().get(i);
                }

            }

             System.out.println("Leader: " +Leader.toString()  );

            /**
             * equation 2.5
             */

            double Dij = Utils.getDistanceBetweenTwoChromosome(populationModel, Leader);
            eps = (1 - it/(maxLoop)) * u1 * Dij; //* Dij equation 2.5

            /**
             * equation 2.6
             */
            A   = u2 * Math.exp(-2*it/maxLoop);

            /*
            distance
            chromosome

            j = i-1

            j might be i+1

            xi = 1 5 3 4 2 1

            xj = 1 3 5 4 2 1
            di = 0 20 20 0 0 0   = 40

            xk = 1 3 5 2 4 1
            dj = 0 20 20 10 10 0 = 60


            fasele beyne cell ha
             */



        }


        System.out.println("Leader: ");
        System.out.println(Leader.toString());
      //  Utils.renderChart(A, eps , populationSize, maxLoop, lastLeader, cities, lastLeadersFitness, totalFitness);




    }

}
