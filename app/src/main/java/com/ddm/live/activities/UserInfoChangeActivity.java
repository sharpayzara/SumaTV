package com.ddm.live.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.constants.Constants;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.UserCenterPresenter;
import com.ddm.live.ui.ActionSheetDialog;
import com.ddm.live.utils.StatusBarUtils;
import com.ddm.live.utils.clip.ClipHeaderActivity;
import com.ddm.live.views.iface.IUserCenterView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class UserInfoChangeActivity extends BaseActivity implements IUserCenterView {

//    private static final int GET_FROM_PHOTOGRAPH = 1;
//    private static final int GET_FROM_ALBUM = 0;

    private static final int GET_FROM_PHOTOGRAPH = 0x01;
    private static final int GET_FROM_ALBUM = 0x00;
    private static final int GET_CROP_PHOTO = 0x02;
    private File tempFile;
    private String directoryPath;
    private Bundle svsavedInstanceState;
    private UploadManager uploadManager;
    private AppComponent appComponent;
    private UserCenterPresenter userCenterPresenter;
    private Bitmap userLogoBitmap = null;
    private byte[] mContent;
    private String qiniuToken;
    private String qiniuHost;
//    private ProgressBarDialogFragment progressBarDialogFragment;

    @BindView(R2.id.user_info_img)
    ImageView ivUser;

    @BindView(R2.id.user_info_username_editor)
    TextView etUsername;

    @OnClick(R2.id.user_change_info_name)
    public void changeName() {
        Intent intent = new Intent();
        intent.putExtra("title", "name");
        intent.setClass(UserInfoChangeActivity.this, UserInfoChangeItemActivity.class);
        startActivity(intent);
    }

    @BindView(R2.id.user_info_gender_editor)
    TextView tvUserGender;
    @BindView(R2.id.indicator)
    AVLoadingIndicatorView indicatorView;

    @OnClick(R2.id.user_change_info_gender)
    public void changeGender() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(
                        new String[]{"女", "男"},
                        appUserActivity.getGender(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
//                                        Toast.makeText(UserInfoChangeActivity.this, "选择了女", Toast.LENGTH_SHORT).show();
                                        userCenterPresenter.changeUserGender(0);
                                        break;

                                    case 1:
//                                        Toast.makeText(UserInfoChangeActivity.this, "选择了男", Toast.LENGTH_SHORT).show();
                                        userCenterPresenter.changeUserGender(1);
                                        break;
                                }
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    @BindView(R2.id.user_info_introduction_editor)
    TextView etIntroduction;

    @OnClick(R2.id.user_change_info_sign)
    public void changeSign() {
        Intent intent = new Intent();
        intent.putExtra("title", "sign");
        intent.setClass(UserInfoChangeActivity.this, UserInfoChangeItemActivity.class);
        startActivity(intent);
    }

    @OnClick(R2.id.blue_user_info_back_btn)
    public void clickToBack() {
        finish();
    }

    private Camera mCamera;// Camera对象
    private SurfaceHolder holder;// SurfaceView的控制器

    @OnClick(R2.id.user_info_img_layout)
    public void selectHeadImg() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        boolean isGranted = true;
        for (String str : permissions) {
            if (ActivityCompat.checkSelfPermission(UserInfoChangeActivity.this, str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                isGranted = false;
                ActivityCompat.requestPermissions(UserInfoChangeActivity.this, permissions, Constants.REQUEST_CODE_READ_CAMERA);
                return;
            }
        }
        if (isGranted) {
//            changeHeadimage();
            showChooseDialog();
        }
    }

//    @OnClick(R2.id.user_info_btn)
//    public void clickToChange() {
//
//    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.svsavedInstanceState = savedInstanceState;
        StatusBarUtils.setWindowStatusBarColor(this, R.color.xiaozhu_top_status_bar);
        StatusBarUtils.setWindowBottomStatusBarColor(this, R.color.xiaozhu_bottom_status_bar);
        setContentView(R.layout.activity_user_info_change);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        userCenterPresenter.attachView(this);
//        progressBarDialogFragment = new ProgressBarDialogFragment();
//        initUI();
        uploadManager = new UploadManager();
//        initDate(savedInstanceState);
    }

    private void initUI() {
        if (appUserActivity.getSmallHeadimgurl()==null || appUserActivity.getSmallHeadimgurl().isEmpty()) {
            ivUser.setImageResource(R.mipmap.user1);
        } else {
            Picasso.with(this)
//                    .load(appUserActivity.getHeadimgurl())
                    .load(appUserActivity.getSmallHeadimgurl())
                    .placeholder(R.mipmap.user1)
                    .error(R.mipmap.user1)
                    .into(ivUser);
        }
//        etUsername.setHint(appUserActivity.getNickname());
        etUsername.setText(appUserActivity.getNickname());

        if (appUserActivity.getGender() == 0) {
            tvUserGender.setText("女");
        } else {
            tvUserGender.setText("男");
        }

        /*if (appUserActivity.getSign().equals("") || appUserActivity.getSign().equals("null")) {
//            etIntroduction.setHint(R.string.user_introduction);
            etIntroduction.setText(R.string.user_introduction);
        } else {
//            etIntroduction.setHint(appUserActivity.getSign());
            etIntroduction.setText(appUserActivity.getSign());
        }*/
    }

    private void initDate(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/ledao/image/"),
                    System.currentTimeMillis() + ".jpg");
            directoryPath = Environment.getExternalStorageDirectory().getPath() + "/ledao/";
        }
    }

    private void showChooseDialog() {
        initDate(svsavedInstanceState);
        userCenterPresenter.getHeaderImgUploadToken();
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                                startActivityForResult(intent, GET_FROM_PHOTOGRAPH);
                            }
                        })
                .addSheetItem("从图库选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(Intent.createChooser(intent, "请选择图片"), GET_FROM_ALBUM);
                            }
                        }).show();

    }

    public void changeHeadimage() {

        userCenterPresenter.getHeaderImgUploadToken();
        final CharSequence[] items = {"相册", "拍照"};

        final AlertDialog dlg = new AlertDialog.Builder(UserInfoChangeActivity.this).setTitle("选择照片").setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //这里item是根据选择的方式，   在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
                        switch (which) {

                            case 0:
                                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                getImage.setType("image/*");
                                startActivityForResult(getImage, GET_FROM_ALBUM);
                                break;

                            case 1:
                                try {
                                    Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivityForResult(getImageByCamera, GET_FROM_PHOTOGRAPH);
                                } catch (Exception e) {
                                    toast("请开启相机权限");
                                    return;
                                }

                                break;
                        }


                    }
                }).setNegativeButton("取消", null).create();
        dlg.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {
            case GET_FROM_PHOTOGRAPH:
                if (resultCode == RESULT_OK) {
                    starCropPhoto(Uri.fromFile(tempFile));
                }
                break;
            case GET_FROM_ALBUM:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();

                    starCropPhoto(uri);
                }

                break;
            case GET_CROP_PHOTO:
                if (resultCode == RESULT_OK) {

                    if (intent != null) {
                        setPicToView(intent);
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 打开截图界面
     *
     * @param uri 原图的Uri
     */
    public void starCropPhoto(Uri uri) {

        if (uri == null) {
            return;
        }
        Log.e("zz", "starCropPhoto---" + uri.getPath());
        Intent intent = new Intent();
        intent.setClass(this, ClipHeaderActivity.class);
        intent.setData(uri);
        intent.putExtra("side_length", 1080);//裁剪图片宽高
        startActivityForResult(intent, GET_CROP_PHOTO);

    }

    private void setPicToView(Intent picdata) {
//        if (!progressBarDialogFragment.isAdded()) {
//            progressBarDialogFragment.show(getFragmentManager(), "loginDialogFragment");
//        }
        indicatorView.setIndicatorColor(R.color.purple);
//        indicatorView.setVisibility(View.VISIBLE);
        Uri uri = picdata.getData();

        if (uri == null) {
            return;
        }

        Picasso.with(this)
                .load(uri.getPath())
                .placeholder(R.mipmap.user1)
                .error(R.mipmap.user1)
                .into(ivUser);
        Log.e("zz", "setPicToView---" + uri.getPath());
        uploadManager.put(uri.getPath(), null, qiniuToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (!info.isOK()) {
                    Toast.makeText(UserInfoChangeActivity.this, info.error, Toast.LENGTH_SHORT).show();
                } else {
                    userCenterPresenter.changeUserHeader(qiniuHost + response.optString("hash"));
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
            }
        }, null));
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver contentResolver = getContentResolver();
        progressBarDialogFragment.show(getFragmentManager(), "");
        if (requestCode == GET_FROM_ALBUM) {
            try {
                //获得图片的uri
                Uri orginalUri = data.getData();
                //将图片内容解析成字节数组
                mContent = readStream(contentResolver.openInputStream(Uri.parse(orginalUri.toString())));
                //将字节数组转换为ImageView可调用的Bitmap对象
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 4;
                userLogoBitmap = getPicFromBytes(mContent, opts);
                ////把得到的图片绑定在控件上显示
                ivUser.setImageBitmap(userLogoBitmap);
                String path = "";
                try {
                    path = com.ddm.live.utils.FileUtils.getPath(this, orginalUri);
                } catch (Exception e) {
                    toast("请开启文件读取权限");
                    return;
                }
                uploadManager.put(path, null, qiniuToken, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (!info.isOK()) {
                            Toast.makeText(UserInfoChangeActivity.this, info.error, Toast.LENGTH_SHORT).show();
                        } else {
                            userCenterPresenter.changeUserHeader(qiniuHost + response.optString("hash"));
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                    }
                }, null));

            } catch (Exception e) {
                e.printStackTrace();
                progressBarDialogFragment.dismiss();
            }

        } else if (requestCode == GET_FROM_PHOTOGRAPH) {
            try {
                Bundle extras = data.getExtras();
                userLogoBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                userLogoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                mContent = baos.toByteArray();
                uploadManager.put(mContent, null, qiniuToken, new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (!info.isOK()) {
                            Toast.makeText(UserInfoChangeActivity.this, info.error, Toast.LENGTH_SHORT).show();
                        } else {
                            userCenterPresenter.changeUserHeader(qiniuHost + response.optString("hash"));
                        }
                    }
                }, new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                    }
                }, null));
            } catch (Exception e) {
                e.printStackTrace();
                progressBarDialogFragment.dismiss();
            }
            ivUser.setImageBitmap(userLogoBitmap);
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isGranted = true;
        if (requestCode == Constants.REQUEST_CODE_READ_CAMERA) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                }
            }
            if (isGranted) {
//                changeHeadimage();
                showChooseDialog();
            }
        }
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }


    public static byte[] readStream(InputStream in) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        while ((len = in.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        in.close();
        return data;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule())
                .build();

        userCenterPresenter = presenterComponent.getUserCenterPresenter();
    }

    @Override
    public void changeResult() {
//        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        initUI();
//        if (progressBarDialogFragment != null && progressBarDialogFragment.isVisible()) {
//            progressBarDialogFragment.dismiss();
//        }
        indicatorView.setVisibility(View.GONE);
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("修改成功")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        initUI();
                        if(directoryPath!=null){
                            deleteDirectory(directoryPath);
                        }
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onfailed(String message) {
        indicatorView.setVisibility(View.GONE);
        switch (message) {
            case com.ddm.live.constants.Constants.FAILED_CONNECT:
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("当前网络状况不佳")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                progressBarDialogFragment.dismiss();
                                indicatorView.setVisibility(View.GONE);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;

            default:
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(message)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                progressBarDialogFragment.dismiss();
                                indicatorView.setVisibility(View.GONE);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
        /*if (message.equals(com.ddm.live.constants.Constants.FAILED_CONNECT)) {
            *//*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("当前网络状况不佳");
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
            builder.show();*//*

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("当前网络状况不佳")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        } else if (message.equals("The refresh token is invalid.")) {//刷新token不成功，重新登录
            super.onfaild(message);
        } else {
            *//*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton("确认",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
            builder.show();*//*

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(message)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }*/
    }

    @Override
    public void getUploadToken(String token, String host) {
        qiniuToken = token;
        qiniuHost = host;
    }

    @Override
    public void getUserInfo(SpecUserInfoBean userInfo) {

    }

    @Override
    public void addFocusResult(boolean result, String msg) {

    }

    @Override
    public void cancelFocusResult(boolean result, String msg) {

    }

    /**
     * @param dirPath
     * @return
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        /*for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }*/
        for (File f : files) {
            if (f.isFile()) {
                //删除子文件
                flag = deleteFile(f.getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(f.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }
}
