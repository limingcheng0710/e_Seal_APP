package com.nerosong.sittingmonitor;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class Fragment2 extends Fragment {

    private boolean isPlayMusic = true;

    @BindView(R.id.tv2BTState)
    TextView tv2BTState;

    @BindView(R.id.tv2SitState)
    TextView tv2SitState;

    @BindView(R.id.sw2Music)
    Switch sw2Music;

    @BindView(R.id.gifviewSit)
    GifImageView gifviewSit;

    GifDrawable gifDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        sw2Music.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    isPlayMusic = true;
                } else {
                    isPlayMusic = false;
                }
            }
        });

        return view;
    }

    @Subscribe
    public void onEvent(Bundle bundle) {
        String value = bundle.getString("data");
        Log.e("6666", "onEvent: " + value);
        if (value!=null && !value.isEmpty()) {
            try {
                showResult(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String value2 = bundle.getString("state");
        Log.e("6666", "onState: " + value);
        if (value2!=null && !value2.isEmpty()) {
            tv2BTState.setText(value2);
        }
    }

    private void showResult(String s) throws IOException {
        /**
         * 1：正
         * 2：左
         * 3：右
         * 4：前
         * 5：后
         * 6：无人
         * *：未知
         */

        switch (s) {
            case "1":
                gifviewSit.setImageResource(R.drawable.sit_0);
                tv2SitState.setText("坐姿正确");
                break;
            case "2":
                tv2SitState.setText("坐姿左倾");
                playMusic("left.wav");
                gifDrawable = new GifDrawable(getResources(), R.drawable.left);
                gifviewSit.setImageDrawable(gifDrawable);
                break;
            case "3":
                tv2SitState.setText("坐姿右倾");
                playMusic("right.wav");
                gifDrawable = new GifDrawable(getResources(), R.drawable.right);
                gifviewSit.setImageDrawable(gifDrawable);
                break;
            case "4":
                tv2SitState.setText("坐姿前倾");
                playMusic("forward.wav");
                gifDrawable = new GifDrawable(getResources(), R.drawable.front);
                gifviewSit.setImageDrawable(gifDrawable);
                break;
            case "5":
                tv2SitState.setText("坐姿后倾");
                playMusic("behind.wav");
                gifDrawable = new GifDrawable(getResources(), R.drawable.back);
                gifviewSit.setImageDrawable(gifDrawable);
                break;
            case "6":
                tv2SitState.setText("无人状态");
                gifviewSit.setImageResource(R.drawable.sit_0);
                break;

            default:
                gifviewSit.setImageResource(R.drawable.sit_0);
                tv2SitState.setText("坐姿正确");
                break;
        }

    }

    private void playMusic(String fileName) {
        if (isPlayMusic) {
            try {

                AssetManager assetManager = getActivity().getAssets();

                AssetFileDescriptor afd = assetManager.openFd(fileName);
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(), afd.getLength());
                player.setLooping(false);
                player.prepare();
                player.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        gifDrawable.recycle();
    }
}
