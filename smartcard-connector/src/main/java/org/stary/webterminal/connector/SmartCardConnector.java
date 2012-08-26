package org.stary.webterminal.connector;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class SmartCardConnector extends JApplet {

    private List<String> readerList;
    private ViewModel model;
    private GuiPanel view;

    @Override
    public void init() {
        readerList = Arrays.asList("Reader 00", "Reader 01");

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
        model = new ViewModel();
        view = new GuiPanel(readerList, model);

        setContentPane(view);
    }
}