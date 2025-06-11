package com.ai.bbcpro.ui.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class ReadBitmap {
    public static Bitmap readBitmap(Context context, int id) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;// 琛ㄧず16浣嶄綅鍥�565浠ｈ〃瀵瑰簲涓夊師鑹插崰鐨勪綅鏁�
        opt.inInputShareable = true;
        opt.inPurgeable = true;// 璁剧疆鍥剧墖鍙互琚洖鏀�
        InputStream is = context.getResources().openRawResource(id);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap readBitmap(Context context, InputStream is) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;// 琛ㄧず16浣嶄綅鍥�565浠ｈ〃瀵瑰簲涓夊師鑹插崰鐨勪綅鏁�
        opt.inInputShareable = true;
        opt.inPurgeable = true;// 璁剧疆鍥剧墖鍙互琚洖鏀�
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
