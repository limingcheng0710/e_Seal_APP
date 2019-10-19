package com.example.tabfragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import umich.cse.yctung.androidlibsvm.LibSVM;


public class LibSVM_Fragment extends Fragment {

    @BindView(R.id.etOutput)
    EditText etOutput;


    static final String LOG_TAG = "666666";
    @BindView(R.id.file_train)
    ImageView fileTrain;
    @BindView(R.id.file_predict)
    ImageView filePredict;


    private LibSVM libSVM;
    private String appFolderPath;
    private String systemPath;
    private String dataPredictPath;
    private String modelPath;
    private String outputPath;
    private String dataTrainPath;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.libsvm_fragment, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            //不断的输出数据
//            float predictValue = (float) (Math.random() * 10);
//            Intent intent = new Intent("com.example.tabfragment.Broad");
//            intent.putExtra("predictValue", predictValue);
//            getActivity().sendBroadcast(intent);
//            sendEmptyMessageDelayed(0, 1000);
//        }
//    };

//    private void test() {
//        mHandler.sendEmptyMessage(0);
//    }

    private void init() {     //初始化
//        Environment.getExternalStorageDirectory().getAbsolutePath();
        systemPath = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/";
        System.out.println(getActivity().getExternalFilesDir(null));
        appFolderPath = systemPath + "libsvm/";
        CreateAppFolderIfNeed();
        copyAssetsDataIfNeed();

        libSVM = new LibSVM();

        dataPredictPath = appFolderPath + "test.txt ";
        modelPath = appFolderPath + "model.txt ";
        outputPath = appFolderPath + "out.txt ";
        dataTrainPath = appFolderPath + "train.txt ";
    }

    public void predictNewData(String data) throws IOException { //看看走不走这里就行了

        File file = new File(appFolderPath + "test.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();

        libSVM.predict(dataPredictPath + modelPath + outputPath);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    String value = readFromSD("libsvm/out.txt");
                    if (value==null||value.trim().length()==0){
                        return;
                    }
                    // 5
                    int intVal = Integer.parseInt(value.trim());
                    etOutput.append('\n' + "本次预测结果为：" +  readFromSD("libsvm/out.txt"));
                    etOutput.setSelection(etOutput.getText().length());
                    //不断的输出数据
                    Intent intent = new Intent("com.example.tabfragment.Broad");
                    intent.putExtra("predictValue", intVal);
                    getActivity().sendBroadcast(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @OnClick(R.id.btnTrainMode)
    public void train() {
        fileTrain.setVisibility(View.VISIBLE);
        filePredict.setVisibility(View.INVISIBLE);
        Log.w(LOG_TAG, "dataTrainPath: " + dataTrainPath);

        String svmTrainOptions = "-t 2 "; // note the ending space
        try {
            libSVM.train(svmTrainOptions + dataTrainPath + modelPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        etOutput.getText().append('\n' + "模型训练成功");
        etOutput.setSelection(etOutput.getText().length());   //移动光标到末尾
    }

    @OnClick(R.id.btnPredict)
    public void go() {

        fileTrain.setVisibility(View.INVISIBLE);
        filePredict.setVisibility(View.VISIBLE);

        Log.w(LOG_TAG, "dataPredictPath: " + dataPredictPath);
        Log.w(LOG_TAG, "modelPath: " + modelPath);
        Log.w(LOG_TAG, "outputPath: " + outputPath);
        libSVM.predict(dataPredictPath + modelPath + outputPath);


        etOutput.getText().append('\n' + "预测当前数据成功");
        etOutput.setSelection(etOutput.getText().length());   //移动光标到末尾

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    etOutput.getText().append('\n' + "预测结果为：" + readFromSD("libsvm/out.txt"));
                    etOutput.setSelection(etOutput.getText().length());   //移动光标到末尾
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    public String readFromSD(String filename) throws IOException {
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
            //boolean mkdirs = folder.mkdirs();
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
}
