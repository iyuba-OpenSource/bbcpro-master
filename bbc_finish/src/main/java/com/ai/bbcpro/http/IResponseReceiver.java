package com.ai.bbcpro.http;

public interface IResponseReceiver {
    void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie);
}
