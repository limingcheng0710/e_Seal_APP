package com.nerosong.sittingmonitor;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.longsh.optionframelibrary.OptionBottomDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class Fragment1 extends Fragment {


    @BindView(R.id.camera)
    ImageButton camera;
    @BindView(R.id.input_FileName)
    EditText inputFileName;
    @BindView(R.id.check)
    Button check;
    @BindView(R.id.check_Result1)
    TextView checkResult1;
    @BindView(R.id.check_Result2)
    TextView checkResult2;

    private SQLiteHelper2 sqLiteHelper2;
    private SQLiteDatabase db2;


    //拍照功能参数
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private final static int CROP_IMAGE = 3;

    @BindView(R.id.picture)
    ImageView picture;


    //imageUri照片真实路径
    private Uri imageUri;
    //照片存储
    File filePath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        sqLiteHelper2 = new SQLiteHelper2(getActivity(), "recorddb.db", null, 1);
        ButterKnife.bind(this, view);


        return view;

    }


    @OnClick(R.id.camera)
    public void onClick() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("拍照");
        stringList.add("从相册选择");
        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(getActivity(), stringList);
        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //取消底部弹框
                //optionBottomDialog.dismiss();
                switch (position) {
                    case 0:
//

                        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT < 24) {
                            imageUri = Uri.fromFile(outputImage);
                        } else {
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.nerosong.sittingmonitor.fileprovider", outputImage);
                        }
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                        optionBottomDialog.dismiss();
                        break;
                    case 1:
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                        optionBottomDialog.dismiss();
                        break;
                    default:
                        break;
                }
            }


        });


    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    @OnClick(R.id.check)
    public void check() {
        db2 = sqLiteHelper2.getWritableDatabase();
        final String input_FileName = inputFileName.getText().toString();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //如果有数据传入，那么就加一条数据
                //如果查到，那么显示内容;
                db2 = sqLiteHelper2.getWritableDatabase();
                Cursor cursor = db2.query("records", new String[]{"filename", "applicant", "approver", "date", "if_Right"}, "filename = ? ", new String[]{input_FileName},
                        null, null, null, null);

                if (cursor.moveToFirst()) {     //cursor对象移动到下一条记录
                    //如果查到，那么显示内容;
                    checkResult1.clearComposingText();
                    checkResult2.setText("校验成功");
                    checkResult1.setText("文件名:" + cursor.getString(cursor.getColumnIndex("filename")));
                    checkResult1.append("\n申请人:" + cursor.getString(cursor.getColumnIndex("applicant")));
                    checkResult1.append("\n审核人:" + cursor.getString(cursor.getColumnIndex("approver")));
                    checkResult1.append("\n审核时间:" + cursor.getString(cursor.getColumnIndex("date")));
                    checkResult1.append("\n是否校验:" + cursor.getString(cursor.getColumnIndex("if_Right")));
                    Toast.makeText(getActivity(), "校验成功，公文合法", Toast.LENGTH_SHORT);
                    cursor.close();
                    db2.close();

                } else {
                    checkResult2.setText("失败");
                    checkResult1.setText("fakjshkashihfaiushfsfhoiejffafsdgsfdghjgdkuq");
                    Toast.makeText(getActivity(), "校验失败，公文非法", Toast.LENGTH_LONG);
                    db2.execSQL("update records set if_Right = ? where filename = ?",new Object[]{"否",input_FileName});
                    cursor.close();
                    db2.close();
                }

            }
        }, 1);



    }
}
