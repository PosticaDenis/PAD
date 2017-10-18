package utils;

import java.io.File;

/**
 * Created by c-denipost on 18-Oct-17.
 **/
public class Validator {

    File schemaFile = new File("/Users/XYZ/schema.json");
    //File jsonFile = new File("/Users/XYZ/data.json");

    public boolean isValid(String path) {

        try {
            if (ValidationUtils.isJsonValid(schemaFile, new File(path))){

                System.out.println("Valid!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
