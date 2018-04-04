package pt.unl.fct.gonca.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {

    private TextView mUsernameView;
    private TextView mEmailView;
    private TextView mAddressView;
    private SharedPreferences userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        mUsernameView = (TextView) findViewById(R.id.username_text);
        mUsernameView.setText(userInfo.getString("username",""));
        mEmailView = (TextView) findViewById(R.id.email_text);
        mEmailView.setText(userInfo.getString("email",""));
        mAddressView = (TextView) findViewById(R.id.address_text);
        mAddressView.setText(userInfo.getString("address",""));

        Button mGoBackButton = (Button) findViewById(R.id.go_back_button);
        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this, HomeActivity.class));
            }
        });
    }
}
