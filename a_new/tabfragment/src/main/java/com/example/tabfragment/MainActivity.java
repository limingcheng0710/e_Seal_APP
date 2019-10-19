package com.example.tabfragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.text_bluetooth)
    TextView textBluetooth;
    @BindView(R.id.text_svm)
    TextView textSvm;
    @BindView(R.id.text_set)
    TextView textSet;   //implements  View.OnClickListener
    private Fragment previousFragment = null;
    private Fragment firstFmt, secondFmt, thirdFmt;
    public static MainActivity mInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        requestRunTimePermission(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, new PermissionListener() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onGranted(List<String> grantedPermission) {

            }

            @Override
            public void onDenied(List<String> deniedPermission) {

            }

            @Override
            public void onDenied() {
            }
        });
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //为按钮设置经过重写的单击事件监听器
        textBluetooth.setOnClickListener(l);
        textSvm.setOnClickListener(l);
        textSet.setOnClickListener(l);
        textBluetooth.performClick();    //页面初始化停留在蓝牙页


    }

    public void unselected() {
        textBluetooth.setSelected(false);
        textSvm.setSelected(false);
        textSet.setSelected(false);


    }

    private void hideFragments(FragmentTransaction transaction) {
        if (firstFmt != null) {
            transaction.hide(firstFmt);
        }
        if (secondFmt != null) {
            transaction.hide(secondFmt);
        }
        if (thirdFmt != null) {
            transaction.hide(thirdFmt);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2001) {
            //在这里进行操作
            Toast.makeText(this, "在这里进行操作", Toast.LENGTH_LONG).show();
            textSvm.performClick();
        }
    }

    //获取单击事件监听器
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction(); //获取Fragment,开启一个事务，使用fragement的对象的方法来开启事务
            hideFragments(transaction);    //让所有Frag


            //为Fragment初始化
            switch (view.getId()) {
                case R.id.text_bluetooth:
                    unselected();
                    textBluetooth.setSelected(true);
                    if (firstFmt == null) {
                        firstFmt = new Bluetooth_Fragment();
                        transaction.add(R.id.fragment, firstFmt);
                    } else {
                        transaction.show(firstFmt);
                    }
                    break;
                case R.id.text_svm:
                    unselected();
                    textSvm.setSelected(true);
                    if (secondFmt == null) {
                        secondFmt = new LibSVM_Fragment();
                        transaction.add(R.id.fragment, secondFmt);
                    } else {
                        transaction.show(secondFmt);
                    }
                    break;
                case R.id.text_set:
                    unselected();
                    textSet.setSelected(true);
                    if (thirdFmt == null) {
                        thirdFmt = new Set_Fragment();
                        transaction.add(R.id.fragment, thirdFmt);
                    } else {
                        transaction.show(thirdFmt);
                    }
                    break;
            }
            transaction.commit();  //提交事务

        }
    };


    private PermissionListener mlistener;

    /**
     * 权限申请
     *
     * @param permissions 待申请的权限集合
     * @param listener    申请结果监听事件
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        this.mlistener = listener;

        //用于存放为授权的权限
        List<String> permissionList = new ArrayList<>();
        //遍历传递过来的权限集合
        for (String permission : permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission);
            }
        }

        //判断集合
        if (!permissionList.isEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {  //为空，则已经全部授权
            if (listener != null) {
                listener.onGranted();
            }
        }
    }

    /**
     * 权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    //被用户拒绝的权限集合
                    List<String> deniedPermissions = new ArrayList<>();
                    //用户通过的权限集合
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        //获取授权结果，这是一个int类型的值
                        int grantResult = grantResults[i];

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        } else {  //用户同意的权限
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {  //用户拒绝权限为空
                        if (mlistener != null) {
                            mlistener.onGranted();
                        }
                    } else {  //不为空
                        if (mlistener != null) {
                            //回调授权成功的接口
                            mlistener.onDenied(deniedPermissions);
                            //回调授权失败的接口
                            mlistener.onGranted(grantedPermissions);
                            mlistener.onDenied();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    public void predictNewData(String p) {
        if (secondFmt != null) {
            try {
                ((LibSVM_Fragment) secondFmt).predictNewData(p);
            } catch (IOException e) {
                Toast.makeText(this, "IO异常", Toast.LENGTH_SHORT).show();
            }

        }
    }
}


//  为每个fragment设置标签

    /*FragmentManager fm;
    private String[] tags = new String[]{"Bluetooth_Fragment", "LibSVM_Fragment", "Set_Fragment"};
    private ImageView mTabOne, mTabTwo, mTabThree;
    private List<Fragment> fragments = new ArrayList<>();
    Fragment Bluetooth_Fragment, LibSVM_Fragment, Set_Fragment;
    private Fragment mContent;//当前fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        fm = getSupportFragmentManager();
        Bluetooth_Fragment = new Bluetooth_Fragment();
        LibSVM_Fragment = new LibSVM_Fragment();
        Set_Fragment = new Set_Fragment();

        fragments.add(0, Bluetooth_Fragment);
        fragments.add(1, LibSVM_Fragment);
        fragments.add(2, Set_Fragment);

        mContent = Bluetooth_Fragment;
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment, mContent);
        ft.commitAllowingStateLoss();
    }


    //当activity非正常销毁时会进入此方法中 保存一些临时性的数据

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        stateCheck(outState);
    }

    private void initView() {
        mTabThree = findViewById(R.id.image_set);
        mTabTwo = findViewById(R.id.image_svm);
        mTabOne = findViewById(R.id.image_bluetooth);

        mTabThree.setOnClickListener(this);
        mTabTwo.setOnClickListener(this);
        mTabOne.setOnClickListener(this);
    }


    // fragment 切换

    public void switchFragment(Fragment from, Fragment to, int position) {

        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fm.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(from)
                        .add(R.id.fragment, to, tags[position]).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    //状态检测 用于内存不足的时候保证fragment不会重叠
    private void stateCheck(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            fm = getSupportFragmentManager();
            fm = getSupportFragmentManager();
            FragmentTransaction fts = fm.beginTransaction();
            Bluetooth_Fragment af = new Bluetooth_Fragment();
            mContent = af;
            fts.add(R.id.fragment, af);
            fts.commit();
        } else {

             //通过设置的tag来寻找fragment

            Bluetooth_Fragment bluetooth_fragment_b = (Bluetooth_Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[0]);

            LibSVM_Fragment libSVM_fragment = (LibSVM_Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[1]);
            Set_Fragment set_fragment = (Set_Fragment) getSupportFragmentManager()
                    .findFragmentByTag(tags[2]);

            getFragmentManager().beginTransaction().show(bluetooth_fragment_b).hide(libSVM_fragment).hide(set_fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_set:
                switchFragment(mContent, fragments.get(3), 3);
                break;
            case R.id.image_svm:
                switchFragment(mContent, fragments.get(2), 2);
                break;
            case R.id.image_bluetooth:
                switchFragment(mContent, fragments.get(1), 1);
                break;
            default:
                break;

        }


    };*/


