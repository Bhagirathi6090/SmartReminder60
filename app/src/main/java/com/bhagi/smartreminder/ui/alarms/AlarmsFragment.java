package com.bhagi.smartreminder.ui.alarms;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bhagi.smartreminder.NotificationActivity;
import com.bhagi.smartreminder.R;

public class AlarmsFragment extends Fragment {

    private EditText editTextTitle;
    private EditText editTextMessage;
    private Button textViewChannel1;
    private Button textViewChannel2;

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    public static AlarmsFragment newInstance() {
        return new AlarmsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_alarms, container, false);

        createNotification();

        editTextTitle = root.findViewById(R.id.edit_text_title);
        editTextMessage = root.findViewById(R.id.edit_text_message);

        textViewChannel1 = root.findViewById(R.id.channel_1_txt);
        textViewChannel2 = root.findViewById(R.id.channel_2_txt);

        textViewChannel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOnChannel1();
            }
        });

        textViewChannel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOnChannel2();
            }
        });

        return root;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void sendOnChannel1() {
        String msg = editTextMessage.getText().toString();
        String title = editTextTitle.getText().toString();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_1_ID);
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.addAction(R.drawable.ic_snooze, getString(R.string.snooze),
                pendingIntent);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());

        // notificationId is a unique int for each notification that you must define
        notificationManagerCompat.notify(1, builder.build());
    }

    public void sendOnChannel2() {
        String msg = editTextMessage.getText().toString();
        String title = editTextTitle.getText().toString();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_2_ID);
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());

        // notificationId is a unique int for each notification that you must define
        notificationManagerCompat.notify(2, builder.build());
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel 1");
            channel1.enableVibration(true);
            channel1.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("This is channel 2");

            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }


}
