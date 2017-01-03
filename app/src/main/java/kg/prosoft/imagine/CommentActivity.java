package kg.prosoft.imagine;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends Activity {

    ImageButton imageButton;
    EditText editText;
    ListView listView;
    CommentListAdapter adapter;
    List<Comment> mCommentList;
    private int post_id;
    private int user_id;
    private int range=0;
    private String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        Intent intent = getIntent();
        post_id = intent.getIntExtra("post_id",0);
        user_id = intent.getIntExtra("user_id",0);
        user_name = intent.getStringExtra("user_name");

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setEnabled(false);
        editText = (EditText) findViewById(R.id.editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnScrollListener(onScrollDo);

        mCommentList = new ArrayList<Comment>();
        adapter = new CommentListAdapter(getApplicationContext(),mCommentList);
        listView.setAdapter(adapter);
        populateList(post_id,range);
    }

    AbsListView.OnScrollListener onScrollDo = new AbsListView.OnScrollListener() {
        private int currentVisibleItemCount;
        private int currentScrollState;
        private int currentFirstVisibleItem;
        private int totalItem;


        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            this.currentScrollState = scrollState;
            this.isScrollCompleted();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            this.currentFirstVisibleItem = firstVisibleItem;
            this.currentVisibleItemCount = visibleItemCount;
            this.totalItem = totalItemCount;
        }

        private void isScrollCompleted() {
            if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                    && this.currentScrollState == SCROLL_STATE_IDLE) {
                /** To do code here **/
                Log.i("ENDDD", "reached");
                range++;
                populateList(post_id,range);
            }
        }
    };

    /*@Override
    public Intent getParentActivityIntent () {
        Intent intent = super.getParentActivityIntent();
        assert intent != null;
        intent.putExtra("username", "asdf");
        return intent;
    }*/
    public void populateList(int post_id, int range){
        Log.i("POST ID", Integer.toString(post_id));
        String uri = String.format("http://temirbek.com/site/comment-list?post_id=%1$s&range=%2$s",post_id, range);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    boolean success = response.getBoolean("success");
                    if(success){
                        JSONArray ja = response.getJSONArray("comments");
                        String rangeTest=response.getString("range");
                        Log.i("RANGE", rangeTest);
                        for(int i=0; i < ja.length(); i++){
                            JSONObject jsonObject = ja.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            int post_id = jsonObject.getInt("post_id");
                            int user_id = jsonObject.getInt("user_id");
                            String username=jsonObject.getString("username");
                            String text=jsonObject.getString("text");
                            String date = jsonObject.getString("ts");

                            Comment comment = new Comment(id, post_id, user_id, username,text, date);
                            mCommentList.add(comment);
                        }
                    }
                    else{
                        Log.i("NO SUCCESS", "no comment");
                    }

                }catch(JSONException e){e.printStackTrace();}

                adapter.notifyDataSetChanged();
            }
        };

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, uri, null, listener, null);
        MyVolley.getInstance(this.getApplicationContext()).addToRequestQueue(jor);
    }

    @Override
    public boolean onNavigateUp () {
        onBackPressed();
        return true;
    }

    public void onClickComment(View v){
        String text = editText.getText().toString();
        Comment c = new Comment(0,post_id,user_id,user_name,text,null);
        mCommentList.add(c);
        adapter.notifyDataSetChanged();
        editText.setText("");
        sendComment(text);
    }

    public void sendComment(final String text){
        String url="http://temirbek.com/site/comment-post";
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    Log.i("Comment post ACTION","SUCCESS");
                }
                else{
                    Log.i("Comment ACTION","FAIL: "+response);
                }
            }

        };

        StringRequest sReq = new StringRequest(Request.Method.POST, url,responseListener, null){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_id",Integer.toString(post_id));
                params.put("user_id",Integer.toString(user_id));
                params.put("text",text);
                params.put("username",user_name);

                return params;
            }
        };
        MyVolley.getInstance(this.getApplicationContext()).addToRequestQueue(sReq);
    }

    public void enableSubmitIfReady(Editable ed) {

        boolean isReady =ed.length()>0;
        if(isReady){
            imageButton.setEnabled(true);
        }
        else{
            imageButton.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestQueue queue = MyVolley.getInstance(this.getApplicationContext()).
                getRequestQueue();
        queue.cancelAll(this);
    }

}
