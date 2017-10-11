package com.zimny.socialfood.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.details.UserDetailsActivity;
import com.zimny.socialfood.model.UserRequest;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 10.10.2017.
 */

public class FriendsAndRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<UserRequest> allUserRequest = new ArrayList<>();
    private ArrayList<UserRequest> originalUserRequest = new ArrayList<>();
    private ArrayList<UserRequest> userRequests = new ArrayList<>();
    private Snackbar snackbar;
    private Filter friendsFilter;

    public FriendsAndRequestAdapter(ArrayList<UserRequest> userRequests, ArrayList<UserRequest> allPlanetList) {
        this.originalUserRequest = userRequests;
        this.userRequests = userRequests;
        this.allUserRequest = allPlanetList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        snackbar = Snackbar.make(parent, "Friends Request rejected.", Snackbar.LENGTH_SHORT);
        if (viewType == 2) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend, parent, false);
            return new FriendsHolder(v);
        } else if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_request_friend, parent, false);
            return new FriendsRequestHolder(v);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (userRequests.get(position).isRequest()) {
            FriendsHolder holder = (FriendsHolder) viewHolder;
            final UserRequest user = userRequests.get(position);
            holder.firstname.setText(user.getFirstname());
            holder.lastname.setText(user.getLastname());
            holder.city.setText(user.getAddress().getCity());
            holder.userImageCircle.setImageDrawable(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_user_circle).sizeDp(40));
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imageRef = storageReference.child(String.format("%s.png", user.getUid()));
            Glide.with(holder.itemView.getContext())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .asBitmap()
                    .placeholder(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_user_circle).sizeDp(40))
                    .signature(new StringSignature(user.getImageUpload()))
                    .into(holder.userImageCircle);
            if (user.getBirthday() != null) {
                LocalDate birthday2 = LocalDate.fromDateFields(user.getBirthday());
                Period period = new Period(birthday2, LocalDate.now(), PeriodType.years());
                holder.age.setVisibility(View.VISIBLE);
                holder.age.setText(String.format("%d y.", period.getYears()));
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
        } else {
            final FriendsRequestHolder holder = (FriendsRequestHolder) viewHolder;
            final UserRequest user = userRequests.get(position);
            holder.firstname.setText(user.getFirstname());
            holder.lastname.setText(user.getLastname());
            holder.city.setText(user.getAddress().getCity());
            holder.userImageCircle.setImageDrawable(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_user_circle).sizeDp(40));
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imageRef = storageReference.child(String.format("%s.png", user.getUid()));
            Glide.with(holder.itemView.getContext())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .asBitmap()
                    .placeholder(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_user_circle).sizeDp(40))
                    .signature(new StringSignature(user.getImageUpload()))
                    .into(holder.userImageCircle);
            if (user.getBirthday() != null) {
                LocalDate birthday2 = LocalDate.fromDateFields(user.getBirthday());
                Period period = new Period(birthday2, LocalDate.now(), PeriodType.years());
                holder.age.setVisibility(View.VISIBLE);
                holder.age.setText(String.format("%d y.", period.getYears()));
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
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    databaseReference.child("relationships").child("deliveryrequest").child(firebaseAuth.getCurrentUser().getUid()).child(originalUserRequest.get(position).getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            snackbar.setText("XDD");
                            snackbar.show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            });
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    databaseReference.child("relationships").child("deliveryrequest").child(firebaseAuth.getCurrentUser().getUid()).child(originalUserRequest.get(position).getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            snackbar.setText("Add");
                            snackbar.show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    databaseReference.child("relationships").child("sendrequest").child(originalUserRequest.get(position).getUid()).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                    databaseReference.child("relationships").child("friends").child(firebaseAuth.getCurrentUser().getUid()).child(originalUserRequest.get(position).getUid()).setValue(true);
                    databaseReference.child("relationships").child("friends").child(originalUserRequest.get(position).getUid()).child(firebaseAuth.getCurrentUser().getUid()).setValue(true);



                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return userRequests.size();
    }

    @Override
    public int getItemViewType(int position) {
        return userRequests.get(position).isRequest() ? 2 : 1;
    }

    public void resetData() {
        userRequests = originalUserRequest;
    }

    @Override
    public Filter getFilter() {
        if (friendsFilter == null)
            friendsFilter = new FriendsFilter();

        return friendsFilter;
    }

    public class FriendsHolder extends RecyclerView.ViewHolder {
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

        public FriendsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FriendsRequestHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.confirm)
        Button confirm;
        @BindView(R.id.cancel)
        Button cancel;

        public FriendsRequestHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private class FriendsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = originalUserRequest;
                results.count = originalUserRequest.size();
            } else {
                // We perform filtering operation
                List<UserRequest> resultList = new ArrayList<>();

                for (UserRequest p : allUserRequest) {
                    if (p.getFirstname().toUpperCase().startsWith(constraint.toString().toUpperCase()) || p.getLastname().toUpperCase().startsWith(constraint.toString().toUpperCase()) || p.getAddress().getCity().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        resultList.add(p);
                }

                results.values = resultList;
                results.count = resultList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count != 0) {
                userRequests = (ArrayList<UserRequest>) results.values;
                notifyDataSetChanged();
            } else {
                userRequests = new ArrayList<>();
                notifyDataSetChanged();
            }
        }
    }
}
