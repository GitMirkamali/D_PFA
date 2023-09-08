package tsp_pfa.model;

public class CityModel {

    private int number;
    private double x;
    private double y;

    public CityModel(double number, double x, double y) {
        this.number = (int)number;
        this.x = x;
        this.y = y;
    }
    public CityModel(double[] data) {
        this.number =(int) data[0];
        this.x  = data[1];
        this.y  = data[2];
    }

    public int getNumber() {
        return number;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return  number + ", " + x + ", " + y +  "\n";
    }
}
