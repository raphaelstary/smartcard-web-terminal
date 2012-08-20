package org.stary.webterminal.server.http.api;

/**
 * RestApiAction
 */
public interface RestApiAction {

    String getAction();

    void process();
}
