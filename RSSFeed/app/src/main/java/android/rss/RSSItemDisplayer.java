package android.rss;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RSSItemDisplayer extends AppCompatActivity implements View.OnClickListener{
    private Button mLinkButton;
    private Button mBackToListButton;
    private String mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item_displayer);

        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        mLink = intent.getStringExtra("link");
        String date = intent.getStringExtra("pubDate");

        mLinkButton = (Button) findViewById(R.id.followTheLink);
        mBackToListButton = (Button) findViewById(R.id.backToList);
        TextView titleView = (TextView) findViewById(R.id.titleTextView);
        TextView contentView = (TextView) findViewById(R.id.contentTextView);
        TextView pubDateView = (TextView) findViewById(R.id.pubDate);

        titleView.setText(title);
        contentView.setText(description);
        pubDateView.setText(date);


        /*RSSItem selectedRssItem = RSSFeed.selectedRssItem;
        //Bundle extras = getIntent().getExtras();
        TextView titleTv = (TextView)findViewById(R.id.titleTextView);
        TextView contentTv = (TextView)findViewById(R.id.contentTextView);

        String title = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");
        title = "\n" + selectedRssItem.getTitle() + "  ( "
                + sdf.format(selectedRssItem.getPubDate()) + " )\n\n";

        String content = "";
        content += selectedRssItem.getDescription() + "\n"
                + selectedRssItem.getLink();

        titleTv.setText(title);
        contentTv.setText(content)*/;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.followTheLink:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink));
                startActivity(browserIntent);
                break;
            case R.id.backToList:
                finish();
                break;
        }
    }

}
