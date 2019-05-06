import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class PointerListener implements MouseMotionListener{
    Main main;
    public PointerListener(Main main) {
        this.main = main;
    }
    @Override
    public void mouseDragged(MouseEvent e){
        System.out.println(e);
    }

    public void mouseMoved(MouseEvent e){
        System.out.println("X:" + e.getX() + "Y:" + e.getY());
    }
}
