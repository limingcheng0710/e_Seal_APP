package com.nerosong.sittingmonitor;

import android.bluetooth.BluetoothGatt;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import umich.cse.yctung.androidlibsvm.LibSVM;


public class Fragment1 extends Fragment {


    /**
     * 1：正
     * 2：左
     * 3：右
     * 4：前
     * 5：后
     * 6：无人
     * *：未知
     */


    static final String LOG_TAG = "6666";


    @BindView(R.id.guangzhexian)
    LinearLayout linearLayout;
    @BindView(R.id.draw_chart)
    ImageButton drawChart;
    @BindView(R.id.layout_table)
    LinearLayout layoutTable;

    boolean chart_Flag = false;
    @BindView(R.id.et_cardView)
    CardView etCardView;
    @BindView(R.id.clear_linechart)
    ImageButton clearLinechart;
    @BindView(R.id.frag1_img_bluetooth)
    ImageView frag1ImgBluetooth;

    private LibSVM libSVM;
    private String appFolderPath;
    private String systemPath;
    private String dataPredictPath;
    private String modelPath;
    private String outputPath;
    private String dataTrainPath;

    private BleDevice mBleDevice;
    private String mMac;
    private String uuid_service;
    private String uuid_characteristic_indicate;
    private String stepData;
    private int mtuStep;

    @BindView(R.id.tvBTState)
    TextView tvBTState;
    @BindView(R.id.etOutput)
    EditText etOutput;

    private GraphicalView chart;
    //    private Timer timer = new Timer();
    //    private TimerTask task;
    private int addY;
    private String addX;
    String[] xkedu = new String[5];//x轴数据缓冲

    int[] ycache = new int[5];   //y轴数据缓冲
    //private final static int SERISE_NR = 1; //曲线数量guang
    private XYSeries series;//用来清空第一个再加下一个
    private XYMultipleSeriesDataset dataset1;//xy轴数据源
    private XYMultipleSeriesRenderer render;
    SimpleDateFormat shijian = new SimpleDateFormat("hh:mm:ss");

    Handler handler2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        ButterKnife.bind(this, view);

        initSVM();
        initBT();

        etOutput.setShowSoftInputOnFocus(false);

