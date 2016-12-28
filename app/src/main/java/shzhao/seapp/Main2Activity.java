package shzhao.seapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private ListView mListView;
    private View mCommentView;
    private MyAdapter myAdapter;
    private double latitude;
    private double longitude;
    private int user_id;
    private String USERNAME;
    private String response;

    ArrayList<Item> data = new ArrayList<>();
    ArrayList<Comment> replys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        USERNAME = bundle.getString("user_name");
        user_id = bundle.getInt("user_id");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");
        mListView = (ListView) findViewById(R.id.listview);
        try {
            myAdapter = new MyAdapter(this, getData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mListView.setAdapter(myAdapter);

        mCommentView = findViewById(R.id.comment_view);
    }

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
        }
    };

    // build data
    private ArrayList<Item> getData() throws JSONException {
        //getData TODO
        new Thread()
        {
            @Override
            public void run()
            {
                double li = latitude * 100;
                double lj = longitude * 100;
                int ii = (int) li;
                int jj = (int) lj;
                String addrName = "" + ii + "" + jj;

                String info = "addr="+addrName+"&longitude="+ii+"&latitude="+jj;
                response = GetPostUtil.sendGet(
                        "http://10.187.113.153:80/add_Addr.php"
                        , info);
                info = "addrname="+addrName+"&start="+6 /*"addrname="+addrName+"&start="+5*/;
                response = GetPostUtil.sendGet(
                        "http://10.187.113.153:80/check_Message.php"
                        , info);
                // 发送消息通知UI线程更新UI组件
                String temp="";
                int tmpCount = 0;
                int flag= 0;
                for(int i = 0; i < response.length(); i++) {
                    if(response.charAt(i) == '[') {
                        flag = 1;
                        tmpCount += 1;
                        temp = temp + response.charAt(i);
                        continue;
                    }
                    else if(response.charAt(i) == ']') {
                        tmpCount -= 1;
                        temp = temp + response.charAt(i);
                        if(tmpCount == 0)
                            break;
                    }
                    else if(flag == 1) {
                        temp = temp + response.charAt(i);
                    }
                }
                response = temp;
                String userName="";
                String comment="";
                int likenum=0;
                int co_id=0;
                int msg_id=0;
                String reply="";

                JSONArray f = null;
                try {
                    f = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i != f.length(); ++i) {
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(f.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        msg_id = (int) jo.get("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        userName = (String) jo.get("userName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        comment = (String) jo.get("comment");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        likenum = (int) jo.get("likenum");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String tmp = null;
                    try {
                        tmp = (String) (jo.get("reply"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray ff = null;
                    try {
                        ff = new JSONArray(tmp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    replys = new ArrayList<>();
                    for (int j = 0; j != ff.length(); ++j) {
                        JSONObject jojo = null;
                        try {
                            jojo = new JSONObject(ff.getString(j));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            co_id = (int) jojo.get("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            reply = (String) jojo.get("comment");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        replys.add(new Comment(co_id,reply));
                    }

                    data.add(new Item(msg_id, R.drawable.user_icon2, userName, comment, likenum+"likes", replys));
                }
            }
        }.start();

        SystemClock.sleep(1000);
        //String response1 = "[\"{\\\"id\\\":14,\\\"userName\\\":\\\"zmtest8\\\",\\\"user_id\\\":13,\\\"comment\\\":\\\"this is test5\\\",\\\"likenum\\\":0,\\\"addr\\\":\\\"6596-1853\\\",\\\"reply\\\":\\\"[]\\\"}\",\"{\\\"id\\\":13,\\\"userName\\\":\\\"zmtest8\\\",\\\"user_id\\\":13,\\\"comment\\\":\\\"this is test4\\\",\\\"likenum\\\":0,\\\"addr\\\":\\\"6596-1853\\\",\\\"reply\\\":\\\"[]\\\"}\",\"{\\\"id\\\":12,\\\"userName\\\":\\\"zmtest8\\\",\\\"user_id\\\":13,\\\"comment\\\":\\\"this is test3\\\",\\\"likenum\\\":0,\\\"addr\\\":\\\"6596-1853\\\",\\\"reply\\\":\\\"[]\\\"}\",\"{\\\"id\\\":11,\\\"userName\\\":\\\"zmtest8\\\",\\\"user_id\\\":13,\\\"comment\\\":\\\"this is test2\\\",\\\"likenum\\\":0,\\\"addr\\\":\\\"6596-1853\\\",\\\"reply\\\":\\\"[]\\\"}\",\"{\\\"id\\\":10,\\\"userName\\\":\\\"zmtest8\\\",\\\"user_id\\\":13,\\\"comment\\\":\\\"this is test1\\\",\\\"likenum\\\":0,\\\"addr\\\":\\\"6596-1853\\\",\\\"reply\\\":\\\"[]\\\"}\"]";

//        replys = new ArrayList<>();
//        data.add(new Item(R.drawable.user_icon2, "薄荷栗", response, "0 likes", replys));
//        data.add(new Item(R.drawable.user_icon2, "欣然", "走遍天涯海角，唯有我家风景最好，啊哈哈", "昨天",replys));
//        data.add(new Item(R.drawable.user_icon2, "陈磊_CL", "老子以后要当行长的，都来找我借钱吧，now", "昨天",replys));
//        data.add(new Item(R.drawable.user_icon2, "永恒依然", "房子车子都到碗里来", "昨天",replys));
//        data.add(new Item(R.drawable.user_icon2, "蓝珊", "你们这群傻×，我笑而不语", "昨天",replys));
        return data;
    }
    protected void dialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
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
    // custom adapter
    private class MyAdapter extends BaseAdapter implements ItemView.OnCommentListener {

        private Context context;
        private ArrayList<Item> mData;
        private Map<Integer, ItemView> mCachedViews = new HashMap<>();

        public MyAdapter(Context context, ArrayList<Item> mData) {
            this.context = context;
            this.mData = mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;

            if (convertView != null) {
                view = convertView;
            } else {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listview_item, null, false);
            }

            if (view instanceof ItemView) {
                Item data = (Item) getItem(position);
                ((ItemView) view).setData(data);
                ((ItemView) view).setPosition(position);
                ((ItemView) view).setCommentListener(this);

                cacheView(position, (ItemView) view);
            }

            return view;
        }

        @Override
        public void onComment(int position) {
            showCommentView(position);
        }

        @Override
        public void onLike(int position) {
            appendLike(position);
        }

        private void cacheView(int position, ItemView view) {
            Iterator<Map.Entry<Integer, ItemView>> entries = mCachedViews.entrySet().iterator();

            while (entries.hasNext()) {

                Map.Entry<Integer, ItemView> entry = entries.next();
                if (entry.getValue() == view && entry.getKey() != position) {
                    mCachedViews.remove(entry.getKey());
                    break;
                }
            }

            mCachedViews.put(position, view);
        }

        private void appendLike(int position) {
            String s = mData.get(position).getCreatedAt();
            int num = 0;
            for(int i = 0; i < s.length(); i++) {
                if(s.charAt(i)>='0' && s.charAt(i)<='9') {
                    num = num * 10 + (s.charAt(i) - '0');
                }
            }
            num += 1;
            String ss = "" + num + "likes";
            mData.get(position).changeCreatedAt(ss);
            ItemView itemView = mCachedViews.get(position);
            if (itemView != null && position == itemView.getPosition()) {
                itemView.addLike(ss);
            }

            final int msg_id = mData.get(position).getMessageId();

            new Thread()
            {
                @Override
                public void run()
                {
                    String response;
                    String info = "comment_id="+msg_id;
                    response = GetPostUtil.sendGet(
                            "http://10.187.113.153:80/like.php"
                            , info);
                    // 发送消息通知UI线程更新UI组件
                }
            }.start();
        }
        private void showCommentView(final int position) {
            mCommentView.setVisibility(View.VISIBLE);

            mCommentView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et = (EditText) mCommentView.findViewById(R.id.edit);
                    final String s = et.getText().toString();

                    if (!TextUtils.isEmpty(s)) {

                        // update model
                        Comment comment = new Comment(user_id, s);
                        mData.get(position).getComments().add(comment);

                        // update view maybe
                        ItemView itemView = mCachedViews.get(position);

                        if (itemView != null && position == itemView.getPosition()) {
                            itemView.addComment();
                        }


                        et.setText("");
                        mCommentView.setVisibility(View.GONE);
                    }

                    final int msg_id = mData.get(position).getMessageId();

                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            String response;
                            String sss = s.replaceAll(" ","%20");
                            String info = "user="+USERNAME+"&comment="+sss+"&reid="+msg_id;
                            response = GetPostUtil.sendGet(
                                    "http://10.187.113.153:80/add_Reply.php"
                                    , info);
                            // 发送消息通知UI线程更新UI组件
                        }
                    }.start();
                }
            });
        }
    }
}

