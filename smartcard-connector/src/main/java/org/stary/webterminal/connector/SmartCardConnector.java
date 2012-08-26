package org.stary.webterminal.connector;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmartCardConnector extends JApplet {

    @Override
    public void init() {
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

    private void createGUI() {
        setContentPane(new GuiPanel(ViewModel.getActiveCardTerminalNames(), new ViewModel()));
    }
}