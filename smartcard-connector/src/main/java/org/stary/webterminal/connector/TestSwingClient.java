package org.stary.webterminal.connector;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raphael
 */
public class TestSwingClient {

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        ViewModel.initCardTerminals();

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

        frame.setContentPane(new GuiPanel(ViewModel.getActiveCardTerminalNames(), new ViewModel()));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


}