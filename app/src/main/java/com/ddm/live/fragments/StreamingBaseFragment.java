package com.ddm.live.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.activities.Config;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.components.PresenterComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.models.bean.common.userbeans.SpecUserInfoBean;
import com.ddm.live.presenter.StartLivePresenter;
import com.ddm.live.qiniu.gles.FBO;
import com.ddm.live.qiniu.ui.RotateLayout;
import com.ddm.live.views.iface.IStreamBaseView;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.List;

import io.rong.imlib.model.ChatRoomInfo;

/**
 * Created by Administrator on 2016/11/30.
 */

public class StreamingBaseFragment extends BaseFragment implements View.OnLayoutChangeListener,
        StreamStatusCallback,
        StreamingPreviewCallback,
        SurfaceTextureCallback,
        AudioSourceCallback,
//        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener,
        IStreamBaseView{
    private static final int ZOOM_MINIMUM_WAIT_MILLIS = 33; //ms

    private Context mContext;

    //    protected Button mShutterButton;
    private Button mMuteButton;
    private Button mTorchBtn;
    private Button mCameraSwitchBtn;
    private Button mCaptureFrameBtn;
    private Button mEncodingOrientationSwitcherBtn;
    private Button mFaceBeautyBtn;
    private RotateLayout mRotateLayout;
    StartLivePresenter presenter;
    protected TextView mSatusTextView;
    private TextView mLogTextView;
    private TextView mStreamStatus;
    protected boolean mShutterButtonPressed = false;
    private boolean mIsTorchOn = false;
    private boolean mIsNeedMute = false;
    private boolean mIsNeedFB = false;
    private boolean isEncOrientationPort = true;

    protected static final int MSG_START_STREAMING = 0;
    protected static final int MSG_STOP_STREAMING = 1;
    private static final int MSG_SET_ZOOM = 2;
    private static final int MSG_MUTE = 3;
    private static final int MSG_FB = 4;

//    protected String mStatusMsgContent;

    protected String mLogContent = "\n";

    private View mRootView;

    protected MediaStreamingManager mMediaStreamingManager;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    protected StreamingProfile mProfile;
    protected JSONObject mJSONObject;
    private boolean mOrientationChanged = false;

    protected boolean mIsReady = false;

    private int mCurrentZoom = 0;
    private int mMaxZoom = 0;

    private FBO mFBO = new FBO();

    private Screenshooter mScreenshooter = new Screenshooter();
    private Switcher mSwitcher = new Switcher();
    private EncodingOrientationSwitcher mEncodingOrientationSwitcher = new EncodingOrientationSwitcher();
    protected LiveMainFragment liveMainFragment;
    private int mCurrentCamFacingIndex;
    protected String streamID;
    protected String streamJsonStrFromServer;
    protected String streamJson;
    protected String qnID;
    protected Integer cameraID = 0;
    protected String roomID;
    protected boolean isStreamCreatedFinished = false;
    StreamingProfile.AudioProfile aProfile;
    StreamingProfile.VideoProfile vProfile;
    StreamingProfile.AVProfile avProfile;

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_STREAMING:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // disable the shutter button before startStreaming
//                            setShutterButtonEnabled(false);
                            boolean res = mMediaStreamingManager.startStreaming();
                            mShutterButtonPressed = true;
//                            Log.i(TAG, "该状态:" + res);
                            if (!res) {
                                mShutterButtonPressed = false;
//                                setShutterButtonEnabled(true);
                            }
//                            setShutterButtonPressed(mShutterButtonPressed);
                        }
                    }).start();
                    break;
                case MSG_STOP_STREAMING:
                    if (mShutterButtonPressed) {
                        // disable the shutter button before stopStreaming
//                        setShutterButtonEnabled(false);
                        boolean res = mMediaStreamingManager.stopStreaming();
                        if (!res) {
                            mShutterButtonPressed = true;
//                            setShutterButtonEnabled(true);
                        }
//                        setShutterButtonPressed(mShutterButtonPressed);
                    }
                    break;
                case MSG_SET_ZOOM:
                    mMediaStreamingManager.setZoomValue(mCurrentZoom);
                    break;
                case MSG_MUTE:
                    mIsNeedMute = !mIsNeedMute;
                    mMediaStreamingManager.mute(mIsNeedMute);
                    updateMuteButtonText();
                    break;
                case MSG_FB:
                    mIsNeedFB = !mIsNeedFB;
                    mMediaStreamingManager.setVideoFilterType(mIsNeedFB ?
                            CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY
                            : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
                    updateFBButtonText();
                    break;
                default:
                    Log.e(TAG, "Invalid message");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        aProfile = new StreamingProfile.AudioProfile(44100, 96 * 1024);
        vProfile = new StreamingProfile.VideoProfile(30, 1000 * 1024, 48);
        avProfile = new StreamingProfile.AVProfile(vProfile, aProfile);

        mProfile = new StreamingProfile();

        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH3)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
