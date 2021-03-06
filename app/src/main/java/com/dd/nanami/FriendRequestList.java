package com.dd.nanami;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dd.nanami.function.Control;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class FriendRequestList extends AppCompatActivity {

    Activity activity;

    Control control;                    //控制方法
    LinearLayout friendRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = (Control) getApplication();
        activity = this;
        setContentView(R.layout.activity_friend_request_list);
        friendRequestList = findViewById(R.id.friendRequestList);

        new Thread(()->{
            try {
                JSONObject json = new JSONObject(control.getFriendRequestList());
                Iterator<String> iterator = json.keys();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    activity.runOnUiThread(()-> addFriendRequest(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void addFriendRequest(String id) {
        LayoutInflater factory=LayoutInflater.from(FriendRequestList.this);
        @SuppressLint("InflateParams") View entryView=factory.inflate(R.layout.view_reques, null);
        ImageView headImage = entryView.findViewById(R.id.head);
        control.setUserHead(id,headImage,this);
        TextView newsName=entryView.findViewById(R.id.Name);
        newsName.setText(control.getUserName(id));
        Button ok = entryView.findViewById(R.id.ok);
        ok.setOnClickListener((view)->{
            control.agreeFriendRequest(Long.parseLong(id));
            friendRequestList.removeView(entryView);
        });
        friendRequestList.addView(entryView);
    }
}