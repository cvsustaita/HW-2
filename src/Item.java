import json.JSONObject;

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
    private String url;
    private String dateAdded;
    private double initialPrice;
    private double recentPrice;
    private double priceChange;
    private Image websiteImage;

    /**
     *
     * @return
     */
    public JSONObject toJson(){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("URL", url);
        map.put("dateAdded", dateAdded);
        map.put("initialPrice", initialPrice);
        map.put("recentPrice", recentPrice);
        map.put("priceChange", priceChange);

        return new JSONObject(map);
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    public static Item fromJson(JSONObject jsonObject){
        Item item = new Item();
        item.setName(jsonObject.getString("name"));
        item.setUrl(jsonObject.getString("URL"));
        item.setDateAdded(jsonObject.getString("dateAdded"));
        item.setInitialPrice(jsonObject.getDouble("initialPrice"));
        item.setRecentPrice(jsonObject.getDouble("recentPrice"));
        item.setPriceChange(jsonObject.getDouble("priceChange"));

        if (item.getUrl().contains("bestbuy.com")) item.setWebsiteImage("best buy.png");
        else if (item.getUrl().contains("apple.com")) item.setWebsiteImage("apple.png");
        else if (item.getUrl().contains("etsy.com")) item.setWebsiteImage("etsy.png");
        else item.setWebsiteImage("missing image.png");

        return item;
    }

    /**Create a new item with no initial values*/
    public Item(){}

    /**Set item name.*/
    public void setName(String name) {
        this.name = name;
    }

    /**Set item url.
     * @param url Website url*/
    public void setUrl(String url) {
        this.url = url;
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

    /**Returns item url.*/
    public String getUrl() {
        return url;
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