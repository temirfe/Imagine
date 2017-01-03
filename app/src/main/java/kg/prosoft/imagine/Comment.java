package kg.prosoft.imagine;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ProsoftPC on 10/19/2016.
 */
public class Comment {
    private int id;
    private int post_id;
    private int user_id; //id of user who left comment
    private String username; //username of user who left comment
    private String text; //comment text
    private String date;

    public Comment(int id, int post_id, int user_id, String username, String text, String date) {
        this.id = id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.username = username;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getPost_id() {
        return post_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        long timeNow = System.currentTimeMillis();
        long timeThen;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(date==null){
                timeThen=timeNow;
            }
            else{
                Date dateObj = formatter.parse(date);
                timeThen=dateObj.getTime();
            }
            return DateUtils.getRelativeTimeSpanString(timeThen, timeNow,
                    DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
                    .toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "getDate() error";
    }
}
