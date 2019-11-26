package com.tedaich.mobile.asr.ui.cloud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tedaich.mobile.asr.App;
import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.ui.cloud.adapter.CloudAudioItemAdapter;
import com.tedaich.mobile.asr.util.Constants;

public class CloudFragment extends Fragment {

    private FragmentActivity fragmentActivity;
    private CloudViewModel cloudViewModel;
    private SharedPreferences sharedPreferences;

    private RecyclerView audioRecyclerView;
    private CloudAudioItemAdapter cloudAudioItemAdapter;
    private DaoSession daoSession;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        daoSession = ((App) fragmentActivity.getApplication()).getDaoSession();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cloudViewModel = ViewModelProviders.of(this).get(CloudViewModel.class);
        cloudViewModel.setDaoSession(daoSession);

        View root = inflater.inflate(R.layout.fragment_cloud, container, false);
        audioRecyclerView = root.findViewById(R.id.audio_recycler_view);
        audioRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        audioRecyclerView.addItemDecoration(new DividerItemDecoration(fragmentActivity, DividerItemDecoration.VERTICAL));
        audioRecyclerView.setLayoutManager(layoutManager);

        int userId = (int)sharedPreferences.getLong("CURRENT_USER_ID", -1);
        cloudViewModel.getAudioList(userId).observe(this, audio -> {
            if (cloudAudioItemAdapter != null){
                cloudAudioItemAdapter.release();
            }
            cloudAudioItemAdapter = new CloudAudioItemAdapter(audio, daoSession, sharedPreferences, this);
            audioRecyclerView.setAdapter(cloudAudioItemAdapter);
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.audio_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        configureSearchView(searchView);
    }

    private void configureSearchView(SearchView searchView) {
        Resources resources = getResources();
        searchView.setQueryHint(resources.getString(R.string.search_hint));
        searchView.setIconifiedByDefault(resources.getBoolean(R.bool.search_default_iconified));
        searchView.setIconified(resources.getBoolean(R.bool.search_default_iconified));
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getContext(), newText, Toast.LENGTH_SHORT).show();
                //thread pool

                //handler to update UI
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.RESULT_OK){
            if (requestCode == Constants.REQUEST_CODE_TRANSFER_TEXT && data != null){
                //nothing
            }
        }
    }
}