package com.ddm.live.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddm.live.AppApplication;
import com.ddm.live.R;
import com.ddm.live.R2;
import com.ddm.live.inject.components.AppComponent;
import com.ddm.live.inject.components.DaggerNetworkingComponent;
import com.ddm.live.inject.components.DaggerPresenterComponent;
import com.ddm.live.inject.components.NetworkingComponent;
import com.ddm.live.inject.modules.PresenterModule;
import com.ddm.live.presenter.LiveViewPresenter;
import com.ddm.live.ui.widget.LoadingView;
import com.ddm.live.utils.Utils;
import com.ddm.live.utils.camera.util.DisplayUtil;
import com.ddm.live.views.iface.IBaseFragmentLayerView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cxx on 2016/12/8.
 */

public class LiveViewFragment extends BaseFragment implements IBaseFragmentLayerView, LiveViewPresenter.LiveViewListener {

    private static final int MESSAGE_ID_RECONNECTING = 0x01;

    private PLMediaPlayer mMediaPlayer;

    private AVOptions mAVOptions;
    public static Bitmap bitmap = null;
    private int mSurfaceWidth = 0;
    private int mSurfaceHeight = 0;
    private boolean mIsCompleted = false;
    private String mVideoPath = null;
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;
    private String TAG = "player";
    private Toast mToast = null;
    private WatcherMainFragment watcherMainFragment;
    private String uname;
    private String headimgUrl;
    private String streamStatus;
    private String avatarSmall;
    private boolean isLive;
    private int qnId;
    private Integer isLiveStreaming;
    private RelativeLayout videoView;
    @BindView(R2.id.LoadingView)
    View mLoadingView;
    @BindView(R2.id.tip)
    TextView tip;
    @BindView(R2.id.SurfaceView)
    SurfaceView mSurfaceView;
    //    @BindView(R2.id.indicator)
//    AVLoadingIndicatorView indicatorView;
    @BindView(R.id.loadView)
    LoadingView loadingView;
    @Inject
    LiveViewPresenter liveViewPresenter;

    public void attachFragment(WatcherMainFragment watcherMainFragment) {
        this.watcherMainFragment = watcherMainFragment;
    }

    @BindView(R2.id.loading_img)
    ImageView loadingImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_player, container, false);
        ButterKnife.bind(this, view);
        mSurfaceView.getHolder().addCallback(mCallback);
        Bundle bundle = getArguments();
        mVideoPath = bundle.getString("mVideoPath");
        headimgUrl = bundle.getString("headimgUrl");
        streamStatus = bundle.getString("streamStatus");
        isLive = bundle.getBoolean("isLive");
        uname = bundle.getString("uname");
        qnId = bundle.getInt("id");
        avatarSmall = bundle.getString("avatarsmall");

        mAVOptions = new AVOptions();
        isLiveStreaming = bundle.getInt("streamStatus", 1);//0为直播，1为点播

        // the unit of timeout is ms
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        // Some optimization with buffering mechanism when be set to 1
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, isLiveStreaming);
        if (isLiveStreaming == 0) {
            mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
        }

        // 1 -> hw codec enable, 0 -> disable [recommended]
//        int codec = bundle.getInt("mediaCodec", 0);//解码方式，软解为0，硬解为1
        int codec = 0;


        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, codec);

        // whether start play automatically after prepared, default value is 1
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        videoView = (RelativeLayout) view.findViewById(R.id.video_main);
        ViewGroup.LayoutParams lp = videoView.getLayoutParams();
//      重绘播放窗口大小
        int[] realScreenSize = DisplayUtil.getScreentHeight(getActivity());
        lp.width = realScreenSize[0];
        lp.height = realScreenSize[1];
        //设置加载背景图片
        if (headimgUrl == null || headimgUrl.isEmpty() || bitmap == null) {
            loadingImage.setImageResource(R.mipmap.bg_error);
        } else if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(bitmap);
            loadingImage.setVisibility(View.VISIBLE);
            loadingImage.setImageDrawable(drawable);
        }
