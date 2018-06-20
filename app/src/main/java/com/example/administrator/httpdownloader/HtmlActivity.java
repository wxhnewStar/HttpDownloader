package com.example.administrator.httpdownloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;


public class HtmlActivity extends AppCompatActivity {
    TextView tv;
    TextView et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        tv= findViewById(R.id.html);
        et=findViewById(R.id.file_path);
        Intent intent=getIntent();
           String url=intent.getStringExtra("url_key");
           //输出给自己看的
           System.out.println("=====================:"+url+"====================");
           Begin(url);
    }

    //异步处理 连接url
    @SuppressLint("StaticFieldLeak")
    public void Begin(String url){
        new AsyncTask<String, Void ,String>(){
            protected String doInBackground(String... arg0) {
                try {
                    //创建Socket连接以及打开连接
                    String s;
                    URL url=new URL(arg0[0]);
                    Socket socket=new Socket(url.getHost(),80);
                    DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                    BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    StringBuilder sb=new StringBuilder("");
                    //发送请求
                    String request=creatRequest(url);
                    out.writeBytes(request);
                    out.flush();
                    //开始读取数据
                    while((s=in.readLine())!=null){
                        sb.append(s);
                    }
                    in.close();
                    socket.close();
                    return sb.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    tv.setText("输入的地址无法找到请重新输入");
                } catch (IOException e) {
                    e.printStackTrace();
                    tv.setText("读取失败");
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(HtmlActivity.this,"即将开始下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(String s) {
                //告诉说下载完成
                if(s!=null) {
                    Toast.makeText(HtmlActivity.this, "访问完成咯", Toast.LENGTH_SHORT).show();
                    tv.setText(s);
                    //创建文件
                    String state = Environment.getExternalStorageState();
                    if (!state.equals(Environment.MEDIA_MOUNTED)) {
                        System.out.println("SD card is not exit");
                        return;
                    }

                    String Sdcard = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
                    File file = new File(Sdcard+ "myfile.html");
                    //输出一下位置
                       System.out.println("----------------------------"+file.getAbsolutePath()+"------------------------");
                    try {
                        file.createNewFile();
                        OutputStream os= new FileOutputStream(file);
                        //存进去文件中
                        os.write(s.getBytes());
                        os.flush();
                        os.close();
                        et.setText("文件存储地址："+file.getPath());
                    } catch (IOException e) {
                        et.setText("文件下载失败！");
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(HtmlActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }


        }.execute(url);
    }


    //伪装成浏览器的请求字符流生成函数
    public String creatRequest(URL url){
        String request="GET "+url.getPath() +" HTTP/1.1\r\n";
        request+="User-Agent: Mozilla/5.0 "
                + "(Macintosh; Intel Mac OS X 10.8;"
                + " rv:20.0)  Gecko/20100101 Firefox/20.0\r\n";
        request+="Host: "+url.getHost()+"\r\n";
        request+="Connection: close\r\n";
        request+="Accept-Language:"
                + "zh-CN,en;q=0.5\r\n";
        request+="Accept-Encoding: gzip,deflate\r\n";
        request+="Accept: text/html,application/xhtml+xml,"
                + "application/xml;q=0.9,*/*;q=0.8\r\n";
        request+="\r\n";
        return request;
    }
}


