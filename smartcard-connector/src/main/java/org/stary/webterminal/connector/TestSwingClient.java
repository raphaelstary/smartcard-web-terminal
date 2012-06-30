package org.stary.webterminal.connector;

import javax.smartcardio.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author raphael
 */
public class TestSwingClient {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        setCardTerminals();

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    createGUI();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void createGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        List<String> terminalNames = new ArrayList<String>();
        try {
            for (CardTerminal terminal: ViewModel.cardTerminals.list()) {
                terminalNames.add(terminal.getName());
            }
        } catch (CardException e) {
            e.printStackTrace();
        }

        frame.setContentPane(new GuiPanel(terminalNames, new ViewModel()));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private static void setCardTerminals() {
        // Factory erstellen
        TerminalFactory tf = TerminalFactory.getDefault();

        // Terminals holen
        ViewModel.cardTerminals = tf.terminals();
    }
}