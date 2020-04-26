package com.oslabs.notinClone;

import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



public class MainActivity extends Activity {
//    private final String CHANNEL_ID = "personal";
    private ImageButton add;
    private EditText text;
    public static int NOTIFICATION_ID  = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pass touch events to the background activity
        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);



        // Note that flag changes must happen *before* the content view is set.
        setContentView(R.layout.activity_main);
        //fetching button
        add = (ImageButton) findViewById(R.id.imageButton);
        text = (EditText) findViewById(R.id.editText);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textValue = text.getText().toString();
                Log.i("Text",textValue);
                if(textValue.isEmpty()){
                    Toast.makeText(getApplicationContext(),"you need to enter some text",Toast.LENGTH_LONG).show();
                    return;
                }

                //Notification code
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                        MainActivity.this,CHANNEL_ID
//
//                );
//                // builder params
//                builder.setSmallIcon(R.drawable.ic_add);
//                builder.setContentTitle(textValue.toString());
//                builder.setContentText("this is notificatin explain ");
//                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
//                notificationManagerCompat.notify(1,builder.build());
                buildNotification(textValue);
                NOTIFICATION_ID+=1;
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        finish();

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    private void buildNotification(String textValue){
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel"+NOTIFICATION_ID;
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel"+NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(textValue)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
