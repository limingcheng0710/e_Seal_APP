package com.nerosong.sittingmonitor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nerosong.sittingmonitor.base.RefreshParams;
import com.nerosong.sittingmonitor.base.adapter.AbsCommonAdapter;
import com.nerosong.sittingmonitor.base.adapter.AbsViewHolder;
import com.nerosong.sittingmonitor.bean.OnlineSaleBean;
import com.nerosong.sittingmonitor.bean.TableModel;
import com.nerosong.sittingmonitor.utils.WeakHandler;
import com.nerosong.sittingmonitor.widget.SyncHorizontalScrollView;
import com.nerosong.sittingmonitor.widget.pullrefresh.AbPullToRefreshView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class TableActivity extends AppCompatActivity {

    private SQLiteHelper2 sqLiteHelper3;
    private SQLiteDatabase db3;


    /**
     * 用于存放标题的id,与textview引用
     */
    private SparseArray<TextView> mTitleTvArray;
    //表格部分
    private TextView tv_table_title_left;
    private LinearLayout right_title_container;
    private ListView leftListView;
    private ListView rightListView;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private SyncHorizontalScrollView titleHorScv;
    private SyncHorizontalScrollView contentHorScv;
    private AbPullToRefreshView pulltorefreshview;
    private int pageNo = 0;
    private WeakHandler mHandler = new WeakHandler();
    private Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.table_layout);
        sqLiteHelper3 = new SQLiteHelper2(TableActivity.this,"recorddb.db",null,1);
        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("关于我（About me.）");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this,AboutMeActivity.class));
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        mContext = getApplicationContext();
        findByid();
        setListener();
        setData();
    }

    public void findByid() {
        pulltorefreshview = (AbPullToRefreshView) findViewById(R.id.pulltorefreshview);
//        pulltorefreshview.setPullRefreshEnable(false);
        tv_table_title_left = (TextView) findViewById(R.id.tv_table_title_left);
        tv_table_title_left.setText("序号");
        leftListView = (ListView) findViewById(R.id.left_container_listview);
        rightListView = (ListView) findViewById(R.id.right_container_listview);
        right_title_container = (LinearLayout) findViewById(R.id.right_title_container);
        getLayoutInflater().inflate(R.layout.table_right_title, right_title_container);
        titleHorScv = (SyncHorizontalScrollView) findViewById(R.id.title_horsv);
        contentHorScv = (SyncHorizontalScrollView) findViewById(R.id.content_horsv);
        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);
        findTitleTextViewIds();
        initTableView();
    }

    /**
     * 初始化标题的TextView的item引用
     */
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i <= 20; i++) {
            try {
                Field field = R.id.class.getField("tv_table_title_" + 0);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);
                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_left_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
                tv_table_content_left.setText(item.getLeftTitle());
            }
        };
        mRightAdapter = new AbsCommonAdapter<TableModel>(mContext, R.layout.table_right_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.tv_table_content_right_item4);


                tv_table_content_right_item0.setText(item.getText0());
                tv_table_content_right_item1.setText(item.getText1());
                tv_table_content_right_item2.setText(item.getText2());
                tv_table_content_right_item3.setText(item.getText3());
                tv_table_content_right_item4.setText(item.getText4());

                //部分行设置颜色凸显
                if(item.getText4() == "否"){
                    item.setTextColor(tv_table_content_right_item4, item.getText0());
                }




                for (int i=0; i<4; i++) {
                    View view = ((LinearLayout) helper.getConvertView()).getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }
            }
        };
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
    }


    public void setListener() {  //下拉刷新
        pulltorefreshview.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果有数据传入，那么就加一条数据
                        pageNo = 0;
                        doGetDatas(0, RefreshParams.REFRESH_DATA);
//                doGetDatas(pageNo, RefreshParams.REFRESH_DATA);

                    }
                }, 1000);
            }
        });
        pulltorefreshview.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(pageNo<2){   //最多显示两页，否则提示
                            doGetDatas(pageNo, RefreshParams.LOAD_DATA);
                            if(RefreshParams.LOAD_DATA == RefreshParams.REFRESH_DATA){
                                pulltorefreshview.onHeaderRefreshFinish();
                            }else{
                                pulltorefreshview.onFooterLoadFinish();
                            }
                        }

                        else{
                            Toast.makeText(TableActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();

                            if(RefreshParams.LOAD_DATA == RefreshParams.REFRESH_DATA){
                                pulltorefreshview.onHeaderRefreshFinish();
                            }else{
                                pulltorefreshview.onFooterLoadFinish();
                            }
                        }
                    }
                }, 1000);
            }

        });
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转界面
                Toast.makeText(TableActivity.this, "打开某条记录的单独详情", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(){
        doGetDatas(0, RefreshParams.REFRESH_DATA);
    }


//    //模拟网络请求
//    //在最下方添加一条数据,内容为第一个fragment
//    public  void addOneDates(int pageno,int state){
//        db3 = sqLiteHelper3.getWritableDatabase();
//        Cursor cursor = db3.query("records", null, null, null, null, null, null);
//        int n=0;
//        if (cursor.moveToFirst()) {
//            do {
//                n++;
//
//            } while (cursor.moveToNext());
//        }
//
//        List<OnlineSaleBean> onlineSaleBeanList = new ArrayList<>();
//        for(int i=0;i<n;i++){
//            onlineSaleBeanList.add(new OnlineSaleBean("No."+i+pageno*20));
//        }
//
//        if(state == RefreshParams.REFRESH_DATA){
//            pulltorefreshview.onHeaderRefreshFinish();
//        }else{
//            pulltorefreshview.onFooterLoadFinish();
//        }
//        setOneDatas(onlineSaleBeanList, state,pageno);
//    }

//    private void setOneDatas(List<OnlineSaleBean> onlineSaleBeanList, int type,int pageno) {
//        if (onlineSaleBeanList.size() > 0) {
//            List<TableModel> mDatas = new ArrayList<>();
//            for (int i = 0; i < onlineSaleBeanList.size(); i++) {
//                OnlineSaleBean onlineSaleBean = onlineSaleBeanList.get(i);
//                TableModel tableMode = new TableModel();
//                tableMode.setOrgCode(onlineSaleBean.getOrgCode());
//                tableMode.setLeftTitle(onlineSaleBean.getCompanyName());
//                db3 = sqLiteHelper3.getWritableDatabase();
//                Cursor cursor = db3.query("records", null, null, null, null, null, null);
//
////                if (cursor.moveToFirst()) {
////                    do {
////                        tableMode.setText0(cursor.getString(cursor.getColumnIndex("filename")));//列0内容
////                        tableMode.setText1(cursor.getString(cursor.getColumnIndex("applicant")));//列1内容
////                        tableMode.setText2(cursor.getString(cursor.getColumnIndex("approver")));//列2内容
////                        tableMode.setText3(cursor.getString(cursor.getColumnIndex("date")));
////                        tableMode.setText4(cursor.getString(cursor.getColumnIndex("filename")));
////                        mDatas.add(tableMode);
////                    } while (cursor.moveToNext());
//                }
//
//
//            }
//            boolean isMore;
//            if (type == RefreshParams.LOAD_DATA) {
//                isMore = true;
//            } else {
//                isMore = false;
//            }
//            mLeftAdapter.addData(mDatas, isMore);
//            mRightAdapter.addData(mDatas, isMore);
//            //加载数据成功，增加页数，但是最大条数限制在100
//            pageNo++;
////            if (mDatas.size() < 20) {
////                pulltorefreshview.setLoadMoreEnable(false);
////            }
//            mDatas.clear();
//        } else {
//            //数据为null
//            if (type == RefreshParams.REFRESH_DATA) {
//                mLeftAdapter.clearData(true);
//                mRightAdapter.clearData(true);
//                //显示数据为空的视图
//                //                mEmpty.setShowErrorAndPic(getString(R.string.empty_null), 0);
//            } else if (type == RefreshParams.LOAD_DATA) {
//                Toast.makeText(mContext, "请求json失败",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    //模拟网络请求
    public void doGetDatas(int pageno, int state) {
        List<OnlineSaleBean> onlineSaleBeanList = new ArrayList<>();
        for(int i=0+pageno*20;i<20*(pageno+1);i++){
            onlineSaleBeanList.add(new OnlineSaleBean("No."+i));

        }
        if(state == RefreshParams.REFRESH_DATA){
            pulltorefreshview.onHeaderRefreshFinish();
        }else{
            pulltorefreshview.onFooterLoadFinish();
        }
        setDatas(onlineSaleBeanList, state,pageno);

    }  public void doGetDatas2(int pageno, int state) {
        List<OnlineSaleBean> onlineSaleBeanList = new ArrayList<>();
        for(int i=0+pageno*21;i<21*(pageno+1);i++){
            onlineSaleBeanList.add(new OnlineSaleBean("No."+i));

        }
        if(state == RefreshParams.REFRESH_DATA){
            pulltorefreshview.onHeaderRefreshFinish();
        }else{
            pulltorefreshview.onFooterLoadFinish();
        }
        setDatas(onlineSaleBeanList, state,pageno);
    }

    private void setDatas(List<OnlineSaleBean> onlineSaleBeanList, int type,int pageno) {
        if (onlineSaleBeanList.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < onlineSaleBeanList.size(); i++) {
                OnlineSaleBean onlineSaleBean = onlineSaleBeanList.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(onlineSaleBean.getOrgCode());
                tableMode.setLeftTitle(onlineSaleBean.getCompanyName());
                tableMode.setText0("与" + String.valueOf(i+1) + "号公司的合同");//列0内容
                tableMode.setText1("员工" + String.valueOf(i+1) + "号");//列1内容
                tableMode.setText2("领导" + String.valueOf(i+1) + "号");//列2内容
                tableMode.setText3("2019/10/" + String.valueOf(pageno+1));
                tableMode.setText4("是");
                mDatas.add(tableMode);
            }
            boolean isMore;
            if (type == RefreshParams.LOAD_DATA) {
                isMore = true;
            } else {
                isMore = false;
            }
            mLeftAdapter.addData(mDatas, isMore);
            mRightAdapter.addData(mDatas, isMore);
            //加载数据成功，增加页数，但是最大条数限制在100
            pageNo++;
//            if (mDatas.size() < 20) {
//                pulltorefreshview.setLoadMoreEnable(false);
//            }
            mDatas.clear();
        } else {
            //数据为null
            if (type == RefreshParams.REFRESH_DATA) {
                mLeftAdapter.clearData(true);
                mRightAdapter.clearData(true);
                //显示数据为空的视图
                //                mEmpty.setShowErrorAndPic(getString(R.string.empty_null), 0);
            } else if (type == RefreshParams.LOAD_DATA) {
                Toast.makeText(mContext, "请求json失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
