package org.stary.webterminal.server.http.api;

/**
 * PcscClientAction
 */
public class PcscClient implements RestApiAction {
    @Override
    public String getAction() {
        return "getPcscClient";
    }

    @Override
    public void process() {
    }
}
