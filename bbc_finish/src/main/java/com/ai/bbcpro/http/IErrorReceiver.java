package com.ai.bbcpro.http;

public interface IErrorReceiver {
    void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie);
}
