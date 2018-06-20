package com.example.administrator.httpdownloader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求权限
        int hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(hasReadPermission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }


        Button b1=(Button) findViewById(R.id.confirm);
        Button b2=(Button) findViewById(R.id.cancle);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
       if(view==findViewById(R.id.confirm)){
           EditText edit=(EditText) findViewById(R.id.textOfUrl);
           String url=edit.getText().toString();
           Intent intent=new Intent(this,HtmlActivity.class);
           intent.putExtra("url_key",url);
           startActivity(intent);
       }else{
           EditText edit=(EditText) findViewById(R.id.textOfUrl);
           edit.clearComposingText();
       }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //用户同意授权，执行读取文件的代码
            }else{
                finish();
            }
        }
    }
}
