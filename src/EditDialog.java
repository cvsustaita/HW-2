import javax.swing.*;
import java.awt.*;

public class EditDialog extends JDialog {

    JLabel nameLabel = new JLabel("Name");
    JTextField nameField = new JTextField();
    JLabel urlLabel = new JLabel("URL");
    JTextField urlField = new JTextField();
    JLabel priceLabel = new JLabel("Price");
    JTextField priceField = new JTextField();

    String enteredName;
    String enteredURL;
    String eneterdPrice;

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



//        JTextField name = new JTextField();
//        JTextField url = new JTextField();
//        JTextField price = new JTextField();
//        Object[] message = {
//                "Name:", name,
//                "URL:", url,
//                "Price:", price
//        };



//        int option = JOptionPane.showConfirmDialog(this, message, "Add", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//        //OK
//        if (option == 0) {
//            try{
//                Item newItem = new Item();
//                newItem.setName(name.getText());
//                newItem.setWebsiteImage("apple.png");
//                newItem.setURL(url.getText());
//                newItem.setInitialPrice(Double.parseDouble(price.getText()));
//                newItem.setRecentPrice(Double.parseDouble(price.getText()));
//                newItem.setPriceChange(0);
//                newItem.setDateAdded(newItem.getDateAdded());
//                itemList.addElement(newItem);
//
//                showMessage("Item Successfully Added");
//            } catch (Exception e) {
//                showMessage("Please enter information.");
//            }
//        }
//
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

