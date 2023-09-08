package tsp_pfa;

import tsp_pfa.model.CityModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public class DataProvider {

    private static final String DATA_PATH = "C:\\Users\\0and1\\Desktop\\TSP_PFA\\TSP_PFA\\data\\";

    public  CityModel[] readData(String fileName) {

        CityModel[] cities = new CityModel[0];

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    DATA_PATH + fileName));
            String line = reader.readLine();
            boolean readNodes = false;
            int counter = 0 ;
            while (line != null) {
                if(line.contains("DIMENSION")) {
                    cities = new CityModel[Integer.parseInt(line.split(": ")[1])];
                }
                else if(line.contains("EOF")) {
                    break;
                }
                else if(line.contains("NODE_COORD_SECTION")) {
                    readNodes = true;
                }
                else if(readNodes) {
                    String [] data = line.trim().split(" ");
                    double[] numbersIntArray = Stream.of(line.trim().split(" ")).mapToDouble(Double::parseDouble).toArray();
                    cities[counter++] = new CityModel(numbersIntArray);
                }
                // read next line
                line = reader.readLine().trim().replaceAll("\\s{2,}", " ");

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cities;
    }
}
