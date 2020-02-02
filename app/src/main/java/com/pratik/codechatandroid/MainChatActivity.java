package com.pratik.codechatandroid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {


    private String myUserName;
    private ListView myChatListView;
    private EditText myChatText;
    private ImageButton mySendChatButton;
    private DatabaseReference myDatabaseRef;

    private ChatListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setUpDiplayName();

        myDatabaseRef = FirebaseDatabase.getInstance().getReference();

        //Get UI elements refs
        myChatListView = (ListView) findViewById(R.id.chat_list_view);
        myChatText = (EditText) findViewById(R.id.messageInput);
        mySendChatButton = (ImageButton) findViewById(R.id.sendButton);



        //push chat to firebase on button tap
        mySendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushChatToFirebase();
            }
        });

        //call push method on keyboard event

        myChatText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                pushChatToFirebase();
                return true;
            }
        });



    }

    //Push chat to Firebase

    private void pushChatToFirebase(){
        String chatInput = myChatText.getText().toString();
        if (!chatInput.equals("")){
            InstantMessage chat = new InstantMessage(chatInput,myUserName);
            myDatabaseRef.child("chats").push().setValue(chat);
            myChatText.setText("");
        }
    }



    //Set userName for user

    private void setUpDiplayName(){
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREF, MODE_PRIVATE);
        myUserName = prefs.getString(RegisterActivity.DISPLAY_NAME, null);

        if(myUserName == null){
            myUserName = "user";
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        myAdapter = new ChatListAdapter(this, myDatabaseRef, myUserName);
        myChatListView.setAdapter(myAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.freeUpResources();
    }
}















