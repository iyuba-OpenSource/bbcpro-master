package com.ai.bbcpro.http;

public interface ProtocolResponse {
    void finish(BaseHttpResponse bhr);

    void error();
}
