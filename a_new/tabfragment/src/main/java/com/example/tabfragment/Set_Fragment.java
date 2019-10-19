package com.example.tabfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Set_Fragment extends Fragment {


    @BindView(R.id.text_quit)
    TextView textQuit;
    @BindView(R.id.about_app)
    TextView aboutApp;
    @BindView(R.id.user_information)
    TextView userInformation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_fragment, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.text_quit)
    public void quit_Login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.about_app)
    public void about_App() {
        Intent intent = new Intent(getActivity(), About_APP.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.user_information)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), UserInformation.class);
        startActivity(intent);
    }
}








