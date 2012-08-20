package org.stary.webterminal.server.http.api;

/**
 * CommandApduAction
 */
public class CommandApdu implements RestApiAction {
    @Override
    public String getAction() {
        return "sendCommandApdu";
    }

    @Override
    public void process() {
    }
}
