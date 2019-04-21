import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListMouseListener extends MouseAdapter {

    JPopupMenu popupMenu = new JPopupMenu();

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JList jItemList = (JList) e.getSource();
        if (e.getClickCount() == 2){
            int index = jItemList.locationToIndex(e.getPoint());
            if (index >= 0){
                Item item = (Item) jItemList.getModel().getElementAt(index);
                System.out.println("--------"+index+" "+item.getName());

                JMenuItem checkPrice = new JMenuItem("Check price");
                checkPrice.addActionListener((this::cancelDialog));

                JMenuItem openWebpage = new JMenuItem("Open webpage");
                openWebpage.addActionListener((this::cancelDialog));

                JMenuItem editItem = new JMenuItem("Edit item");
                editItem.addActionListener((this::cancelDialog));

                JMenuItem removeItem = new JMenuItem("Remove Item");
                removeItem.addActionListener((this::cancelDialog));

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

    public void cancelDialog(ActionEvent event){
        popupMenu.removeAll();
        popupMenu.setVisible(false);
    }
}
