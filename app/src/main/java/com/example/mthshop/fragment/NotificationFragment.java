package com.example.mthshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mthshop.adapter.NotificationAdapter;
import com.example.mthshop.databinding.FragmentNotificationBinding;
import com.example.mthshop.datalocal.SQLite;

import java.util.List;

public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding thisFragment;
    private List<String> listContent;
    private NotificationAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentNotificationBinding.inflate(getLayoutInflater());
        listContent = SQLite.getAllNotification(getContext());
        return thisFragment.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecycleView();
    }

    private void setRecycleView() {
        adapter = new NotificationAdapter(listContent, getActivity());
        LinearLayoutManager ln = new LinearLayoutManager(getContext());
        thisFragment.fNotificationRecyclerView.setLayoutManager(ln);
        thisFragment.fNotificationRecyclerView.setHasFixedSize(true);
        thisFragment.fNotificationRecyclerView.setAdapter(adapter);
    }



}
