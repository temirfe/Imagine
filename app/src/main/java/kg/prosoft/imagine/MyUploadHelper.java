package kg.prosoft.imagine;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by ProsoftPC on 10/5/2016.
 */
public class MyUploadHelper {

    Context context;
    Bitmap bitmap;
    SessionManager session;
    String description;

    public MyUploadHelper(Context context, Bitmap bitmap, String description){
        this.context = context;
        this.bitmap = bitmap;
        this.description=description;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadToServer(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(context, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://temirbek.com/site/upload",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        volleyError.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                String image = getStringImage(bitmap);

                session = new SessionManager(context.getApplicationContext());

                //Adding parameters
                params.put("photo", image);
                params.put("username", MainActivity.USER_NAME);
                params.put("user_id", Integer.toString(MainActivity.USER_ID));
                params.put("description", description);

                //returning parameters
                return params;
            }
        };

        MyVolley.getInstance(context).addToRequestQueue(stringRequest);
    }

}
