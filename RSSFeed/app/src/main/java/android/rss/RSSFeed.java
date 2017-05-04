package android.rss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.ListView;
import android.widget.TextView;

import static android.R.attr.type;

public class RSSFeed extends AppCompatActivity implements Serializable{

    public static RSSItem selectedRssItem = null;
    private RecyclerView mRssListView = null;
    private RecyclerView.Adapter mRssAdapter = null;
    private RecyclerView.LayoutManager mLayoutManager;
    private String mRssFeedUrl = "https://lenta.ru/rss";
    private String mFileName = "news.txt";
    private String mFeedName = "feed";
    private ArrayList<RSSItem> mRssItems = new ArrayList<RSSItem>();
    private RSSFeed self = this;
    private FileManager mManager = null;
    private Timer mTimer;
    private Context mContext = null;
    private Handler mHandler;
    private MyRunnable mRunnable;
    private boolean mIsUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssfeed);
        mManager = new FileManager(this, mFileName);

        mRssItems = mManager.readTasksFromFile();
        Collections.reverse(mRssItems);


        mRssListView = (RecyclerView) findViewById(R.id.rssRecyclerView);
        mRssListView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRssListView.setLayoutManager(mLayoutManager);
        mRssAdapter = new MyRecyclerViewAdapter(mRssItems);
        mRssListView.setAdapter(mRssAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRssListView.addItemDecoration(itemDecoration);
//        mRssListView = (RecyclerView) findViewById(R.id.rssRecyclerView);
        /*mRssListView.(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int index,
                                    long arg3) {

                selectedRssItem = mRssItems.get(index);
                Intent intent = new Intent(self, RSSItemDisplayer.class);
                intent.putExtra("description", selectedRssItem.getDescription());
                intent.putExtra("title", selectedRssItem.getTitle());
                intent.putExtra("link", selectedRssItem.getLink());
                intent.putExtra("pubDate", selectedRssItem.getPubDate());

                startActivity(intent);
            }
        });
*/
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                mRssAdapter = new MyRecyclerViewAdapter(mRssItems);
                mRssListView.setAdapter(mRssAdapter);
                mRssAdapter.notifyDataSetChanged();
            }
        };
        mRunnable = new MyRunnable(this, mFeedName);
        try {
            Intent service = new Intent(this, MyService.class);
            service.putExtra("size", mRssItems.size());
            startService(service);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }



        settingTimerToUpdate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mRssAdapter).setOnItemClickListener(new
             MyRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    selectedRssItem = mRssItems.get(position);
                    Intent intent = new Intent(self, RSSItemDisplayer.class);
                    intent.putExtra("description", selectedRssItem.getDescription());
                    intent.putExtra("title", selectedRssItem.getTitle());
                    intent.putExtra("link", selectedRssItem.getLink());
                    intent.putExtra("pubDate", selectedRssItem.getPubDate());
                    startActivity(intent);
                }});
    }

    public int getNumberItems(){
        return mRssItems.size();
    }

    public void settingTimerToUpdate()
    {
        mTimer = new Timer("timer");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!self.mRunnable.isAlive()) {
                    self.updateNews();
                }
            }
        },1000,6000);
    }

    public void updateRssList(String name) {

        if (mFeedName == null) {
            mFeedName = "feed";
        }
        if (mManager == null) {
            mManager = new FileManager(mContext, mFileName);
        }
        if (mRssItems.isEmpty()) {
            mRssItems = mManager.readTasksFromFile();
        }
        mIsUpdate = false;
        ArrayList<RSSItem> newItems = RSSItem.getRssItems(mRssFeedUrl);
        int size = mRssItems.size();
        if (size == 0 || !mRssItems.get(0).getTitle().equals(newItems.get(0).getTitle()))
        {
            mIsUpdate = true;
            if (name.equals(mFeedName)){
                mRssItems = mManager.append(mRssItems, newItems);
            }
        }
    }

    public boolean isUpdate()
    {
        return mIsUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {mIsUpdate = isUpdate;}

    public ArrayList<RSSItem> getRssItems()
    {
        return mRssItems;
    }

    public void setContext(Context context)
    {
        mContext = context;
    }

    public void  updateNews()
    {
        mRunnable.start();
        mHandler.sendEmptyMessage(0);
    }

}
