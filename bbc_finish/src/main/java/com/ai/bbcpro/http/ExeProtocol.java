package com.ai.bbcpro.http;

import android.util.Log;

import com.ai.bbcpro.widget.BlogExpressionRequest;

public class ExeProtocol {
    public static <T extends BaseHttpRequest> void exe(T request, final ProtocolResponse protocolResponse) {
        ClientSession.getInstance().asynGetResponse(request, new IResponseReceiver() {
            @Override
            public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                protocolResponse.finish(response);
            }
        }, new IErrorReceiver() {

            @Override
            public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                protocolResponse.error();
                Log.e("ExeProtocol", "onError: "+errorResponse.getErrorDesc() );
            }
        }, null);
    }
}
