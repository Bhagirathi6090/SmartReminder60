package com.bhagi.smartreminder.ui.birthdays;

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

import com.bhagi.smartreminder.BirthdayActivity;
import com.bhagi.smartreminder.EditorActivity;
import com.bhagi.smartreminder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BirthdayFragment extends Fragment {

    private BirthdayViewModel birthdayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_birthday, container, false);

        Button addBirthday = root.findViewById(R.id.add_birth_day);
        addBirthday.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(getActivity(), BirthdayActivity.class);
        startActivity(intent);
    }

}