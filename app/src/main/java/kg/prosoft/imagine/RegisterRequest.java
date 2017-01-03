package kg.prosoft.imagine;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ProsoftPC on 9/28/2016.
 */
public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL ="http://temirbek.com/site/register";
    private Map<String, String> params;

    public RegisterRequest(String username, String email, int phone, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email",username);
        params.put("email",email);
        params.put("phone",phone+"");
        params.put("password", password);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }

}
