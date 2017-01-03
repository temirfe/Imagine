package kg.prosoft.imagine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AccountActivity extends Activity {

    SessionManager session;

    public EditText vetEmail;
    public EditText vetPhone;
    public TextView vtvAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        session = new SessionManager(getApplicationContext());

        vetEmail = (EditText) findViewById(R.id.etEmail);
        vetPhone = (EditText) findViewById(R.id.etPhone);
        vtvAccount = (TextView) findViewById(R.id.tvAccountHome);

       /* Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        int phone = intent.getIntExtra("phone", -1);*/

        vetEmail.setText(session.getEmail());
    }

    public void onClickLogout(View v){
        session.logoutUser();
    }
}
