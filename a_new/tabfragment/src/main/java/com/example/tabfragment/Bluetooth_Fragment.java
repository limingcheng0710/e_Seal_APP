package com.example.tabfragment;

import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Bluetooth_Fragment extends Fragment {

    @BindView(R.id.et_bluetooth_output)
    EditText etBluetoothOutput;

    @BindView(R.id.img_bluetooth)
    ImageView imgBluetooth;


    @BindView(R.id.clear)
    Button clear;
    @BindView(R.id.bluetooth_to_LineChart)
    Button bluetoothToLineChart;
    @BindView(R.id.bt_conncet_bluetooth)
    Button btConncetBluetooth;
    @BindView(R.id.bt_receive)
    Button btReceive;
    @BindView(R.id.appleswitch)
    AppleSwitch appleswitch;
    @BindView(R.id.switch_android)
    Switch switchAndroid;


    private BleDevice mBleDevice;
    private String mMac;
    private String uuid_service;
    private String uuid_characteristic_indicate;
    private String stepData;
    private int mtuStep;


    //LibSVM_Fragment svm_fragment = (LibSVM_Fragment) getActivity().getSupportFragmentManager().findFragmentByTag(getFragmentTag(0));
   /*  private String getFragmentTag(int position){
        return  "android:switcher:"+R.id.fragment+":"+position;
   }*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bluetooth_fragment, null);
        ButterKnife.bind(this, view);
        appleswitch.setOnToggleListener(new AppleSwitch.OnToggleListener() {
            @Override
            public void onSwitchChangeListener(boolean switchState) {
                outPut("123");
            }
        });
        switchAndroid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    outPut("123");}
                else outPut("!!!!!!!!!!");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBT();  //初始化


    }

    private void initBT() {
        BleManager.getInstance().init(getActivity().getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setOperateTimeout(5000);

        mMac = "90:9A:77:0C:38:2C";
        uuid_service = "0000ffe0-0000-1000-8000-00805f9b34fb";
        uuid_characteristic_indicate = "0000ffe1-0000-1000-8000-00805f9b34fb";

        mtuStep = 1;

//        formatRawData("0738149321471576114721872923254306781059164819690693132734281952");
//        formatRawData("2469278312940000234425252026117706180000076706480000000000000000"); // 2
    }

    public void outPut(String s) {     //蓝牙面板输出
        etBluetoothOutput.getText().append('\n' + s);
        etBluetoothOutput.setSelection(etBluetoothOutput.getText().length());   //移动光标到末尾
    }

    private void formatRawData(String rawData) {
        if (rawData.length() != 64) {
            outPut("原始数据格式错误，非64字节");
        } else {
            //DecimalFormat df = new DecimalFormat("0.000000");
            DecimalFormat df = new DecimalFormat("0.000000");
            int t = 0;
            double j = 0;
            String p = "";
            String[] temp = new String[16];
            while (t < 16) {
                p = rawData.substring(0, 4);
                j = Integer.parseInt(p);
                j = j / 4095 * 3.25;
                temp[t] = df.format(j);
                rawData = rawData.substring(4);
                t++;
            }
            t = 1;
            p = "1";
            while (t < 17) {
                p += " " + t + ":" + temp[t - 1];
                t++;
            }

            Log.e("finalData", p);
            outPut("计算后写入文件的数据为：" + p);
            MainActivity mInstance = MainActivity.mInstance;
            if (mInstance != null) {
                mInstance.predictNewData(p);
            }
           /* try {
                predictNewData(p);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    @OnClick(R.id.clear)
    public void et_out_clear() {
        etBluetoothOutput.getText().clear();
    }

    @OnClick(R.id.bluetooth_to_LineChart)
    public void bluetooth_to_Linechart() {
        Intent intent = new Intent(getActivity(), LineChart.class);
        startActivity(intent);
    }

    @OnClick({R.id.bt_conncet_bluetooth, R.id.bt_receive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_conncet_bluetooth:
                if (!BleManager.getInstance().isBlueEnable()) {
                    outPut("蓝牙未打开");
                } else {
                    BleManager.getInstance().connect(mMac, new BleGattCallback() {
                        @Override
                        public void onStartConnect() {
                            outPut("开始连接...");
                        }

                        @Override
                        public void onConnectFail(BleDevice bleDevice, BleException exception) {
                            outPut("连接失败");
                            outPut(exception.toString());
                            imgBluetooth.setSelected(false);
                        }

                        @Override
                        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                            outPut("连接成功");
                            mBleDevice = bleDevice;
                            imgBluetooth.setSelected(true);  //蓝牙图标变蓝，代表已连接,便于交互
                        }

                        @Override
                        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                            outPut("连接断开");
                            imgBluetooth.setSelected(false);
                        }
                    });
                }
                break;
            case R.id.bt_receive:
                BleManager.getInstance().notify(
                        mBleDevice,
                        uuid_service,
                        uuid_characteristic_indicate,
                        new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                                outPut("监听成功");
                            }

                            @Override
                            public void onNotifyFailure(BleException exception) {
                                outPut("监听失败");

                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {

                                String rawData = new String(data);
                                outPut("收到数据-----分包：" + mtuStep + "，数据长度：" + rawData.length());
                                outPut("收到数据-----原始数据为：");
                                outPut(rawData);
                                if (mtuStep <= 3) {
                                    if (mtuStep == 1) {
                                        stepData = "";
                                    }
                                    stepData += rawData;
                                    mtuStep++;
                                } else {
                                    stepData += rawData;
                                    outPut("4次分包结束，开始解析数据，长度为：" + stepData.length());
                                    outPut("本次总包数据为：" + stepData);
                                    formatRawData(stepData);
                                    mtuStep = 1;
                                }
                            }
                        }

                );
                break;
        }
    }

}





