package com.ai.bbcpro.ui.http.bean;

import android.os.Environment;
import android.util.Log;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.entity.DownloadRefresh;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.AnnouncerBean;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.common.CommonConstant;
import com.google.gson.annotations.SerializedName;
import com.youdao.sdk.nativeads.NativeResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SumBean {

    public String mytotal;
    public String total;
    public List<DataDTO> data;

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public String getMytotal() {
        return mytotal;
    }

    public void setMytotal(String mytotal) {
        this.mytotal = mytotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public static class DataDTO implements Serializable {
        @SerializedName("CreatTime")
        public String creatTime;
        @SerializedName("Category")
        public String category;
        @SerializedName("Title")
        public String title;
        @SerializedName("Texts")
        public int texts;
        @SerializedName("Sound")
        public String sound;
        @SerializedName("Pic")
        public String pic;
        @SerializedName("BbcId")
        public String bbcId;
        @SerializedName("Url")
        public String url;
        @SerializedName("DescCn")
        public String descCn;
        @SerializedName("Title_cn")
        public String title_cn;
        @SerializedName("PublishTime")
        public String publishTime;
        @SerializedName("HotFlg")
        public String hotFlg;
        @SerializedName("ReadCount")
        public String readCount;
        public int isDownload;

        /**
         * 配音
         */
        private AnnouncerBean announcerBean;

        private int endFlag;

        /**
         * 听到第几句
         */
        private int TestNumber = 0;

        /**
         * 已做练习题数量
         */
        private int exercises = 0;

        /**
         * 评测的个数
         */
        private int evalCount = 0;

        //广告
        private transient NativeResponse nativeResponse;

        /**
         * 下载进度
         */
        private transient int downloadProgress;


        public AnnouncerBean getAnnouncerBean() {
            return announcerBean;
        }

        public void setAnnouncerBean(AnnouncerBean announcerBean) {
            this.announcerBean = announcerBean;
        }

        /**
         * 获取下载文件的名字
         *
         * @return
         */
        public String getDownloadName() {

            return bbcId + ".mp3";
        }

        /**
         * 下载音频文件
         */
        public void download() {

            File file = new File(MainApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_MUSIC), getDownloadName());
            if (!file.exists()) {

                new Thread(new DownloadRunnable(bbcId, sound)).start();
            }
        }

        public int getDownloadProgress() {
            return downloadProgress;
        }

        public void setDownloadProgress(int downloadProgress) {
            this.downloadProgress = downloadProgress;
        }

        public NativeResponse getNativeResponse() {
            return nativeResponse;
        }

        public void setNativeResponse(NativeResponse nativeResponse) {
            this.nativeResponse = nativeResponse;
        }

        public int getEvalCount() {
            return evalCount;
        }

        public void setEvalCount(int evalCount) {
            this.evalCount = evalCount;
        }

        public int getExercises() {
            return exercises;
        }

        public void setExercises(int exercises) {
            this.exercises = exercises;
        }

        public int getEndFlag() {
            return endFlag;
        }

        public void setEndFlag(int endFlag) {
            this.endFlag = endFlag;
        }

        public int getTestNumber() {
            return TestNumber;
        }

        public void setTestNumber(int testNumber) {
            TestNumber = testNumber;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTexts() {
            return texts;
        }

        public void setTexts(int texts) {
            this.texts = texts;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getBbcId() {
            return bbcId;
        }

        public void setBbcId(String bbcId) {
            this.bbcId = bbcId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescCn() {
            return descCn;
        }

        public void setDescCn(String descCn) {
            this.descCn = descCn;
        }

        public String getTitle_cn() {
            return title_cn;
        }

        public void setTitle_cn(String title_cn) {
            this.title_cn = title_cn;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getHotFlg() {
            return hotFlg;
        }

        public void setHotFlg(String hotFlg) {
            this.hotFlg = hotFlg;
        }

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }

        public int getIsDownload() {
            return isDownload;
        }

        public void setIsDownload(int isDownload) {
            this.isDownload = isDownload;
        }


        /**
         * 下载音频的线程
         */
        public class DownloadRunnable implements Runnable {

            //文件命名："concept" + bookid + voaid + "US"    "concept" + bookid + voaid + "UK"
            private String voaid;
            private String sound;
            private String url;

            private int preProgress = 0;

            public DownloadRunnable(String voaId, String sound) {

                this.voaid = voaId;
                this.sound = sound;

                //生成url
                url = "http://staticvip." + CommonConstant.domain + "/sounds/minutes/" + sound;
            }

            @Override
            public void run() {

                String name = voaid + ".mp3";
                File file = new File(MainApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_MUSIC), name);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //设置要访问的网络地址
                    Request request = new Request.Builder()
                            .url(url)//http、https
                            .build();
                    //发送请求并获取数据
                    Response response = client.newCall(request).execute();
                    long length = response.body().contentLength();
                    InputStream inputStream = response.body().byteStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    byte[] bytes = new byte[2048];
                    int total = 0;
                    int size = inputStream.read(bytes);
                    while (size != -1) {

                        fileOutputStream.write(bytes, 0, size);
                        total = total + size;
                        downloadProgress = (int) (total * 100.0 / length);
                        if (preProgress != downloadProgress) {

                            EventBus.getDefault().post(new DownloadRefresh(voaid));

                            preProgress = downloadProgress;
                        }

                        size = inputStream.read(bytes);
                    }
                    fileOutputStream.flush();
                    inputStream.close();
                    fileOutputStream.close();

                    //存储下载的id
                    SyncDataHelper.getInstance(MainApplication.getApplication()).addDownload(bbcId);

                } catch (IOException e) {

                    if (file.exists()) {

                        boolean isDelete = file.delete();
                        if (isDelete) {

                            Log.d("文件下载", "文件下载异常，删除未下载的文件");
                        }
                    }
                }
            }
        }
    }
}
