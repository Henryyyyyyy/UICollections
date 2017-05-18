package me.henry.uicollections.quickindex;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import me.henry.uicollections.R;



public class QuickIndexActivity extends AppCompatActivity {

    private QuickIndexBar quickIndexBar;
    private ListView listView;
    private TextView currentIndexTextView;
    private ArrayList<Friend> friends = new ArrayList<Friend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar1);
        currentIndexTextView = (TextView) findViewById(R.id.current_index_textview);
        listView = (ListView) findViewById(R.id.listview);
        quickIndexBar.setOntouchIndexListener(new QuickIndexBar.onTouchIndexListener() {

            @Override
            public void onTouchIndex(String index) {
                showIndex(index);

                for (int i = 0; i < friends.size(); i++) {
                    String firstWord = friends.get(i).getPinyin().charAt(0)
                            + "";
                    if (firstWord.equals(index)) {
                        listView.setSelection(i);
                        // 只需要第一条，所以break
                        break;
                    }
                }

            }

        });
        filllist();
        // 排序工具类,排序的对象bean要实现implements Comparable<Friend>接口,重写里面的一个方法
        Collections.sort(friends);
        listView.setAdapter(new MyAdapter(friends, this));


    }

    /**
     * 1.我觉得，如果是涉及到网络操作，就要在创建的时候写上 handlermessage，然后在网络操作那里sendmessage
     * 2.如果单纯操作ui那就直接用handler就可以了。
     */
    private Handler handler = new Handler();

    private void showIndex(String index) {
        currentIndexTextView.setVisibility(View.VISIBLE);
        currentIndexTextView.setText(index);
        // 移除所有回调
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                currentIndexTextView.setVisibility(View.GONE);

            }
        }, 2000);
    }

    private void filllist() {
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("啊三"));
        friends.add(new Friend("段与"));
        friends.add(new Friend("段你"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("O"));
        friends.add(new Friend("P"));
        friends.add(new Friend("Q"));
        friends.add(new Friend("R"));
        friends.add(new Friend("S"));

        friends.add(new Friend("T"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("MaggieQ"));
        friends.add(new Friend("赵4"));
        friends.add(new Friend("李伟"));
        friends.add(new Friend("李伟"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("宝强"));
        friends.add(new Friend("李伟"));
        friends.add(new Friend("子龙马"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("还你haha"));
        friends.add(new Friend("海信"));
        friends.add(new Friend("memeda "));
        friends.add(new Friend("李伟"));
        friends.add(new Friend("gost"));
        friends.add(new Friend("曾进"));
        friends.add(new Friend("amandas"));
        friends.add(new Friend("小豆豆"));
        friends.add(new Friend("呵呵哒"));
        friends.add(new Friend("么么哒"));
        friends.add(new Friend("好难听"));
        friends.add(new Friend("停下来"));
        friends.add(new Friend("还起来"));
        friends.add(new Friend("鸡巴"));

    }

    class MyAdapter extends BasicAdapter<Friend> {

        public MyAdapter(ArrayList<Friend> list, Context context) {
            super(list, context);

        }

        MyAdapter.ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View
                        .inflate(context, R.layout.adapter_list, null);

            }
            holder = getHolder(convertView);
            Friend friend = list.get(position);
            // 加个双引号就变成string类型
            String currentFirstWord = friend.getPinyin().charAt(0) + "";
            if (position > 0) {
                String lastFirstWord = list.get(position - 1).getPinyin()
                        .charAt(0)
                        + "";
                if (currentFirstWord.equals(lastFirstWord)) {
                    holder.index.setVisibility(View.GONE);
                } else {
                    holder.index.setVisibility(View.VISIBLE);
                    holder.index.setText(currentFirstWord);
                }
            } else {
                holder.index.setVisibility(View.VISIBLE);
                holder.index.setText(currentFirstWord);
            }

            holder.name.setText(friend.getName());
            return convertView;
        }

        private MyAdapter.ViewHolder getHolder(View convertView) {
            MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new MyAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }

        class ViewHolder {
            TextView index;
            TextView name;

            public ViewHolder(View convertView) {
                index = (TextView) convertView.findViewById(R.id.index);
                name = (TextView) convertView.findViewById(R.id.name);

            }

        }

    }
}
