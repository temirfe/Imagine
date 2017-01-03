package kg.prosoft.imagine;

import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {

    SessionManager session;
    TextView tvWelcome;
    public static int USER_ID;
    public static String USER_NAME;
    //private CoordinatorLayout coordinatorLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        tvWelcome = (TextView) findViewById(R.id.welcome_text_view);
        tvWelcome.setText(session.getEmail());
        if(USER_ID==0){USER_ID=session.getUserId();}
        if(USER_NAME==null){USER_NAME=session.getUsername();}

        RichBottomNavigationView botNav = (RichBottomNavigationView)
                findViewById(R.id.bottom_navigation);

        botNav.setOnNavigationItemSelectedListener(
                new RichBottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        //.getMenu().findItem(R.id.recent_item).setChecked(false);
                        switch (item.getItemId()) {
                            case R.id.home_item:
                                Log.i("BottomNav","Recent");
                                //item.setChecked(true);
                                break;
                            case R.id.search_item:
                                Log.i("BottomNav","Location");
                                //item.setChecked(true);
                                break;
                            case R.id.post_item:
                                Log.i("BottomNav","Location");
                                //item.setChecked(true);
                                break;
                            case R.id.favorite_item:
                                Log.i("BottomNav","Favorite");
                                //item.setChecked(true);
                                break;
                            case R.id.account_item:
                                Log.i("BottomNav","Favorite");
                                //item.setChecked(true);
                                break;
                        }
                        return true;
                    }
                });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void onClickToAccount(View v) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void onClickUserList(View v) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    public void onClickPhoto(View v) {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    public void onClickCamera(View v) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_inbox:
                Intent intent = new Intent(this, InboxActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://kg.prosoft.imagine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://kg.prosoft.imagine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
