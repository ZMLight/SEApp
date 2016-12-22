package shzhao.seapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "shzhao.se_app.MainInterface";
    public final static String USERNAME = "zhaoming";
    public final static String PASSWD = "zm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
    }

    public void confirm(View view) {
        Intent intent = new Intent(this, MainInterface.class);
        //Intent intent2 = new Intent(this, Notation1.class);
        EditText eTUsername = (EditText) findViewById(R.id.Loginusername);
        EditText eTPasswd = (EditText) findViewById(R.id.Loginpasswd);
        String username = eTUsername.getText().toString();
        String passwd = eTPasswd.getText().toString();
        //Server myserver = new Server();
        if(username.equals(USERNAME) && passwd.equals(PASSWD)) {/*myserver.check_user(username, passwd) > 0*/
            int user_id = 1/*myserver.check_user(username, passwd)*/;
            //String message = user_id + "";
            intent.putExtra(EXTRA_MESSAGE, user_id);
            startActivity(intent);
        }
        else {
            dialog();
        }
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

