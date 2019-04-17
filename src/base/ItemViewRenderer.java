package base;

import edu.utep.cs.cs3331.pw.Item;

import javax.swing.*;
import java.awt.*;

public class ItemViewRenderer extends ItemView implements ListCellRenderer<Item> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Item> list, Item value, int index, boolean isSelected, boolean cellHasFocus) {
        item = value;
        return this;
    }
}
