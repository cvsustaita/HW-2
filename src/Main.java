import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
    private DefaultListModel<Item> itemList = new DefaultListModel<>();
    private JList<Item> jItemList = new JList<>(itemList);
    private PriceFinder priceFinder = new PriceFinder();

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_SIZE = new Dimension(400, 300);

    /** Special panel to display the watched item. */
    private ItemViewRenderer itemRenderer = new ItemViewRenderer();

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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        showMessage("Welcome!");
        pack();
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
        String priceFormatted = df.format(updatedPrice);
        updatedPrice = Double.parseDouble(priceFormatted);
        priceFormatted = df.format(percentIncrease);
        percentIncrease = Double.parseDouble(priceFormatted);
        item.setRecentPrice(updatedPrice);
        item.setPriceChange(percentIncrease);

        if (item.getPriceChange() < 0)
            playSound();

        super.repaint();
        showMessage("Updated item price: " + item.getRecentPrice());
    }

    private void playSound(){
        try{
            URL url = Main.class.getResource("/sound/chaching.au");
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

    private void aboutClicked(ActionEvent event) {
        JPanel p = new JPanel();
        JFrame frame = new JFrame();
        JDialog dialog = new JDialog(frame, "About");
        JLabel version = new JLabel("Price Watcher Version 1.3", JLabel.CENTER);
        JLabel authors = new JLabel("Erik Macik && Cynthia Sustaita", JLabel.CENTER);
        JButton ok = new JButton("OK");
        frame.add(p);
        p.add(version);
        p.add(authors);
        p.add(ok);
        dialog.add(p);
        dialog.setLocationRelativeTo(null);
        dialog.setSize(275, 145);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /** Configure UI. */
    private void configureUI() {
        setLayout(new BorderLayout());
        JPanel control = new JPanel();
        control.setLayout(new BorderLayout());
        control.add(makeControlPanel(), BorderLayout.NORTH);
        control.add(makeButtonPanel(), BorderLayout.CENTER);
        add(control, BorderLayout.NORTH);

        JPanel board = new JPanel();
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10,16,0,16),
                BorderFactory.createLineBorder(Color.GRAY)));
        board.setLayout(new GridLayout(1,1));

        Item item = new Item();
        item.setName("LED Monitor");
        item.setURL("https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022");
        item.setInitialPrice(370.0);
        item.setRecentPrice(370.0);
        item.setPriceChange(0);
        item.setDateAdded(item.getDateAdded());

        itemList.addElement(item);
        itemList.addElement(item);
        itemList.addElement(item);
        itemList.addElement(item);

        //itemRenderer.setClickListener(this::viewPageClicked);
        jItemList.setCellRenderer(itemRenderer);

        board.add(new JScrollPane(jItemList));
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
        about.addActionListener(this::aboutClicked);

        JMenuItem exit = new JMenuItem("Exit");
        App.add(exit);

        exit.addActionListener(this::exitClicked);

        /**************************************************************/

        JMenu Item = new JMenu("Item");
        menuBar.add(Item);

        JMenuItem check = new JMenuItem("Check Prices", getIconImage("blue check.png"));
        check.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        Item.add(check);
        check.addActionListener(this::refreshButtonClicked);

        JMenuItem addItem = new JMenuItem("Add Item", getIconImage("blue plus.png"));
        addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        Item.add(addItem);
        Item.addSeparator();

        JMenuItem search = new JMenuItem("Search", getIconImage("blue search.png"));
        search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        Item.add(search);

        JMenuItem selectFirst = new JMenuItem("Select First", getIconImage("blue up.png"));
        selectFirst.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        Item.add(selectFirst);

        JMenuItem selectLast = new JMenuItem("Select Last", getIconImage("blue down.png")); //new ImageIcon
        selectLast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        Item.add(selectLast);
        Item.addSeparator();

        JMenu selected = new JMenu("Selected");
        Item.add(selected);

        JMenuItem price = new JMenuItem("Price", getIconImage("green check.png"));
        price.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        selected.add(price);

        JMenuItem view = new JMenuItem("View", getIconImage("green internet.png"));
        view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
        selected.add(view);

        JMenuItem edit = new JMenuItem("Edit", getIconImage("green edit.png"));
        edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        selected.add(edit);

        JMenuItem remove = new JMenuItem("Remove", getIconImage("green minus.png"));
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

        ButtonGroup sortGroup = new ButtonGroup();

        JCheckBoxMenuItem addedOldest = new JCheckBoxMenuItem("Added oldest");
        sortGroup.add(addedOldest);
        Sort.add(addedOldest);

        JCheckBoxMenuItem addedNewest = new JCheckBoxMenuItem("Added newest");
        sortGroup.add(addedNewest);
        Sort.add(addedNewest);

        Sort.addSeparator();

        JCheckBoxMenuItem nameAscending = new JCheckBoxMenuItem("Name ascending");
        sortGroup.add(nameAscending);
        Sort.add(nameAscending);

        JCheckBoxMenuItem nameDescending = new JCheckBoxMenuItem("Name descending");
        sortGroup.add(nameDescending);
        Sort.add(nameDescending);

        Sort.addSeparator();

        JCheckBoxMenuItem priceChange = new JCheckBoxMenuItem("Price change(%)");
        sortGroup.add(priceChange);
        Sort.add(priceChange);

        JCheckBoxMenuItem priceLow = new JCheckBoxMenuItem("Price low($)");
        sortGroup.add(priceLow);
        Sort.add(priceLow);

        JCheckBoxMenuItem priceHigh = new JCheckBoxMenuItem("Price high($)");
        sortGroup.add(priceHigh);
        Sort.add(priceHigh);

        panel.add(menuBar);
        return panel;
    }

    private JPanel makeButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JToolBar buttons = new JToolBar();

        JButton blueCheck = new JButton(getIconImage("blue check.png"));
        buttons.add(blueCheck);
        blueCheck.addActionListener(this::refreshButtonClicked);
        buttons.add(new JButton(getIconImage("blue plus.png")));
        buttons.add(new JButton(getIconImage("blue search.png")));
        buttons.add(new JButton(getIconImage("blue up.png")));
        buttons.add(new JButton(getIconImage("blue down.png")));

        buttons.addSeparator();

        buttons.add(new JButton(getIconImage("green check.png")));
        buttons.add(new JButton(getIconImage("green internet.png")));
        buttons.add(new JButton(getIconImage("green edit.png")));
        buttons.add(new JButton(getIconImage("green minus.png")));

        buttons.addSeparator();

        buttons.add(new JButton(getIconImage("blue info.png")));

        panel.add(buttons);
        return panel;
    }

    private ImageIcon getIconImage(String fileName){
        try {
            URL url = new URL(getClass().getResource("/image/"), fileName);
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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