import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/** A special panel to display the detail of an item. */

@SuppressWarnings("serial")

public class ItemView extends JPanel {

    Item item;

    /** Interface to notify a click on the view page icon. */
    public interface ClickListener {
        /** Callback to be invoked when the view page icon is clicked. */
        void clicked();
    }

    /** Directory for image files: src/image in Eclipse. */
    private final static String IMAGE_DIR = "/image/";

    /** View-page clicking listener. */
    private ClickListener listener;

    /** Create a new instance. */
    public ItemView() {
        setPreferredSize(new Dimension(100, 160));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (isViewPageClicked(e.getX(), e.getY()) && listener != null) {
                    listener.clicked();
                }
            }
        });
    }

    /** Set the view-page click listener. */
    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    /** Overridden here to display the details of the item. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 20, y = 30;

        Image image = item.getWebsiteImage();
        Font italics = new Font("Arial", Font.ITALIC, 12);
        g.setFont(italics);
        g.drawImage(item.getWebsiteImage(), x,x-5, x+x, x+x-5, 0, 0, image.getWidth(null), image.getHeight(null), null);
        y += 20;
        g.drawString("Name: ", x, y);
        int itemNameX = x;
        int itemNameY = y;
        y += 20;
        g.drawString("URL: " + item.getURL(), x, y);
        y += 20;
        g.drawString("Price:$", x, y);
        int itemPriceY = y;
        y += 20;
        g.drawString("Change: %", x, y);
        int itemChangeY = y;
        y += 20;
        g.drawString("Added: " + item.getDateAdded() + " $" + item.getInitialPrice(), x, y);

        Font font = new Font("Arial", Font.BOLD, 12);
        g.setFont(font);
        g.drawString(item.getName(), 40 + itemNameX, itemNameY);

        g.setColor(Color.BLUE);
        String price = Double.toString(item.getRecentPrice());
        g.drawString(price, x + 40, itemPriceY);

        String changeString = Double.toString(item.getPriceChange());

        if(item.getPriceChange() < 0) {
            g.setColor(Color.GREEN);
            g.drawString(changeString, x + 65, itemChangeY);
        } else {
            g.setColor(Color.RED);
            g.drawString(changeString, x + 65, itemChangeY);
        }
    }

    /** Return true if the given screen coordinate is inside the viewPage icon. */
    private boolean isViewPageClicked(int x, int y) {
        return new Rectangle(20, 20, 30, 20).contains(x,  y);
    }


}