package com.prasadmukne.android.peopleinteractive.matchmaker.services.network;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.prasadmukne.android.peopleinteractive.matchmaker.R;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.Result;
import com.prasadmukne.android.peopleinteractive.matchmaker.services.db.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasad Mukne on 22-10-2020.
 */
public class NetworkService {

    private final String USER_API_URL = "https://randomuser.me/api/?results=10";
    private MutableLiveData<List<Result>> mUserData;

    public void getNextUser(final Context context, final MutableLiveData<List<Result>> userData) {
        this.mUserData = userData;
        AndroidNetworking.get(USER_API_URL)
                .build()
                .getAsObject(JsonObject.class, new ParsedRequestListener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        try {
                            String parsedResponse = response.getAsJsonArray("results").toString();
                            List<Result> resultList = new Gson().fromJson(parsedResponse, new TypeToken<ArrayList<Result>>() {
                            }.getType());
                            DatabaseManager.getInstance().saveAllUsers(resultList);
                            userData.postValue(resultList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, R.string.some_error_occurred + " " + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        mUserData.postValue(mUserData.getValue());
                    }
                });
    }
}
