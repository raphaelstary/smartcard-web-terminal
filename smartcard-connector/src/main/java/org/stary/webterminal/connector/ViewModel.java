package org.stary.webterminal.connector;

import javax.smartcardio.*;

/**
 * @author raphael
 */
public class ViewModel {
    static CardTerminals cardTerminals;
    static CardChannel cardChannel;

    private PcscClient client;

    public void connect(String host, String port, String readerName) {

        CardTerminal terminal = ViewModel.cardTerminals.getTerminal(readerName);
        CardChannel channel;
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
}
