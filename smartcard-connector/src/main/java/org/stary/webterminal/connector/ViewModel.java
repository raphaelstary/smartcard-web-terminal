package org.stary.webterminal.connector;

/**
 * @author raphael
 */
public class ViewModel {
    private PcscClient client;

    public void connect(String host, String port) {
        client = new PcscClient(host, Integer.getInteger(port));
        client.run();
    }

    public void disconnect() {
        client.stop();
    }
}
