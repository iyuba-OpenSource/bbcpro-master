package com.ai.bbcpro.widget;




import android.util.Log;

import com.ai.bbcpro.manager.RuntimeManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;


//这是一个抽象类是用来确定网络请求的,其中设置了几个标志来表示是get还是post请求是否进行压缩,是否进行缓存.
public abstract class BaseHttpRequest {


    public final static int GET = 1;
    public final static int POST = 2;

    protected boolean needGZIP = true;

    public boolean isGetCache = true;

    private String absoluteURI;
    private int method = POST;
    private int connectionTimeout = 30000;

    public boolean getNeedGZip() {
        return needGZIP;
    }


    public Header[] headers;

    //该方法的作用是根据传入的字符串把起拼接成一个url
    public final void setAbsoluteURI(String absoluteURI) {
        this.absoluteURI = absoluteURI;
        Log.e("BaseHttpRequest", "setAbsoluteURI: "+absoluteURI);
    }


    public final void setMethod(int method) {
        this.method = method;
    }


    public final String getHost() {
        try {
            URL parser = new URL(absoluteURI);
            if (parser != null) {
                return parser.getHost();
            } else {
                return "";
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }


    public final String getRelativeURI() {
        try {
            URL parser = new URL(absoluteURI);
            if (parser != null) {
                return parser.getPath();
            } else {
                return "";
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    //对外提供一个get和set方法来为外界调用
    public final String getAbsoluteURI() {
        return absoluteURI;
    }


    public abstract BaseHttpResponse createResponse();

    public int getMethod() {
        return method;
    }


    public String[][] getExtraHeaders() {
        return null;
    }


    public void fillOutputStream(int cookie, OutputStream output,
                                 INetStateReceiver stateReceiver) throws IOException {
    }


    public boolean needCacheResponse() {
        return false;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    protected int requestId = -1;

    protected int requestMsg = -1;

    public String getRequestId() {
        if (requestId == -1) {
            return "";
        } else {
            return RuntimeManager.getString(requestId);
        }
    }

    public String getRequestMsg() {
        if (requestMsg == -1) {
            return "";
        } else {
            return RuntimeManager.getString(requestMsg);
        }

    }

}
