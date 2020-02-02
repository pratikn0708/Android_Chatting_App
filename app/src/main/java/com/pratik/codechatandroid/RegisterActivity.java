package com.pratik.codechatandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREF="ChatPref";
    public static final String DISPLAY_NAME = "Username";

    //Ref to fields
    private AutoCompleteTextView myUserNameView;
    private EditText myEmail;
    private EditText myPassword;
    private EditText myPasswordConfirm;

    //firebase reference
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get values on create
        myEmail=(EditText)findViewById(R.id.register_email);
        myUserNameView=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        myPassword=(EditText)findViewById(R.id.register_password);
        myPasswordConfirm=(EditText)findViewById(R.id.register_confirm_password);


        //Get a hold of firebase instance
        myAuth=FirebaseAuth.getInstance();


    }

    //Method call by tapping
    public void signUp(View v)
    {
        regiterUser();
    }


    //Actual registration happens here
    private void regiterUser(){
        myEmail.setError(null);
        myPassword.setError(null);
        myPasswordConfirm.setError(null);

        //Grab values
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //password validation
        if (!TextUtils.isEmpty(password) && !checkPassword(password))
        {
            myPassword.setError(getString(R.string.invalid_password));
            focusView = myPassword;
            cancel = true;

        }

        //email validation
        if (!TextUtils.isEmpty(email) && !checkEmail(email)){
            myEmail.setError(getString(R.string.invalid_email));
            focusView = myEmail;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else {
            createUser();
        }



    }

    //validation for email
    private boolean checkEmail(String email)
    {
        return email.contains("@");
    }

    //validation for password
    private boolean checkPassword(String password){
        String confPassword=myPasswordConfirm.getText().toString();
        return confPassword.equals(password) && password.length() > 4;
    }


    // sign in user at firebase
    private  void createUser(){
        // Grab values
        String email=myEmail.getText().toString();
        String password= myPassword.getText().toString();

        // call method from firebase
        myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("FINDCODE","User creation was:" + task.isSuccessful() );

                if(!task.isSuccessful())
                {
                    showErrorbox("Oops registration failed!!");
                }
                else
                {
                    saveUserName();
                    Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        });
    }


    //use shared pref for usernames

    private void saveUserName(){
        String userName=myUserNameView.getText().toString();
        SharedPreferences pref = getSharedPreferences(CHAT_PREF,0);
        pref.edit().putString(DISPLAY_NAME,userName).apply();

    }

    //create errorbox for errors
    private void showErrorbox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Heyyyy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
