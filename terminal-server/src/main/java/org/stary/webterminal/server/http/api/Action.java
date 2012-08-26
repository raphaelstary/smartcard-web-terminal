package org.stary.webterminal.server.http.api;

import org.stary.webterminal.server.http.JsonMapper;

/**
 * Action
 */
public abstract class Action {
    protected final JsonMapper mapper;

    protected Action(JsonMapper mapper) {
        this.mapper = mapper;
    }
}
