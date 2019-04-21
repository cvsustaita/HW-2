import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
* Responsible for handing mouse events on a JList to display a popup menu.
*
 * @author Erik Madik
 * @author Cynthia Sustaita
**/

class ListMouseListener extends MouseAdapter {

    Main main;
    JPopupMenu popupMenu = new JPopupMenu();

    /**Creates a new mouse listener.
     * @param main The main execution to be referenced later.*/
    public ListMouseListener(Main main) {
        this.main = main;

        JMenuItem checkPrice = new JMenuItem("Check price");
        checkPrice.addActionListener(this::cancelPopup);
        checkPrice.addActionListener(main::refreshButtonClicked);

        JMenuItem openWebpage = new JMenuItem("Open webpage");
        openWebpage.addActionListener(this::cancelPopup);
        openWebpage.addActionListener(main::openWebsite);

        JMenuItem editItem = new JMenuItem("Edit item");
        editItem.addActionListener(this::cancelPopup);
        editItem.addActionListener(main::editClicked);

        JMenuItem removeItem = new JMenuItem("Remove Item");
        removeItem.addActionListener(this::cancelPopup);
        removeItem.addActionListener(main::deleteClicked);

        popupMenu.removeAll();

        popupMenu.add(checkPrice);
        popupMenu.add(openWebpage);
        popupMenu.add(editItem);
        popupMenu.add(removeItem);
    }

    /**Display popup menu when mouse is double clicked*/
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JList jItemList = (JList) e.getSource();
        if (e.getClickCount() == 2){
            int index = jItemList.locationToIndex(e.getPoint());
            if (index >= 0){
                Item item = (Item) jItemList.getModel().getElementAt(index);
                System.out.println("--------"+index+" "+item.getName());

                popupMenu.setVisible(true);
                popupMenu.setLocation(e.getLocationOnScreen());
            }
        }
    }

    /**Action listener to cancel popup*/
    void cancelPopup(ActionEvent event){
        popupMenu.setVisible(false);
    }
}