import javax.swing.*;
import java.awt.*;

public class EditDialog extends JPanel {

    JLabel nameLabel = new JLabel("Name");
    JTextField nameField = new JTextField();
    JLabel urlLabel = new JLabel("URL");
    JTextField urlField = new JTextField();
    JLabel priceLabel = new JLabel("Price");
    JTextField priceField = new JTextField();

    EditDialog(){
        Dimension dim = new Dimension();
        dim.setSize(100, 25);
        nameField.setPreferredSize(dim);
        urlField.setPreferredSize(dim);
        priceField.setPreferredSize(dim);

        add(nameLabel);
        add(nameField);
        add(urlLabel);
        add(urlField);
        add(priceLabel);
        add(priceField);
    }


}
