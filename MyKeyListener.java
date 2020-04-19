import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import java.awt.*;

public class MyKeyListener implements KeyListener {
    Vector pressedKeys = new Vector();

    TextArea boite;
    TextArea message;

    public MyKeyListener(TextArea boite, TextArea message) {
        this.boite = boite;
        this.message = message;
    }

    @Override
    public void keyTyped(KeyEvent e) {

        // System.out.println(e.getKeyText(e.getKeyChar()));

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyCode());
        if (pressedKeys.size() != 0) {

            if (((int) pressedKeys.elementAt(pressedKeys.size() - 1)) != e.getKeyCode()) {

                pressedKeys.add(e.getKeyCode());
                System.out.println("press" + pressedKeys.size());

            }
        } else {

            pressedKeys.add(e.getKeyCode());
            System.out.println("press" + pressedKeys.size());

        }
        if (pressedKeys.size() >= 2) {

            if ((e.getKeyCode() == 10) && (((int) pressedKeys.elementAt(pressedKeys.size() - 2)) == 16)) {
                System.out.println("return to line");
                
                message.append("");

            }

        }

        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        // int i=e.getKeyCode();

        if ((pressedKeys.size() == 1) && (e.getKeyCode() == 10)) {
            boite.setText(boite.getText() + message.getText() );
            message.setText("");

        }

        pressedKeys.removeElementAt(search(pressedKeys, e.getKeyCode()));
        // pressedKeys.remove(e.getKeyCode());
        System.out.println("release" + pressedKeys.size());

    }

    public int search(Vector v, int value) {
        for (int i = 0; i < v.size(); i++) {
            if ((int) v.elementAt(i) == value)
                return i;
        }
        return -1;
    }

}