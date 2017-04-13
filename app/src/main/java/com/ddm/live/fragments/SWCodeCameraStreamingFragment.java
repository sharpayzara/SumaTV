package com.ddm.live.fragments;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.activities.Config;
import com.ddm.live.constants.Constants;
import com.ddm.live.models.bean.user.AppUser;
import com.ddm.live.presenter.RongYunPresenter;
import com.ddm.live.presenter.VideoPlayerPresenter;
import com.ddm.live.utils.BlurBitmap;
import com.ddm.live.utils.NetworkUtils;
import com.ddm.live.utils.camera.util.DisplayUtil;
import com.ddm.live.utils.clip.BitmapUtil;
import com.ddm.live.views.iface.IFragmentLayerView;
import com.ddm.live.views.iface.IPreLiveView;
import com.ddm.live.views.iface.IPreliveLayerView;
import com.ddm.live.views.iface.ISWcodeCameraStreamingView;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by Administrator on 2016/11/30.
 */

public class SWCodeCameraStreamingFragment extends StreamingBaseFragment implements IFragmentLayerView, IPreLiveView, IPreliveLayerView {
    private UMImage image;
    private BFLiveOverFragment bfLiveOverFragment;
    private int totalPeople;
    private List<String> userIdList = new ArrayList<String>();
    private ProgressBarDialogFragment progressBarDialogFragment;
    //    private Button mTorchBtn;
    private boolean mIsTorchOn = false;

    //    private Button mCaptureFrameBtn;
    private static final int MSG_ADD_VIEW = 0x00;
    //    private ImageView mCameraSwitchBtn;
    private View mRootView;
    private ImageView mMsgVisiableBtn;
    private ImageView mMsgSendBtn;
    private EditText mMsgEdit;
    private RongYunPresenter presenterRongYun;
    private VideoPlayerPresenter presenterVideoPlayer;
    private String topic;
    private int sWidth, sHeight;
    private ScrollView msgScroll;
    private LinearLayout msgLayout;
    private HorizontalScrollView headScroll;
    private LinearLayout headLayout;
    private AppUser appUser;
    private Set<String> watchers = new HashSet<String>();
    PreStartLiveFragment preStartLiveFragment;
    RelativeLayout preLiveLayout;
    @BindView(R2.id.bf_over_bg)
    ImageView bfOverBg;
    int radioId;


    public void attachFragment(LiveMainFragment liveMainFragment) {
        this.liveMainFragment = liveMainFragment;
    }

    private boolean isPause = false;
    private boolean isStop = false;
    private ISWcodeCameraStreamingView isWcodeCameraStreamingView;

    public static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_VIEW:
                    int off = msgLayout.getMeasuredHeight() - msgScroll.getHeight();
                    if (off > 0) {
                        msgScroll.scrollTo(0, off);//改变滚动条的位置
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_camera_streaming, container, false);
        ButterKnife.bind(this, view);
        presenter.attachIView(this);
        AppApplication application = (AppApplication) getActivity().getApplication();
        appUser = application.getAppUser();
        image = new UMImage(getActivity(), appUser.getSmallHeadimgurl());
        progressBarDialogFragment = new ProgressBarDialogFragment();

        AspectFrameLayout afl = (AspectFrameLayout) view.findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        GLSurfaceView glSurfaceView = (GLSurfaceView) view.findViewById(R.id.cameraPreview_surfaceView);

        mRootView = view.findViewById(R.id.content);
        mRootView.addOnLayoutChangeListener(this);

//        mShutterButton = (Button) view.findViewById(R.id.toggleRecording_button);

        mSatusTextView = (TextView) view.findViewById(R.id.streamingStatus);


