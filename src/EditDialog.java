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

    @Override
    public void doLayout() {
        nameLabel.setBounds(10, 10, 60, 30);
        nameField.setBounds(70, 15, 270, 20);

        urlLabel.setBounds(10, 40, 60, 30);
        urlField.setBounds(70, 45, 270, 20);

        priceLabel.setBounds(10, 70, 60, 30);
        priceField.setBounds(70, 75, 270, 20);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 100);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(300, 100);
    }

    public void reset(){
        nameField.setText("");
        urlField.setText("");
        priceField.setText("");
    }
}

