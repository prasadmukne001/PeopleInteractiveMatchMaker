package com.prasadmukne.android.peopleinteractive.matchmaker.services.db;

import com.google.gson.Gson;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.Result;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.ResultObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasad Mukne on 27-10-2020.
 */
public class DatabaseManager {

    private static DatabaseManager mDatabaseManager;

    private DatabaseManager() {

    }

    public static DatabaseManager getInstance() {
        if (mDatabaseManager == null) {
            mDatabaseManager = new DatabaseManager();
        }
        return mDatabaseManager;
    }

    public void saveAllUsers(List<Result> usersList) {
        for (int i = 0; i < usersList.size(); i++) {
            ResultObjectMapper resultObjectMapper = new ResultObjectMapper();
            resultObjectMapper.jsonRecord = new Gson().toJson(usersList.get(i));
            resultObjectMapper.username = usersList.get(i).getName().getFirst() + usersList.get(i).getName().getLast();
            resultObjectMapper.save();
        }
        usersList.size();
    }

    public List<Result> getAllUsers() {
        List<ResultObjectMapper> resultObjectMapperList = ResultObjectMapper.listAll(ResultObjectMapper.class);
        List<Result> usersList = new ArrayList<>();
        for (int i = 0; i < resultObjectMapperList.size(); i++) {
            usersList.add(new Gson().fromJson(resultObjectMapperList.get(i).jsonRecord, Result.class));
        }
        return usersList;
    }


}
