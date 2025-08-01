package com.ai.bbcpro.widget;


import com.ai.bbcpro.http.BaseXMLRequest;
import com.ai.bbcpro.http.ErrorResponse;

/**
 * 执行网络交互
 *
 * @author 陈彤
 */
public class ExeProtocol {

    public static void exe(BaseJSONRequest request,
                           final ProtocolResponse protocolResponse) {
        ClientSession.Instace().asynGetResponse(request,
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response,
                                           BaseHttpRequest request, int rspCookie) {
                        protocolResponse.finish(response);
                    }
                }, null, new INetStateReceiver() {

                    @Override
                    public void onStartSend(BaseHttpRequest request,
                                            int rspCookie, int totalLen) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onStartRecv(BaseHttpRequest request,
                                            int rspCookie, int totalLen) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onStartConnect(BaseHttpRequest request,
                                               int rspCookie) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSendFinish(BaseHttpRequest request,
                                             int rspCookie) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSend(BaseHttpRequest request, int rspCookie,
                                       int len) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onRecvFinish(BaseHttpRequest request,
                                             int rspCookie) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onRecv(BaseHttpRequest request, int rspCookie,
                                       int len) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onNetError(BaseHttpRequest request,
                                           int rspCookie, ErrorResponse errorInfo) {
                        protocolResponse.error();
                    }

                    @Override
                    public void onConnected(BaseHttpRequest request,
                                            int rspCookie) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onCancel(BaseHttpRequest request, int rspCookie) {
                        // TODO Auto-generated method stub

                    }
                });
    }



}
