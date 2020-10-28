package com.prasadmukne.android.peopleinteractive.matchmaker.commons.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.prasadmukne.android.peopleinteractive.matchmaker.R;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.Result;
import com.prasadmukne.android.peopleinteractive.matchmaker.commons.model.ResultObjectMapper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prasad Mukne on 24-10-2020.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private List<Result> mUserData;
    private Context mContext;
    private int mPosition;

    public UserAdapter(List<Result> userData, Context context) {
        this.mUserData = userData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_swipe_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.profileImageView.callOnClick();
        Glide.with(mContext).load(mUserData.get(position).getPicture().getLarge()).into(holder.mProfileImage);
        this.mPosition = position;
        setMemberStatus(holder, position);
    }

    private void setMemberStatus(MyViewHolder holder, int position) {
        if (null == mUserData.get(position).getMemberStatus()) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.rejectButton.setVisibility(View.VISIBLE);
            holder.statusTextView.setVisibility(View.INVISIBLE);
        } else {
            holder.acceptButton.setVisibility(View.INVISIBLE);
            holder.rejectButton.setVisibility(View.INVISIBLE);
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setText(mUserData.get(position).getMemberStatus());
        }
    }

    @Override
    public int getItemCount() {
        return mUserData.size();
    }

    public void removeTopItem() {
        mUserData.remove(mPosition);
        notifyDataSetChanged();
    }

    private void updateMemberStatusInDb(Result result) {
        String usernameToBeSearched = result.getName().getFirst() + result.getName().getLast();
        List<ResultObjectMapper> searchedRecord = ResultObjectMapper.find(ResultObjectMapper.class, "USERNAME=?", usernameToBeSearched);
        if (null != searchedRecord && searchedRecord.size() != 0) {
            ResultObjectMapper searchedObjectMapper = searchedRecord.get(0);
            searchedObjectMapper.jsonRecord = new Gson().toJson(result);
            searchedObjectMapper.save();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView, valueTextView, statusTextView;
        ImageView profileImageView, detailsImageView, locationImageView, contactImageView, personalImageView;
        CircleImageView mProfileImage;
        AppCompatButton acceptButton, rejectButton;

        MyViewHolder(View view) {
            super(view);
            initialiseUI(view);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateStatusTextView(R.string.accepted);
                    Result result = mUserData.get(mPosition);
                    result.setMemberStatus(mContext.getResources().getString(R.string.accepted));
                    updateMemberStatusInDb(result);
                }
            });
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateStatusTextView(R.string.rejected);
                    Result result = mUserData.get(mPosition);
                    result.setMemberStatus(mContext.getResources().getString(R.string.rejected));
                    updateMemberStatusInDb(result);
                }
            });
        }

        private void initialiseUI(View view) {
            titleTextView = view.findViewById(R.id.title_text_view);
            valueTextView = view.findViewById(R.id.value_text_view);
            profileImageView = view.findViewById(R.id.profile_image_view);
            detailsImageView = view.findViewById(R.id.details_image_view);
            locationImageView = view.findViewById(R.id.location_image_view);
            contactImageView = view.findViewById(R.id.contact_image_view);
            personalImageView = view.findViewById(R.id.personal_image_view);
            statusTextView = view.findViewById(R.id.status_text_view);
            acceptButton = view.findViewById(R.id.accept_button);
            rejectButton = view.findViewById(R.id.reject_button);
            mProfileImage = view.findViewById(R.id.profile_image);
            profileImageView.setOnClickListener(this);
            detailsImageView.setOnClickListener(this);
            locationImageView.setOnClickListener(this);
            contactImageView.setOnClickListener(this);
            personalImageView.setOnClickListener(this);
        }

        private void updateStatusTextView(int stringId) {
            statusTextView.setText(stringId);
            statusTextView.setVisibility(View.VISIBLE);
            acceptButton.setVisibility(View.INVISIBLE);
            rejectButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            setCheckedItems(view);
            switch (view.getId()) {
                case R.id.profile_image_view:
                    titleTextView.setText(R.string.profile_title_text);
                    valueTextView.setText("Name:- " + mUserData.get(mPosition).getName().getTitle().toUpperCase() + " " + mUserData.get(mPosition).getName().getFirst().toUpperCase() + " " + mUserData.get(mPosition).getName().getLast().toUpperCase()
                            + "\n" + "Gender:- " + mUserData.get(mPosition).getGender()
                            + "\n" + "BirthDate:- " + mUserData.get(mPosition).getDob().getDate().substring(0, 10)
                            + "\n" + "Age:- " + mUserData.get(mPosition).getDob().getAge()
                            + "\n" + "Nationality:- " + mUserData.get(mPosition).getNat());
                    break;
                case R.id.details_image_view:
                    titleTextView.setText(R.string.info_title_text);
                    valueTextView.setText("Email Address:- " + mUserData.get(mPosition).getEmail()
                            + "\n" + "Registration Date:- " + mUserData.get(mPosition).getRegistered().getDate().substring(0, 10));
                    break;
                case R.id.location_image_view:
                    titleTextView.setText(R.string.location_details_text);
                    valueTextView.setText(mUserData.get(mPosition).getLocation().getStreet().getNumber() + ", " + mUserData.get(mPosition).getLocation().getStreet().getName() + ", " + mUserData.get(mPosition).getLocation().getCity() + ", " + mUserData.get(mPosition).getLocation().getState() + ", " + mUserData.get(mPosition).getLocation().getCountry() + ", " + mUserData.get(mPosition).getLocation().getPostcode());
                    break;
                case R.id.contact_image_view:
                    titleTextView.setText(R.string.contact_details_text);
                    valueTextView.setText("Phone no:- " + mUserData.get(mPosition).getPhone()
                            + "\n" + "Cell No:- " + mUserData.get(mPosition).getCell());
                    break;
                case R.id.personal_image_view:
                    String str = mUserData.get(mPosition).getLogin().getPassword().replaceAll(".", "*");
                    titleTextView.setText(R.string.security_details_text);
                    valueTextView.setText("Username:- " + mUserData.get(mPosition).getLogin().getUsername()
                            + "\n" + "Password:- " + str);
                    break;
            }
        }

        private void setCheckedItems(View view) {
            int id = view.getId();
            int color;

            color = id == R.id.profile_image_view ? R.color.selected_color : R.color.grey_color;
            profileImageView.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.MULTIPLY);

            color = id == R.id.details_image_view ? R.color.selected_color : R.color.grey_color;
            detailsImageView.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.MULTIPLY);

            color = id == R.id.location_image_view ? R.color.selected_color : R.color.grey_color;
            locationImageView.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.MULTIPLY);

            color = id == R.id.contact_image_view ? R.color.selected_color : R.color.grey_color;
            contactImageView.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.MULTIPLY);

            color = id == R.id.personal_image_view ? R.color.selected_color : R.color.grey_color;
            personalImageView.setColorFilter(ContextCompat.getColor(mContext, color), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
}