package com.example.thefunzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private EditText userName;

    public static final String PLAYERS_NAME = "playersName";

    CallbackManager callbackManager;
    ShareDialog shareDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        //initialize
        userName = findViewById(R.id.userName);
        Button playButton = findViewById(R.id.playButton);

        //for facebook
        Button shareLinkButton = findViewById(R.id.shareLinkButton);

        //initialize facebook
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote("This is useful link")
                        .setContentUrl(Uri.parse("https://facebook.com"))
                        .build();
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                }
            }
        });


        //To start playing
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userName.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
                }
                else{

                    String storedName = userName.getText().toString();

                    Toast.makeText(getBaseContext(), "Hello " + storedName, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, categoriesPage.class);
                    intent.putExtra(PLAYERS_NAME, storedName);
                    startActivity(intent);

                }
            }
        });

        //share link for facebook



        //to create hash for facebook
        printKeyHash();
    }

    //to create hash for facebook
    private void printKeyHash(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.thefunzone",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //settings in options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);

        return true;
    }

    //go to settings page
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(MainActivity.this, settingsPage.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}
