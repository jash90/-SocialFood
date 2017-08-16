package com.zimny.socialfood;

import com.google.android.gms.tasks.Task;
import com.zimny.socialfood.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ideo7 on 16.08.2017.
 */

public class RemoteService {

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://socialfood-a9ae3.firebaseio.com")
            .addConverterFactory(GsonConverterFactory.create()) // use gson converter
            .build();

    private static Service service = null;

    public static Service getInstance() {
        if (service == null) {
            service = retrofit.create(Service.class);
        }
        return service;
    }

    public interface Service {

        @GET("/users/{user}.json")
        Call<User>getUser(@Path("user") String user);
    }

}