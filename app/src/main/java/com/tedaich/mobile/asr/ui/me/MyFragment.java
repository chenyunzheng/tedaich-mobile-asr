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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.widget.CircleImageView;

public class MyFragment extends Fragment {

    private FragmentActivity fragmentActivity;

    private MyViewModel myViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = getActivity();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        CircleImageView userImage = root.findViewById(R.id.user_front_image);
        TextView userAction = root.findViewById(R.id.user_action);

        userImage.setOnClickListener(this::handleUserImageChange);
        userAction.setOnClickListener(this::handleUserActionClick);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences spf = fragmentActivity.getSharedPreferences("info", Context.MODE_PRIVATE);
        spf.edit().putString("", null);
    }

    private void handleUserImageChange(View view){
        Toast.makeText(fragmentActivity, "in developing", Toast.LENGTH_SHORT).show();
    }

    private void handleUserActionClick(View view) {
        Intent intent = new Intent();

    }
}