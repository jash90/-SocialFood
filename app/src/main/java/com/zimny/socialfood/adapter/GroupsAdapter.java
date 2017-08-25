package com.zimny.socialfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Group;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by ideo7 on 22.08.2017.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    ArrayList<Group> groups = new ArrayList<>();

    public GroupsAdapter(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        return new GroupsAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.nameGroup.setText(group.getName());
        holder.city.setText(group.getAddress().getCity());
        holder.tagGroup.setTags(group.getAddress().getCity());


    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameGroup)
        TextView nameGroup;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.tagGroup)
        TagGroup tagGroup;
//        @BindView(R.id.recyclerView)
//        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
