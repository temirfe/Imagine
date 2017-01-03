package kg.prosoft.imagine;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ProsoftPC on 10/12/2016.
 */
public class Post {
    private String username, imgUrl, date, description;
    private ArrayList<HashMap<String,String>> comments;
    private int post_id, user_id, comment_count;

    public Post() {
    }

    public Post(String url, int id, String d, int ccount, ArrayList<HashMap<String,String>> c
    ) {
        //username = name;
        imgUrl = url;
        post_id=id;
        date =d;
        //description = desc;
        comments = c;
        comment_count = ccount;
    }

    public String getName() {
        return username;
    }
    public String getDescription() {
        return description;
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
    public int getId() {
        return post_id;
    }

    public void setUsername(String name) {
        this.username = name;
    }
    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<HashMap<String,String>> getComments() {
        return comments;
    }

    public int getComCount() {
        return comment_count;
    }
    public int setComCount() {
        return comment_count;
    }

    public void setComments(ArrayList<HashMap<String,String>> comments) {
        this.comments = comments;
    }

}
