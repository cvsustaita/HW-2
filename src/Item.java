import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Item {
    private String name;
    private String URL;
    private String dateAdded;
    private double initialPrice;
    private double recentPrice;
    private double priceChange;
    private Image websiteImage;

    public Item(){}

    public Item(String name, String URL, String dateAdded, double initialPrice) {
        this.name = name;
        this.URL = URL;
        this.dateAdded = dateAdded;
        this.initialPrice = initialPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setInitialPrice(double initialPrice){
        this.initialPrice = initialPrice;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public String getDateAdded() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        dateAdded = currentDate;
        return dateAdded;
    }

    public double getInitialPrice(){
        return initialPrice;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public double getRecentPrice() {
        return recentPrice;
    }

    public void setRecentPrice(double recentPrice) {
        this.recentPrice = recentPrice;
    }

    public Image getWebsiteImage() {
        return websiteImage;
    }

    public void setWebsiteImage(String file) {
        try {
            java.net.URL url = new URL(getClass().getResource("/image/"), file);
            websiteImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}