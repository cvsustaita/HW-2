package edu.utep.cs.cs3331.pw;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Item {
    private String itemName;
    private String URL;
    private String itemDate;
    private double itemPrice;
    private double itemChange;

    public Item(){}

    public Item(String itemName, String URL, String itemDate, double itemPrice) {
        this.itemName = itemName;
        this.URL = URL;
        this.itemDate = itemDate;
        this.itemPrice = itemPrice;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
    }

    public void setItemPrice(double itemPrice){
        this.itemPrice = itemPrice;
    }

    public void setItemChange(double itemChange) {
        this.itemChange = itemChange;
    }

    public String getItemName() {
        return itemName;
    }

    public String getURL() {
        return URL;
    }

    public String getItemDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        itemDate = currentDate;
        return itemDate;
    }

    public double getItemPrice(){
        return itemPrice;
    }

    public double getItemChange() {
        return itemChange;
    }
}