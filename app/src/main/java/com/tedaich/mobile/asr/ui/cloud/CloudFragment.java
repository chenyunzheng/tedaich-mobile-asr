package com.tedaich.mobile.asr.ui.cloud;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.tedaich.mobile.asr.R;

public class CloudFragment extends Fragment {

    private CloudViewModel cloudViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        cloudViewModel =
                ViewModelProviders.of(this).get(CloudViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cloud, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        cloudViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}