//                .setPreferredVideoEncodingSize(960, 544)
                .setEncodingSizeLevel(Config.ENCODING_LEVEL)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
                .setAVProfile(avProfile)
                .setDnsManager(getMyDnsManager())
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
//                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

        CameraStreamingSetting.CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = cameraFacingId.ordinal();
        mCameraStreamingSetting = new CameraStreamingSetting();

        if (cameraID == 1) {
            mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                    .setContinuousFocusModeEnabled(true)
                    .setRecordingHint(false)
                    .setCameraFacingId(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK)
                    .setBuiltInFaceBeautyEnabled(true)
//                    .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                    .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                    .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                    .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.5f, 0.5f, 0.5f))
                    .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);
        } else {
            mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                    .setContinuousFocusModeEnabled(true)
                    .setRecordingHint(false)
                    .setCameraFacingId(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)
                    .setBuiltInFaceBeautyEnabled(true)//开启内置美颜
//                    .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                    .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                    .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                    .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                    .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);
        }

        mIsNeedFB = true;
        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mIsReady = false;
        mShutterButtonPressed = false;
        mHandler.removeCallbacksAndMessages(null);
//        mMediaStreamingManager.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
    }

    protected void setShutterButtonPressed(final boolean pressed) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButtonPressed = pressed;
//                mShutterButton.setPressed(pressed);
            }
        });
    }

    protected void setShutterButtonEnabled(final boolean enable) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mShutterButton.setFocusable(enable);
//                mShutterButton.setClickable(enable);
//                mShutterButton.setEnabled(enable);
            }
        });
    }

    protected void startStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_START_STREAMING), 50);
    }

    protected void stopStreaming() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_STOP_STREAMING), 50);
    }

    @Override
    public boolean onRecordAudioFailedHandled(int err) {
        mMediaStreamingManager.updateEncodingType(AVCodecType.SW_VIDEO_CODEC);
        mMediaStreamingManager.startStreaming();
        return true;
    }

    @Override
    public boolean onRestartStreamingHandled(int err) {
        Log.i(TAG, "onRestartStreamingHandled");
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        Camera.Size size = null;
        if (list != null) {
            for (Camera.Size s : list) {
                if (s.height >= 480) {
                    size = s;
                    break;
                }
            }
        }
//        Log.e(TAG, "selected size :" + size.width + "x" + size.height);
        return size;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.i(TAG, "view!!!!:" + v);
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
        if (getActivity() == null) return;
        mFBO.initialize(getActivity());
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        Log.i(TAG, "onSurfaceDestroyed");
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        // newTexId should not equal with texId. texId is from the SurfaceTexture.
        // Otherwise, there is no filter effect.
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
//        Log.i(TAG, "onDrawFrame texId:" + texId + ",newTexId:" + newTexId + ",texWidth:" + texWidth + ",texHeight:" + texHeight);
        return newTexId;
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int size, long tsInNanoTime, boolean eof) {
//        for (int i = 0; i < size; i++) {
//            byteBuffer.put(i, (byte) 0x00);
//        }
    }

    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStreamStatus.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
                        + "\naudio:" + streamStatus.audioFps + " fps"
                        + "\nvideo:" + streamStatus.videoFps + " fps");
            }
        });
    }

    private void setTorchEnabled(final boolean enabled) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String flashlight = enabled ? getString(R.string.flash_light_off) : getString(R.string.flash_light_on);
