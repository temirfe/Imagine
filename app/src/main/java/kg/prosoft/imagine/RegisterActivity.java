package kg.prosoft.imagine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    public EditText vetEmail;
    public EditText vetUsername;
    public EditText vetPhone;
    public EditText vetPassword;
    public Button vbRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        vetEmail = (EditText) findViewById(R.id.etEmail);
        vetUsername = (EditText) findViewById(R.id.etUsername);
        vetPhone = (EditText) findViewById(R.id.etPhone);
        vetPassword = (EditText) findViewById(R.id.etPassword);
        vbRegister = (Button) findViewById(R.id.button_register);
    }

    public void onClickRegister(View view){
        final String email=vetEmail.getText().toString();
        final String username=vetUsername.getText().toString();
        final String password=vetPassword.getText().toString();
        final int phone = Integer.parseInt(vetPhone.getText().toString());

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Registration Failed").setNegativeButton("Retry",null).create().show();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(username, email, phone, password, responseListener);
        /*RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);*/
        MyVolley.getInstance(this).addToRequestQueue(registerRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestQueue queue = MyVolley.getInstance(this.getApplicationContext()).
                getRequestQueue();
        queue.cancelAll(this);
    }
}
