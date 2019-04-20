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

        Item item = jItemList.getSelectedValue();
        if (item == null) return;

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
        showMessage("Updated item price: $" + item.getRecentPrice());
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
    private void openWebsite(ActionEvent event) {
        Item item = jItemList.getSelectedValue();
        if (item == null) return;

        System.out.println("Web page displaying in your browser");
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(item.getURL()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        showMessage("View clicked!");
    }

    private void upClicked(ActionEvent event){
        int newSelected = jItemList.getSelectedIndex() -1;
        if (newSelected < 0)
            jItemList.setSelectedIndex(itemList.getSize() - 1);
        else
            jItemList.setSelectedIndex(newSelected);
    }

    private void downClicked(ActionEvent event){
        jItemList.setSelectedIndex((jItemList.getSelectedIndex()+1) % itemList.getSize());
    }

    private void exitClicked(ActionEvent event){
        System.exit(0);
    }

    private void aboutClicked(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Erik Macik && Cynthia Sustaita", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteClicked(ActionEvent event) {
        int selected = JOptionPane.showConfirmDialog(null, "Do you really want to remove this item?", "Remove", JOptionPane.YES_NO_OPTION);
        System.out.println(selected);

        if (selected == JOptionPane.YES_OPTION)
            itemList.remove(jItemList.getSelectedIndex());
    }

    private void editClicked(ActionEvent event){
        JOptionPane.showMessageDialog(null, new EditDialog());
    }

    private void addItemClicked(ActionEvent event){
        JOptionPane.showMessageDialog(null, new EditDialog());
    }

    private void refreshAllClicked(ActionEvent event){
        for (int i = 0; i < itemList.getSize(); i++){
            jItemList.setSelectedIndex(i);
            refreshButtonClicked(null);
        }
        jItemList.clearSelection();
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

        Item ledMonitor = new Item();
        ledMonitor.setName("LED Monitor");
        ledMonitor.setWebsiteImage("best buy.png");
        ledMonitor.setURL("https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022");
        ledMonitor.setInitialPrice(370.0);
        ledMonitor.setRecentPrice(370.0);
        ledMonitor.setPriceChange(0);
        ledMonitor.setDateAdded(ledMonitor.getDateAdded());

        itemList.addElement(ledMonitor);

        Item airPods = new Item();
        airPods.setName("AirPods");
        airPods.setWebsiteImage("apple.png");
        airPods.setURL("https://www.apple.com/shop/product/MRXJ2/airpods-with-wireless-charging-case");
        airPods.setInitialPrice(199.0);
        airPods.setRecentPrice(199.0);
        airPods.setPriceChange(0);
        airPods.setDateAdded(airPods.getDateAdded());

        itemList.addElement(airPods);

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

        JMenuItem about = new JMenuItem("About", getIconImage("blue info.png"));
        App.add(about);
        App.addSeparator();
        about.addActionListener(this::aboutClicked);

        JMenuItem exit = new JMenuItem("Exit", getIconImage("blue power.png"));
        App.add(exit);

        exit.addActionListener(this::exitClicked);

        /**************************************************************/

        JMenu Item = new JMenu("Item");
        menuBar.add(Item);

        JMenuItem check = new JMenuItem("Check Prices", getIconImage("blue check.png"));
        check.addActionListener(this::refreshAllClicked);
        check.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        Item.add(check);

        JMenuItem addItem = new JMenuItem("Add Item", getIconImage("blue plus.png"));
        addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        Item.add(addItem);

        Item.addSeparator();

        JMenuItem search = new JMenuItem("Search", getIconImage("blue search.png"));
        search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        Item.add(search);

        JMenuItem selectPrevious = new JMenuItem("Select Previous", getIconImage("blue up.png"));
        selectPrevious.addActionListener(this::upClicked);
        selectPrevious.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        Item.add(selectPrevious);

        JMenuItem selectNext = new JMenuItem("Select Next", getIconImage("blue down.png")); //new ImageIcon
        selectNext.addActionListener(this::downClicked);
        selectNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        Item.add(selectNext);

        Item.addSeparator();

        JMenu selected = new JMenu("Selected");
        Item.add(selected);

        JMenuItem price = new JMenuItem("Price", getIconImage("green check.png"));
        price.addActionListener(this::refreshButtonClicked);
        price.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        selected.add(price);

        JMenuItem view = new JMenuItem("View", getIconImage("green internet.png"));
        view.addActionListener(this::openWebsite);
        view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
        selected.add(view);

        JMenuItem edit = new JMenuItem("Edit", getIconImage("green edit.png"));
        edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        selected.add(edit);

        JMenuItem remove = new JMenuItem("Remove", getIconImage("green minus.png"));
        remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        remove.addActionListener(this::deleteClicked);
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
        blueCheck.addActionListener(this::refreshAllClicked);
        buttons.add(blueCheck);

        JButton bluePlus = new JButton(getIconImage("blue plus.png"));
        bluePlus.addActionListener(this::addItemClicked);
        buttons.add(bluePlus);

        buttons.add(new JButton(getIconImage("blue search.png")));

        JButton blueUp = new JButton(getIconImage("blue up.png"));
        blueUp.addActionListener(this::upClicked);
        buttons.add(blueUp);

        JButton blueDown = new JButton(getIconImage("blue down.png"));
        blueDown.addActionListener(this::downClicked);
        buttons.add(blueDown);

        buttons.addSeparator();

        JButton greenCheck = new JButton(getIconImage("green check.png"));
        greenCheck.addActionListener(this::refreshButtonClicked);
        buttons.add(greenCheck);

        JButton greenInternet = new JButton(getIconImage("green internet.png"));
        greenInternet.addActionListener(this::openWebsite);
        buttons.add(greenInternet);

        JButton greenEdit = new JButton(getIconImage("green edit.png"));
        greenEdit.addActionListener(this::editClicked);
        buttons.add(greenEdit);

        JButton greenMinus = new JButton(getIconImage("green minus.png"));
        greenMinus.addActionListener(this::deleteClicked);
        buttons.add(greenMinus);

        buttons.addSeparator();

        JButton blueInfo = new JButton(getIconImage("blue info.png"));
        blueInfo.addActionListener(this::aboutClicked);
        buttons.add(blueInfo);

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