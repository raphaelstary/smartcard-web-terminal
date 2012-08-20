package org.stary.webterminal.server.http.api;

/**
 * PcscClientsAction
 */
public class PcscClients implements RestApiAction {
    @Override
    public String getAction() {
        return "getPcscClients";
    }

    @Override
    public void process() {
    }
}
