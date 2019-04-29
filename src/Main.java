import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.sound.sampled.*;

@SuppressWarnings("serial")
public class Main extends JFrame{

    private DefaultListModel<Item> itemList = new DefaultListModel<>();
    private JList<Item> jItemList = new JList<>(itemList);
    private WebPriceFinder webPriceFinder = new WebPriceFinder();
    private JProgressBar progressBar = new JProgressBar();

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
    void refreshButtonClicked(ActionEvent event) {
        Item item = jItemList.getSelectedValue();
        if (item == null) return;

        double updatedPrice;

        if (item.getURL().contains("bestbuy") || item.getURL().contains("apple")){
            updatedPrice = webPriceFinder.findPrice(item.getURL(), this);
        } else {
            JOptionPane.showMessageDialog(this, "Store not supported.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (updatedPrice <= 0) {
            JOptionPane.showMessageDialog(this, item.getName()+" price could not be updated.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double oldPrice = item.getInitialPrice();
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

    /**Play sound from resources that means it is a good time to buy an item.*/
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
    void openWebsite(ActionEvent event) {
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

    /**Select previous item*/
    private void upClicked(ActionEvent event){
        int newSelected = jItemList.getSelectedIndex() -1;
        if (newSelected < 0)
            jItemList.setSelectedIndex(itemList.getSize() - 1);
        else
            jItemList.setSelectedIndex(newSelected);
    }

    /**Select next item*/
    private void downClicked(ActionEvent event){
        jItemList.setSelectedIndex((jItemList.getSelectedIndex()+1) % itemList.getSize());
    }

    /**Close price watcher*/
    private void exitClicked(ActionEvent event){
        System.exit(0);
    }

    /**Display "About Price Watcher" dialog*/
    private void aboutClicked(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Erik Macik && Cynthia Sustaita", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /**Delete an item from price watcher*/
    void deleteClicked(ActionEvent event) {
        int selected = JOptionPane.showConfirmDialog(null, "Do you really want to remove this item?", "Remove", JOptionPane.YES_NO_OPTION);
        System.out.println(selected);

        if (selected == JOptionPane.YES_OPTION)
            itemList.remove(jItemList.getSelectedIndex());
    }

    /**Edit item in price watcher*/
    void editClicked(ActionEvent event){
        int index = jItemList.getSelectedIndex();
        if(index != -1){
            Item tempItem = itemList.get(jItemList.getSelectedIndex());

            JTextField itemName = new JTextField(tempItem.getName());
            JTextField itemURL = new JTextField(tempItem.getURL());
            String tempPrice = String.valueOf(tempItem.getRecentPrice());
            JTextField itemPrice = new JTextField(tempPrice);
            Object[] message = {
                    "Name:", itemName,
                    "URL:", itemURL,
                    "Price:", itemPrice
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
                if (option == 0) {
                    try {
                        itemList.get(index).setName(itemName.getText());
                        itemList.get(index).setURL(itemURL.getText());
                        itemList.get(index).setRecentPrice(Double.parseDouble(itemPrice.getText()));

                        double oldPrice = itemList.get(index).getInitialPrice();
                        double updatedPrice = itemList.get(index).getRecentPrice();
                        double increase = updatedPrice - oldPrice;
                        double percentIncrease = increase / oldPrice * 100;

                        DecimalFormat df = new DecimalFormat("#.00");
                        String priceFormatted = df.format(updatedPrice);
                        priceFormatted = df.format(percentIncrease);
                        percentIncrease = Double.parseDouble(priceFormatted);
                        itemList.get(index).setPriceChange(percentIncrease);
                        repaint();
                        showMessage("Edited correctly");
                    } catch (Exception e) {
                        showMessage("Please enter information.");
                    }
                }
            } else {
                showMessage("Not Selecting an Item");
            }
        }

    /**Add item to price watcher*/
    private void addItemClicked(ActionEvent event){
        JTextField name = new JTextField();
        JTextField url = new JTextField();
        JTextField price = new JTextField();
        Object[] message = {
                "Name:", name,
                "URL:", url,
                "Price:", price
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try{
                Item newItem = new Item();
                newItem.setName(name.getText());
                newItem.setURL(url.getText());
                newItem.setInitialPrice(Double.parseDouble(price.getText()));
                newItem.setRecentPrice(Double.parseDouble(price.getText()));
                newItem.setPriceChange(0);
                newItem.setDateAdded(newItem.getDateAdded());

                if (newItem.getURL().contains("bestbuy")) newItem.setWebsiteImage("best buy.png");
                else if (newItem.getURL().contains("apple")) newItem.setWebsiteImage("apple.png");
                else newItem.setWebsiteImage("missing image.png");

                itemList.addElement(newItem);
                showMessage("Item Successfully Added");
            } catch (Exception e) {
                showMessage("Please enter information.");
            }
        }
    }

    /**Refresh all items tracked by price watcher*/
    private void refreshAllClicked(ActionEvent event){
        progressBar.setVisible(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(itemList.getSize());
        progressBar.setValue(0);
        for (int i = 0; i < itemList.getSize(); i++){
            System.out.println("------"+progressBar.getValue());
            jItemList.setSelectedIndex(i);
            refreshButtonClicked(null);
            progressBar.setValue(progressBar.getValue()+1);
        }
        System.out.println("------"+progressBar.getValue());
        jItemList.clearSelection();
        showMessage("All item prices updated");
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
        itemList.addElement(ledMonitor);
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

        jItemList.addMouseListener(new ListMouseListener(this));

        board.add(new JScrollPane(jItemList));
        add(board, BorderLayout.CENTER);
        msgBar.setBorder(BorderFactory.createEmptyBorder(10,16,10,0));
        add(msgBar, BorderLayout.SOUTH);
    }


    /** @return Custom control panel */
    private JPanel makeControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JMenuBar menuBar = new JMenuBar();

        JMenu App = new JMenu("App");
        menuBar.add(App);

        JMenuItem about = new JMenuItem("About", getIconImage("blue info.png"));
        about.addActionListener(this::aboutClicked);
        App.add(about);

        App.addSeparator();

        JMenuItem exit = new JMenuItem("Exit", getIconImage("blue power.png"));
        exit.addActionListener(this::exitClicked);
        App.add(exit);

        /**************************************************************/

        JMenu Item = new JMenu("Item");
        menuBar.add(Item);

        JMenuItem check = new JMenuItem("Check Prices", getIconImage("blue check.png"));
        check.addActionListener(this::refreshAllClicked);
        check.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        check.setMnemonic(KeyEvent.VK_C);
        Item.add(check);

        JMenuItem addItem = new JMenuItem("Add Item", getIconImage("blue plus.png"));
        addItem.addActionListener(this::addItemClicked);
        addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        addItem.setMnemonic(KeyEvent.VK_A);
        Item.add(addItem);

        Item.addSeparator();

        JMenuItem search = new JMenuItem("Search", getIconImage("blue search.png"));
        search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        search.setMnemonic(KeyEvent.VK_S);
        Item.add(search);

        JMenuItem selectPrevious = new JMenuItem("Select Previous", getIconImage("blue up.png"));
        selectPrevious.addActionListener(this::upClicked);
        selectPrevious.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        selectPrevious.setMnemonic(KeyEvent.VK_U);
        Item.add(selectPrevious);

        JMenuItem selectNext = new JMenuItem("Select Next", getIconImage("blue down.png")); //new ImageIcon
        selectNext.addActionListener(this::downClicked);
        selectNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        selectNext.setMnemonic(KeyEvent.VK_N);
        Item.add(selectNext);

        Item.addSeparator();

        JMenu selected = new JMenu("Selected");
        Item.add(selected);

        JMenuItem price = new JMenuItem("Price", getIconImage("green check.png"));
        price.addActionListener(this::refreshButtonClicked);
        price.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_MASK));
        price.setMnemonic(KeyEvent.VK_P);
        selected.add(price);

        JMenuItem view = new JMenuItem("View", getIconImage("green internet.png"));
        view.addActionListener(this::openWebsite);
        view.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
        view.setMnemonic(KeyEvent.VK_V);
        selected.add(view);

        JMenuItem edit = new JMenuItem("Edit", getIconImage("green edit.png"));
        edit.addActionListener(this::editClicked);
        edit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        edit.setMnemonic(KeyEvent.VK_E);
        selected.add(edit);

        JMenuItem remove = new JMenuItem("Remove", getIconImage("green minus.png"));
        remove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        remove.setMnemonic(KeyEvent.VK_R);
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

    /**@return Custom button panel*/
    private JPanel makeButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JToolBar buttons = new JToolBar();

        JButton blueCheck = new JButton(getIconImage("blue check.png"));
        blueCheck.setToolTipText("Update all prices");
        blueCheck.addActionListener(this::refreshAllClicked);
        buttons.add(blueCheck);

        JButton bluePlus = new JButton(getIconImage("blue plus.png"));
        bluePlus.setToolTipText("Add an item to Price Watcher");
        bluePlus.addActionListener(this::addItemClicked);
        buttons.add(bluePlus);

        JButton blueSearch = new JButton(getIconImage("blue search.png"));
        blueSearch.setToolTipText("Search for an item");
        buttons.add(blueSearch);

        JButton blueUp = new JButton(getIconImage("blue up.png"));
        blueUp.setToolTipText("Select Previous item");
        blueUp.addActionListener(this::upClicked);
        buttons.add(blueUp);

        JButton blueDown = new JButton(getIconImage("blue down.png"));
        blueDown.setToolTipText("Select next item");
        blueDown.addActionListener(this::downClicked);
        buttons.add(blueDown);

        buttons.addSeparator();

        JButton greenCheck = new JButton(getIconImage("green check.png"));
        greenCheck.setToolTipText("Update selected item price");
        greenCheck.addActionListener(this::refreshButtonClicked);
        buttons.add(greenCheck);

        JButton greenInternet = new JButton(getIconImage("green internet.png"));
        greenInternet.setToolTipText("Open selected item's website");
        greenInternet.addActionListener(this::openWebsite);
        buttons.add(greenInternet);

        JButton greenEdit = new JButton(getIconImage("green edit.png"));
        greenEdit.setToolTipText("Edit selected item details");
        greenEdit.addActionListener(this::editClicked);
        buttons.add(greenEdit);

        JButton greenMinus = new JButton(getIconImage("green minus.png"));
        greenMinus.setToolTipText("Delete selected item");
        greenMinus.addActionListener(this::deleteClicked);
        buttons.add(greenMinus);

        buttons.addSeparator();

        JButton blueInfo = new JButton(getIconImage("blue info.png"));
        blueInfo.setToolTipText("About Price Watcher");
        blueInfo.addActionListener(this::aboutClicked);
        buttons.add(blueInfo);


        panel.add(buttons);
        panel.add(progressBar);
        return panel;
    }

    /**@return image in resources given by fileName*/
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