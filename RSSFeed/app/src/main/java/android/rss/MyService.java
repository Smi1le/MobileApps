package android.rss;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {
    private RSSFeed mRssFeed;
    private Timer mTimer;
    private ArrayList<RSSItem> rssItems = new ArrayList<RSSItem>();
    private int mSizeArray;
    private MyRunnable mRunnable;
    private Handler mHandler;
    private Service self = this;
    private int mId = 12;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
        FileManager manager = new FileManager(getApplicationContext(), "news.txt");
        rssItems = manager.readTasksFromFile();
        Collections.reverse(rssItems);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSizeArray = intent.getIntExtra("size", 0);
        mRssFeed = new RSSFeed();
        mRunnable = new MyRunnable(mRssFeed, "service");
        mRssFeed.setContext(getApplicationContext());
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mRunnable.isAlive()) {
                    updateNews();
                }
            }
        },1000,6000);

        return START_STICKY;
    }

    public void  updateNews()
    {
        mRunnable.start();
        mRunnable.join();
        if (mRssFeed.isUpdate() && mRssFeed.getNumberItems() > mSizeArray) {
            sendNotif();
            mSizeArray = mRssFeed.getNumberItems();
            mRssFeed.setIsUpdate(false);
        }
    }

    private void sendNotif() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Новости")
                        .setContentText("У вас " + String.valueOf(mRssFeed.getNumberItems() - mSizeArray) + " новых сообщений");
        Intent resultIntent = new Intent(this, RSSFeed.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RSSItemDisplayer.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mId, mBuilder.build());
    }
}
