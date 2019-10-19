package com.example.tabfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LineChart extends AppCompatActivity {

    @BindView(R.id.LineChart_to_Bluetooth)
    ImageView LineChartToBluetooth;
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
    TextView guangzhi2;
    private ChartDataReceiver chartDataReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);
        guangzhi2 = (TextView) findViewById(R.id.guangzhi2);

        //制作曲线图，貌似不好下手只能变抄边理解
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.guangzhexian);
        chart = ChartFactory.getLineChartView(this, getdemodataset(), getdemorenderer());
        linearLayout.removeAllViews();//先remove再add可以实现统计图更新
        linearLayout.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        updatechart(0);//!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        handler2 = new Handler() {
//            public void handleMessage(Message msg) {
////                updatechart();
//                guangzhi2.setText(String.valueOf(addY));
//            }
//
//        };
//        task = new TimerTask() {
//            public void run() {
//                Message msg = new Message();
//                msg.what = 200;
//                handler2.sendMessage(msg);
//            }
//        };
//        timer.schedule(task, 0, 1000);   //设置刷新频率
        IntentFilter filter = new IntentFilter("com.example.tabfragment.Broad");
        chartDataReceiver = new ChartDataReceiver();
        registerReceiver(chartDataReceiver, filter);    //注册广播接收器

    }//oncreate结束


    class ChartDataReceiver extends BroadcastReceiver {    //定义广播接收类，里面设置接收的数据
        @Override
        public void onReceive(Context context, Intent intent) {
            int predictValue = intent.getIntExtra("predictValue", 0);
            Toast.makeText(getApplicationContext(), "预测值为" + predictValue, Toast.LENGTH_LONG).show();
            updatechart(predictValue);
        }
    }

    //更新折线图
    private void updatechart(int predictValue) {
        guangzhi2.setText(String.valueOf(predictValue));
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
            //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中

            for (int k = 0; k < length - 1; k++) {
                series.add(k + 1, ycache[k]);
                render.addXTextLabel(k + 1, xkedu[k]);
            }
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
        render.setChartTitleTextSize(50);//设置整个图表标题文字的大小
        render.setAxisTitleTextSize(40);//设置轴标题文字的大小
        render.setAxesColor(Color.BLACK);
        render.setXTitle("时间");
        render.setYTitle("坐姿参数");

        render.setLabelsTextSize(16);//设置轴刻度文字的大小
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
        render.setShowGrid(true);//显示网格
        render.setYAxisMax(6);//设置y轴的范围
        render.setYAxisMin(0);
        render.setYLabels(6);//分七等份


        render.setInScroll(true);
        render.setLabelsTextSize(14);
        render.setLabelsColor(Color.BLACK);
        render.getSeriesRendererAt(0).setDisplayChartValues(true); //显示折线上点的数值
        render.setPanEnabled(false, false);//禁止报表的拖动
        render.setPointSize(10f);//设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
        render.setMargins(new int[]{100, 100, 60, 100}); //设置图形四周的留白
        render.setMarginsColor(Color.WHITE);
        render.setXLabels(0);// 取消X坐标的数字zjk,只有自己定义横坐标是才设为此值


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
        unregisterReceiver(chartDataReceiver);
    }

    @OnClick(R.id.LineChart_to_Bluetooth)
    public void LineChart_to_Bluetooth() {
        Intent intent = new Intent(LineChart.this, MainActivity.class);
        startActivity(intent);

    }
}
