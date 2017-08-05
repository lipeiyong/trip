package net.lvtushiguang.trip.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import net.lvtushiguang.trip.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xinxin on 16/4/6.
 */
public class VideoPlayView extends RelativeLayout implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnVideoSizeChangedListener,
        TextureView.SurfaceTextureListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    //MediaPlayer 内部所有可能出现的状态
    private static final int STATE_ERROR = -1;//错误状态
    private static final int STATE_IDLE = 0;//闲置状态
    private static final int STATE_INITIALIZED = 1;//初始化状态
    private static final int STATE_PREPARING = 2;//正在准备状态
    private static final int STATE_PREPARED = 3;//准备完成状态
    private static final int STATE_PLAYING = 4;//正在播放状态
    private static final int STATE_PAUSE = 5;//暂停状态
    private static final int STATE_COMPLETED = 6;//播放完成状态
    private static final int STATE_END = 7;

    private static final int SHOW_MSG = 000;//控制界面显示信号
    private static final int HIDE_MSG = 111;//控制界面隐藏信号
    private static final int SHOW_DELETE_TIME = 3000;
    private static final int PROGRESS_MSG = 222;//更新进度和时间的消息

    private static final int DEFAULT_WIDTH = 1080;
    private static final int DEFAULT_HEIGHT = 1920;

    public static final int SCREEN_ORIENTATION_LANDSCAPE = 0x1;//横屏
    public static final int SCREEN_ORIENTATION_PORTRAIT = 0x2;//竖屏


    //记录 mediaplayer 当前处于的状态, 默认是 idle 状态
    private int mCurrentState = STATE_IDLE;
    private MediaPlayer mediaPlayer;
    //记录缓冲进度
    private int mSecProgress;
    private TextureView textureView;
    private Surface mSurface;
    private String url;

    //控制界面
    private LinearLayout controller_layout;
    //播放暂停按钮
    private ImageView pause;
    //当前播放的时间
    private TextView media_current_time;
    //视频总时长
    private TextView media_totle_time;
    //点击全屏按钮
    private ImageView expand;
    //点击退出全屏按钮
    private ImageView shrink;
    //等待进度框布局
    private FrameLayout progress_bar_layout;
    //进度条
    private SeekBar mProgressSeekBar;
    private TimerTask mTimerTask;
    private Timer mUpdateTimer;
    private MediaPlayerImpl mediaPlayerListenr;

    public VideoPlayView(Context context) {
        this(context, null);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(DEFAULT_WIDTH, widthMeasureSpec);
        int height = getDefaultSize(DEFAULT_HEIGHT, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void initView(Context mContext) {
        LayoutInflater.from(mContext).inflate(R.layout.play_and_controller_view, this);
        textureView = (TextureView) findViewById(R.id.play_view);
        controller_layout = (LinearLayout) findViewById(R.id.controller_layout);
        pause = (ImageView) findViewById(R.id.btn_pause);
        media_current_time = (TextView) findViewById(R.id.media_currentTime);
        media_totle_time = (TextView) findViewById(R.id.durtain_text);
        expand = (ImageView) findViewById(R.id.btn_expand);
        shrink = (ImageView) findViewById(R.id.btn_shrink);
        mProgressSeekBar = (SeekBar) findViewById(R.id.media_progress);
        progress_bar_layout = (FrameLayout) findViewById(R.id.progress_bar_layout);
        textureView.setSurfaceTextureListener(this);

        pause.setOnClickListener(this);
        shrink.setOnClickListener(this);
        expand.setOnClickListener(this);
        mProgressSeekBar.setOnSeekBarChangeListener(this);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_MSG:
                case HIDE_MSG:
                    showOrHidenController(msg.what);
                    setPlayBtn();
                    break;
                case PROGRESS_MSG:
                    updatePlayTime();
                    updateProgress();
                    break;
            }
        }
    };

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMediaPlayerListenr(MediaPlayerImpl mediaPlayerListenr) {
        this.mediaPlayerListenr = mediaPlayerListenr;
    }

    /**
     * 判断当前状态是否可以进行播放,暂停,获取进度等操作
     *
     * @return
     */
    private boolean isPlayState() {
        return (mediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_END &&
                mCurrentState != STATE_PREPARING);
    }

    /**
     * 将 mediaplayer 对象 release 掉
     */
    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            mCurrentState = STATE_END;
        }
    }

    /**
     * 发送消息是否显示控制界面
     */
    private void sendShowOrHideMsg() {
        mHandler.sendEmptyMessage(SHOW_MSG);
        mHandler.removeMessages(HIDE_MSG);
        mHandler.sendEmptyMessageDelayed(HIDE_MSG, SHOW_DELETE_TIME);
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mCurrentState = STATE_END;
            setPlayBtn();
            mHandler.removeCallbacksAndMessages(null);
            textureView.setVisibility(GONE);
            Log.i("YY", "被停了");
        }
    }

    /**
     * 播放
     */
    public void play() {
        if (isPlayState()) {
            mediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            sendShowOrHideMsg();
            Log.i("TAG", "PLAYING");
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (isPlayState()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mCurrentState = STATE_PAUSE;
            }

        }
    }

    /**
     * 点击按钮暂停还是播放
     */
    private void playOrPause() {
        if (isPlayState()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mCurrentState = STATE_PAUSE;
            } else {
                mediaPlayer.start();
                mCurrentState = STATE_PLAYING;
            }
        }
        setPlayBtn();
    }

    /**
     * 获取视频总时长
     *
     * @return
     */
    public int getDuration() {
        if (isPlayState()) {
            return mediaPlayer.getDuration();
        }
        return -1;
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getCuttentPosition() {
        if (isPlayState()) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 拖动进度条到指定位置
     *
     * @param msec
     */
    public void seekTo(int msec) {
        if (isPlayState()) {
            mediaPlayer.seekTo(msec);
        }
    }

    /**
     * 判断当前是否在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlayState() && mediaPlayer.isPlaying();
    }

    /**
     * 更新播放按钮的状态
     */
    private void setPlayBtn() {
        if (isPlaying()) {
            pause.setImageResource(R.drawable.ic_pause_circle_filled_white_24dp);
        } else {
            pause.setImageResource(R.drawable.biz_video_play);
        }
    }

    /**
     * 设置控制横屏竖屏按钮的状态
     *
     * @param isExpend
     */
    public void setExpendBtn(boolean isExpend) {
        if (isExpend) {
            expand.setVisibility(GONE);
            shrink.setVisibility(VISIBLE);
        } else {
            expand.setVisibility(VISIBLE);
            shrink.setVisibility(GONE);
        }
    }

    /**
     * 转换时间格式
     *
     * @param time
     * @return
     */
    private String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    /**
     * 设置进度条
     *
     * @param progress
     * @param secondProgress
     */
    public void setProgressBar(int progress, int secondProgress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (secondProgress < 0)
            secondProgress = 0;
        if (secondProgress > 100)
            secondProgress = 100;
        mProgressSeekBar.setProgress(progress);
        mProgressSeekBar.setSecondaryProgress(secondProgress);
    }

    /**
     * 更新进度条以及当前播放时间
     */
    private void updatePlayTime() {
        int totleTiem = getDuration();//视频总时长
        int currentTime = getCuttentPosition();//挡墙播放到的时间
        media_current_time.setText(formatPlayTime(currentTime));
        media_totle_time.setText(formatPlayTime(totleTiem));
    }

    private void updateProgress() {
        int totleTime = getDuration();//视频总时长
        int currentTime = getCuttentPosition();//挡墙播放到的时间
        int progress = currentTime * 100 / totleTime;
        setProgressBar(progress, mSecProgress);
    }

    /**
     * 打开视频播放界面
     */
    public void openVideo() {

        try {
            release();
            progress_bar_layout.setVisibility(VISIBLE);
            textureView.setVisibility(VISIBLE);
            mediaPlayer = new MediaPlayer();//初始化,进入 idle 状态
            mCurrentState = STATE_IDLE;
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.setDataSource(url);//进入到初始化状态
            mCurrentState = STATE_INITIALIZED;
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//格式
            mediaPlayer.setSurface(mSurface);
            mediaPlayer.setLooping(true);//是否进行循环播放
            mediaPlayer.prepareAsync();//进入到正在准备状态
            mCurrentState = STATE_PREPARING;
        } catch (IOException e) {
            e.printStackTrace();
            mCurrentState = STATE_ERROR;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            mCurrentState = STATE_ERROR;
        }
    }

    /**
     * 显示或隐藏控制界面
     */
    private void showOrHidenController(int tag) {
        if (tag == SHOW_MSG) {
            controller_layout.setVisibility(VISIBLE);
            Log.e("TAG", "SHOW_MSG");
        } else {
            controller_layout.setVisibility(GONE);
            Log.e("TAG", "GONE");
        }
    }

    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void resetUpdateTimer() {
        stopUpdateTimer();
        mUpdateTimer = new Timer();
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(PROGRESS_MSG);
            }
        };
        mUpdateTimer.schedule(mTimerTask, 0, PROGRESS_MSG);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mCurrentState = STATE_PREPARED;
        sendShowOrHideMsg();
        resetUpdateTimer();
        //  progress_bar_layout.setVisibility(GONE);
        play();
    }

    /**
     * 当前设置为循环状态,所以这里不做处理
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mCurrentState = STATE_COMPLETED;
    }

    //缓冲进度监听
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mSecProgress = percent;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mCurrentState = STATE_ERROR;
        mediaPlayerListenr.onError();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                if (progress_bar_layout.isShown()) {
                    progress_bar_layout.setVisibility(GONE);
                }
                return true;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
                if (!progress_bar_layout.isShown()) {
                    progress_bar_layout.setBackgroundResource(android.R.color.transparent);
                    progress_bar_layout.setVisibility(VISIBLE);
                }
                return true;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲结束
                if (progress_bar_layout.isShown()) {
                    progress_bar_layout.setVisibility(GONE);
                }
                return true;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.i("TAG", "未知错误");
                return true;
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        Log.i("TAG", "播放界面可见");
        openVideo();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // stop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.i("TAG", "界面被销毁");
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_expand:
                mediaPlayerListenr.onExpend();
                setExpendBtn(true);
                break;
            case R.id.btn_pause:
                playOrPause();
                break;
            case R.id.btn_shrink:
                mediaPlayerListenr.onShrik();
                setExpendBtn(false);
                break;
        }
        sendShowOrHideMsg();
    }

    /**
     * 当按返回键时更新屏幕方向
     *
     * @param orientation
     */
    public void setScreenOrientation(int orientation) {
        switch (orientation) {
            case SCREEN_ORIENTATION_LANDSCAPE:
                setExpendBtn(true);
                break;
            case SCREEN_ORIENTATION_PORTRAIT:
                setExpendBtn(false);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int time = progress * getDuration() / 100;
            seekTo(time);
            updatePlayTime();
            updateProgress();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sendShowOrHideMsg();
        return true;
    }

    public interface MediaPlayerImpl {

        void onError();

        void onExpend();

        void onShrik();
    }
}
