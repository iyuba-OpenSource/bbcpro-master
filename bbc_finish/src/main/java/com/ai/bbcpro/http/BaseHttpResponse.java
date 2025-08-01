package com.ai.bbcpro.http;

import android.util.Log;

import com.ai.bbcpro.util.LOGUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseHttpResponse implements BaseResponse{
    protected Map<String, String> mResponseHeaders = Collections.emptyMap();

    public void setResponseHeaders(Map<String, String> headers) {
        Log.d("测试", "setResponseHeaders:设置请求头");
        this.mResponseHeaders = headers;
    }

    public void logHeaderContent() {
        Iterator<Map.Entry<String, String>> it = mResponseHeaders.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (it.hasNext()) {
            entry = it.next();
            LOGUtils.e("http233", "key : " + entry.getKey());
            LOGUtils.e("http233", "value : " + entry.getValue());
        }
    }

    /**
     * 从服务器端获取的http输入流解析接口
     *
     * @param inputStream ：回复输入流
     * @throws IOException
     * @return: 如果解析成功返回null, 否则返回相应错误回复包
     */
    public abstract ErrorResponse parseInputStream(InputStream inputStream) throws IOException;

    public ErrorResponse parseInputStream(int rspCookie,
                                          BaseHttpRequest request, InputStream inputStream, int len,
                                          INetStateReceiver stateReceiver) throws IOException {
        return null;
    }

    /**
     * 是否允许关闭输入流接口 此接口为性能需求而设，考虑到某些应用可能在最上层处理原始输入流更有效率
     *
     * @return
     */
    public abstract boolean isAllowCloseInputStream();

    /**
     * 获得输入流
     *
     * @return
     */
    public abstract InputStream getInputStream();
}
