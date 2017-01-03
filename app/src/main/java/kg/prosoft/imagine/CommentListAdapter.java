package kg.prosoft.imagine;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ProsoftPC on 10/19/2016.
 */
public class CommentListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mCommentList;
    private LayoutInflater inflater;
    int range=0;

    public CommentListAdapter(Context mContext, List<Comment> mCommentList) {
        this.mContext = mContext;
        this.mCommentList = mCommentList;
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_comment_row,null);

        Comment comment = mCommentList.get(position);
        String username=comment.getUsername();
        String usercomment = username+" "+comment.getText();
        final SpannableStringBuilder boldUsername = new SpannableStringBuilder(usercomment);
        boldUsername.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, username.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView commentTv=(TextView) convertView.findViewById(R.id.textView_text);
        commentTv.setText(boldUsername);
        TextView dateTv=(TextView) convertView.findViewById(R.id.textView_date);
        dateTv.setText(comment.getDate());

        convertView.setTag(comment.getId());

        /*if(position==getCount()-1){
            Log.i("END", "Reached");
        }*/

        return convertView;
    }
}
