package shzhao.seapp;

/**
 * Created by shzha on 2016/12/14.
 */

import android.text.Html;
import android.text.Spanned;

public class Comment {
    private Spanned comment;

    public Comment(int user_id, String comment) {
        this.comment = Html.fromHtml("<font color='#4A766E'>"+user_id+": </font>" + comment);
    }

    public Spanned getComment() {
        return comment;
    }
}