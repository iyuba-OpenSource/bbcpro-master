package com.ai.bbcpro.widget;



public interface IResponseReceiver {
    void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie);
}
