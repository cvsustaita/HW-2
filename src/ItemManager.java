import json.JSONArray;
import json.JSONTokener;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Class responsible of item's data persistence
 * The items are stored in an external storage (JSON)
 * so they are available when the application is closed
 * and launched once again
 *
 * @author Erik Macik
 * @author Cynthis Sustaita
 * */

public class ItemManager {
    private static final String PATHNAME = "src/stored/storedItems.json";

    /** Saving items in current list into json file -> responsible for holding items
     * @param itemList */
    public void saveToJSON(DefaultListModel<Item> itemList) {
        try {
            FileWriter fr = new FileWriter(PATHNAME);

            BufferedWriter br = new BufferedWriter(fr);
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < itemList.size(); i++) {
                jsonArray.put(itemList.get(i).toJson());
            }
            Files.write(Paths.get(PATHNAME), jsonArray.toString().getBytes(), StandardOpenOption.APPEND);
            //System.out.println(jsonArray.toString());

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /** Retrieve items from json file in order to store into items list
     * @param itemList */
    public void fromJSON(DefaultListModel<Item> itemList) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PATHNAME));
            JSONTokener token = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(token);

            for(int i = 0; i < jsonArray.length(); i++){
                Item newItem = Item.fromJson(jsonArray.getJSONObject(i));
                itemList.addElement(newItem);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
