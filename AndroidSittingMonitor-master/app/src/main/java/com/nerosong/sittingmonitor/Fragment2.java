package com.nerosong.sittingmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment2 extends Fragment {


    @BindView(R.id.select_All_Records)
    Button selectAllRecords;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ButterKnife.bind(this, view);

        return view;
    }




    @OnClick(R.id.select_All_Records)
    public void onClick() {
        startActivity(new Intent(getActivity(), TableActivity.class));
    }
}
