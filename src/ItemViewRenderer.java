import javax.swing.*;
import java.awt.*;

/**
 * Class that is responsible for a rendering an item and its details on a JList
 *
 * @author Erik Macik
 * @author Cynthia Sustaita
 * */

public class ItemViewRenderer extends ItemView implements ListCellRenderer<Item> {

    /**@return The component to be displayed on the JList*/
    @Override
    public Component getListCellRendererComponent(JList<? extends Item> list, Item value, int index, boolean isSelected, boolean cellHasFocus) {
        item = value;

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}