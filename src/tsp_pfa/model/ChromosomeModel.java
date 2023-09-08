package tsp_pfa.model;

import tsp_pfa.Starter;
import tsp_pfa.Utils;

import java.util.Arrays;

public class ChromosomeModel implements Cloneable {
    private int [] chromosome ;
    private double fitness = 0;
    private int index = 0 ;
    private double random  ;

    public ChromosomeModel(int [] chromosome, int index, double random) {
        this.chromosome = chromosome;
        fitness = getFitness(true);
        this.index = index;
        this.random = random;
    }

    public ChromosomeModel() {
    }

    public static ChromosomeModel copy( ChromosomeModel from ) {
        ChromosomeModel copy = new ChromosomeModel();
        copy.chromosome = from.chromosome;
        copy.fitness = from.fitness;
        copy.index = from.index;
        return copy;
    }


    public int[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(int[] chromosome) {
        this.chromosome = chromosome;
    }


    public void shiftLeaderChromosome(int step , int min, int max) {
        for( int i = 1 ; i < chromosome.length-1; i++) {
            if(chromosome[i] == max)
                chromosome[i] = 2;
            else chromosome[i]+=step;
        }
    }

    /**
     * equation 2.4
     * update leader
     */
    public void shiftLeaderChromosome(double r3, ChromosomeModel oldLeader, ChromosomeModel Leader, double A, int upperBound) {

        for( int i = 1 ; i < chromosome.length-1; i++) {
            int movement = (int) (Leader.getChromosome()[i] +
                    (2 * r3 * (oldLeader.getChromosome()[i] - Leader.getChromosome()[i]))
                    + A);
            for(int j = 0 ; j < movement; j++) {
                if(chromosome[i] == upperBound)
                    chromosome[i] = 2;
                else chromosome[i]++;
            }
        }
    }
    /**
     * equation 2.3
     * update member
     */
    public void shiftMemberChromosome(double alpha, double beta, int varNum, ChromosomeModel leader, ChromosomeModel current, ChromosomeModel previous,
                                      double eps, int lowerBound, int upperBound) {
        double  R1=alpha * Utils.myRandom(0 , 1);
        double  R2=beta *  Utils.myRandom(0 , 1);

        for( int i = 1 ; i < chromosome.length-1; i++) {
            double  d = Math.sqrt( (  Math.pow(current.getChromosome()[i], 2)  +
                    Math.pow(previous.getChromosome()[i], 2)));
            int movement = (int)
                    (current.getChromosome()[i]  + R1 *
                            (previous.getChromosome()[i] - current.getChromosome()[i] )  + R2
                            * (leader.getChromosome()[i] - current.getChromosome()[i])
                            + eps * d/2 );
            for(int j = 0 ; j < movement; j++) {
                if(chromosome[i] == upperBound)
                    chromosome[i] = 2;
                else chromosome[i]++;
            }

        }
    }


    @Override
    public String toString() {
        return "{" + index + ": fitness: " + fitness + " " +
                "chromosome=" + Arrays.toString(chromosome) +
                '}';
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
    }



    public double getFitness(Boolean override){
        if(!override) return fitness;
        double fitness = 0 ;


        for(int i = 0 ; i < chromosome.length-1; i++) {
            
            try {
                if(i < chromosome.length-1)
                        fitness+= Starter.distanceArray[chromosome[i]-1][chromosome[i+1]-1];
                else if(i < chromosome.length-2)
                    fitness+= Starter.distanceArray[chromosome[i]-1][chromosome[chromosome.length-1]-1];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(Arrays.toString(chromosome));
                System.out.println(e.toString());
                System.out.println("error calculating fitness + linr 64: " + i  );
                Utils.terminate("");
            }
        }
        this.fitness = fitness;
        return this.fitness;
    }
    public double getFitness(){
        return this.fitness;
    }
    public void updateFitness(){


        double fitness = 0 ;
        for(int i = 0 ; i < chromosome.length-1; i++) {
            try {
                if(i < chromosome.length-1)
                        fitness+= Starter.distanceArray[chromosome[i]-1][chromosome[i+1]-1];
                else if(i < chromosome.length-2)
                    fitness+= Starter.distanceArray[chromosome[i]-1][chromosome[chromosome.length-1]-1];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(Arrays.toString(chromosome));
                System.out.println(e.toString());
                System.out.println("error calculating fitness: " + i  );
                System.out.println(Arrays.toString(e.getStackTrace()));

                Utils.terminate("");
            }
        }
        this.fitness = fitness;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Assign the shallow copy to
        // new reference variable t
        ChromosomeModel copy = (ChromosomeModel)super.clone();

        // Creating a deep copy for c
        copy.chromosome = this.chromosome.clone();
        copy.fitness = this.fitness;
        copy.index = this.index;

        // Create a new object for the field c
        // and assign it to shallow copy obtained,
        // to make it a deep copy
        return copy;
    }
}
