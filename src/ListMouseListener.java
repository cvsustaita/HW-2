import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ListMouseListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JList jItemList = (JList) e.getSource();
        if (e.getClickCount() == 2){
            int index = jItemList.locationToIndex(e.getPoint());
            if (index >= 0){
                JPopupMenu popupMenu = new JPopupMenu();
                Item item = (Item) jItemList.getModel().getElementAt(index);
                System.out.println("--------"+index+" "+item.getName());

                JMenuItem checkPrice = new JMenuItem("Check price");
                //checkPrice.addActionListener(Main::aboutClicked);

                JMenuItem openWebpage = new JMenuItem("Open webpage");

                JMenuItem editItem = new JMenuItem("Edit item");

                JMenuItem removeItem = new JMenuItem("Remove Item");

                popupMenu.removeAll();

                popupMenu.add(checkPrice);
                popupMenu.add(openWebpage);
                popupMenu.add(editItem);
                popupMenu.add(removeItem);

                popupMenu.setVisible(true);
                popupMenu.setLocation(e.getLocationOnScreen());
            }
        }
    }

    void cancelDialog(){

    }
}
