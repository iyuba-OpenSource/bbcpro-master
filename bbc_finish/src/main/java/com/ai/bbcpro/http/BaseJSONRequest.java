package com.ai.bbcpro.http;

public abstract class BaseJSONRequest extends BaseHttpRequest{
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s",
            PROTOCOL_CHARSET);

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }
}
