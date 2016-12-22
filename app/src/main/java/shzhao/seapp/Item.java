package shzhao.seapp;

/**
 * Created by shzha on 2016/12/14.
 */

import java.util.ArrayList;

public class Item {

    private int portraitId; // 头像
    private String nickName; // 昵称
    private String content; // 说说
    private String createdAt; // 发布时间
    private ArrayList<Comment> comments = new ArrayList<>(); // 评论


    public Item(int portraitId, String nickName, String content, String createdAt) {
        this.portraitId = portraitId;
        this.nickName = nickName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public boolean hasComment() {
        return comments.size() > 0;
    }

    public int getPortraitId() {
        return portraitId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
