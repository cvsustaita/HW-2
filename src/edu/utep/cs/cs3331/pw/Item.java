package edu.utep.cs.cs3331.pw;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Item {
    private String itemName = "LED Monitor";
    private String URL = "https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022";
    private double maxPrice = 369.99;
    private double minPrice = 61.67;
    private double itemPrice = getRandomPrice();
    private double itemChange = change();
    private String itemDate = "08/25/2018";

    public String getItemName() {
        return itemName;
    }

    public String getURL() {
        return URL;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public double getItemChange() {
        return itemChange;
    }

    public String getItemDate() {
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        itemDate = currentDate;
        return itemDate;
    }

    public double getRandomPrice(){
        Random ran = new Random();
        return ran.doubles(minPrice, (maxPrice + 1)).findFirst().getAsDouble();
    }

    public double change(){
        double increase = maxPrice - itemPrice;
        increase = (increase / maxPrice) * 100;
        return increase;
    }
}