package com.hemaapp.hm_FrameWork;

import java.io.File;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

/**
 * 选择图片方式
 */
public class HemaImageWay extends XtomObject {
    protected Activity mContext;// 上下文对象
    protected Fragment mFragment;// 上下文对象
    protected Builder mBuilder;// 弹出对象
    protected int albumRequestCode;// 相册选择时startActivityForResult方法的requestCode值
    protected int cameraRequestCode;// 拍照选择时startActivityForResult方法的requestCode值
    protected static final String IMAGE_TYPE = ".jpg";// 图片名后缀
    protected String imagePathByCamera;// 拍照时图片保存路径

    /**
     * 创建一个选择图片方式实例
     *
     * @param mContext          上下文对象
     * @param albumRequestCode  相册选择时startActivityForResult方法的requestCode值
     * @param cameraRequestCode 拍照选择时startActivityForResult方法的requestCode值
     */
    public HemaImageWay(Activity mContext, int albumRequestCode,
                        int cameraRequestCode) {
        this.mContext = mContext;
        this.albumRequestCode = albumRequestCode;
        this.cameraRequestCode = cameraRequestCode;
    }

    /**
     * 创建一个选择图片方式实例
     *
     * @param mFragment         上下文对象
     * @param albumRequestCode  相册选择时startActivityForResult方法的requestCode值
     * @param cameraRequestCode 拍照选择时startActivityForResult方法的requestCode值
     */
    public HemaImageWay(Fragment mFragment, int albumRequestCode,
                        int cameraRequestCode) {
        this.mFragment = mFragment;
        this.albumRequestCode = albumRequestCode;
        this.cameraRequestCode = cameraRequestCode;
    }

    /**
     * 显示图片选择对话
     */
    public void show() {
        if (mBuilder == null) {
            mBuilder = new Builder(mContext == null ? mFragment.getActivity()
                    : mContext);
            mBuilder.setTitle("请选择");
            mBuilder.setItems(R.array.imgway, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    click(which);
                }
            });
        }
        mBuilder.show();
    }

    private void click(int which) {
        switch (which) {
            case 0:
                album();
                break;
            case 1:
                camera();
                break;
            case 2:
                break;
        }
    }

    /**
     * 相册获取
     * 2017年4月10日
     * 加入响应式权限和适应Android7.0的特性
     */
    public void album() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, albumRequestCode);
            return;
        }
        //2017年4月10日17:45:08 胡芳林弃用
        /*Intent it1;
        if (Build.VERSION.SDK_INT < 19) {
            it1 = new Intent(Intent.ACTION_GET_CONTENT);
            it1.setType("image*//*");
        } else {
            it1 = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        if (mContext != null)
            mContext.startActivityForResult(it1, albumRequestCode);
        else
            mFragment.startActivityForResult(it1, albumRequestCode);*/
        Intent it1 = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (mContext != null)
            mContext.startActivityForResult(it1, albumRequestCode);
    }

    /**
     * 相机获取
     * 2017年4月10日
     * 加入响应式权限和适应Android7.0的特性
     */
    public void camera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA}, cameraRequestCode);
            return;
        }
        String imageName = XtomBaseUtil.getFileName() + IMAGE_TYPE;
        Intent it3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imageDir = XtomFileUtil
                .getTempFileDir(mContext == null ? mFragment.getActivity()
                        : mContext);
        imagePathByCamera = imageDir + imageName;
        File file = new File(imageDir);
        if (!file.exists())
            file.mkdir();
        // 设置图片保存路径
        File out = new File(file, imageName);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packageName = mContext.getPackageName();
            uri = FileProvider.getUriForFile(mContext, packageName + ".fileprovider", out);
        } else {
            uri = Uri.fromFile(out);
        }

        it3.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (mContext != null)
            mContext.startActivityForResult(it3, cameraRequestCode);
        else
            mFragment.startActivityForResult(it3, cameraRequestCode);
    }

    /**
     * 获取拍照图片路径
     *
     * @return 图片路径
     */
    public String getCameraImage() {
        return imagePathByCamera;
    }

}