package shzhao.seapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Intent intent = getIntent();
    }

    public void confirm(View view) {
        //Intent intent = new Intent(this, MainInterface.class);
        //Server myserver = new Server();
        EditText eTUsername = (EditText) findViewById(R.id.Registerusername);
        EditText eTPasswd = (EditText) findViewById(R.id.Registerpasswd);
        String username = eTUsername.getText().toString();
        String passwd = eTPasswd.getText().toString();
        java.util.Random random=new java.util.Random();
        int i = random.nextInt(10);
        if ((i++)%2 == 1) { /*test  myserver.add_user(username, passwd)*/
            dialog1();
        } else {
            dialog2();
        }
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

