package kg.prosoft.imagine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ProsoftPC on 10/12/2016.
 */
public class PostListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postItems;
    private List<String> userLikes;
    private ViewGroup myViewGroup;

    private int myTag;

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator();
    private GestureDetector detector;

    public PostListAdapter(Activity activity, List<Post> postItems, List userlikes) {
        this.activity = activity;
        this.postItems = postItems;
        this.userLikes=userlikes;
    }

    ImageLoader imageLoader = MyVolley.getInstance(activity).getImageLoader();

    @Override
    public int getCount() {
        return postItems.size();
    }

    @Override
    public Object getItem(int location) {
        return postItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ItemViewHolder {
        NetworkImageView thumbNail;
        ImageView likeView;
        ImageView unlikeView;
        ImageView bigheartView;
        ImageView commentView;
        TextView dateText;
        LinearLayout descLayout;
        LinearLayout iconsLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ItemViewHolder holder;
        if (convertView == null)
        {
            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_item,null);

            holder= new ItemViewHolder();
            holder.thumbNail = (NetworkImageView) convertView
                    .findViewById(R.id.networkImageView);
            holder.likeView = (ImageView) convertView.findViewById(R.id.heartO);
            holder.unlikeView = (ImageView) convertView.findViewById(R.id.heartRed);
            holder.bigheartView = (ImageView) convertView.findViewById(R.id.bigHeart);
            holder.commentView = (ImageView) convertView.findViewById(R.id.commentIcon);
            holder.dateText =(TextView)convertView.findViewById(R.id.textView_date);
            holder.descLayout = (LinearLayout) convertView.findViewById(R.id.layout_desc);
            holder.iconsLayout = (LinearLayout) convertView.findViewById(R.id.iconsWrapper);

            convertView.setTag(holder);
        }
        else{
            holder = (ItemViewHolder) convertView.getTag();
        }

        myViewGroup=parent;
        holder.iconsLayout.setTag("view"+position);

        if (imageLoader == null)
            imageLoader = MyVolley.getInstance(activity).getImageLoader();

        // getting Post data for the row
        Post post = postItems.get(position);
        int post_id = post.getId();

        //mark liked posts as liked
        if(userLikes.contains(Integer.toString(post_id))){
            holder.likeView.setVisibility(View.GONE);
            holder.unlikeView.setVisibility(View.VISIBLE);
        }

        // thumbnail image
        holder.thumbNail.setDefaultImageResId(R.drawable.ic_hourglass_empty_white_24px);
        holder.thumbNail.setImageUrl(post.getImgUrl(), imageLoader);

        holder.likeView.setTag(position);
        holder.unlikeView.setTag(position);
        holder.commentView.setTag(position);
        holder.likeView.setOnClickListener(onClickLike);
        holder.unlikeView.setOnClickListener(onClickUnlike);
        holder.commentView.setOnClickListener(onClickComment);

        detector = new GestureDetector(activity, new GestureListener());

        holder.bigheartView.setTag("heart"+position);
        holder.thumbNail.setTag(position);

        holder.thumbNail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myTag=(Integer)v.getTag();
                return detector.onTouchEvent(event);
            }

        });

        String userdesc=null;
        ArrayList<HashMap<String,String>> comments=post.getComments();

        if(holder.descLayout.findViewWithTag(position)==null){
            LinearLayout linlay= new LinearLayout(activity);
            linlay.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linlay.setOrientation(LinearLayout.VERTICAL);
            linlay.setTag(position);
            for(int i=0; i<comments.size(); i++){
                HashMap<String,String> comhash=comments.get(i);
                for (Map.Entry<String, String> entry : comhash.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if(key!=null && value!=null && value.length()>0){
                        userdesc=key+" "+value;
                        Log.i("SUKA", userdesc);
                        final SpannableStringBuilder boldUsername = new SpannableStringBuilder(userdesc);
                        boldUsername.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, key.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        TextView descText= new TextView(activity);
                        descText.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        descText.setText(boldUsername);
                        descText.setId(View.generateViewId());
                        linlay.addView(descText);
                    }
                }
            }

            int comments_count=post.getComCount();
            if(comments_count>3){
                TextView loadMoreCommentsText= new TextView(activity);
                loadMoreCommentsText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                String load="View all "+comments_count+" comments";
                loadMoreCommentsText.setText(load);
                loadMoreCommentsText.setPadding(0,8,0,0);
                loadMoreCommentsText.setTextColor(Color.GRAY);
                loadMoreCommentsText.setTag(position);
                loadMoreCommentsText.setOnClickListener(onClickComment);
                linlay.addView(loadMoreCommentsText);
            }

            holder.descLayout.addView(linlay);
        }
        holder.dateText.setText(post.getDate());

        return convertView;
    }

    Post getPost(int position) {
        return ((Post) getItem(position));
    }

    View.OnClickListener onClickComment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Post p = getPost((Integer) v.getTag());
            int post_id = p.getId();
            int user_id=MainActivity.USER_ID;
            String user_name=MainActivity.USER_NAME;
            Intent commentIntent = new Intent(activity, CommentActivity.class);
            commentIntent.putExtra("post_id",post_id);
            commentIntent.putExtra("user_id",user_id);
            commentIntent.putExtra("user_name",user_name);
            activity.startActivity(commentIntent);
        }
    };

    View.OnClickListener onClickLike = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Post p = getPost((Integer) v.getTag());
            int post_id = p.getId();
            int user_id=MainActivity.USER_ID;
            likeAnimation(v);
            heartAction(post_id, user_id,"like");

        }
    };

    View.OnClickListener onClickUnlike = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Post p = getPost((Integer) v.getTag());
            int post_id = p.getId();
            int user_id=MainActivity.USER_ID;
            unlikeAnimation(v);
            heartAction(post_id, user_id,"unlike");
        }
    };

    private void heartAction(final int post_id, final int user_id, final String action){

        String url="http://temirbek.com/site/heart";
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if(response.equals("success")){
                    Log.i("LIKE ACTION","SUCCESS");
                    if(action.equals("like")){
                        userLikes.add(Integer.toString(post_id));
                    }
                    else{
                        userLikes.remove(Integer.toString(post_id));
                    }
                }
                else{
                    Log.i("LIKE ACTION","FAIL: "+response);
                }
            }

        };

        StringRequest sReq = new StringRequest(Request.Method.POST, url,responseListener, null){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_id",Integer.toString(post_id));
                params.put("user_id",Integer.toString(user_id));
                params.put("action",action);

                return params;
            }
        };
        MyVolley.getInstance(activity.getApplicationContext()).addToRequestQueue(sReq);
    }

    private void likeAnimation(View v) {
        ViewGroup row;
        if(v==null){
            row =(ViewGroup) myViewGroup.findViewWithTag("view"+myTag);
        }
        else{
            row = (ViewGroup) v.getParent();
        }

        ImageView likeView=(ImageView) row.findViewById(R.id.heartO);
        ImageView unlikeView=(ImageView) row.findViewById(R.id.heartRed);
        likeView.setVisibility(View.GONE);
        unlikeView.setVisibility(View.VISIBLE);
        unlikeView.setScaleY(0.1f);
        unlikeView.setScaleX(0.1f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(unlikeView, "scaleY", 0.1f, 1f);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(OVERSHOOT_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(unlikeView, "scaleX", 0.1f, 1f);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(OVERSHOOT_INTERPOLATOR);

        animatorSet.playTogether(imgScaleUpYAnim, imgScaleUpXAnim);
        animatorSet.start();
    }

    private void unlikeAnimation(View v) {
        ViewGroup row = (ViewGroup) v.getParent();

        ImageView likeView=(ImageView) row.findViewById(R.id.heartO);
        ImageView unlikeView=(ImageView) row.findViewById(R.id.heartRed);

        likeView.setVisibility(View.VISIBLE);
        unlikeView.setVisibility(View.GONE);
        likeView.setScaleY(0.1f);
        likeView.setScaleX(0.1f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(likeView, "scaleY", 0.1f, 1f);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(OVERSHOOT_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(likeView, "scaleX", 0.1f, 1f);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(OVERSHOOT_INTERPOLATOR);

        animatorSet.playTogether(imgScaleUpYAnim, imgScaleUpXAnim);
        animatorSet.start();
    }

    private void heartAnimation() {
        ImageView bigheartView =(ImageView) myViewGroup.findViewWithTag("heart"+myTag);

        bigheartView.setVisibility(View.VISIBLE);
        bigheartView.setScaleY(0.1f);
        bigheartView.setScaleX(0.1f);
        bigheartView.setAlpha(1f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(bigheartView, "scaleY", 0.1f, 1f);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(OVERSHOOT_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(bigheartView, "scaleX", 0.1f, 1f);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(bigheartView, "alpha", 1f, 0.9f);
        bgAlphaAnim.setDuration(500);
        bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(bigheartView, "scaleY", 1f, 0f);
        imgScaleDownYAnim.setDuration(200);
        imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(bigheartView, "scaleX", 1f, 0f);
        imgScaleDownXAnim.setDuration(200);
        imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        animatorSet.playTogether(imgScaleUpYAnim, imgScaleUpXAnim);
        animatorSet.play(bgAlphaAnim).after(imgScaleUpYAnim);
        animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(bgAlphaAnim).after(1000);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ImageView bigheartView =(ImageView) myViewGroup.findViewWithTag("heart"+myTag);
                bigheartView.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

    // Place this inner class anywhere outside onCreate()
    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {return true;}

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            heartAnimation();
            likeAnimation(null);
            Post p = getPost(myTag);
            int post_id = p.getId();

            //send request to server if it hasn't been already liked
            if(!userLikes.contains(Integer.toString(post_id))){
                int user_id=MainActivity.USER_ID;
                heartAction(post_id, user_id, "like");
            }

            return true;
        }
    }
}
