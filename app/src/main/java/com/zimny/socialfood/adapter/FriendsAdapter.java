package com.zimny.socialfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.FriendsFragment;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 05.09.2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{
    ArrayList<User> users = new ArrayList<>();

    public FriendsAdapter(ArrayList<User> users) {
        this.users=users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend,parent,false);
        return new FriendsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.firstname.setText(user.getFirstname());
        holder.lastname.setText(user.getLastname());
        holder.city.setText(user.getAddress().getCity());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(String.format("%s.png",user.getUid()));
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.userImageCircle);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userImageCircle)
        CircleImageView userImageCircle;
        @BindView(R.id.firstname)
        TextView firstname;
        @BindView(R.id.lastname)
        TextView lastname;
        @BindView(R.id.age)
        TextView age;
        @BindView(R.id.city)
        TextView city;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
