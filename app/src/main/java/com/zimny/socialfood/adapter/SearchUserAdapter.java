package com.zimny.socialfood.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.User;
import com.zimny.socialfood.model.UserRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 16.10.2017.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> implements Filterable{
    ArrayList<UserRequest> userRequests = new ArrayList<>();
    ArrayList<UserRequest> userAll = new ArrayList<>();
    Filter userFilter;

    public SearchUserAdapter(ArrayList<UserRequest> userRequests, ArrayList<UserRequest> userAll) {
        this.userRequests = userRequests;
        this.userAll = userAll;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_user,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchUserAdapter.ViewHolder holder, int position) {
        final UserRequest userRequest = userRequests.get(position);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child(String.format("%s.png", userRequest.getUid()));
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageReference)
                .asBitmap()
                .placeholder(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_user_circle).sizeDp(40))
                .signature(new StringSignature(userRequest.getImageUpload()))
                .into(holder.image);
        holder.name.setText(userRequest.getFirstname()+" "+userRequest.getLastname());
        holder.city.setText(userRequest.getAddress().getCity());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                userRequest.setRequest(b);
            }
        });
    }



    @Override
    public int getItemCount() {
        return userRequests.size();
    }

    @Override
    public Filter getFilter() {
        if (userFilter == null)
            userFilter = new UserFilter();

        return userFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.image)
        CircleImageView image;
        @BindView(R.id.checkbox)
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = userAll;
                results.count = userAll.size();
            } else {
                List<UserRequest> resultList = new ArrayList<>();
                for (UserRequest userRequest : userAll) {
                    if (userRequest.getFirstname().toUpperCase().startsWith(charSequence.toString().toUpperCase()) || userRequest.getLastname().toUpperCase().startsWith(charSequence.toString().toUpperCase()) || userRequest.getAddress().getCity().toUpperCase().startsWith(charSequence.toString().toUpperCase()))
                        resultList.add(userRequest);

                }
                results.values = resultList;
                results.count = resultList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.count != 0) {
                userRequests = (ArrayList<UserRequest>) filterResults.values;
                notifyDataSetChanged();
            } else {
                userRequests = new ArrayList<>();
                notifyDataSetChanged();
            }
        }
    }
}
