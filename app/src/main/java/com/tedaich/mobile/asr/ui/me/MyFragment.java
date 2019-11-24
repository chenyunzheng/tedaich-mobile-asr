package com.tedaich.mobile.asr.ui.me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.activity.UserLoginActivity;
import com.tedaich.mobile.asr.model.User;
import com.tedaich.mobile.asr.util.Constants;
import com.tedaich.mobile.asr.widget.CircleImageView;

public class MyFragment extends Fragment {

    private FragmentActivity fragmentActivity;

    private MyViewModel myViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = getActivity();
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        CircleImageView userImage = root.findViewById(R.id.user_front_image);
        TextView userAction = root.findViewById(R.id.user_action);

        userImage.setOnClickListener(this::handleAvatarChange);
        userAction.setOnClickListener(this::handleUserActionClick);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getLong("G_USER_ID", -1) != -1){
            //update ui

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_OK){
            if (requestCode == Constants.REQUEST_CODE_USER_LOGIN && data != null){
                //update current user info into shared preference
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putLong("G_USER_ID", data.getLongExtra("G_USER_ID", -1));

                spEditor.apply();
                //check or update user table by g_user_id
                User user = new User();
                //update local user id into shared preference
                spEditor.putLong("CURRENT_USER_ID", user.getId() == null ? -1 : user.getId());
                spEditor.apply();
            }
        }
    }

    private void handleAvatarChange(View view){
        //login or not
        if (sharedPreferences.getLong("G_USER_ID", -1) == -1){
            Toast.makeText(fragmentActivity, "Login first", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(fragmentActivity, "in developing", Toast.LENGTH_SHORT).show();
            //update user table and shared preference for avatar path

        }
    }

    private void handleUserActionClick(View view) {
        Intent intent = new Intent(view.getContext(), UserLoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_USER_LOGIN);
    }
}