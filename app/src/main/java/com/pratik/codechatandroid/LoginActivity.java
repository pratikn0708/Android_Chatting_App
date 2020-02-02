package com.pratik.codechatandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    //Ref to firebase
    private FirebaseAuth myAuth;

    //UI refs
    private EditText myEmail;
    private EditText myPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Grab data
        myEmail= (EditText)findViewById(R.id.login_email);
        myPassword = (EditText) findViewById(R.id.login_password);


        //Get firebase instance
        myAuth  = FirebaseAuth.getInstance();

    }

    //SignIn button was tapped
    public void signinUser(View v)
    {
        loginUserWithFirebase();
    }

    //Login user with firebase

    private void loginUserWithFirebase(){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(getApplicationContext()," Something Is Missing!!",Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext()," Logging you in...",Toast.LENGTH_LONG).show();

        myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("FINDCODEL","Was user Logged in:" + task.isSuccessful());

                if (!task.isSuccessful())
                {
                    showErrorbox(" There is problem in logging in");
                    Log.i("FINDCODE","Message: " + task.getException());
                } else{
                    Intent intent = new Intent(LoginActivity.this,MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }


            }
        });






    }

    //Move user to register activity
    public  void registerNewUser(View v)
    {
        Intent intent = new Intent(this,RegisterActivity.class);
        finish();
        startActivity(intent);
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
