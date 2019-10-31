package com.bhagi.smartreminder.ui.remindMe;

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

public class RemindMeFragment extends Fragment {

    private RemindMeViewModel remindMeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindMeViewModel =
                ViewModelProviders.of(this).get(RemindMeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_remind, container, false);
        final TextView textView = root.findViewById(R.id.text_remind);
        remindMeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}