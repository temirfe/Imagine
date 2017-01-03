package kg.prosoft.imagine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnKeyListener {

    SessionManager session;

    public EditText emailField;
    public EditText passField;
    public Button loginButton;
    public TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        emailField = (EditText) findViewById(R.id.etEmail);
        passField = (EditText) findViewById(R.id.etPassword);
        loginButton = (Button) findViewById(R.id.button_login);
        registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(regIntent);
            }
        });

        emailField.setOnKeyListener(this);
        passField.setOnKeyListener(this);

    }

    public void onClickLogin(View view){
        final String email=emailField.getText().toString();
        final String password=passField.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        String username = jsonResponse.getString("username");
                        int user_id = jsonResponse.getInt("id");
                        int phone = jsonResponse.getInt("phone");

                        session.createLoginSession(username,email, user_id);

                        Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("username", username);
                        intent.putExtra("phone", phone);
                        intent.putExtra("email", email);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login Failed").setNegativeButton("Retry",null).create().show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
        MyVolley.getInstance(this).addToRequestQueue(loginRequest);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_ENTER){
            if(v.getId()==R.id.etPassword){
                onClickLogin(v);
            }
            /*if(TextUtils.isEmpty(emailField.getText())){
                Log.i("field","email is empty");
            }
            */
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestQueue queue = MyVolley.getInstance(this.getApplicationContext()).
                getRequestQueue();
        queue.cancelAll(this);
    }
}
