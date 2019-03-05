package edu.utep.cs.cs3331.pw;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Item {
    private String itemName;
    private String URL;
    private double maxPrice;
    private double minPrice;
    private double itemPrice;
    private double itemChange;
    private String itemDate;

    public Item(){}

    public Item(String itemName, String URL, double maxPrice, double minPrice, double itemPrice, double itemChange, String itemDate) {
        this.itemName = itemName;
        this.URL = URL;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.itemPrice = itemPrice;
        this.itemChange = itemChange;
        this.itemDate = itemDate;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setItemChange(double itemChange) {
        this.itemChange = itemChange;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

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