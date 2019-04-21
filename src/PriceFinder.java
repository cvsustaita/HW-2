import java.text.DecimalFormat;
import java.util.Random;

/**
 * Class responsible for determining the most recent price of an item.
 *
 * @author Erik Macik
 * @author Cynthia Sustaita
 * */

public class PriceFinder {

    /**@return A random price for an item for demonstration purposes. This random price is in no way representative of a real world price for an item.*/
    public double getRandomPrice(){
        Random ran = new Random();
        double maxPrice = 500.00;
        double minPrice = 50.00;
        double randomPrice = ran.doubles(minPrice, (maxPrice + 1)).findFirst().getAsDouble();

        DecimalFormat df = new DecimalFormat("#.00");
        String priceFormated = df.format(randomPrice);
        randomPrice = Double.parseDouble(priceFormated);

        return randomPrice;
    }
}