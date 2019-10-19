package com.nerosong.sittingmonitor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment3 extends Fragment {

    @BindView(R.id.pieChart)
    PieChart pieChart;

    @BindView(R.id.et3output)
    EditText etOP;
    @BindView(R.id.clear_sql)
    Button clearSql;


    @BindView(R.id.record)
    Button record;


    private float left = 0;
    private float right = 0;
    private float front = 0;
    private float back = 0;
    private float normal = 0;
    private float sum = 0;


    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase db;

    private SQLiteHelper2 sqLiteHelper2;
    private SQLiteDatabase db2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        initChart();

        TextView textView1 = getActivity().findViewById(R.id.text_normal);
        TextView textView2 = getActivity().findViewById(R.id.text_left);
        TextView textView3 = getActivity().findViewById(R.id.text_right);
        TextView textView4 = getActivity().findViewById(R.id.text_front);
        TextView textView5 = getActivity().findViewById(R.id.text_back);
        etOP.setShowSoftInputOnFocus(false);
        //假如app关闭的时候，没有清空饼状图，那么当下一次app运行的时候，需要恢复上一次的状态，因此这里需要查询数据库
        sqLiteHelper = new SQLiteHelper(getActivity(), "chairdb.db", null, 1);
        db = sqLiteHelper.getWritableDatabase();
        Cursor cursor = db.query("chair", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int num = cursor.getInt((cursor.getColumnIndex("num")));
                if (num == 1) normal++;
                if (num == 2) left++;
                if (num == 3) right++;
                if (num == 4) front++;
                if (num == 5) back++;

            } while (cursor.moveToNext());
        }


        //点击保存坐姿数据以后，坐姿数据需要长期保存，故每次app打开的时候，需要通过数据库恢复坐姿数据记录表格
        sqLiteHelper2 = new SQLiteHelper2(getActivity(),"chairdb2.db",null,1);
        db2 = sqLiteHelper2.getWritableDatabase();
        Cursor cursor2 = db2.query("chair2",null,null,null,null,null,null);
        if(cursor2.moveToFirst()){
            textView1.setText(cursor2.getString(cursor2.getColumnIndex("num2")));
            cursor2.moveToNext();
            textView2.setText(cursor2.getString(cursor2.getColumnIndex("num2")));
            cursor2.moveToNext();
            textView3.setText(cursor2.getString(cursor2.getColumnIndex("num2")));
            cursor2.moveToNext();
            textView4.setText(cursor2.getString(cursor2.getColumnIndex("num2")));
            cursor2.moveToNext();
            textView5.setText(cursor2.getString(cursor2.getColumnIndex("num2")));
        }
        return view;
    }

    private void outRecord(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String dataStr = simpleDateFormat.format(new Date());

        etOP.getText().append('\n' + dataStr + " --------------------------- 坐姿" + s);
        etOP.setSelection(etOP.getText().length());
    }


    private void initChart() {
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(48f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("坐姿分布图");
        pieChart.setNoDataText("暂无数据");
        pieChart.setCenterTextSizePixels(46);
        pieChart.setDrawEntryLabels(true);

        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13);

        //把参数清零以后，开始计算各种坐姿的百分比
        normal = 0;
        left = 0;
        right = 0;
        front = 0;
        back = 0;
        sum = 0;

        sqLiteHelper = new SQLiteHelper(getActivity(), "chairdb.db", null, 1);
        db = sqLiteHelper.getWritableDatabase();
        Cursor cursor = db.query("chair", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int num = cursor.getInt((cursor.getColumnIndex("num")));
                if (num == 1) normal++;
                if (num == 2) left++;
                if (num == 3) right++;
                if (num == 4) front++;
                if (num == 5) back++;

            } while (cursor.moveToNext());
        }

        //将各种坐姿的数据输入饼图
        List<PieEntry> entries = new ArrayList<>();

        if (left != 0) {
            entries.add(new PieEntry(left, "左倾"));
        }
        if (right != 0) {
            entries.add(new PieEntry(right, "右倾"));
        }
        if (front != 0) {
            entries.add(new PieEntry(front, "前倾"));
        }
        if (back != 0) {
            entries.add(new PieEntry(back, "后倾"));
        }
        if (normal != 0) {
            entries.add(new PieEntry(normal, "坐正"));
        }

        sum = left + right + front + back + normal;


        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setDrawValues(true);
        set.setValueTextSize(12f);
        set.setValueTextColor(Color.WHITE);
        set.setSliceSpace(2f);
        set.setSelectionShift(5f);

        PieData data = new PieData(set);

        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

    }


    @Subscribe
    public void onEvent(Bundle bundle) {

        /**
         * 1：正
         * 2：左
         * 3：右
         * 4：前
         * 5：后
         * 6：无人
         * *：未知
         */

        String value = bundle.getString("data");
//        Log.e("6666", "onEvent: " + Integer.parseInt(value));
        if (value != null && !value.isEmpty()) {
            insertData(sqLiteHelper.getWritableDatabase(), "st", Integer.parseInt(value));//收到什么insert什么
            switch (value) {
                case "1":
                    outRecord("正确");
                    break;
                case "2":
                    outRecord("左倾");
                    break;
                case "3":
                    outRecord("右倾");
                    break;
                case "4":
                    outRecord("前倾");
                    break;
                case "5":
                    outRecord("后倾");
                    break;
                case "6":
                    outRecord("为空");
                    break;

                default:
                    break;
            }
            initChart();
        }

        Log.d("6666", "1---：" + normal);
        Log.d("6666", "2---：" + left);
        Log.d("6666", "3---：" + right);
        Log.d("6666", "4---：" + front);
        Log.d("6666", "5---：" + back);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void insertData(SQLiteDatabase writeableDatabase, String st, int num) {
        ContentValues values = new ContentValues();
        values.put("st", st);
        values.put("num", num);
        writeableDatabase.insert("chair", null, values);
    }
    private void insertData2(SQLiteDatabase writeableDatabase, String st2, String num2){
        ContentValues values = new ContentValues();
        values.put("st2",st2);
        values.put("num2",num2);
        writeableDatabase.insert("chair",null,values);
    }


    @OnClick(R.id.clear_sql)
    public void onViewClicked() {
        db.delete("chair", "st=?", new String[]{"st"});
        initChart();
    }


    public void initTable() {
        TextView textView1 = getActivity().findViewById(R.id.text_normal);
        TextView textView2 = getActivity().findViewById(R.id.text_left);
        TextView textView3 = getActivity().findViewById(R.id.text_right);
        TextView textView4 = getActivity().findViewById(R.id.text_front);
        TextView textView5 = getActivity().findViewById(R.id.text_back);

        textView1.setText(String.format("%.2f",normal / sum * 100) + "%");
        textView2.setText(String.format("%.2f",left / sum * 100) + "%");
        textView3.setText(String.format("%.2f",right / sum * 100) + "%");
        textView4.setText(String.format("%.2f",front / sum * 100) + "%");
        textView5.setText(String.format("%.2f",back / sum * 100) + "%");




       /* for (int i = 1; i < 6; i++) {
            LinearLayout varlayout = new LinearLayout(getActivity());
            varlayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int a = 0; a < 2; a++) {
                EditText text = new EditText(getActivity());
                text.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                text.getText().clear();
                if (i == 1) {
                    String s = String.valueOf(normal / sum * 100);
                    if (a == 0) text.append("正坐");
                    else text.append(s);
                }
                if (i == 2) {
                    String s = String.valueOf(left / sum * 100);
                    if (a == 0) text.append("左倾");
                    else text.append(s);
                }
                if (i == 3) {
                    String s = String.valueOf(right / sum * 100);
                    if (a == 0) text.append("右倾");
                    else text.append(s);
                }
                if (i == 4) {
                    String s = String.valueOf(front / sum * 100);
                    if (a == 0) text.append("前倾");
                    else text.append(s + "％");
                }
                if (i == 5) {
                    String s = String.valueOf(back / sum * 100);
                    if (a == 0) text.append("后倾");
                    else text.append(s + "％");
                }
                text.setGravity(Gravity.CENTER);

                varlayout.addView(text);
                TextView reit = new TextView(getActivity());

                reit.setLayoutParams(new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.WRAP_CONTENT));
                //设置TEXTVIEW宽为2
                reit.setBackgroundColor(Color.BLACK);
                varlayout.addView(reit);
            }
            TextView reit = new TextView(getActivity());
            //划横线
            reit.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            reit.setBackgroundColor(Color.BLACK);
            Linayout.addView(varlayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Linayout.addView(reit);
        }*/
    }


    @OnClick(R.id.record)
    public void record() {   //点击记录以后，首先将目前的五个比例记录在数据库中，然后建表
        sqLiteHelper2 = new SQLiteHelper2(getActivity(), "chairdb2.db", null, 1);
        db2 = sqLiteHelper2.getWritableDatabase();
        insertData2(sqLiteHelper2.getWritableDatabase(), "st2",String.format("%.2f",normal / sum * 100) + "%");
        insertData2(sqLiteHelper2.getWritableDatabase(), "st2",String.format("%.2f",left / sum * 100) + "%");
        insertData2(sqLiteHelper2.getWritableDatabase(), "st2",String.format("%.2f",right / sum * 100) + "%");
        insertData2(sqLiteHelper2.getWritableDatabase(), "st2",String.format("%.2f",front / sum * 100) + "%");
        insertData2(sqLiteHelper2.getWritableDatabase(), "st2",String.format("%.2f",back/ sum * 100) + "%");
        initTable();
    }

}