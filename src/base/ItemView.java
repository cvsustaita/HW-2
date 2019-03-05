package base;

import edu.utep.cs.cs3331.pw.ConsoleUi;
import edu.utep.cs.cs3331.pw.Item;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/** A special panel to display the detail of an item. */

@SuppressWarnings("serial")
public class ItemView extends JPanel implements Main.RefreshClicked {

    Graphics graphics;

//    Item item = new Item("LED Monitor", "https://www.bestbuy.com/site/samsung-ue590-series-28-led-4k-uhd-monitor-black/5484022.p?skuId=5484022",
//            369.99, 61.67, item.getRandomPrice(), item.change());

    //private ConsoleUi ui = new ConsoleUi(item);

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
        graphics = g;
        //Dimension dim = getSize();
        
        //--
        //-- WRITE YOUR CODE HERE!
        //--
        int x = 20, y = 30;
        g.drawString("[View]", x, y);
        y += 20;
        g.drawString("Hi, I am your item!", x, y);
        y += 20;
        g.drawString("Name: " + Main.item.getItemName(), x, y);
        y += 20;
        g.drawString("URL: " + Main.item.getURL(), x, y);
        y += 20;
        g.drawString("Price: " + Main.item.getItemPrice(), x, y);
        y += 20;
        g.drawString("Change: " + Main.item.getItemChange(), x, y);
        y += 20;
        g.drawString("Added: " + Main.item.getItemDate(), x, y);
    }
    
    /** Return true if the given screen coordinate is inside the viewPage icon. */
    private boolean isViewPageClicked(int x, int y) {
    	//--
    	//-- WRITE YOUR CODE HERE
    	//--
    	return new Rectangle(20, 20, 30, 20).contains(x,  y);
    }

    /** Return the image stored in the given file. */
    public Image getImage(String file) {
        try {
        	URL url = new URL(getClass().getResource(IMAGE_DIR), file);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Graphics graphics){
        paintComponent(graphics);
    }

    @Override
    public void refreshClicked() {
        update(graphics);
    }
}