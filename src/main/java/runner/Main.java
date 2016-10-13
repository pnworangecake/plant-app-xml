package runner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.pnworangecake.aquariumplantxml.Plant;

public class Main {
    public static void main(String[] args) {
        List<Plant> plants = plants();
        final int i = 0;
    }

    private static Reader generateReader(String fileName) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileName));
    }

    private static List<Plant> plants() {
        List<Plant> plants = new ArrayList<>();
        try {
            plants.addAll(Plant.parseFile(generateReader("plantxmlfiles/plants.xml")));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return plants;
    }
}
