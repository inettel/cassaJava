package ru.inettel.cassa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ksork on 29.06.17.
 */
public class ConfigHelper {

    public static List<String> getStreets(){
//        ObservableList<String> streets = FXCollections.observableArrayList("");
        List<String> streets = new ArrayList<>();
        try {
            Scanner scanner  = new Scanner(new File("streets.cfg"), "cp1251");
            while (scanner.hasNext())
                streets.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            System.out.println("Файл street.cfg не найден");
        }
        return streets;
    }
}
