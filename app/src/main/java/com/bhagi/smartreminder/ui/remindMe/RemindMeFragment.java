package com.bhagi.smartreminder.ui.remindMe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bhagi.smartreminder.EditorActivity;
import com.bhagi.smartreminder.R;
import com.bhagi.smartreminder.RemindMeActivity;

public class RemindMeFragment extends Fragment {

    private RemindMeViewModel remindMeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        remindMeViewModel =
                ViewModelProviders.of(this).get(RemindMeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_remind, container, false);

        Button addReminder = root.findViewById(R.id.write_a_reminder);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetail();
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_birthday_remind, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), RemindMeActivity.class);
        startActivity(intent);
    }

}