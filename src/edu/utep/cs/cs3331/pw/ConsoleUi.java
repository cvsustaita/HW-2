package edu.utep.cs.cs3331.pw;

import java.awt.*;
import java.net.URI;
import java.util.*;

public class ConsoleUi {
    Scanner scan = new Scanner(System.in);
    private Item item;

    public ConsoleUi(Item item) {
        this.item = item;
    }

    public void showWelcome() {
        System.out.println("Welcome to Price Watcher!");
    }

    public void showItem() {
        System.out.println("Here are the item details: \n");
        System.out.print("Item name: " + item.getItemName() + "\nItem URL: " + item.getURL());
        showPrice();
        showChange();
        System.out.println("Added: " + item.getItemDate());
    }

    public int promptUser() {
        System.out.println("\nEnter 1 (to check price), 2 (to view page), 3 (view details once again), or -1 to quit? ");
        return scan.nextInt();
    }

    public void showPrice(){
        System.out.printf("\nItem Price: $%.2f" , item.getItemPrice());
    }

    public void showChange(){
        System.out.printf("\nItem Change: %.2f" , item.getItemChange());
        System.out.println("%");
    }

    public void showURL(){
        System.out.println("Web page displaying in your browser");
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(item.getURL()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}