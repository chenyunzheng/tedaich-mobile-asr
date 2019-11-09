package com.tedaich.mobile.asr.ui.me;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tedaich.mobile.asr.R;

public class MyFragment extends Fragment {

    private MyViewModel myViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        SharedPreferences spf = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
    }
}