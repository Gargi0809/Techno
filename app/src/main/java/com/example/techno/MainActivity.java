package com.example.techno;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private LoginButton Loginbutton;
    private CircleImageView circleImageView;
    private TextView textname,textemail;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Loginbutton=findViewById(R.id.login_button);
        circleImageView=findViewById(R.id.profile_photo);
        textname=findViewById(R.id.profile_name);
        textemail=findViewById(R.id.profile_email);

        callbackManager=CallbackManager.Factory.create();
        Loginbutton.setPermissions(Arrays.asList("email","public_profile"));

        Loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null)
            {
                textname.setText(" ");
                textemail.setText(" ");
                circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this,"User logged out!!",Toast.LENGTH_LONG).show();
            }
            else
                loaduserProfile(currentAccessToken);

        }
    };

    private  void loaduserProfile(AccessToken newAccessToken)
    {
        GraphRequest request= GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String fname=object.getString("first name");
                    String lname=object.getString("last name");
                    String email=object.getString("email id");
                    String id=object.getString("id");

                    String image_url="https://graph.facebook.com/"+id+"/pictures?type=normal";

                    textemail.setText(email);
                    textname.setText(fname+" "+lname);
                    RequestOptions requestOptions=new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(MainActivity.this).load(image_url).into(circleImageView);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });
        Bundle para=new Bundle();
        para.putString("fields","first_name,last_name,email,id");
        request.setParameters(para);
        request.executeAsync();

    }
}