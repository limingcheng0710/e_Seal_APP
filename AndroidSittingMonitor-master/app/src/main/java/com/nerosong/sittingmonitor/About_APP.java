package com.nerosong.sittingmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class About_APP extends AppCompatActivity {


    @BindView(R.id.about_to_set)
    ImageView aboutToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.about_to_set)
    public void onViewClicked() {

        //setResult(2001);
        finish();
    }
}
