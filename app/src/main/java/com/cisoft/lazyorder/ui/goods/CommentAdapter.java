package com.cisoft.lazyorder.ui.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cisoft.lazyorder.R;
import com.cisoft.lazyorder.bean.goods.Comment;

import java.util.List;

/**
 * Created by Lecion on 11/3/14.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;

    private List<Comment> data;


    public CommentAdapter() {
    }

    public CommentAdapter(Context context, List<Comment> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_goods_comment_cell, parent, false);
            holder.tvCommentUser = (TextView) convertView.findViewById(R.id.tv_comment_user);
            holder.tvCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        Comment comment = (Comment) getItem(position);
        holder.tvCommentUser.setText(comment.getUserName());
        holder.tvCommentContent.setText(comment.getDiscussContent());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvCommentUser;
        TextView tvCommentContent;
    }
}
