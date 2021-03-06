package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseConfig;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseImageWay;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.EventBusConfig;
import com.hemaapp.wcpc_user.EventBusModel;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.User;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.greenrobot.event.EventBus;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;

/**
 * 个人中心 -- 编辑个人信息
 */
public class PersonInforActivity extends BaseActivity {

    private ImageView left;
    private TextView right;
    private TextView title;

    private RoundedImageView image_avatar;
    private EditText edit_username;
    private LinearLayout layout_sex;
    private TextView text_sex;
    private TextView tvInvit;
    private TextView tv_username;

    private User user;
    private String realname, sex;

    public BaseImageWay imageWay;
    private String tempPath;
    private String imagePathCamera;

    private PopupWindow mWindow;
    private ViewGroup mViewGroup;
    private TextView boy;
    private TextView girl;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personinfor);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            imageWay = new BaseImageWay(mContext, 1, 2);
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new BaseImageWay(mContext, 1, 2);
        }
        user = BaseApplication.getInstance().getUser();
        getNetWorker().clientGet(user.getToken(), user.getId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null)
            outState.putString("imagePathCamera", imageWay.getCameraImage());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        deleteCompressPics();
        super.onDestroy();
    }

    // 删除临时图片文件
    private void deleteCompressPics() {
        if (!isNull(imagePathCamera)) {
            File file = new File(imagePathCamera);
            file.delete();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:// 相册选择图片
                album(data);
                break;
            case 2:// 拍照
                camera();
                break;
            case 3:// 裁剪
                image_avatar.setCornerRadius(90);
                imageWorker.loadImage(new XtomImageTask(image_avatar, tempPath, mContext));
                break;
        }
    }

    private void camera() {
        if (imagePathCamera == null) {
            imagePathCamera = imageWay.getCameraImage();
        }
        editImage(imagePathCamera, 3);
    }

    private void album(Intent data) {
        if (data == null)
            return;
        Uri selectedImageUri = data.getData();
        // 获取图片路径
        String[] proj = {MediaStore.Images.Media.DATA};
        final CursorLoader loader = new CursorLoader(mContext);
        loader.setUri(selectedImageUri);
        loader.setProjection(proj);
        loader.registerListener(0, new Loader.OnLoadCompleteListener<Cursor>() {

            @Override
            public void onLoadComplete(Loader<Cursor> arg0, Cursor cursor) {
                int columnIndex = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String imagepath = cursor.getString(columnIndex);
                editImage(imagepath, 3);
                loader.stopLoading();
                cursor.close();
            }
        });
        loader.startLoading();
    }

    private void editImage(String path, int requestCode) {
        File file = new File(path);
        startPhotoZoom(Uri.fromFile(file), requestCode);
    }

    private void startPhotoZoom(Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //进行适当的放大缩小
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("noFaceDetection", false);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", BaseConfig.IMAGE_WIDTH);
        intent.putExtra("aspectY", BaseConfig.IMAGE_WIDTH);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", BaseConfig.IMAGE_WIDTH);
        intent.putExtra("outputY", BaseConfig.IMAGE_WIDTH);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, requestCode);
    }

    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {
        String savedir = XtomFileUtil.getTempFileDir(mContext);
        File dir = new File(savedir);
        if (!dir.exists())
            dir.mkdirs();
        // 保存入sdCard
        tempPath = savedir + XtomBaseUtil.getFileName() + ".jpg";// 保存路径
        File file = new File(tempPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return file;
    }

    private void initUserData() {
        try {
            URL url = new URL(user.getAvatar());
            image_avatar.setCornerRadius(90);
            imageWorker.loadImage(new XtomImageTask(image_avatar, url, mContext));
        } catch (MalformedURLException e) {
            image_avatar.setImageResource(R.mipmap.default_user);
        }
        realname = user.getRealname();
        edit_username.setText(realname);
        edit_username.setSelection(realname.length());
        sex = user.getSex();
        text_sex.setText(sex);
        tvInvit.setText(user.getInvitecode());
        tv_username.setText(user.getUsername());
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog("请稍后...");
                break;
            case CLIENT_SAVE:
                showProgressDialog("正在保存信息...");
                break;
            case FILE_UPLOAD:
                showProgressDialog("正在上传文件...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case CLIENT_SAVE:
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayParse<User> uResult = (HemaArrayParse<User>) baseResult;
                user = uResult.getObjects().get(0);
                initUserData();
                break;
            case CLIENT_SAVE:
                if (!isNull(tempPath)) {
                    getNetWorker().fileUpload(user.getToken(), "1", "0", "0", "0", "无",
                            tempPath);
                } else {
                    EventBus.getDefault().post(new EventBusModel(EventBusConfig.REFRESH_CUSTOMER_INFO));
                    showTextDialog(baseResult.getMsg());
                    right.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK, mIntent);
                            finish();
                        }
                    }, 1000);
                }
                break;
            case FILE_UPLOAD:
                EventBus.getDefault().post(new EventBusModel(EventBusConfig.REFRESH_CUSTOMER_INFO));
                showTextDialog(baseResult.getMsg());
                right.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK, mIntent);
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case CLIENT_SAVE:
                ;
            case FILE_UPLOAD:
                showTextDialog(baseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("抱歉，获取数据失败");
                break;
            case CLIENT_SAVE:
                showTextDialog("保存用户信息失败");
                break;
            case FILE_UPLOAD:
                showTextDialog("上传文件失败");
                break;
        }
    }

    @Override
    protected void findView() {
        title = (TextView) findViewById(R.id.title_text);
        left = (ImageView) findViewById(R.id.title_btn_left);
        right = (TextView) findViewById(R.id.title_btn_right);

        image_avatar = (RoundedImageView) findViewById(R.id.imageview);
        edit_username = (EditText) findViewById(R.id.edittext);
        layout_sex = (LinearLayout) findViewById(R.id.layout_0);
        text_sex = (TextView) findViewById(R.id.textview_0);
        tvInvit = (TextView) findViewById(R.id.tv_invit);
        tv_username= (TextView) findViewById(R.id.tv_username);
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        title.setText("编辑个人信息");
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right.setText("保存");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realname = edit_username.getText().toString();
                if (isNull(realname)) {
                    showTextDialog("请填写姓名");
                    return;
                }

                String content = "^[\u4E00-\u9FA5]+$";
                char[] str = realname.toCharArray();
                int count = 0;
                for (int i = 0; i < str.length; i++) {
                    char c = str[i];
                    if (String.valueOf(c).matches(content))
                        count++;
                }
                int length = realname.length() + count;
                if (length > 16) {
                    showTextDialog("昵称不能超过16个字符,请重新填写");
                    return;
                }

                if (isNull(sex)) {
                    showTextDialog("请选择性别");
                    return;
                }

                getNetWorker().clientSave(user.getToken(), realname, sex);
            }
        });
        setListener(image_avatar);
        setListener(layout_sex);
    }

    private void setListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                switch (v.getId()) {
                    case R.id.imageview:
                        imageWay.show();
                        break;
                    case R.id.layout_0:
                        showPopWindow(0);
                        break;
//                    case R.id.layout_1:
//                        showPopWindow(1);
//                        break;
                }
            }
        });
    }

    private void showPopWindow(int type) {
        if (mWindow != null) {
            mWindow.dismiss();
        }
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        mWindow.setAnimationStyle(R.style.PopupAnimation);
        mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_sex, null);
        boy = (TextView) mViewGroup.findViewById(R.id.textview);
        girl = (TextView) mViewGroup.findViewById(R.id.textview_0);
        cancel = (TextView) mViewGroup.findViewById(R.id.textview_2);
        BaseUtil.fitPopupWindowOverStatusBar(mWindow,true);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);

        if (type == 0) {
            boy.setText("男");
            girl.setText("女");
        } else {
//            boy.setText("身份证");
//            girl.setText("驾驶证");
        }
        setListener(boy, type);
        setListener(girl, type);
        setListener(cancel, type);
    }

    private void setListener(View view, final int type) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                switch (v.getId()) {
                    case R.id.textview: // 男
                        if (type == 0) {
                            sex = "男";
                            text_sex.setText(sex);
                            text_sex.setTextColor(mContext.getResources().getColor(R.color.cl_3f3f3f));
                        } else {
//                            IDtype = "1";
//                            text_kind.setText("身份证");
//                            text_kind.setTextColor(mContext.getResources().getColor(R.color.cl_3f3f3f));
                        }
                        break;
                    case R.id.textview_0: // 女
                        if (type == 0) {
                            sex = "女";
                            text_sex.setText(sex);
                            text_sex.setTextColor(mContext.getResources().getColor(R.color.cl_3f3f3f));
                        } else {
//                            IDtype = "2";
//                            text_kind.setText("驾驶证");
//                            text_kind.setTextColor(mContext.getResources().getColor(R.color.cl_3f3f3f));
                        }
                        break;
                    case R.id.textview_2:
                        break;
                }
            }
        });
    }
}
