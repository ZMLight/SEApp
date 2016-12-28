package shzhao.seapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0x123)
            {
                // 设置show组件显示服务器响应
                dialog1();
            }
            else if(msg.what == 0x121)
            {
                dialog2();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void confirm(View view) {

        new Thread()
        {
            @Override
            public void run()
            {
                String response;
                EditText eTUsername = (EditText) findViewById(R.id.Registerusername);
                EditText eTPasswd = (EditText) findViewById(R.id.Registerpasswd);
                String username = eTUsername.getText().toString();
                String passwd = eTPasswd.getText().toString();
                String info = "user="+username+"&password="+passwd;
                response = GetPostUtil.sendGet(
                        "http://10.187.113.153:80/add_User.php"
                        , info);
                // 发送消息通知UI线程更新UI组件
                if(response.contains("1"))
                    handler.sendEmptyMessage(0x123);
                else
                    handler.sendEmptyMessage(0x121);

            }
        }.start();
    }

    protected void dialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setMessage("注册成功");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        builder.create().show();
    }

    protected void dialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setMessage("注册失败，用户名已存在");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        builder.create().show();
    }
}

