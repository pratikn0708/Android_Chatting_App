package com.pratik.codechatandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by abc on 12/18/2017.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity myActivity;
    private DatabaseReference myDatabaseRef;
    private String myUserName;
    private ArrayList<DataSnapshot> mySnapShot;

    //Child event listener

    private ChildEventListener myListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mySnapShot.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    //Constructor goes here

    public ChatListAdapter(Activity activity, DatabaseReference ref, String name){
        myActivity = activity;
        myUserName = name;
        myDatabaseRef = ref.child("chats");
        mySnapShot = new ArrayList<>();

        //add listener
        myDatabaseRef.addChildEventListener(myListener);

    }

    //static class

    static class ViewHolder{
        TextView senderName;
        TextView chatBody;
        LinearLayout.LayoutParams layoutParams;
    }





    @Override
    public int getCount() {
        return mySnapShot.size();
    }

    @Override
    public InstantMessage getItem(int i) {

        DataSnapshot snapshot = mySnapShot.get(i);
        return snapshot.getValue(InstantMessage.class);


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){

            LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.senderName = (TextView) view.findViewById(R.id.author);
            holder.chatBody = (TextView) view.findViewById(R.id.message);
            holder.layoutParams = (LinearLayout.LayoutParams) holder.senderName.getLayoutParams();

            view.setTag(holder);


        }

        final InstantMessage message = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        //Styling
        boolean isMe = message.getAuthor().equals(myUserName);
        //call a function for styling
        chatRowStyling(isMe, holder);

        String author = message.getAuthor();
        holder.senderName.setText(author);

        String msg = message.getMessage();
        holder.chatBody.setText(msg);






        return view;
    }


    private void chatRowStyling(boolean isItme, ViewHolder holder){
        if (isItme){
            holder.layoutParams.gravity = Gravity.END;
            holder.senderName.setTextColor(Color.BLUE);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_orange);
        } else {
            holder.layoutParams.gravity = Gravity.START;
            holder.senderName.setTextColor(Color.RED);
            holder.chatBody.setBackgroundResource(R.drawable.speech_bubble_green);


        }

        //Forgot this setUp
        holder.senderName.setLayoutParams(holder.layoutParams);
        holder.chatBody.setLayoutParams(holder.layoutParams);
    }

    public void freeUpResources(){
        myDatabaseRef.removeEventListener(myListener);
    }


}









