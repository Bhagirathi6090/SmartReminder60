package com.bhagi.smartreminder.ui.alarms;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bhagi.smartreminder.R;

public class AlarmsFragment extends Fragment {

    private AlarmsViewModel mViewModel;

    public static AlarmsFragment newInstance() {
        return new AlarmsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(AlarmsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_alarms, container, false);
        final TextView textView = root.findViewById(R.id.text_alarms);
        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AlarmsViewModel.class);
        // TODO: Use the ViewModel
    }

}
