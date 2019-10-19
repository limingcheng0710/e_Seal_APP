package com.example.tabfragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.et_register_pwd)
    EditText etRegisterPwd;
    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.bt_register_submit)
    Button btRegisterSubmit;

    private SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_one);
        ButterKnife.bind(this);
        sqLiteHelper = new SQLiteHelper(RegisterActivity.this, "logindb.db", null, 1);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_register_submit).setOnClickListener(this);
        cbProtocol.setClickable(false);
        cbProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    cbProtocol.setClickable(true);
                    btRegisterSubmit.setSelected(true);
                } else {
                    cbProtocol.setClickable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back: {

                finish();
            }
            break;
            case R.id.bt_register_submit: {
                register();
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void register() {
        String username = etRegisterUsername.getText().toString();
        String pwd = etRegisterPwd.getText().toString();
        if (username.equals("") || pwd.equals("")) {
            Toast.makeText(RegisterActivity.this, "填写的账号或者密码为空，请重新填写", Toast.LENGTH_LONG).show();
        } else if (username.length() < 6 || username.length() > 12) {
            Toast.makeText(RegisterActivity.this, "账号长度小于6位或者大于12位", Toast.LENGTH_LONG).show();
        } else if (pwd.length() < 6 || pwd.length() > 12) {
            Toast.makeText(RegisterActivity.this, "密码长度小于6位或者大于12位", Toast.LENGTH_LONG).show();
        } else {
            //调用insertData()方法，实现账号密码的注册
            insertData(sqLiteHelper.getWritableDatabase(), username, pwd);
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void insertData(SQLiteDatabase writeableDatabase, String username, String pwd) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("pwd", pwd);
        writeableDatabase.insert("login", null, values);
    }


}