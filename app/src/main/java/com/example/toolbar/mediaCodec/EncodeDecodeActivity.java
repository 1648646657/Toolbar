package com.example.toolbar.mediaCodec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.toolbar.R;

import java.io.IOException;
import java.nio.ByteBuffer;

public class EncodeDecodeActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Button mButton;
    private Button mButton1;
    private AsyncDecode mAsyncDecode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode_decode);
        mSurfaceView = findViewById(R.id.video_surfaceView);
        mButton = findViewById(R.id.video_button);
        mButton1 = findViewById(R.id.video_button1);
        mAsyncDecode = new AsyncDecode();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncDecode.start();
            }
        });
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncDecode.reStart();
            }
        });
    }

    class AsyncDecode {
        MediaFormat mediaFormat;
        MediaCodec mediaCodec;
        MyExtractor extractor;
        long mStartMs = -1;


        public AsyncDecode() {
            try {
                //解析视频，拿到 mediaformat
                extractor = new MyExtractor("/sdcard/DCIM/Camera/video.mp4");
                Log.d("lzx", "AsyncDecode: path = "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
                        +"/Camera/video.mp4");
                mediaFormat = (extractor.getVideoFormat());
                String mime = mediaFormat.getString(MediaFormat.KEY_MIME);
                extractor.selectTrack(extractor.getVideoTrackId());
                mediaCodec = MediaCodec.createDecoderByType(mime);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void reStart(){
            mediaCodec.reset();
        }

        /**
         * 数据的时间戳对齐
         **/
        private void sleepRender(MediaCodec.BufferInfo info, long startMs) {
            /**
             * 注意这里是以 0 为出事目标的，info.presenttationTimes 的单位为微秒
             * 这里用系统时间来模拟两帧的时间差
             */
            long ptsTimes = info.presentationTimeUs / 1000;
            long systemTimes = System.currentTimeMillis() - startMs;
            long timeDifference = ptsTimes - systemTimes;
            Log.d("lzx", "sleepRender: ptsTimes = "+ptsTimes +"---"+"systemTimes = "+systemTimes
                    +"---"+"System.currentTimeMillis() = "+System.currentTimeMillis()
                    +"---"+"startMs = "+startMs);
            // 如果当前帧比系统时间差快了，则延时以下
            if (timeDifference > 0) {
                try {
                    Thread.sleep(timeDifference);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        private void start() {
            //异步解码
            mediaCodec.setCallback(new MediaCodec.Callback() {
                @Override
                public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
                    ByteBuffer inputBuffer = codec.getInputBuffer(index);
                    int size = extractor.readBuffer(inputBuffer);
                    if (size >= 0) {
                        codec.queueInputBuffer(
                                index,
                                0,
                                size,
                                extractor.getCurSampleTime(),
                                extractor.getCurSampleFlags()
                        );
//                        handler.sendEmptyMessage(1);
                    } else {
                        //结束
                        codec.queueInputBuffer(
                                index,
                                0,
                                0,
                                0,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        );
                    }
                }

                @Override
                public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
                    if (mStartMs == -1) {
                        mStartMs = System.currentTimeMillis();
                    }
                    //矫正pts
                    sleepRender(info, mStartMs);
                    mediaCodec.releaseOutputBuffer(index, true);
                }

                @Override
                public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
                    codec.reset();
                }

                @Override
                public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

                }
            });
            //需要在 setCallback 之后，配置 configure
            mediaCodec.configure(mediaFormat, mSurfaceView.getHolder().getSurface(), null, 0);
            //开始解码
            mediaCodec.start();
        }
    }


}