package com.nerosong.sittingmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInformation extends AppCompatActivity {

    @BindView(R.id.information_to_set)
    ImageButton informationToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.information_to_set)
    public void onViewClicked() {
        finish();
    }
}
