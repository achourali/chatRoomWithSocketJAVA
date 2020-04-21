import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;

// import javafx.scene.control.ScrollBar;

import javax.swing.text.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Window implements ActionListener, WindowListener, KeyListener {
    JTextPane boiteReception;
    JTextArea boiteEcriture;
    JScrollPane scroll;
    JScrollPane scroll2;
    Vector pressedKeys = new Vector();
    JFrame f;
    JButton bsend;
    JButton bScrollDown;
    int status;
    Socket s;
    PrintStream p;
    Scanner sc;
    String id;
    ObjectOutputStream printer;
    Message msg;
    DefaultStyledDocument document = new DefaultStyledDocument();

    public Window(Socket s, String id, String listeClient) {
        this.id = id;
        this.s = s;
        status = 1;

        f = new JFrame("chat room");
        bsend = new JButton("send");
        Label help1 = new Label("you can press esc to scroll down");
        bScrollDown = new JButton("scroll down");
        boiteReception = new JTextPane(document);
        boiteEcriture = new JTextArea();
        scroll = new JScrollPane(boiteEcriture);
        scroll2 = new JScrollPane(boiteReception);

        boiteReception.setText(listeClient);

        f.setSize(550, 500);
        help1.setBounds(310, 280, 200, 20);
        bScrollDown.setBounds(310, 300, 150, 30);
        bsend.setBounds(310, 330, 80, 30);
        boiteReception.setBounds(10, 30, 300, 300);
        scroll2.setBounds(10, 30, 300, 300);
        boiteEcriture.setBounds(10, 330, 300, 70);
        scroll.setBounds(10, 330, 300, 70);

        f.add(bsend);
        f.add(bScrollDown);
        f.add(scroll2);
        f.add(help1);

        f.add(scroll);

        bScrollDown.addActionListener(this);
        bsend.addActionListener(this);
        boiteEcriture.addKeyListener(this);
        boiteReception.addKeyListener(this);
        f.addWindowListener(this);
        f.addKeyListener(this);

        boiteEcriture.setBackground(Color.orange);
        boiteReception.setEditable(false);
        f.setLayout(null);
        f.setVisible(true);
        f.setFocusable(true);
    }

    public void run() throws Exception {
        printer = new ObjectOutputStream(s.getOutputStream());

        try {
            
            ObjectInputStream sc = new ObjectInputStream(s.getInputStream());
            Message msg;
            StyleContext context = new StyleContext();
            Style style = context.addStyle("test", null);
            Color c1;
            Color c2;
            while (status != 0) {
                msg = (Message) sc.readObject();

                if (msg.id.contentEquals("notification")) {
                    c1 = Color.RED;
                    c2 = Color.RED;

                } else if (msg.id.contentEquals(id)) {
                    c1 = new Color(0, 180, 0, 255);
                    c2 = new Color(0, 180, 0, 255);

                    msg.id = "YOU";
                } else {
                    c1 = Color.BLACK;
                    c2 = Color.BLUE;
                }

                StyleConstants.setForeground(style, c1);
                document.insertString(document.getLength(), "\n" + msg.id + ":\n", style);

                StyleConstants.setForeground(style, c2);
                document.insertString(document.getLength(), msg.data, style);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boiteEcriture.setText("exit");
        bsend.doClick();
        f.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(bsend)) {
            sendMessage();
        }
        if (e.getSource().equals(bScrollDown)) {
            JScrollBar vertical = scroll2.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }

    }

    private void sendMessage() {
        String message = boiteEcriture.getText();
        if ((!message.isEmpty()) && (message.charAt(message.length() - 1) == '\n'))
            message = removeLastCharacter(message);

        if (message.contentEquals("exit")) {

            status = 0;
            System.out.println("exit");
        }

        msg = new Message(id, message);
        try {
            printer.writeObject(msg);
            boiteEcriture.setText("");

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        // int i=e.getKeyCode();

        if ((pressedKeys.size() == 1) && (e.getKeyCode() == 10)) {

            sendMessage();

        }

        if ((pressedKeys.size() > 0) && (search(pressedKeys, e.getKeyCode()) != -1))
            pressedKeys.removeElementAt(search(pressedKeys, e.getKeyCode()));

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == 27) {
            bScrollDown.doClick();
        }
        if (e.getKeyCode() == 9 || e.getKeyCode() == 18)
            return;

        if (pressedKeys.size() != 0) {

            if (((int) pressedKeys.elementAt(pressedKeys.size() - 1)) != e.getKeyCode()) {

                pressedKeys.add(e.getKeyCode());

            }
        } else {

            pressedKeys.add(e.getKeyCode());

        }

        if (pressedKeys.size() == 2) {

            if ((e.getKeyCode() == 10) && (((int) pressedKeys.elementAt(pressedKeys.size() - 2)) == 16)) {

                boiteEcriture.setText(boiteEcriture.getText() + "\n");

            }

        }

        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub
        System.exit(0);
        status = 0;

    }

    public int search(Vector v, int value) {
        for (int i = 0; i < v.size(); i++) {
            if ((int) v.elementAt(i) == value)
                return i;
        }
        return -1;
    }

    public static String removeLastCharacter(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 1);
        }
        return result;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}