        WatermarkSetting watermarksetting = new WatermarkSetting(getActivity());
        watermarksetting.setResourceId(R.drawable.aa_dialog_bg)
                .setAlpha(100)
                .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
                .setCustomPosition(0.5f, 0.5f);
        mMediaStreamingManager = new MediaStreamingManager(getActivity(), afl, glSurfaceView,
                AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC); // sw codec

        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting, null, mProfile);

        mMediaStreamingManager.setStreamingStateListener(this);
        mMediaStreamingManager.setSurfaceTextureCallback(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
//        mMediaStreamingManager.setNativeLoggingEnabled(false);
        mMediaStreamingManager.setStreamStatusCallback(this);
        mMediaStreamingManager.setStreamingPreviewCallback(this);
        mMediaStreamingManager.setAudioSourceCallback(this);


        RelativeLayout videoView = (RelativeLayout) view.findViewById(R.id.content);
        ViewGroup.LayoutParams lp = videoView.getLayoutParams();
        int[] realScreenSize = DisplayUtil.getScreentHeight(getActivity());
        lp.width = realScreenSize[0];
        lp.height = realScreenSize[1];
        Picasso.with(context).load(appUser.getHeadimgurl())
                .placeholder(R.mipmap.blue_over_test_bgd_livein)
                .error(R.mipmap.blue_over_test_bgd_livein)
                .into(bfOverBg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) bfOverBg.getDrawable()).getBitmap();
                        bfOverBg.setImageBitmap(BlurBitmap.blur(context, bitmap, false));
                        Bitmap bitmap2 = ((BitmapDrawable) bfOverBg.getDrawable()).getBitmap();
                        BFLiveOverFragment.bitmap = BitmapUtil.compressBitmap(bitmap2);
                    }

                    @Override
                    public void onError() {
                    }
                });
        return view;
    }

    public void attachISWcodeCameraStreamingView(ISWcodeCameraStreamingView isWcodeCameraStreamingView) {
        this.isWcodeCameraStreamingView = isWcodeCameraStreamingView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause && liveMainFragment.isVisible()) {
            liveMainFragment.getLiveLayerFragment().rongSendStatusMessage(1);//发送主播回来消息
            mMediaStreamingManager.pause();
        }
        mMediaStreamingManager.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (liveMainFragment != null && liveMainFragment.isVisible()) {
            isPause = true;
            liveMainFragment.getLiveLayerFragment().rongSendStatusMessage(0);//发送主播暂时离开消息
        } else {
            mMediaStreamingManager.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.pause();
    }

    private Switcher mSwitcher = new Switcher();

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//        Log.i(TAG, "view!!!!:" + v);
    }


    @Override
    public boolean onPreviewFrame(byte[] bytes, int width, int height) {
//        deal with the yuv data.
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = 0x00;
//        }
//        Log.i(TAG, "old onPreviewFrame cost :" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public void onSurfaceCreated() {
//        Log.i(TAG, "onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
//        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
    }

    @Override
    public void onSurfaceDestroyed() {
//        Log.i(TAG, "onSurfaceDestroyed");
    }


    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*mStreamStatus.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
                        + "\naudio:" + st

                        reamStatus.audioFps + " fps"
                        + "\nvideo:" + streamStatus.videoFps + " fps");*/
            }
        });
    }

    private class Switcher implements Runnable {
        @Override
        public void run() {

            mMediaStreamingManager.switchCamera();
        }
    }

