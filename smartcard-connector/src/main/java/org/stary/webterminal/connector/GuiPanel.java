package org.stary.webterminal.connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

/**
 * @author raphael
 */
public class GuiPanel extends JPanel implements ActionListener {

    private static final String reader_lbl = "Reader: ";
    private static final String host_lbl = "Server: ";
    private static final String port_lbl = "Port: ";
    private static final String connect_btn = "Connect";
    private static final String disconnect_btn = "Disconnect";

    private final ViewModel model;

    private JComboBox<String> readerList;

    private JTextField host;
    private JTextField port;

    private JLabel readerLabel;
    private JLabel hostLabel;
    private JLabel portLabel;

    private JButton connect;
    private JButton disconnect;

    public GuiPanel(List<String> availableReader, ViewModel model) {
        super(new GridBagLayout());

        this.model = model;

        GridBagConstraints constraints = new GridBagConstraints();

        readerList = new JComboBox<String>((String[]) availableReader.toArray());
        readerLabel = new JLabel(reader_lbl, JLabel.LEFT);
        readerLabel.setLabelFor(readerList);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;

        add(readerLabel, constraints);

        constraints.gridx = 2;
        constraints.gridwidth = 5;
        constraints.anchor = GridBagConstraints.LINE_START;

        add(readerList, constraints);

        host = new JTextField(10);
        hostLabel = new JLabel(host_lbl, JLabel.LEFT);
        hostLabel.setLabelFor(host);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_END;

        add(hostLabel, constraints);

        constraints.gridx = 2;
        constraints.gridwidth = 5;
        constraints.anchor = GridBagConstraints.LINE_START;

        add(host, constraints);

        port = new JTextField(5);
        portLabel = new JLabel(port_lbl, JLabel.LEFT);
        portLabel.setLabelFor(port);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_END;

        add(portLabel, constraints);

        constraints.gridx = 2;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.LINE_START;

        add(port, constraints);

        connect = new JButton(connect_btn);
        disconnect = new JButton(disconnect_btn);

        connect.addActionListener(this);

        disconnect.addActionListener(this);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.gridwidth = 2;

        add(connect, constraints);

        constraints.gridx = 2;

        add(disconnect, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (connect.equals(event.getSource()))
            model.connect(host.getText(), port.getText());

        if (disconnect.equals(event.getSource()))
            model.disconnect();
    }
}