        //制作折线图
        chart = ChartFactory.getLineChartView(getActivity(), getdemodataset(), getdemorenderer());
        linearLayout.removeAllViews();//先remove再add可以实现统计图更新
        linearLayout.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        return view;

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


//        测试
//        formatRawData("0738149321471576114721872923254306781059164819690693132734281952");
//        formatRawData("2469278312940000234425252026117706180000076706480000000000000000"); // 2
    }

    private void initSVM() {
        systemPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        appFolderPath = systemPath + "libsvm/";
        CreateAppFolderIfNeed();
        copyAssetsDataIfNeed();

        libSVM = new LibSVM();

        dataPredictPath = appFolderPath + "test.txt ";
        modelPath = appFolderPath + "model.txt ";
        outputPath = appFolderPath + "out.txt ";
        dataTrainPath = appFolderPath + "train.txt ";
    }

    @OnClick(R.id.btnConnectBT)
    public void connect() {
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
                    frag1ImgBluetooth.setSelected(false);
                    outPut(exception.toString());

                    Bundle bundle = new Bundle();
                    bundle.putString("state", "断开");
                    EventBus.getDefault().post(bundle);
                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    outPut("连接成功");
                    frag1ImgBluetooth.setSelected(true);

                    Bundle bundle = new Bundle();
                    bundle.putString("state", "已连接");
                    EventBus.getDefault().post(bundle);

                    mBleDevice = bleDevice;
                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                    outPut("连接断开");
                    frag1ImgBluetooth.setSelected(false);

                    Bundle bundle = new Bundle();
                    bundle.putString("state", "断开");
                    EventBus.getDefault().post(bundle);

                }
            });
        }
    }

    @OnClick(R.id.btnListenBT)
    public void listen() {
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
    }


    private void formatRawData(String rawData) {
        if (rawData.length() != 64) {
            outPut("原始数据格式错误，非64字节");
        } else {
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

            try {
                predictNewData(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void predictNewData(String data) throws IOException {
        File file = new File(appFolderPath + "test.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();

        libSVM.predict(dataPredictPath + modelPath + outputPath);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    outPut("本次预测结果为：" + readFromSD("libsvm/out.txt"));

                    String s = readFromSD("libsvm/out.txt");

                    int intVal = Integer.parseInt(s.trim());
                    updatechart(intVal);   //更新折线图

                    getResult(s.substring(0, 1));
//                    Log.e("6666",s.length()+"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    private void getResult(String s) {
        Bundle bundle = new Bundle();
        bundle.putString("data", s);
        EventBus.getDefault().post(bundle);
    }


    @OnClick(R.id.btnTrainModel)
    public void train() {
        Log.w(LOG_TAG, "dataTrainPath: " + dataTrainPath);

        String svmTrainOptions = "-t 2 "; // note the ending space
        libSVM.train(svmTrainOptions + dataTrainPath + modelPath);

        outPut("模型训练成功");
    }

    @OnClick(R.id.btnPredict)
    public void go() {

        Log.w(LOG_TAG, "dataPredictPath: " + dataPredictPath);
        Log.w(LOG_TAG, "modelPath: " + modelPath);
        Log.w(LOG_TAG, "outputPath: " + outputPath);


        libSVM.predict(dataPredictPath + modelPath + outputPath);
        outPut("预测当前数据成功");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    outPut("预测结果为：" + readFromSD("libsvm/out.txt"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000); // 延时1秒
    }

    private void outPut(String s) {
        etOutput.getText().append('\n' + s);
        etOutput.setSelection(etOutput.getText().length());
    }

    private String readFromSD(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            //打开文件输入流
            FileInputStream input = new FileInputStream(filename);
            byte[] temp = new byte[1024];

            int len = 0;
            //读取文件内容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //关闭输入流
            input.close();
        }
        return sb.toString();
    }

    private void CreateAppFolderIfNeed() {
        // 1. create app folder if necessary
        File folder = new File(appFolderPath);

        if (!folder.exists()) {
            folder.mkdir();
            Log.d(LOG_TAG, "Appfolder is not existed, create one");
            etOutput.getText().append('\n' + "Appfolder is not existed, create one");

        } else {
            Log.w(LOG_TAG, "WARN: Appfolder has not been deleted");
            etOutput.getText().append('\n' + "WARN: Appfolder has not been deleted");

        }

    }

    private void copyAssetsDataIfNeed() {
        String assetsToCopy[] = {"model.txt", "test.txt", "train.txt"};
        //String targetPath[] = {C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigTrain+".wav", C.systemPath+C.INPUT_FOLDER+C.INPUT_PREFIX+AudioConfigManager.inputConfigPredict+".wav",C.systemPath+C.INPUT_FOLDER+"SomeoneLikeYouShort.mp3"};

        for (int i = 0; i < assetsToCopy.length; i++) {
            String from = assetsToCopy[i];
            String to = appFolderPath + from;

            // 1. check if file exist
            File file = new File(to);
            if (file.exists()) {
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: file exist, no need to copy:" + from);
                etOutput.getText().append('\n' + "copyAssetsDataIfNeed: file exist, no need to copy:" + from);

            } else {
                // do copy
                boolean copyResult = copyAsset(getActivity().getAssets(), from, to);
                Log.d(LOG_TAG, "copyAssetsDataIfNeed: copy result = " + copyResult + " of file = " + from);
                etOutput.getText().append('\n' + "copyAssetsDataIfNeed: copy result = " + copyResult + " of file = " + from);
            }
        }
    }

    private boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "[ERROR]: copyAsset: unable to copy file = " + fromAssetPath);
            etOutput.getText().append('\n' + "[ERROR]: copyAsset: unable to copy file = " + fromAssetPath);
            return false;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void updatechart(int predictValue) {

        //判断当前点集中到底有多少点，因为屏幕总共只能容纳5个，所以当点数超过5时，长度永远是5
        int length = series.getItemCount();
        int a = length;
        if (length > 5) {
            length = 5;
        }
		/*try {
			if(guangzhi2.getText().toString()!=null){
				addY = Float.valueOf(guangzhi2.getText().toString());//要不要判断再说
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}*/


        addX = shijian.format(new Date());   //生成当前的北京时间

        addY = predictValue;  //y轴数据的来源


        //移除数据集中旧的点集
        dataset1.removeSeries(series);
        if (a < 5)//当数据集中不够五个点的时候直接添加就好，因为初始化的时候只有一个点，所以前几次更新的时候直接添加
        {
            series.add(a + 1, addY);//第一个参数代表第几个点，要与下面语句中的第一个参数对应
            render.addXTextLabel(a + 1, addX);
            xkedu[a] = addX;
        } else //超过了五个点要去除xcache【0】换成【1】的.....
        {
            //将旧的点集中x和y的数值取出来放入backup中，造成曲线向左平移的效果
            for (int i = 0; i < length - 1; i++) {
                ycache[i] = (int) series.getY(i + 1);
                xkedu[i] = xkedu[i + 1];
            }

            //点集先清空，为了做成新的点集而准备
            series.clear();
            //将新产生的点首先加体中将坐标变换后的一系列点都重新加入到点集中
            //
            //            for (int k = 0; k < length - 1; k++) {
            //                series.add(k + 1, ycache[k]);
            //                render.addXTextLabel(k + 1, xkedu[k]);
            //            }入到点集中，然后在循环
            xkedu[4] = addX;
            series.add(5, addY);
            render.addXTextLabel(5, addX);
        }
        //在数据集中添加新的点集
        dataset1.addSeries(series);
        //视图更新，没有这一步，曲线不会呈现动态
        chart.invalidate();
    }

    private XYMultipleSeriesRenderer getdemorenderer() {
        // TODO Auto-generated method stub
        render = new XYMultipleSeriesRenderer();
        render.setChartTitle("坐姿参数实时曲线");
        render.setChartTitleTextSize(35);//设置整个图表标题文字的大小
        render.setAxisTitleTextSize(25);//设置轴标题文字的大小
        render.setAxesColor(Color.BLACK);
        render.setXTitle("时间");
        render.setYTitle("坐姿参数");

        render.setLabelsTextSize(30.0f);//设置轴刻度文字的大小
        render.setLabelsColor(Color.BLACK);
        render.setXLabelsColor(Color.BLACK);
        render.setYLabelsColor(0, Color.BLACK);
        render.setLegendTextSize(30);//设置图例文字大小
        //render.setShowLegend(false);//显示不显示在这里设置，非常完美

        XYSeriesRenderer r = new XYSeriesRenderer();//设置颜色和点类型
        r.setColor(Color.RED);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillPoints(true);
        r.setChartValuesSpacing(3);

        render.addSeriesRenderer(r);
        render.setYLabelsAlign(Paint.Align.RIGHT);//刻度值相对于刻度的位置
        render.setYAxisMax(7);//设置y轴的范围
        render.setYAxisMin(0);
        render.setYLabels(7);//分七等份


        render.setInScroll(true);
        render.setLabelsTextSize(30);
        render.setLabelsColor(Color.BLACK);
        render.getSeriesRendererAt(0).setDisplayChartValues(true); //显示折线上点的数值
        render.setPanEnabled(true, true);//禁止报表的拖动
        render.setPointSize(10f);//设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        render.setMargins(new int[]{50, 50, 50, 50}); //设置图形四周的留白
        render.setMarginsColor(Color.WHITE);
        render.setXLabels(0);// 取消X坐标的数字zjk,只有自己定义横坐标是才设为此值

        render.setClickEnabled(true);  //设置可以移动折线
        render.setFitLegend(true);
        render.setShowGrid(true);//显示网格
        render.setGridColor(0xFF6666FF);
        render.setShowGridY(true);
        // render.setBarWidth(10f);


        return render;
    }

    private XYMultipleSeriesDataset getdemodataset() {
        // TODO Auto-generated method stub
        dataset1 = new XYMultipleSeriesDataset();//xy轴数据源, 数据集, 其中可以封装图表所需的数据;
        series = new XYSeries("坐姿参数 ");//这个事是显示多条用的，显不显示在上面render设置,
        // 属于 图表数据集的一部分, 每个都代表了一个数据集合 例如 折线, 一个图表中可以有多条折线, 所有的数据放在一起就是 数据集 XYMultipleSeriesDataset ;
        //这里相当于初始化，初始化中无需添加数据，因为如果这里添加第一个数据的话，
        //很容易使第一个数据和定时器中更新的第二个数据的时间间隔不为两秒，所以下面语句屏蔽
        //这里可以一次更新五个数据，这样的话相当于开始的时候就把五个数据全部加进去了，但是数据的时间是不准确或者间隔不为二的
       /* for(int i=0;i<5;i++)
        series.add(1, Math.random()*10);//横坐标date数据类型，纵坐标随即数等待更新*/


        dataset1.addSeries(series);
        return dataset1;
    }

    public void onDestroy() {
//        //当结束程序时关掉Timer
////        timer.cancel();
        super.onDestroy();
    }


    @OnClick(R.id.draw_chart)
    public void draw_chart() {
        chart_Flag = !chart_Flag;
        if (chart_Flag == true) {
            layoutTable.setVisibility(View.VISIBLE);
            etCardView.setVisibility(View.INVISIBLE);
        } else {
            layoutTable.setVisibility(View.INVISIBLE);
            etCardView.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.et_cardView)
    public void onViewClicked() {
    }


    @OnClick(R.id.clear_linechart)
    public void clartLineChart() {
        etOutput.getText().clear();
      /*  series.clear();
        //在数据集中添加新的点集
        dataset1.addSeries(series);*/
        dataset1.removeSeries(series);
        //视图更新，没有这一步，曲线不会呈现动态
        chart.invalidate();

    }


}
