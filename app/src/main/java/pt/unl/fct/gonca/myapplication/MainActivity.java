package pt.unl.fct.gonca.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {


    /**
     * Keep track of the authentication task to ensure we can cancel it if requested.
     */
    private MainActivity.LoginAuthTask mLoginAuthTask = null;


    private SharedPreferences sharedPref;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mContext = this;
        sharedPref = getSharedPreferences("AuthToken", Context.MODE_PRIVATE);
    }

    protected void onStart() {
        super.onStart();
        attemptAuth();
    }


    /**
     * Attempts to check if the token is consistent with the database.
     * If there are form errors, the user is taken to login screen
     * Else user proceeds to .
     */
    public void attemptAuth() {
        if (mLoginAuthTask != null) {
            return;
        }
        // Store values at the time of the login attempt.
        String username = sharedPref.getString("username", "");
        String tokenID = sharedPref.getString("tokenID", "");
        long creationData = sharedPref.getLong("creationData",0);
        long expirationData = sharedPref.getLong("expirationData",0);



            // Show a progress spinner, and kick off a background task to
            // perform the user authentication attempt.

            mLoginAuthTask = new LoginAuthTask(username, tokenID, creationData, expirationData);
            mLoginAuthTask.execute((Void) null);

    }






    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class LoginAuthTask extends AsyncTask<Void, Void, String> {

        private final String mUsername;
        private final String mTokenID;
        private final long mCreationData;
        private final long mExpirationData;

        LoginAuthTask(String username, String tokenID, long creationData, long expirationData) {
            mUsername = username;
            mTokenID = tokenID;
            mCreationData = creationData;
            mExpirationData = expirationData;
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        @Override
        protected void onPreExecute() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                cancel(true);
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //TODO: create JSON object with credentials and call doPost

                JSONObject authToken = new JSONObject();
                authToken.put("username", mUsername);
                authToken.put("tokenID", mTokenID);
                authToken.put("creationData", mCreationData);
                authToken.put("expirationData", mExpirationData);

                URL url = new URL("https://centered-inn-196103.appspot.com/rest/login/validation");

                String s = RequestsREST.doPOST(url, authToken);
                System.out.println("Strrrrriiiing - " + s.toString());
                return s;
            } catch (Exception e) {
                return e.toString();
            }
        }


        @Override
        protected void onPostExecute(final String result) {
            mLoginAuthTask = null;

            if (result != null) {
                String response = null;
                try  {
                    // We parse the result
                    response = result;
                    Log.i("MainActivity", response.toString());
                    if(response.equals("true")) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }

                } catch (Exception e) {
                    // WRONG DATA SENT BY THE SERVER
                    Log.e("Authentication",e.toString());
                }
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        }

        @Override
        protected void onCancelled() {
            mLoginAuthTask = null;

        }
    }
}
