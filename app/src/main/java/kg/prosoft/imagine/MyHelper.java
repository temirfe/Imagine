package kg.prosoft.imagine;

import android.content.Context;
import android.net.ParseException;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ProsoftPC on 10/20/2016.
 */
public class MyHelper {

    public static CharSequence formatTime(Context context, String time) {
        long timestamp = 0;
        long MINUTE=60;
        long HOUR=3600;
        long DAY=86400;
        long WEEK=604800;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(time);
            timestamp = date.getTime();
        } catch (ParseException e) {
            Log.w("MY HELPER", "can't parse time!");
            return time;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        final long now = System.currentTimeMillis();
        final long timeGap = (now - timestamp) / 1000;
        if (timeGap < 0) {
            return time;
        } else if (timeGap < MINUTE) {
            return DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.SECOND_IN_MILLIS);
        } else if (timeGap < HOUR) {
            return DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.MINUTE_IN_MILLIS);
        } else if (timeGap < DAY) {
            return DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.HOUR_IN_MILLIS);
        } else if (timeGap < WEEK) {
            return DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.DAY_IN_MILLIS);
        } else if (timeGap < WEEK * 4) {
            return DateUtils.getRelativeTimeSpanString(timestamp, now, DateUtils.WEEK_IN_MILLIS);
        } else {
            return time;
        }
    }
}
