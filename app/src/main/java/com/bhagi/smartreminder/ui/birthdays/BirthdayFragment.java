package com.bhagi.smartreminder.ui.birthdays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bhagi.smartreminder.R;

public class BirthdayFragment extends Fragment {

    private BirthdayViewModel birthdayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        birthdayViewModel = ViewModelProviders.of(this).get(BirthdayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_birthday, container, false);
        final TextView textView = root.findViewById(R.id.text_birthday);
        birthdayViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}