//                mTorchBtn.setText(flashlight);
            }
        });
    }

    Runnable modifyStreamStateRunnable = new Runnable() {
        @Override
        public void run() {
            presenter.modifyStreamStateAsLive(qnID);//修改流状态为直播状态
//            Log.e("cxx", "该状态");
        }
    };

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
//        Log.i(TAG, "StreamingState streamingState:" + streamingState + ",extra:" + extra);

        switch (streamingState) {
            case PREPARING:
//                mStatusMsgContent = getString(R.string.string_state_preparing);
//                System.out.println("准备");
                break;
            case READY:
                mIsReady = true;
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
//                mStatusMsgContent = getString(R.string.string_state_ready);
                // start streaming when READY
                if (isStreamCreatedFinished) {//判断已经成功创建流（第一次开启预览为创建流之前也会进入监听到ready）
                    startStreaming();//分享回来后等待相机再次处于准备好状态再推流
                }
                break;
            case CONNECTING:
//                mStatusMsgContent = getString(R.string.string_state_connecting);
//                System.out.println("连接");
                break;
            case STREAMING:
//                mStatusMsgContent = getString(R.string.string_state_streaming);
//                setShutterButtonEnabled(true);
//                setShutterButtonPressed(true);
//                System.out.println("Streaming");
                mHandler.postDelayed(modifyStreamStateRunnable, 5000);//5秒后执行
                break;
            case SHUTDOWN:
//                setShutterButtonEnabled(true);
//                setShutterButtonPressed(false);
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreaming();
                }
//                System.out.println("关闭");
                break;
            case IOERROR:
                mLogContent += "IOERROR\n";
//                mStatusMsgContent = getString(R.string.string_state_ready);
//                setShutterButtonEnabled(true);
                break;
            case UNKNOWN:
//                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case SENDING_BUFFER_EMPTY:
                toast("流为空");
                break;
            case SENDING_BUFFER_FULL:
                break;
            case AUDIO_RECORDING_FAIL:
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case DISCONNECTED:
                mLogContent += "DISCONNECTED\n";
                break;
            case INVALID_STREAMING_URL:
                Log.e(TAG, "Invalid streaming url:" + extra);
                break;
            case UNAUTHORIZED_STREAMING_URL:
                Log.e(TAG, "Unauthorized streaming url:" + extra);
                mLogContent += "Unauthorized Url\n";
                break;
            case CAMERA_SWITCHED:
//                mShutterButtonPressed = false;
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");
                final int currentCamId = (Integer) extra;
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateCameraSwitcherButtonText(currentCamId);
                    }
                });
                break;
            case TORCH_INFO:
                if (extra != null) {
                    final boolean isSupportedTorch = (Boolean) extra;
                    Log.i(TAG, "isSupportedTorch=" + isSupportedTorch);
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isSupportedTorch) {
//                                mTorchBtn.setVisibility(View.VISIBLE);
                            } else {
//                                mTorchBtn.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
        }
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mLogTextView != null) {
//                    mLogTextView.setText(mLogContent);
//                }
//                mSatusTextView.setText(mStatusMsgContent);
//            }
//        });
    }

