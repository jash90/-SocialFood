package com.zimny.socialfood.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.elvishew.xlog.XLog;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.details.UserDetailsActivity;
import com.zimny.socialfood.model.User;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 05.09.2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    ArrayList<User> users = new ArrayList<>();

    public UsersAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend, parent, false);
        return new UsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XLog.d(users);
        final User user = users.get(position);
        holder.firstname.setText(user.getFirstname());
        holder.lastname.setText(user.getLastname());
        holder.city.setText(user.getAddress().getCity());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(String.format("%s.png", user.getUid()));
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .signature(new StringSignature(user.getImageUpload()))
                .into(holder.userImageCircle);
        if (user.getBirthday() != null) {
            LocalDate birthday2 = LocalDate.fromDateFields(user.getBirthday());
            //XLog.d(birthday2);
            Period period = new Period(birthday2, LocalDate.now(), PeriodType.years());
            //XLog.d("BD " + period);
            holder.age.setVisibility(View.VISIBLE);
            holder.age.setText(String.format("%d y.o.", period.getYears()));
        } else {
            holder.age.setVisibility(View.GONE);
        }
        holder.userImageCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserDetailsActivity.class);
                intent.putExtra("user", new Gson().toJson(user));
                view.getContext().startActivity(intent);
            }
        });

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
            ButterKnife.bind(this, itemView);
        }
    }
}
