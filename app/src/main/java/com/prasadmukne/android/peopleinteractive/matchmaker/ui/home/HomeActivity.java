package com.prasadmukne.android.peopleinteractive.matchmaker.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prasadmukne.android.peopleinteractive.matchmaker.R;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.Result;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.views.adapter.UserAdapter;
import com.prasadmukne.android.peopleinteractive.matchmaker.utils.PeopleInteractiveUtility;

import java.util.ArrayList;
import java.util.List;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class HomeActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private UserAdapter mUserAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressbar;
    private List<Result> mUserList;
    private boolean mHelpFlag;
    private FloatingActionButton mOfflineViewFAB, mRefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        initialiseUI();

        setupDataObserver();
    }

    private void initialiseUI() {
        mRecyclerView = findViewById(R.id.user_recycler_view);
        mProgressbar = findViewById(R.id.progress_bar);
        mUserList = new ArrayList<>();
        mOfflineViewFAB = findViewById(R.id.saved_fab_button);
        mRefreshButton = findViewById(R.id.refresh_fab_button);
        mProgressbar.setVisibility(View.VISIBLE);
        mUserViewModel.getNextSetOfUsers();
        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                new SwipeableTouchHelperCallback(new OnItemSwiped() {
                    @Override
                    public void onItemSwiped() {
                        mUserAdapter.removeTopItem();
                        if (mUserList.size() == 0) {
                            mProgressbar.setVisibility(View.VISIBLE);
                            mUserViewModel.getNextOfOnlineUsers();
                        }
                    }

                    @Override
                    public void onItemSwipedLeft() {

                    }

                    @Override
                    public void onItemSwipedRight() {

                    }

                    @Override
                    public void onItemSwipedUp() {

                    }

                    @Override
                    public void onItemSwipedDown() {

                    }
                });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeableTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        SwipeableLayoutManager swipeableLayoutManager = new SwipeableLayoutManager();
        mRecyclerView.setLayoutManager(swipeableLayoutManager);

        swipeableLayoutManager.setAngle(10)
                .setAnimationDuratuion(500)
                .setMaxShowCount(3)
                .setScaleGap(0.1f)
                .setTransYGap(0);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserList = new ArrayList<>();
                mProgressbar.setVisibility(View.VISIBLE);
                mUserViewModel.getNextSetOfUsers();
            }
        });

    }

    private void setupDataObserver() {
        mUserViewModel.getData().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> usersList) {
                if (null != usersList && usersList.size() != 0) {
                    mUserList.addAll(usersList);
                    mUserAdapter = new UserAdapter(mUserList, HomeActivity.this);
                    mRecyclerView.setAdapter(mUserAdapter);
                    if (!mHelpFlag)
                        Toast.makeText(getApplication(), R.string.swipe_left_right, Toast.LENGTH_LONG).show();
                    mHelpFlag = true;
                }
                mProgressbar.setVisibility(View.INVISIBLE);
                if (PeopleInteractiveUtility.isNetworkAvailable(getApplication())) {
                    mOfflineViewFAB.setVisibility(View.INVISIBLE);
                } else {
                    mOfflineViewFAB.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
