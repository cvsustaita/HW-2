package base;

import edu.utep.cs.cs3331.pw.Item;
import edu.utep.cs.cs3331.pw.PriceFinder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
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
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
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
        menuBar.add(App);

        JMenuItem about = new JMenuItem("About");
        App.add(about);
        App.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        App.add(exit);

        exit.addActionListener(this::exitClicked);

        /**************************************************************/

        JMenu Item = new JMenu("Item");
        menuBar.add(Item);

        JMenuItem check = new JMenuItem("Check Prices");
        check.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        Item.add(check);
        check.addActionListener(this::refreshButtonClicked);

        JMenuItem addItem = new JMenuItem("Add Item");
        addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        Item.add(addItem);
        Item.addSeparator();

        JMenuItem search = new JMenuItem("Search");
        search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        Item.add(search);

        JMenuItem selectFirst = new JMenuItem("Select First");
        selectFirst.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        Item.add(selectFirst);

        JMenuItem selectLast = new JMenuItem("Select Last"); //new ImageIcon
        selectLast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        Item.add(selectLast);
        Item.addSeparator();

        JMenu selected = new JMenu("Selected");
        Item.add(selected);

        JMenuItem price = new JMenuItem("Price");
        price.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        selected.add(price);

        JMenuItem view = new JMenuItem("View");
        view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
        selected.add(view);

        JMenuItem edit = new JMenuItem("Edit");
        edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        selected.add(edit);

        JMenuItem remove = new JMenuItem("Remove");
        remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        selected.add(remove);
        selected.addSeparator();

        JMenuItem copyName = new JMenuItem("Copy name");
        selected.add(copyName);

        JMenuItem copyURL = new JMenuItem("Copy URL");
        selected.add(copyURL);

        JMenuItem copyItem = new JMenuItem("Copy item");
        selected.add(copyItem);

        /**************************************************************/

        JMenu Sort = new JMenu("Sort");
        menuBar.add(Sort);

        JRadioButton addedOldest = new JRadioButton("Added oldest");
        Sort.add(addedOldest);

        JRadioButton addedNewest = new JRadioButton("Added newest");
        Sort.add(addedNewest);
        Sort.addSeparator();

        JRadioButton nameAscending = new JRadioButton("Name ascending");
        Sort.add(nameAscending);

        JRadioButton nameDescending = new JRadioButton("Name descending");
        Sort.add(nameDescending);
        Sort.addSeparator();

        JRadioButton priceChange = new JRadioButton("Price change(%)");
        Sort.add(priceChange);

        JRadioButton priceLow = new JRadioButton("Price low($)");
        Sort.add(priceLow);

        JRadioButton priceHigh = new JRadioButton("Price high($)");
        Sort.add(priceHigh);

        /*
        birdButton.setActionCommand(birdString);
        birdButton.setSelected(true);
         birdButton.addActionListener(this);
        */

        /*JButton refreshButton = new JButton("Refresh");
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(this::refreshButtonClicked);
        panel.add(refreshButton);
        */

        panel.add(menuBar);

        return panel;
    }

    private JPanel makeButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JToolBar buttons = new JToolBar();

        URL url = null;
        try {
            url = new URL(getClass().getResource("/image/"), "websiteIcon.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        JButton jb = new JButton();
        jb.setIcon(icon);

        buttons.add(jb);
        buttons.add(new JButton());
        panel.add(buttons);
        return panel;
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
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