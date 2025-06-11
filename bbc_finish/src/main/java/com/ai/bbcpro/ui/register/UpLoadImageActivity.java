package com.ai.bbcpro.ui.register;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.ui.BasisActivity;
import com.ai.bbcpro.ui.activity.EditUserInfoActivity;
import com.ai.common.CommonConstant;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 修改头像
 *
 * @author ct
 * @version 1.0
 * @para "regist" 是否来自注册 是则下一步补充详细信息
 */
public class UpLoadImageActivity extends BasisActivity {

    ImageView userImage;

    private boolean fromRegist = false;
    private Context mContext;
    private CustomDialog waitingDialog;
    private boolean isSend = true;
    private boolean isSelectPhoto = false;

    private static final String ACTION_URL = "http://api." + CommonConstant.domainLong + "/v2/avatar?uid=";

    public String size;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private String mAuthority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        userImage = findViewById(R.id.userImage);
//        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        fromRegist = getIntent().getBooleanExtra("regist", fromRegist);
        waitingDialog = WaittingDialog.showDialog(mContext);
        initWidget();
    }

    public void onUploadBackBtnClicked() {
        onBackPressed();
    }

    public void onUserImageClicked() {
    }

    public void onUpLoadClicked() {
        if (isSelectPhoto) {
            handler.sendEmptyMessage(0);
            CustomToast.showToast(mContext, R.string.uploadimage_uploading);
            new uploadThread().start();
        } else {
            CustomToast.showToast(mContext, "请先选择图片！");
        }
    }

    public void onPhotoClicked() {
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT >= 23) {
            String permission = Manifest.permission.CAMERA;
            String permission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
            String permission3 = Manifest.permission.WRITE_EXTERNAL_STORAGE;

            if (mContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(permission2) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission, permission2, permission3}, 100);
                return;
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mAuthority = getString(R.string.file_provider_name_personal);
            contentUri = FileProvider.getUriForFile(mContext, mAuthority,
                    new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
        }
        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    }

    public void onLocalClicked() {
        if (Build.VERSION.SDK_INT >= 23) {
            String permission = Manifest.permission.CAMERA;
            String permission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
            String permission3 = Manifest.permission.WRITE_EXTERNAL_STORAGE;

            if (mContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED ||
                    mContext.checkSelfPermission(permission2) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{permission, permission2, permission3}, 100);
                return;
            }
        }
        // 手机相册中选择
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            mAuthority = mContext.getApplicationContext().getPackageName();
        } else {
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
        }
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void onSkipClicked() {
        if (fromRegist) {
            Intent intent = new Intent(mContext,
                    EditUserInfoActivity.class);
            intent.putExtra("regist", fromRegist);
            startActivity(intent);
            finish();
        } else {
            onBackPressed();
        }
    }


    private void initWidget() {
        requestPermissions();
        GitHubImageLoader.Instace(mContext).setCirclePic(AccountManager.Instance(mContext).userId,
                userImage);
        findViewById(R.id.upload_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.userImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUserImageClicked();
            }
        });
        findViewById(R.id.upLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpLoadClicked();
            }
        });
        findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhotoClicked();
            }
        });
        findViewById(R.id.local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocalClicked();
            }
        });
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSkipClicked();
            }
        });
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri contentUri;

        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (resultCode == RESULT_CANCELED) {
                    Log.d("PHOTO_REQUEST_TAKEPHOTO", "onActivityResult: " + RESULT_CANCELED);
                    return;
                }
                Log.d("PHOTO_REQUEST_TAKEPHOTO", "onActivityResult: " + resultCode);
                File picture = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    contentUri = FileProvider.getUriForFile(mContext, mAuthority, picture);
                    Log.e("CKTAG", "contentUri " + contentUri.toString());
                } else {
                    contentUri = Uri.fromFile(picture);
                }
                SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT, "header.jpg");
                break;

            case PHOTO_REQUEST_GALLERY:
                if (resultCode == RESULT_CANCELED) {
                    Log.d("PHOTO_REQUEST_GALLERY", "onActivityResult: " + RESULT_CANCELED);
                    return;
                }
                if (data != null) {
                    isSelectPhoto = true;
                    Log.d("PHOTO_REQUEST_GALLERY", "onActivityResult: " + resultCode);
                    String path = SelectPicUtils.getPath(this, data.getData());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        contentUri = FileProvider.getUriForFile(mContext, mAuthority, new File(path));
                    } else {
                        contentUri = Uri.fromFile(new File(path));
                    }
                    SelectPicUtils.cropPicture(this, contentUri, PHOTO_REQUEST_CUT, "header.jpg");
                }
                break;

            case PHOTO_REQUEST_CUT:
                if (resultCode == RESULT_CANCELED) {
                    Log.d("PHOTO_REQUEST_CUT", "onActivityResult: " + resultCode);
                    return;
                }
                if (data != null) {
                    Log.d("PHOTO_REQUEST_CUT", "onActivityResult: " + resultCode);
                    isSelectPhoto = true;

                    File cropFile = new File(Environment.getExternalStorageDirectory() + "/header.jpg");

                    Bitmap bitmap = BitmapFactory.decodeFile(cropFile.getAbsolutePath(), getBitmapOption(2));
                    userImage.setImageBitmap(bitmap);
                    ImageLoader.getInstance().cancelDisplayTask(userImage);
                    SaveImage.saveImage(Environment.getExternalStorageDirectory() + "/header.jpg", bitmap);
                }
                break;
            default:
                break;
        }

    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    private void showDialog(String mess, OnClickListener ocl) {
        if (!UpLoadImageActivity.this.isFinishing())
            new AlertDialog.Builder(UpLoadImageActivity.this)
                    .setTitle(R.string.alert_title).setMessage(mess)
                    .setNegativeButton(R.string.alert_btn_ok, ocl).show();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int jiFen = 0;
            String picUrl = "";
            String fileUrl = "";
            super.handleMessage(msg);
            jiFen = msg.arg1;
            switch (msg.what) {
                case 0:
                    waitingDialog.show();
                    break;
                case 1:
                    waitingDialog.dismiss();
                    break;
                case 2:
                    isSend = false;
                    picUrl = "http://api." + CommonConstant.domainLong + "/v2/api.iyuba?protocol=10005&uid="
                            + AccountManager.Instance(mContext).userId
                            + "&size=middle";
                    fileUrl = Environment.getExternalStorageDirectory() + "/temp.jpg";
                    new FileOpera(mContext).deleteFile(fileUrl);
                    GitHubImageLoader.Instace(mContext).clearCache();
                    showDialog(
                            getResources().getString(R.string.uploadimage_success),
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (fromRegist) {
                                        Intent intent = new Intent(mContext,
                                                EditUserInfoActivity.class);
                                        intent.putExtra("regist", fromRegist);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        onBackPressed();
                                    }
                                }
                            });
                    break;
                case 3:
                    isSend = false;
                    showDialog(
                            getResources().getString(
                                    R.string.uploadimage_failupload),
                            new OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            });
                    waitingDialog.dismiss();
                    break;
                case 4:
                    isSend = false;
                    picUrl = "http://api." + CommonConstant.domainLong + "/v2/api.iyuba?protocol=10005&uid="
                            + AccountManager.Instance(mContext).userId
                            + "&size=middle";
                    fileUrl = Environment.getExternalStorageDirectory() + "/header.jpg";
                    new FileOpera(mContext).deleteFile(fileUrl);
                    GitHubImageLoader.Instace(mContext).clearCache();
                    showDialog(
                            getResources().getString(R.string.uploadimage_success) + "+" + jiFen + "积分！",
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (fromRegist) {
                                        Intent intent = new Intent(mContext,
                                                EditUserInfoActivity.class);
                                        intent.putExtra("regist", fromRegist);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        onBackPressed();
                                    }
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };


    class uploadThread extends Thread {
        @Override
        public void run() {

            super.run();
            final File tempHead = new File(Environment.getExternalStorageDirectory() + "/header.jpg");
            String url = ACTION_URL + AccountManager.Instance(mContext).userId;
            try {
                String success = uploadFile(url, tempHead);
//            uploadFile();
                /* 将Response显示于Dialog */
                JSONObject jsonObject = new JSONObject(success.substring(
                        success.indexOf("{"), success.lastIndexOf("}") + 1));
//                System.out.println("cc=====" + jsonObject.getString("status"));
                Log.e("json", jsonObject.toString());
                if (jsonObject.getString("status").equals("0")) {// status 为0则修改成功
                    if (Integer.parseInt(jsonObject.getString("jiFen")) > 0) {
                        Message msg = new Message();
                        msg.what = 4;
                        msg.arg1 = Integer.parseInt(jsonObject.getString("jiFen"));
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
//                    finish();
                } else {
                    handler.sendEmptyMessage(3);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 上传头像、文件到服务器上
    private static String uploadFile(String actionUrl, File files) {
        String result = "";
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设定传送的method=POST */
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            // con.setRequestProperty("iyu_describe",
            // URLEncoder.encode(mood_content.getText().toString(),"utf-8"));
            /* 设定DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//				File files = new File(TakePictureUtil.photoPath);
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\""
                    + files + "\"" + ";filename=\""
                    + System.currentTimeMillis() + ".jpg\"" + end);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */

            FileInputStream fStream = new FileInputStream(files);
            /* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1) { /* 将数据写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            fStream.close();
            ds.flush();

            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 关闭DataOutputStream */
            ds.close();
            result = b.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //(this);
    }
}

