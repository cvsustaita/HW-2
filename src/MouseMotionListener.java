import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

class MouseMotionListener extends MouseAdapter{
    Main main;

    public mouseMotionListener(Main main){
        this.main = main;

        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);

            System.out.println("X:"+e.getX()+"Y:"+e.getY());
        }

        public void mouseDragged(MouseEvent e) {
        }

    }

}
