package com.ai.bbcpro.widget;


import com.ai.bbcpro.http.ErrorResponse;

public interface IErrorReceiver {
    void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie);
}
