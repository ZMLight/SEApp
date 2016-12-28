package shzhao.seapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "shzhao.se_app.MainInterface";
    public String username;
    public int user_id;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0x123)
            {
                // 设置show组件显示服务器响应
                launchMainIntf();
            }
            else if(msg.what == 0x121)
            {
                dialog();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void launchMainIntf() {
        Intent intent = new Intent(this, MainInterface.class);
        intent.putExtra(EXTRA_MESSAGE, user_id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void confirm(View view) {

        new Thread()
        {
            @Override
            public void run()
            {
                String response;
                EditText eTUsername = (EditText) findViewById(R.id.Loginusername);
                EditText eTPasswd = (EditText) findViewById(R.id.Loginpasswd);
                String Username = eTUsername.getText().toString();
                String passwd = eTPasswd.getText().toString();
                String info = "user="+Username+"&password="+passwd;
                response = GetPostUtil.sendGet(
                        "http://10.187.113.153:80/check_User.php"
                        , info);
                // 发送消息通知UI线程更新UI组件
                //int num = Integer.parseInt(response);
                //user_id = num;
                username = Username;
                if(response.contains("-1"))
                    handler.sendEmptyMessage(0x121);
                else {
                    int num = 0;
                    int flag = 0;
                    for(int j = 0; j < response.length(); j++) {
                        if(response.charAt(j) >='0' && response.charAt(j) <='9'){
                            num = num*10 + (response.charAt(j) - '0');
                            flag = 1;
                        }
                        else if(flag == 1) break;
                    }
                    user_id = num;
                    handler.sendEmptyMessage(0x123);
                }
            }
        }.start();
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("账号或密码错误");

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

