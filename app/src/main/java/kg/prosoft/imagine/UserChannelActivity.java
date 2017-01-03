package kg.prosoft.imagine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserChannelActivity extends Activity {
    TextView userTextView;
    private String username;
    private String user_id;
    ListView mListView;
    private List<String> userLikes = new ArrayList<String>();
    private List<Post> postList = new ArrayList<Post>();
    private PostListAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_channel);

        mListView = (ListView) findViewById(R.id.postListView);
        adapter = new PostListAdapter(this, postList, userLikes);
        mListView.setAdapter(adapter);


        userTextView = (TextView) findViewById(R.id.text_view_username);
        if (savedInstanceState != null) {
            username = savedInstanceState.getString("username");
            user_id = savedInstanceState.getString("user_id");
            Log.i("INSTANCE is not null", "was saved "+username);
        }
        else{
            Intent intent=getIntent();
            username=intent.getStringExtra("username");
            user_id=intent.getStringExtra("user_id");
            Log.i("getIntent", "wasn't saved "+username);
        }
        userTextView.setText(username);
        getUserLikes();
        showUserPosts(user_id);
        showLoading();
    }

    private void showLoading(){
        loading = new ProgressDialog(this);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading...");
        loading.show();
    }

    public void showUserPosts(String user_id){
        String uri = String.format("http://temirbek.com/site/user-post?user_id=%1$s",user_id);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    boolean success = response.getBoolean("success");
                    if(success){
                        JSONArray ja = response.getJSONArray("posts");
                        for(int i=0; i < ja.length(); i++){
                            JSONObject jsonObject = ja.getJSONObject(i);
                            String url=jsonObject.getString("photo");
                            String mdate=jsonObject.getString("date");
                            int post_id=jsonObject.getInt("id");
                            int comment_count=jsonObject.getInt("comment_count");

                            ArrayList<HashMap<String,String>> comments = new ArrayList<>();
                            HashMap<String,String> comhash = new HashMap<>();
                            comhash.put(jsonObject.getString("title"),jsonObject.getString("text"));
                            comments.add(comhash);

                            JSONArray commentsArray = jsonObject.getJSONArray("comments");
                            if(commentsArray!=null && commentsArray.length()>0){
                                for(int j=0; j < commentsArray.length(); j++){
                                    JSONObject jsonComObject = commentsArray.getJSONObject(j);
                                    HashMap<String,String> comhash2 = new HashMap<>();
                                    comhash2.put(jsonComObject.getString("username"),jsonComObject.getString("text"));
                                    comments.add(comhash2);
                                }
                            }

                            Post post = new Post(url, post_id, mdate, comment_count, comments);
                            postList.add(post);
                        }
                    }
                    else{
                        Log.i("NO SUCCESS", "UserChannelActivity/showUserPosts");
                    }


                }catch(JSONException e){e.printStackTrace();}
                loading.dismiss();
                adapter.notifyDataSetChanged();
            }
        };

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, uri, null, listener,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        jor.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyVolley.getInstance(this.getApplicationContext()).addToRequestQueue(jor);
    }

    public void getUserLikes(){
        final int user_id=MainActivity.USER_ID;
        String uri = String.format("http://temirbek.com/site/user-likes?user_id=%1$s",user_id);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    boolean success = response.getBoolean("success");
                    if(success){
                        JSONArray ja = response.getJSONArray("likes");
                        for(int i=0; i < ja.length(); i++){
                            JSONObject jsonObject = ja.getJSONObject(i);
                            String post_id=jsonObject.getString("post_id");
                            userLikes.add(post_id);
                        }
                    }

                }catch(JSONException e){e.printStackTrace();}
            }
        };

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, uri, null, listener, null);
        MyVolley.getInstance(this.getApplicationContext()).addToRequestQueue(jor);
    }

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("username", username);

        Log.i("onSaveInstanceState", "save instance state is run "+username);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String fileUri = savedInstanceState.getString("username");
        Log.i("onRestoreInstanceState", "restore is run "+fileUri);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestQueue queue = MyVolley.getInstance(this.getApplicationContext()).
                getRequestQueue();
        queue.cancelAll(this);
    }
}
