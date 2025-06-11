package com.ai.bbcpro.http;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseHttpRequest {
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    // 希望请求被发送的方式
    public final static int GET = 1;
    public final static int POST = 2;

    public void fillOutputStream(int cookie, OutputStream output,
                                 INetStateReceiver stateReceiver) throws IOException {
    }

    // http返回协议头
    public Header[] headers;

    public String[][] getExtraHeaders() {
        return null;
    }

    // The supported HTTP methods
    public interface Method {
        int GET = 0;
        int POST = 1;
    }

    protected boolean needGZIP = true;

    // TODO
    // the cache component hasn't been built now, it's the next job to be finished
    // public boolean isGetCache = true;

    private String absoluteURI;
    private int method = Method.POST;
    private int connectionTimeout = 10 * 1000;

    public boolean getNeedGZip() {
        return needGZIP;
    }

    protected byte[] mRequestBody;

    /**
     * 设置请求资源绝对地址 格式为：http://www.zhangxinda.cn/xxxx/xxx.xx
     *
     * @param absoluteURI
     */
    public final void setAbsoluteURI(String absoluteURI) {
        this.absoluteURI = absoluteURI;
    }

    /**
     * 设置请求方法
     */
    public final void setMethod(int method) {
        this.method = method;
    }

    /**
     * 获取请求主机地址接口 格式分两种: 1. IP地址，eg：192.168.12.12[:port] 2.
     * 域名形式，eg：www.zhangxinda.cn[:port]
     *
     * @return
     */
    public final String getHost() {
        try {
            URL targetUrl = new URL(absoluteURI);
            return targetUrl.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取请求资源相对地址接口 格式为：/xxxx/xxx.xx
     *
     * @return
     */
    public final String getRelativeURI() {
        try {
            URL targetUrl = new URL(absoluteURI);
            return targetUrl.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取请求资源的绝对地址接口 格式为：http://www.zhangxinda.cn/xxxx/xxx.xx
     *
     * @return
     */
    public final String getAbsoluteURI() {
        return absoluteURI;
    }

    /**
     * 创建该请求特定回复接口
     *
     * @return
     */
    public abstract BaseHttpResponse createResponse();

    public int getMethod() {
        return method;
    }

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    /**
     * To return the request's body part, which is set by the {@link #setBody(Object)} method
     * or you can directly override this method to return the needed body part.
     *
     * @return the request body's binary byte array
     */
    public byte[] getBody() {
        return mRequestBody;
    }

    /**
     * To set the request's body part, the recommended place to call this method
     * for the subclasses is in their constructor method, but the content-type
     * should match the corresponding header part.
     *
     * @param object
     */
    public void setBody(Object object) {
        byte[] bytes = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            bytes = baos.toByteArray();
            oos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRequestBody = bytes;
    }

    /**
     * 是否需要缓存本请求回复接口，默认不需要 注：需要缓存回复的请求需要重写hashCode方法，方便hashtable正常工作
     *
     * @return
     */
    public boolean needCacheResponse() {
        return false;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

}
