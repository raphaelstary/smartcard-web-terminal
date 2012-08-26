package org.stary.webterminal.connector;

import javax.smartcardio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raphael
 */
public class ViewModel {
    static CardTerminals cardTerminals;
    static CardChannel cardChannel;

    private PcscClient client;

    public void connect(String host, String port, String readerName) {

        CardTerminal terminal = ViewModel.cardTerminals.getTerminal(readerName);
        try {

            if (!terminal.isCardPresent())
                throw new RuntimeException("no card in reader");

            Card card = terminal.connect("T=1");
            ViewModel.cardChannel = card.getBasicChannel();

        } catch (CardException e) {
            e.printStackTrace();
        }

        client = new PcscClient(host, Integer.parseInt(port));
        client.run();
    }

    public void disconnect() {
        client.stop();
    }

    static void initCardTerminals() {
        // Factory erstellen
        TerminalFactory tf = TerminalFactory.getDefault();

        // Terminals holen
        ViewModel.cardTerminals = tf.terminals();
    }

    static List<String> getActiveCardTerminalNames() {
        List<String> terminalNames = new ArrayList<>();
        try {
            for (CardTerminal terminal : ViewModel.cardTerminals.list()) {
                terminalNames.add(terminal.getName());
            }
        } catch (CardException e) {
            e.printStackTrace();
        }
        return terminalNames;
    }
}
