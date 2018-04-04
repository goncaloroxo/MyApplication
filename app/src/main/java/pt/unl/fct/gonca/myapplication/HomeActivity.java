package pt.unl.fct.gonca.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences userInfo;
    private Context mContext;
    private TextView mWelcome;
    private String mUserRole;
    private View mSMOperations;
    private View mBOMOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getSharedPreferences("AuthToken", Context.MODE_PRIVATE);
        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main2);

        mSMOperations = (View) findViewById(R.id.sm_operations);
        mBOMOperations = (View) findViewById(R.id.bom_operations);

        mContext = this;
        mWelcome = (TextView) findViewById(R.id.welcome);
        mWelcome.setText("Bem vindo, " + userInfo.getString("username", ""));

        mUserRole = userInfo.getString("role","");

        Button mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().clear().apply();
                Toast toast = Toast.makeText(mContext, "Goodbye!", Toast.LENGTH_LONG);
                toast.show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));

            }
        });

        Button mMapButton = (Button) findViewById(R.id.map_button);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

        Button mProfileButton = (Button) findViewById(R.id.profile_button);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfilePage.class));
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUserRole.equals("GS")) {
            mSMOperations.setVisibility(View.VISIBLE);
        }else if (mUserRole.equals("GBO")) {
            mBOMOperations.setVisibility(View.VISIBLE);
        }else {

        }
    }
}
