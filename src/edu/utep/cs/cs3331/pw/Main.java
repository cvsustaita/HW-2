package edu.utep.cs.cs3331.pw;

public class Main {
    public void run() {
        Item item = new Item();
        ConsoleUi ui = new ConsoleUi(item);
        ui.showWelcome();
        int request;
        ui.showItem();

        do {
            request = ui.promptUser();
            switch (request) {
                case 1:
                    ui.showPrice();
                    System.out.println();
                    break;
                case 2:
                    ui.showURL();
                    break;
                case 3:
                    ui.showItem();
                    break;
                case -1:
                    System.out.println("Thanks for using Price Watcher");
                    break;
                default:
                    System.out.println("This is not an option");
                    break;
            }
        } while (request != -1);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}