package android.rss;

import android.os.Handler;
import android.util.Log;

import org.xml.sax.InputSource;

/**
 * Created by Тима on 26.04.2017.
 */

public class MyRunnable implements Runnable {
    private Thread thread;
    private RSSFeed rss;
    private boolean mIsAlive = false;
    private int mNumber = 0;
    private String mName = "";
    MyRunnable(RSSFeed rssFeed, String name) {
        rss = rssFeed;
        mName = name;
        thread = new Thread(this, name + String.valueOf(mNumber++));
    }

    public void run() {
        mIsAlive = true;
        rss.updateRssList(mName);
        mIsAlive = false;
    }

    public void join()
    {
        try {
            if (thread.isAlive()) {
                thread.join();
            }
        }catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isAlive()
    {
        return mIsAlive;
    }

    public void start()
    {
        thread = new Thread(this, mName + String.valueOf(mNumber++));
        mIsAlive = true;
        thread.start();
    }

}
