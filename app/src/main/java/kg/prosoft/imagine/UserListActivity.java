package kg.prosoft.imagine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends Activity {
    //ArrayList<String> userlist = new ArrayList<String>();
    //ArrayAdapter<String> arrayAdapter;
    ListView mListView;

    ArrayList<HashMap<String, String>> countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mListView = (ListView) findViewById(R.id.userListView);
        showUserList();

        mListView.setOnItemClickListener(itemClickListener);
    }


    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> listView,
                                View itemView,
                                int position,
                                long id) {
            HashMap<String, String> item = countries.get(position);
            Intent intent = new Intent(UserListActivity.this, UserChannelActivity.class);
            intent.putExtra("username",item.get("username"));
            intent.putExtra("user_id",item.get("user_id"));
            startActivity(intent);
        }
    };

    public void showUserList(){
        String uri = "http://temirbek.com/site/users";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    JSONArray ja = response.getJSONArray("users");
                    countries = new ArrayList<HashMap<String, String>>();
                    for(int i=0; i < ja.length(); i++){
                        JSONObject jsonObject = ja.getJSONObject(i);
                        // int id = Integer.parseInt(jsonObject.optString("id").toString());
                        //userlist.add(jsonObject.getString("username"));

                        HashMap<String, String> country = new HashMap<String, String>();
                        country.put("user_id", jsonObject.getString("id"));
                        country.put("username", jsonObject.getString("username"));
                        countries.add(country);
                    }

                    //arrayAdapter = new ArrayAdapter<String>(UserListActivity.this, android.R.layout.simple_list_item_1, userlist);
                    //userList.setAdapter(arrayAdapter);

                    SimpleAdapter adapter = new SimpleAdapter(UserListActivity.this, countries,
                            R.layout.user_item, new String[] {"username"},new int[] {R.id.username});

                    mListView.setAdapter(adapter);
                }catch(JSONException e){e.printStackTrace();}
            }
        };

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, uri, null, listener, null);
        /*RequestQueue queue = Volley.newRequestQueue(UserListActivity.this);
        queue.add(jor);*/

        MyVolley.getInstance(this).addToRequestQueue(jor);
    }
}