//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (liveMainFragment.isVisible()) {
//            liveMainFragment.getLiveLayerFragment().onTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (liveMainFragment.isVisible()) {
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                if (fansContributonListFragment.isVisible()) {
//                    FragmentManager manager = getFragmentManager();
//                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.frament_enter_right, R.anim.fragment_close);
//                    transaction.remove(fansContributonListFragment);
//                    transaction.commit();
//                } else {
//                    liveMainFragment.getLiveLayerFragment().onKeyDown(keyCode, event);
//                }
//
//            }
//        } else {
//            finish();
//        }
//
//        return false;
//    }


    @Override
    public void switchCamera() {
        mHandler.removeCallbacks(mSwitcher);
        mHandler.postDelayed(mSwitcher, 100);
    }

    @Override
    public void getFansContributionList() {
//        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.frament_enter_right, R.anim.fragment_close);
////        transaction.add(R.id.content, fansContributonListFragment);
//        transaction.commit();
    }

    @Override
    public void closeLive() {
        isStop = true;
        stopStreaming();
        mMediaStreamingManager.pause();
        Bundle bundle = new Bundle();
        bundle.putString("qnID", qnID);
        bundle.putString("topic", topic);
        bundle.putString("watchers", liveMainFragment.getLiveLayerFragment().getWatchers() + "");
        bfLiveOverFragment = new BFLiveOverFragment();
        bfLiveOverFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.live_fragments, bfLiveOverFragment).commit();
    }

    @Override
    public void setLocation(String address) {

    }

    public void preShareWxCircle(String qnId) {
        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(Constants.SHARE_TITLE)
                .withText(appUser.getNickname() +getResources().getString(R.string.xiaozhu_share_pre_title) + topic + getResources().getString(R.string.xiaozhu_share_after_title))
                .withTargetUrl(com.ddm.live.constants.Constants.SHARE_SERVER_URL + qnId + "/")
                .share();

    }

    public void preShareWX(String qnId) {
        new ShareAction(getActivity()).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                .withMedia(image)
                .withTitle(Constants.SHARE_TITLE)
                .withText(appUser.getNickname() +getResources().getString(R.string.xiaozhu_share_pre_title) + topic + getResources().getString(R.string.xiaozhu_share_after_title))
                .withTargetUrl(com.ddm.live.constants.Constants.SHARE_SERVER_URL + qnId + "/")
                .share();

    }

    @Override
    public void startLive(String subject, String title, String address, int radioId) {
        this.topic=subject;
        if (!mIsReady) {
            toast("请等待相机开启");
        }
        this.radioId = radioId;
        if (!progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.show(getActivity().getFragmentManager(), "");
        }

        presenter.createStream2(subject, title, address);
    }

    @Override
    public void onFinishCreateStream(String streamJson, String streamId, String roomId, String qnId) {
        this.streamID = streamId;
        this.roomID = roomId;
        this.qnID = qnId;
        this.roomID = roomId;
        this.streamJson = streamJson;
        this.isStreamCreatedFinished = true;
        /**
         *  startLive();//并未真正开始，调用startStreaming才真正开始推流（1）未选择分享时，相机处于准备状态，直接推流
         *  (2）选中分享，分享时相机关闭准备状态，返回时需等待相机再次进入准备状态再推流
         */

        startLive();//并未真正开始，调用startStreaming才真正开始推流（1）未选择分享时，相机处于准备状态，直接推流，

//流创建结束检测分享状态
        switch (radioId) {
            case R.id.radio_btn1:
                preShareWX(qnId);
//                Toast.makeText(PreStartLiveActivity.this,"微信分享",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_btn2:
                preShareWxCircle(qnId);
//                Toast.makeText(PreStartLiveActivity.this,"微信朋友圈分享",Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_btn3:
//                progressBarDialogFragment.dismiss();
                break;
            case R.id.radio_btn4:
//                progressBarDialogFragment.dismiss();
                break;
            case R.id.radio_btn5:
//                progressBarDialogFragment.dismiss();
                break;
            default:
                if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance()) && getActivity() != null) {
                    Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                startStreaming();//未选中分享，直接推流
                break;
        }


    }

    //开始直播
    private void startLive() {
        if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        Bundle bundle = new Bundle();
        bundle.putString("roomID", roomID);
        bundle.putString("videoID", qnID);
        bundle.putString("streamID", streamID);
        bundle.putString("topic",topic);
        isWcodeCameraStreamingView.showLiveLayer(bundle);//进入直播界面
        //设置直播地址
        String publishUrlFromServer2 = Config.EXTRA_PUBLISH_JSON_PREFIX + streamJson;
        if (publishUrlFromServer2.startsWith(Config.EXTRA_PUBLISH_URL_PREFIX)) {
            // publish url
            try {
                mProfile.setPublishUrl(publishUrlFromServer2.substring(Config.EXTRA_PUBLISH_URL_PREFIX.length()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (publishUrlFromServer2.startsWith(Config.EXTRA_PUBLISH_JSON_PREFIX)) {
            try {
                mJSONObject = new JSONObject(publishUrlFromServer2.substring(Config.EXTRA_PUBLISH_JSON_PREFIX.length()));
                StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
                mProfile.setStream(stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (getActivity() != null) {
            Toast.makeText(getActivity(), "Invalid Publish Url", Toast.LENGTH_LONG).show();
        }
        mMediaStreamingManager.setStreamingProfile(mProfile);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"分享成功",Toast.LENGTH_SHORT).show();
            if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance()) && getActivity() != null) {
                Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_SHORT).show();
                return;
            }
            startLive();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), platform + " 分享失败", Toast.LENGTH_SHORT).show();
                if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance())) {
                    Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            startLive();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(AppApplication.getInstance(),"分享取消",Toast.LENGTH_SHORT).show();
            if (!NetworkUtils.isNetworkAvailable(AppApplication.getInstance()) && getActivity() != null) {
                Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_SHORT).show();
                return;
            }
            startLive();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
        }

        if (isGranted) {
            preStartLiveFragment.reSetLocation();
        }
    }

    @Override
    public void preStartLiveSwitchCamera() {
        switchCamera();
    }


    @Override
    public void onfailed(String message) {
        if (progressBarDialogFragment != null && progressBarDialogFragment.isAdded()) {
            progressBarDialogFragment.dismiss();
        }
        super.onfaild(message);
    }

    @Override
    public void pauseLive() {

    }

    @Override
    public void masterStopLive() {

    }

    @Override
    public void restartLive() {

    }
}
