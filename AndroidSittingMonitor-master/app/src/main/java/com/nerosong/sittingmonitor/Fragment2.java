package com.nerosong.sittingmonitor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nerosong.sittingmonitor.base.RefreshParams;
import com.nerosong.sittingmonitor.utils.WeakHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment2 extends Fragment {


    @BindView(R.id.select_All_Records)
    Button selectAllRecords;
    @BindView(R.id.input_FileName2)
    EditText inputFileName2;
    @BindView(R.id.text_1)
    TextView text1;
    @BindView(R.id.text_normal)
    TextView textNormal;
    @BindView(R.id.text_2)
    TextView text2;
    @BindView(R.id.text_left)
    TextView textLeft;
    @BindView(R.id.text_3)
    TextView text3;
    @BindView(R.id.text_right)
    TextView textRight;
    @BindView(R.id.text_4)
    TextView text4;
    @BindView(R.id.text_front)
    TextView textFront;
    @BindView(R.id.text_5)
    TextView text5;
    @BindView(R.id.text_back)
    TextView textBack;
    @BindView(R.id.search)
    Button search;
    private SQLiteHelper2 sqLiteHelper2;
    private SQLiteDatabase db2;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ButterKnife.bind(this, view);
        sqLiteHelper2 = new SQLiteHelper2(getActivity(),"recorddb.db",null,1);
        return view;
    }


    @OnClick(R.id.select_All_Records)
    public void onClick() {
        startActivity(new Intent(getActivity(), TableActivity.class));
    }



    @OnClick(R.id.search)
    public void search() {

        final String input_FileName2 = inputFileName2.getText().toString();

        //第一个参数，数据表；第二个参数，查询的列；第三个参数查询条件，为占位符;第四个字段指定第三个条件中的占位符的值.剩下的三个参数全部设置为空



            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //如果有数据传入，那么就加一条数据
                    //如果查到，那么显示内容;
                    db2 = sqLiteHelper2.getWritableDatabase();
                    Cursor cursor = db2.query("records", new String[]{"filename","applicant" ,"approver" ,"date" ,"if_Right"}, "filename = ? ", new String[]{input_FileName2},
                            null, null, null, null);

                    if (cursor.moveToFirst()) {     //cursor对象移动到下一条记录
                        textBack.clearComposingText();
                        textFront.clearComposingText();
                        textNormal.clearComposingText();
                        textLeft.clearComposingText();
                        textRight.clearComposingText();
                        textNormal.setText(cursor.getString(cursor.getColumnIndex("filename")));
                        textLeft.setText(cursor.getString(cursor.getColumnIndex("applicant")));
                        textRight.setText(cursor.getString(cursor.getColumnIndex("approver")));
                        textFront.setText(cursor.getString(cursor.getColumnIndex("date")));
                        textBack.setText(cursor.getString(cursor.getColumnIndex("if_Right")));
                        Toast.makeText(getActivity(), "查询成功", Toast.LENGTH_SHORT);
                        cursor.close();
                        db2.close();

                    }
                    else{
                        textBack.clearComposingText();
                        textFront.setText("NULL");
                        textNormal.setText("NULL");
                        textLeft.setText("NULL");
                        textRight.setText("NULL");
                        Toast.makeText(getActivity(),"查询失败，不存在此条记录",Toast.LENGTH_LONG);
                        cursor.close();
                        db2.close();
                    }

                }
            }, 1000);



    }

    }