//        indicatorView.setIndicatorColor(R.color.purple);
        loadingView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void closeLive() {
        LiveViewFragment.bitmap = null;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        mIsStopped = true;
        mMediaPlayer = null;
//        Intent intent=new Intent(getActivity(), HomeActivity.class);
//        getActivity().startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.activity_close_form_bottom);
    }

    @Override
    public void pauseLive() {
        tip.setVisibility(View.VISIBLE);
    }

    @Override
    public void restartLive() {
        tip.setVisibility(View.INVISIBLE);
    }

    @Override
    public void masterStopLive() {
        close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPaused = true;
    }


    public void releaseWithoutStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(null);
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void prepare() {

        if (mMediaPlayer != null) {
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            return;
        }

        try {
            mMediaPlayer = new PLMediaPlayer(getActivity(), mAVOptions);

            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
//            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);

            // set replay if completed
            // mMediaPlayer.setLooping(true);

            mMediaPlayer.setWakeMode(getActivity().getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer.setDataSource(mVideoPath);
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
            mMediaPlayer.prepareAsync();

            watcherMainFragment.getWatcherLayerFragment().setOnProgressChangeListener(new WatcherLayerFragment.OnProgressChangeListener() {
                @Override
                public void onProgressChange(long currTime) {
                    mMediaPlayer.seekTo(currTime);
                }
            });
            watcherMainFragment.getWatcherLayerFragment().setOnPlayPauseChangeListener(new WatcherLayerFragment.OnPlayPauseChangeListener() {
                @Override
                public void onPlayPauseChange(boolean play) {
                    if(play)
                        mMediaPlayer.start();
                    else
                        mMediaPlayer.pause();
                }
            });
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回看结束进行重播
     */
    private void rePlay () {
        prepare();
        bitmap = BFPlayOverFragment.bitmap;
        BFPlayOverFragment.bitmap = null;
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragments, watcherMainFragment.getWatcherLayerFragment()).commit();
//        watcherMainFragment.getWatcherLayerFragment().clearProgress();
    }

    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            prepare();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth = width;
            mSurfaceHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // release();
            releaseWithoutStop();
        }
    };

    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        public void onVideoSizeChanged(PLMediaPlayer mp, int width, int height) {
            Log.i(TAG, "onVideoSizeChanged, width = " + width + ",height = " + height);
            // resize the display window to fit the screen
            if (width != 0 && height != 0) {
                float ratioW = (float) width / (float) mSurfaceWidth;
                float ratioH = (float) height / (float) mSurfaceHeight;
                float ratio = Math.max(ratioW, ratioH);
                width = (int) Math.ceil((float) width / ratio);
                height = (int) Math.ceil((float) height / ratio);
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(width, height);
//                layout.gravity = Gravity.CENTER;

                mSurfaceView.setLayoutParams(layout);
            }
        }
    };

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp) {
//            Log.i(TAG, "On Prepared !");
            mMediaPlayer.start();
            watcherMainFragment.getWatcherLayerFragment().setTotalTime(mMediaPlayer.getDuration()/1000);
            watcherMainFragment.getWatcherLayerFragment().sendProgressChangeMsg();
            mIsStopped = false;
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
//            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    loadingView.setVisibility(View.VISIBLE);
                    watcherMainFragment.getWatcherLayerFragment().removeProgressChangeMsg();
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    mLoadingView.setVisibility(View.GONE);
                    loadingImage.setVisibility(View.GONE);
//                    indicatorView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    watcherMainFragment.getWatcherLayerFragment().sendProgressChangeMsg();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
            Log.d(TAG, "onBufferingUpdate: " + percent + "%");
        }
    };

    /**
     * Listen the event of playing complete
     * For playing local file, it's called when reading the file EOF
     * For playing network stream, it's called when the buffered bytes played over
     * <p>
     * If setLooping(true) is called, the player will restart automatically
     * And ｀onCompletion｀ will not be called
     */
    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            if (isLiveStreaming.equals(0)) {//直播
                liveViewPresenter.getStreamStatus(String.valueOf(qnId));
            } else {
                close();
            }

        }
    };

    public void close() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mIsStopped = true;
            mMediaPlayer = null;
            BFPlayOverFragment.bitmap = bitmap;
            LiveViewFragment.bitmap = null;
            watcherMainFragment.getWatcherLayerFragment().quitLiveRoom();
            watcherMainFragment.getWatcherLayerFragment().getBFPlayOverFragment().setOnReviewListener(new BFPlayOverFragment.OnReviewListener() {
                @Override
                public void onReview() {
                    rePlay();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
//            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToastTips("视频加载异常!");
//                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToastTips("视频加载异常!");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToastTips("视频加载异常!");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
//                    showToastTips("Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToastTips("视频加载异常!");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
//                    showToastTips("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    showToastTips("当前网络状况不佳!");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
//                    showToastTips("Unauthorized Error !");
                    showToastTips("视频加载异常!");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToastTips("当前网络状况不佳!");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
//                    showToastTips("视频加载超时!");
//                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    showToastTips("视频加载异常!");
                    break;
                default:
                    showToastTips("视频加载异常!");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
//            release();
            if (isNeedReconnect) {
                release();
                sendReconnectMessage();
            }

            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return false;
        }
    };

    @Override
    public void getStreamStatus(Integer status) {
        if (status.equals(0)) {
            //重连
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDisplay(mSurfaceView.getHolder());
                mMediaPlayer.setDataSource(mVideoPath);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            close();
        }
    }

    @Override
    public void onfailed() {

    }

    private void showToastTips(final String tips) {
        if (mIsActivityPaused) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                if (getActivity() != null) {
                    mToast = Toast.makeText(getActivity(), tips, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
    }

    private void sendReconnectMessage() {
//        showToastTips("正在火速救场...");
        mLoadingView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
                return;
            }
            if (getActivity() != null && !Utils.isNetworkAvailable(getActivity())) {
                sendReconnectMessage();
                return;
            }
            prepare();
        }
    };

    @Override
    public void setupFragmentComponent() {
        AppComponent appComponent = AppApplication.get(getActivity()).getAppComponent();

        NetworkingComponent networkingComponent = DaggerNetworkingComponent.create();

        DaggerPresenterComponent.builder()
                .appComponent(appComponent)
                .networkingComponent(networkingComponent)
                .presenterModule(new PresenterModule()).build().inject(this);

        liveViewPresenter.attachView(this);
    }
}