//    private void initUIs() {
//        mRootView = findViewById(R.id.content);
//        mRootView.addOnLayoutChangeListener(this);
//
//        mMuteButton = (Button) findViewById(R.id.mute_btn);
//        mShutterButton = (Button) findViewById(R.id.toggleRecording_button);
//        mTorchBtn = (Button) findViewById(R.id.torch_btn);
//        mCameraSwitchBtn = (Button) findViewById(R.id.camera_switch_btn);
//        mCaptureFrameBtn = (Button) findViewById(R.id.capture_btn);
//        mFaceBeautyBtn = (Button) findViewById(R.id.fb_btn);
//        mSatusTextView = (TextView) findViewById(R.id.streamingStatus);
//
//        mLogTextView = (TextView) findViewById(R.id.log_info);
//        mStreamStatus = (TextView) findViewById(R.id.stream_status);
//
//        mFaceBeautyBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mHandler.hasMessages(MSG_FB)) {
//                    mHandler.sendEmptyMessage(MSG_FB);
//                }
//            }
//        });
//
//        mMuteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mHandler.hasMessages(MSG_MUTE)) {
//                    mHandler.sendEmptyMessage(MSG_MUTE);
//                }
//            }
//        });
//
//        mShutterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mShutterButtonPressed) {
//                    stopStreaming();
//                } else {
//                    startStreaming();
//                }
//            }
//        });
//
//        mTorchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!mIsTorchOn) {
//                            mIsTorchOn = true;
//                            mMediaStreamingManager.turnLightOn();
//                        } else {
//                            mIsTorchOn = false;
//                            mMediaStreamingManager.turnLightOff();
//                        }
//                        setTorchEnabled(mIsTorchOn);
//                    }
//                }).start();
//            }
//        });
//
//        mCameraSwitchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mHandler.removeCallbacks(mSwitcher);
//                mHandler.postDelayed(mSwitcher, 100);
//            }
//        });
//
//        mCaptureFrameBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mHandler.removeCallbacks(mScreenshooter);
//                mHandler.postDelayed(mScreenshooter, 100);
//            }
//        });
//
//
//        mEncodingOrientationSwitcherBtn = (Button) findViewById(R.id.orientation_btn);
//        mEncodingOrientationSwitcherBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.removeCallbacks(mEncodingOrientationSwitcher);
//                mHandler.post(mEncodingOrientationSwitcher);
//            }
//        });
//
//        SeekBar seekBarBeauty = (SeekBar) findViewById(R.id.beautyLevel_seekBar);
//        seekBarBeauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
//                fbSetting.beautyLevel = progress / 100.0f;
//                fbSetting.whiten = progress / 100.0f;
//                fbSetting.redden = progress / 100.0f;
//
//                mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//
//        initButtonText();
//    }

    private void initButtonText() {
        updateFBButtonText();
        updateCameraSwitcherButtonText(mCameraStreamingSetting.getReqCameraId());
//        mCaptureFrameBtn.setText("Capture");
        updateFBButtonText();
        updateMuteButtonText();
        updateOrientationBtnText();
    }

    private void updateOrientationBtnText() {
        if (isEncOrientationPort) {
//            mEncodingOrientationSwitcherBtn.setText("Land");
        } else {
//            mEncodingOrientationSwitcherBtn.setText("Port");
        }
    }

    protected void setFocusAreaIndicator() {
        if (mRotateLayout == null) {
//            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
//            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
//                    mRotateLayout.findViewById(R.id.focus_indicator));
        }
    }

    private void updateFBButtonText() {
        if (mFaceBeautyBtn != null) {
//            mFaceBeautyBtn.setText(mIsNeedFB ? "FB Off" : "FB On");
        }
    }

    private void updateMuteButtonText() {
        if (mMuteButton != null) {
//            mMuteButton.setText(mIsNeedMute ? "Unmute" : "Mute");
        }
    }

    private void updateCameraSwitcherButtonText(int camId) {
        if (mCameraSwitchBtn == null) {
            return;
        }
        if (camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mCameraSwitchBtn.setText("Back");
        } else {
//            mCameraSwitchBtn.setText("Front");
        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "Save frame to:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            if (getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private static DnsManager getMyDnsManager() {
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

    private CameraStreamingSetting.CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();

            CameraStreamingSetting.CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);
        }
    }

    private class EncodingOrientationSwitcher implements Runnable {

        @Override
        public void run() {
            Log.i(TAG, "isEncOrientationPort:" + isEncOrientationPort);
            stopStreaming();
            mOrientationChanged = !mOrientationChanged;
            isEncOrientationPort = !isEncOrientationPort;
            mProfile.setEncodingOrientation(isEncOrientationPort ? StreamingProfile.ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
            mMediaStreamingManager.setStreamingProfile(mProfile);

            getActivity().setRequestedOrientation(isEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mMediaStreamingManager.notifyActivityOrientationChanged();
            updateOrientationBtnText();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), Config.HINT_ENCODING_ORIENTATION_CHANGED,
                        Toast.LENGTH_SHORT).show();
            }

//            Log.i(TAG, "EncodingOrientationSwitcher -");
        }
    }

    private class Screenshooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            mMediaStreamingManager.captureFrame(100, 100, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();
        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();
        PresenterComponent presenterComponent = DaggerPresenterComponent.builder().appComponent(appComponent).networkingComponent(networkingComponent).presenterModule(new PresenterModule()).build();
        presenter = presenterComponent.getStartLivePresenter();
        presenter.attachIStreamBaseView(this);
    }

    @Override
    public void getStreamChatRoomUser(ChatRoomInfo userArray) {

    }

    @Override
    public void getStreamUserInfoResult(List<SpecUserInfoBean> userInfoArrayList, int code) {

    }

    @Override
    public void quitStreamRoomResult(String result) {

    }

    @Override
    public void modifyWatchedNumResult() {

    }

    @Override
    public void modifyStreamStateAsLive(boolean streamState) {
                    liveMainFragment.getLiveLayerFragment().modifyStreamStateAsLive(true);
    }

    @Override
    public void onfailed(String message) {

    }

    @Override
    public void toast(String message) {
        super.toast(message);
    }
}
