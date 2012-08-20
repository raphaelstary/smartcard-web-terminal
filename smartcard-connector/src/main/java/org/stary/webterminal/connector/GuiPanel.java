package org.stary.webterminal.connector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static final String LOCALHOST = "localhost";
    private static final String DEFAULT_PORT = "8088";

    private final ViewModel model;

    private JComboBox readerList;

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

        readerList = new JComboBox(availableReader.toArray());
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
        host.setText(LOCALHOST);
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
        port.setText(DEFAULT_PORT);
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
        disconnect.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (connect.equals(event.getSource())) {
            model.connect(host.getText(), port.getText(), readerList.getSelectedItem().toString());

            connect.setVisible(false);
            disconnect.setVisible(true);
        }

        if (disconnect.equals(event.getSource())) {
            model.disconnect();

            disconnect.setVisible(false);
            connect.setVisible(true);
        }
    }
}
