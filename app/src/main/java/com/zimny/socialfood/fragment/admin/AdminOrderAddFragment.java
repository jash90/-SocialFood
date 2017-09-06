package com.zimny.socialfood.fragment.admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodOrderAdminAdapter;
import com.zimny.socialfood.model.FoodOrder;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Order;
import com.zimny.socialfood.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminOrderAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.user)
    Spinner user;
    @BindView(R.id.date)
    TextView selectDate;
    @BindView(R.id.group)
    Spinner group;
    @BindView(R.id.paying)
    Switch paying;
    @BindView(R.id.save)
    Button save;
    FoodOrderAdminAdapter foodOrderAdminAdapter;
    ArrayList<FoodOrder> foodOrders;
    ArrayList<User> users;
    ArrayAdapter<User> usersAdapter;
    ArrayList<Group> groups;
    ArrayAdapter<Group> groupAdapter;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_order_add, container, false);
        ButterKnife.bind(this, v);
        calendar = Calendar.getInstance();
        foodOrders = new ArrayList<>();
        users = new ArrayList<>();
        groups = new ArrayList<>();
        foodOrders.add(new FoodOrder());
        usersAdapter = new ArrayAdapter<User>(getContext(), android.R.layout.simple_spinner_item, users);
        groupAdapter = new ArrayAdapter<Group>(getContext(), android.R.layout.simple_spinner_item, groups);
        user.setAdapter(usersAdapter);
        group.setAdapter(groupAdapter);
        foodOrderAdminAdapter = new FoodOrderAdminAdapter(foodOrders);
        recyclerView.setAdapter(foodOrderAdminAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUid(dataSnapshot.getKey());
                    users.add(user);
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    Group group = dataSnapshot.getValue(Group.class);
                    group.setUid(dataSnapshot.getKey());
                    groups.add(group);
                    groupAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Calendar now = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(),
                AdminOrderAddFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(getContext(),
                AdminOrderAddFragment.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Order order = new Order();
                order.setUidUser(((User) user.getSelectedItem()).getUid());
                order.setUidGroup(((Group) group.getSelectedItem()).getUid());
                order.setPaying(paying.isChecked());
//                String s = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z").format(calendar.getTime());
//                XLog.d(s);
//                try {
//                    Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z").parse(s);
//                    XLog.d(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                order.setDate(null);
                String uid = databaseReference.child("orders").push().getKey();
                databaseReference.child("orders").child(uid).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), order.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                databaseReference.child("orders").child(uid).child("date").setValue(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z").format(calendar.getTime())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), foodOrders.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                for (FoodOrder foodOrder : foodOrders) {
                    final Map<String, Object> foodOrders = new HashMap<>();
                    String uidfood = databaseReference.child("orders").child(uid).child("foodOrders").push().getKey();
                    foodOrders.put("uidFood", foodOrder.getUid());
                    foodOrders.put("count", foodOrder.getCount());
                    databaseReference.child("orders").child(uid).child("foodOrders").child(uidfood).setValue(foodOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), foodOrders.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        return v;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        timePickerDialog.show();


    }

    @Override
    public void onTimeSet(TimePicker timePicker, int h, int m) {
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        selectDate.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.getTime()));
    }

}
