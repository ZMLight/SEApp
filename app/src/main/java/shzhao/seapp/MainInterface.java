package shzhao.seapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainInterface extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "shzhao.se_app.viewMessage";
    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private int user_id;
    private String userName;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what == 0x123)
            {
                // 设置show组件显示服务器响应
                dialog();
            }
            else if(msg.what == 0x121)
            {
            //fail
            dialog2();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        Intent intent = getIntent();
        user_id = intent.getIntExtra(Login.EXTRA_MESSAGE, 1);
        userName = intent.getStringExtra("username");
        positionTextView = (TextView) findViewById(R.id.position_text_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            //当没有可用的位置提供器时，提示用户,并结束程序
            Toast.makeText(this, "No Location Provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (location != null) {
            showLocation(location);
        }

        try {
            //实时更新地理信息
            locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //设置positionTextView的值并显示
    private void showLocation(Location location) {
        String currentPosition = "latitude is " + location.getLatitude() +
                "\n" + "longitude is " + location.getLongitude() +
                "\n" + "user_id is " + user_id +
                "\n" + "username is " + userName;
        positionTextView.setText(currentPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //关闭程序时将监听器移除
            try {
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void viewMessage(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        double i = location.getLatitude();
        double j = location.getLongitude();
        intent.putExtra("latitude", i);
        intent.putExtra("longitude", j);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_name", userName);
        startActivity(intent);
    }

    public void send_message(View view) {
        //send message

        new Thread()
        {
            @Override
            public void run()
            {
                String response;
                double li = location.getLatitude() * 100;
                double lj = location.getLongitude() * 100;
                int ii = (int) li;
                int jj = (int) lj;
                String addrName = "" + ii + "" + jj;
                EditText message = (EditText) findViewById(R.id.sendmyownmessage);
                String Mess = message.getText().toString();

                String info = "addr="+addrName+"&longitude="+ii+"&latitude="+jj;
                response = GetPostUtil.sendGet(
                        "http://10.187.113.153:80/add_Addr.php"
                        , info);
                Mess = Mess.replaceAll(" ", "%20");
                info = "comment="+Mess+"&addrname="+addrName+"&user="+userName;
                response = GetPostUtil.sendGet(
                        "http://10.187.113.153:80/add_Message.php"
                        , info);
                // 发送消息通知UI线程更新UI组件

                if(response.contains("1"))
                    handler.sendEmptyMessage(0x123);
                else
                    handler.sendEmptyMessage(0x121);

            }
        }.start();
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainInterface.this);
        builder.setMessage("发送成功");

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainInterface.this);
        builder.setMessage("发送失败，请确认网络连接或者权限");

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

