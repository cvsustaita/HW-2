package base;

import edu.utep.cs.cs3331.pw.Item;
import edu.utep.cs.cs3331.pw.PriceFinder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.sound.sampled.*;

/**
 * A dialog for tracking the price of an item.
 *
 * @author Yoonsik Cheon
 */
@SuppressWarnings("serial")
public class Main extends JFrame {
    private Item item = new Item();
    private PriceFinder priceFinder = new PriceFinder();

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_SIZE = new Dimension(400, 300);

    /** Special panel to display the watched item. */
    private ItemView itemView;

    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel(" ");

    /** Create a new dialog. */
    public Main() {
        this(DEFAULT_SIZE);
    }

    /** Create a new dialog of the given screen dimension. */
    public Main(Dimension dim) {
        super("Price Watcher");
        setSize(dim);
        configureUI();
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        //setResizable(false);
        showMessage("Welcome!");
    }

    /** Callback to be invoked when the refresh button is clicked.
     * Find the current price of the watched item and display it
     * along with a percentage price change. */
    private void refreshButtonClicked(ActionEvent event) {

        double oldPrice = item.getInitialPrice();
        double updatedPrice = priceFinder.getRandomPrice();
        double increase = updatedPrice - oldPrice;
        double percentIncrease = increase / oldPrice * 100;

        DecimalFormat df = new DecimalFormat("#.00");
        String priceFormated = df.format(updatedPrice);
        updatedPrice = Double.parseDouble(priceFormated);
        priceFormated = df.format(percentIncrease);
        percentIncrease = Double.parseDouble(priceFormated);
        item.setRecentPrice(updatedPrice);
        item.setPriceChange(percentIncrease);

        if (item.getPriceChange() < 0)
            playSound();

        super.repaint();
        showMessage("Updated item price: " + item.getRecentPrice());
    }

    private void playSound(){
        try{
            URL url = Main.class.getResource("chaching.au");
            Clip clip = AudioSystem.getClip();
            // getAudioInputStream() also accepts a File or InputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /** Callback to be invoked when the view-page icon is clicked.
     * Launch a (default) web browser by supplying the URL of
     * the item. */
    private void viewPageClicked() {
        System.out.println("Web page displaying in your browser");
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(item.getURL()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        showMessage("View clicked!");
    }

    private void exitClicked(ActionEvent event){
        System.exit(0);
    }

    /** Configure UI. */
    private void configureUI() {
        setLayout(new BorderLayout());
        JPanel control = makeControlPanel();
        control.setBorder(BorderFactory.createEmptyBorder(10,16,0,16));
        add(control, BorderLayout.NORTH);
        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10,16,0,16),
                BorderFactory.createLineBorder(Color.GRAY)));
        board.setLayout(new GridLayout(1,1));

        item.setName("LED Monitor");
        item.setURL("https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022");
        item.setInitialPrice(370.0);
        item.setRecentPrice(370.0);
        item.setPriceChange(0);
        item.setDateAdded(item.getDateAdded());
        itemView = new ItemView(item);

        itemView.setClickListener(this::viewPageClicked);
        board.add(itemView);
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        add(msgBar, BorderLayout.SOUTH);
    }

    /** Create a control panel consisting of a refresh button. */
    private JPanel makeControlPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JMenuBar menuBar = new JMenuBar();

        JMenu App = new JMenu("App");
        App.setMnemonic(KeyEvent.VK_M);
        menuBar.add(App);

        JMenuItem menuItem2 = new JMenuItem("Exit", KeyEvent.VK_C);
        //menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        App.add(menuItem2);
        panel.add(menuBar);
        menuItem2.addActionListener(this::exitClicked);

        JMenu Item = new JMenu("Item");
        //Item.setMnemonic(KeyEvent.VK_M);
        //menu.getAccessibleContext().setAccessibleDescription("Main menu");
        menuBar.add(Item);

        JMenuItem menuItem = new JMenuItem("Check Prices", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        //menuItem.getAccessibleContext().setAccessibleDescription("Check the price");
        Item.add(menuItem);
        panel.add(menuBar);

        JMenu Sort = new JMenu("Sort");
        menuBar.add(Sort);

        /*JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(this::refreshButtonClicked);
        panel.add(refreshButton);
        */

        return panel;
    }

    /** Show briefly the given string in the message bar. */
    private void showMessage(String msg) {
        msgBar.setText(msg);
        new Thread(() -> {
            try {
                Thread.sleep(3 * 1000); // 3 seconds
            } catch (InterruptedException e) {
            }
            if (msg.equals(msgBar.getText())) {
                SwingUtilities.invokeLater(() -> msgBar.setText(" "));
            }
        }).start();
    }

    public static void main(String[] args) {
        new Main();
    }
}