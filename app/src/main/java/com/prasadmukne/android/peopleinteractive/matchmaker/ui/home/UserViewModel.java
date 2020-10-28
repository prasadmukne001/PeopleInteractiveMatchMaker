package com.prasadmukne.android.peopleinteractive.matchmaker.ui.home;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.prasadmukne.android.peopleinteractive.matchmaker.R;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.Result;
import com.prasadmukne.android.peopleinteractive.matchmaker.services.db.DatabaseManager;
import com.prasadmukne.android.peopleinteractive.matchmaker.services.network.NetworkService;
import com.prasadmukne.android.peopleinteractive.matchmaker.utils.PeopleInteractiveUtility;

import java.util.List;

/**
 * Created by Prasad Mukne on 23-10-2020.
 */
public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<List<Result>> mUserData = new MutableLiveData<List<Result>>();

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Result>> getData() {
        return mUserData;
    }

    public void getNextSetOfUsers() {
        List<Result> resultList = DatabaseManager.getInstance().getAllUsers();
        if (PeopleInteractiveUtility.isNetworkAvailable(getApplication())) {
            if (null != resultList && resultList.size() != 0)
                mUserData.postValue(resultList);
            else
                new NetworkService().getNextUser(getApplication(), mUserData);
        } else {
            Toast.makeText(getApplication(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            if (null != resultList)
                mUserData.postValue(resultList);
        }
    }

    public void getNextOfOnlineUsers() {
        if (PeopleInteractiveUtility.isNetworkAvailable(getApplication())) {
            new NetworkService().getNextUser(getApplication(), mUserData);
        } else {
            Toast.makeText(getApplication(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            mUserData.postValue(DatabaseManager.getInstance().getAllUsers());
        }
    }

}