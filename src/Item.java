import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class responsible for representing an item a user wishes to keep track of
 *
 * @author Erik Macik
 * @author Cynthis Sustaita
 * */

public class Item {
    private String name;
    private String URL;
    private String dateAdded;
    private double initialPrice;
    private double recentPrice;
    private double priceChange;
    private Image websiteImage;

    /**Create a new item with no initial values*/
    public Item(){}

    /**Set item name.*/
    public void setName(String name) {
        this.name = name;
    }

    /**Set item URL.
     * @param URL Website url*/
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**Set item date added.
     * @param dateAdded Date item was added*/
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**Set item initial price.
     * @param initialPrice initial price to be set*/
    public void setInitialPrice(double initialPrice){
        this.initialPrice = initialPrice;
    }

    /**Set item price change.
     * @param priceChange Price change to be set.*/
    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    /**Returns item name.*/
    public String getName() {
        return name;
    }

    /**Returns item URL.*/
    public String getURL() {
        return URL;
    }

    /**Returns item date added.*/
    public String getDateAdded() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        dateAdded = currentDate;
        return dateAdded;
    }

    /**Returns item inital price.*/
    public double getInitialPrice(){
        return initialPrice;
    }

    /**Returns item price change.*/
    public double getPriceChange() {
        return priceChange;
    }

    /**Returns item recent price.*/
    public double getRecentPrice() {
        return recentPrice;
    }

    /**Set item recent price.*/
    public void setRecentPrice(double recentPrice) {
        this.recentPrice = recentPrice;
    }

    /**Returns item website image.*/
    public Image getWebsiteImage() {
        return websiteImage;
    }

    /**Set item website image.
     * @param file name of file in /src/image/ to be referenced*/
    public void setWebsiteImage(String file) {
        try {
            java.net.URL url = new URL(getClass().getResource("/image/"), file);
            websiteImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}