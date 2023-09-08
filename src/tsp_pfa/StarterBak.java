package tsp_pfa;

import tsp_pfa.model.ChromosomeModel;
import tsp_pfa.model.CityModel;
import tsp_pfa.model.PopulationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StarterBak {

    //todo gahi oghat meghdar movement zire 0 dar miad, in eshtebah nist?
    public static final int populationSize = 400; //input
    public static final int generationSize = 10000;//input
    public static int chromosomeSize       = 0;
    public static  double [][] distanceArray;
    public static int maxShift = 0 ;

    public static void main(String [] args) throws CloneNotSupportedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to TSP PFA");
        System.out.println("we are going to solve TSP problem with PFA algorithm");
        System.out.println("Please enter K > 0 ");


        List<Double> lastLeadersFitness = new ArrayList<>();
        List<Double> totalFitness = new ArrayList<>();

    /*    try {
            k = Integer.parseInt(scanner.nextLine());

            if(k < 1) {
                System.out.println("  K <= 0 ");
                System.exit(0);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("K is invalid");
            System.exit(0);
        }*/

        // CityModel[] cities =  new DataProvider().readData("a280.tsp");
         CityModel[] cities =  new DataProvider().readData("a280-small.tsp");

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


        chromosomeSize = cities.length;
        maxShift = cities.length;
        PopulationModel populationModel = new PopulationModel();
        //populationModel.generateRandomPopulation(populationSize, cities);




        long startTime = System.nanoTime();
        ChromosomeModel leader = null;
        ChromosomeModel lastLeader = null;
        double A = 0 ;
        double E = 0 ;


        boolean breakFlag = false;

        for(int k=1 ; k<=generationSize; k++) {


            double bestFitness = 0 ;
            if(lastLeader != null)
                bestFitness = lastLeader.getFitness();
            Utils.printProgress(" Generation: " + k + " Fitness: " + bestFitness + " E: " + E);
          //  System.out.println("================================");



            Utils.sortByFitness(populationModel.getChromosomeModelList());

           // for(int m=0 ; m<5; m++) {
           //     System.out.println(populationModel.getChromosomeModelList().get(m).toString());
           // }





          //  System.out.println(populationModel.getChromosomeModelList().get(0).getFitness());

            leader = (ChromosomeModel) populationModel.getChromosomeModelList().get(0).clone();
            int leaderPosition = 0 ;
           // System.out.println("New Leader: " + leader.getFitness() );


            ChromosomeModel xiChromosome = PFA.getRandomChromosome(2, populationModel.getChromosomeModelList(), new ChromosomeModel[]{leader});
            ChromosomeModel xjChromosome = PFA.getRandomChromosome(2, populationModel.getChromosomeModelList(), new ChromosomeModel[]{leader, xiChromosome});


            double alpha=  Utils.myRandom(1 , 2);
            double beta =  Utils.myRandom(1 , 2);


            double u1 = Utils.myRandom(-1 , 1);
            double u2 = Utils.myRandom(-1 , 1);


            double xi = xiChromosome.getFitness();
            double xj = xjChromosome.getFitness();
            double r3 = Utils.myRandom(0, cities.length);



            double Dij = Math.abs(xi - xj);

            A = u2 * Math.pow(Math.E , (-2*k)/generationSize);
            E = (1 - (k / generationSize+1)) * (u1 ) ;


//            System.out.println("  " );
//            System.out.println("E: " + E);
            /**
             * member movement
             */







            for(int i = 0 ; i < populationModel.getChromosomeModelList().size() ; i++) {


                double r1 = Utils.myRandom(1 , 1);
                double r2 = Utils.myRandom(0 , 1);


                int membersMovement = (int) (xi +
                        ( r1 * (xj - xi)) +
                        ( r2 * (leader.getFitness() - xi)) +
                        E );

                if(membersMovement < 0) {
                    System.out.println("membersMovement: " + membersMovement);
                    //    Utils.terminate("member movement is less than 0");

                }

                ChromosomeModel member = populationModel.getChromosomeModelList().get(i);
              //  System.out.println("before shift: " + membersMovement);
              //  System.out.println(member.toString());
                if(member.getIndex() == leader.getIndex()) continue;

                for( int j = 1 ; j <= membersMovement; j++) {
                    member.shiftLeaderChromosome(1 , 2 , maxShift);
                    break;
                }
               // System.out.println("after shift");
               // System.out.println(member.toString());
                member.updateFitness();
                populationModel.getChromosomeModelList().set(i, member);
            }

           // System.out.println("new fitness: " + populationModel.getChromosomeModelList().size());
          //  for(int j = 0 ; j < populationModel.getChromosomeModelList().size() ; j++) {
         //       System.out.print(populationModel.getChromosomeModelList().get(j).getFitness() + " - ");
         //   }

           // System.out.println("");


            double lastLeaderFitness = 0 ;

            Utils.sortByFitness(populationModel.getChromosomeModelList());
          //  System.out.println(populationModel.getChromosomeModelList().get(0).getFitness());

            final ChromosomeModel temporaryLeader = (ChromosomeModel) populationModel.getChromosomeModelList().get(0).clone();
             if(temporaryLeader.getFitness() <= leader.getFitness()) {
            //    System.out.println("hereeeeeeeeeeeeeeeeeeeee");
                lastLeaderFitness = leader.getFitness();
                leader = (ChromosomeModel) temporaryLeader.clone();
                leader.updateFitness();
                populationModel.getChromosomeModelList().set(Utils.getChromosomePositionByIndex(populationModel.getChromosomeModelList(), leader.getIndex()), leader);

            }
          //  System.out.println("getting temp1: " + leader.toString());
            final ChromosomeModel temporaryLeader1 = (ChromosomeModel) leader.clone();

            temporaryLeader1.updateFitness();
            //System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
            //System.out.println(temporaryLeader1.toString());

            /**
             * leader movement
             */


            if(lastLeader != null ) {
               lastLeaderFitness = lastLeader.getFitness();
            //    System.out.println("lastLeaderFitness: " + lastLeaderFitness + " - index: " +lastLeader.getIndex());

            }



           // System.out.println("getting temp1111: " + temporaryLeader1.toString());

            //leader update
            int leaderMovement = (int)  (leader.getFitness() + (
                                             (2 * r3) *
                                            ( lastLeaderFitness - leader.getFitness())
                                        ) + A);

          //  System.out.println("leaderMovement: " + leaderMovement);

            //System.out.println("getting temp222221: " + temporaryLeader1.toString());

          //  if(lastLeader != null)
           //     System.out.println("lastLeader fitness : " + temporaryLeader1.getFitness() );

            for( int i = 1 ; i <= leaderMovement; i++) {
                    leader.shiftLeaderChromosome(1 , 2 , maxShift);
            }



           // System.out.println("getting temp3333331: " + temporaryLeader1.toString());
            leader.updateFitness();


          // System.out.println("getting temp144444444444: " + temporaryLeader1.toString());
          // System.out.println("leader after update ");
          // System.out.println(leader.toString());

            if(leader.getFitness() > temporaryLeader1.getFitness()) {
                leader.setChromosome(temporaryLeader1.getChromosome());

               //System.out.println("revert leader");
               //System.out.println("leader was: " + temporaryLeader1.toString());
               //System.out.println("leader is: " + leader.toString());
                temporaryLeader1.updateFitness();
               // System.out.println(temporaryLeader1.toString());

                populationModel.getChromosomeModelList().set(Utils.getChromosomePositionByIndex(populationModel.getChromosomeModelList(),
                        temporaryLeader1.getIndex()),
                        temporaryLeader1);


              // System.out.println("-----------------------------");
              // System.out.println(
              //         populationModel.getChromosomeModelList().get(Utils.getChromosomePositionByIndex(populationModel.getChromosomeModelList(),
              //         temporaryLeader1.getIndex())).toString());
              // System.out.println(leader.toString() + " position: " + Utils.getChromosomePositionByIndex(populationModel.getChromosomeModelList(), leader.getIndex()));
              // System.out.println("before update: " + temporaryLeader1.toString());



            }




            //age leader avaz shod bayad in etefagh biofte
            if(lastLeader != null) {
             //  System.out.println("compare leaders : " + lastLeader.getFitness()  + " - " +  leader.getFitness());

                if(lastLeader.getFitness() >= leader.getFitness()) {
                    lastLeader = (ChromosomeModel) leader.clone();

                }

            }
            else {
                lastLeader = (ChromosomeModel) leader.clone();
            }


            lastLeadersFitness.add(lastLeader.getFitness());

            double totalFitnessTemporary = 0 ;

            for(ChromosomeModel chromosomeModel : populationModel.getChromosomeModelList()) {
                totalFitnessTemporary+= chromosomeModel.getFitness();
            }
            totalFitness.add(totalFitnessTemporary);

        }


        System.out.println("");
        System.out.println("A: " + A);
        System.out.println("E: " + E);
        System.out.println(lastLeader.toString());

        Utils.renderChart(A, E , populationSize, generationSize, lastLeader, cities, lastLeadersFitness, totalFitness);






    }

}
