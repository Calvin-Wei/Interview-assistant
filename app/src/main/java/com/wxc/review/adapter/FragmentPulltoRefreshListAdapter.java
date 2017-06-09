package com.wxc.review.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wxc.review.R;
import com.wxc.review.entity.Invitation;

import java.util.List;

/**
 * Created by wxc on 2017/5/5.
 */

public class FragmentPulltoRefreshListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<Invitation> list;
    public FragmentPulltoRefreshListAdapter(Context context, List<Invitation> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list =list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;//PullToRefreshListView position从1开始 listview从0开始
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null) {
            vh = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.fragment_list_item, parent, false);
            vh.titleText = (TextView) convertView.findViewById(R.id.fragment1_list_item_title_text);
            vh.zanCountText = (TextView) convertView.findViewById(R.id.fragment1_list_item_zangcount_text);
            vh.contentText = (TextView) convertView.findViewById(R.id.fragment1_list_item_content_text);
            vh.creatTimtText = (TextView) convertView.findViewById(R.id.fragment1_list_item_time_text);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        Invitation invitation = list.get(position);
        vh.titleText.setText(invitation.getTitle());
        vh.zanCountText.setText(invitation.getDianzangCount()+"");
        vh.contentText.setText("       "+invitation.getContent());
        vh.creatTimtText.setText(invitation.getCreatedAt());
        return convertView;
    }
    //优化模板类
    class ViewHolder{
        TextView titleText,contentText,zanCountText, creatTimtText;
    }
}
