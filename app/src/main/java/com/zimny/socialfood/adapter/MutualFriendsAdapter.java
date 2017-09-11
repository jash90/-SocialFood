package com.zimny.socialfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 29.08.2017.
 */

public class MutualFriendsAdapter extends RecyclerView.Adapter<MutualFriendsAdapter.ViewHolder> {
    ArrayList<User> mutualfriends = new ArrayList<>();

    public MutualFriendsAdapter(ArrayList<User> mutualfriends) {
        this.mutualfriends = mutualfriends;
    }

    @Override
    public MutualFriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mutual_friends, parent, false);
        return new MutualFriendsAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User mutualfriend = mutualfriends.get(position);
        // holder.userIcon.setImageBitmap();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child(String.format("%s.png", mutualfriend.getUid()));
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageReference)
                .error(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_user_circle).sizeDp(40))
                .signature(new StringSignature(mutualfriend.getImageUpload()))
                .into(holder.userIcon);
        //XLog.d("MUTUAL "+mutualfriends);
    }

    @Override
    public int getItemCount() {
        return mutualfriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userIcon)
        CircleImageView userIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
