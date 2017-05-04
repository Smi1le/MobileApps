package android.rss;

import android.widget.TextView;

public class ItemViewHolder {
    private TextView mHeader;
    private TextView mDate;

    public ItemViewHolder() {
    }

    public ItemViewHolder(TextView header, TextView date) {
        this.mHeader = header;
        this.mDate = date;
    }

    public TextView getHeader() {
        return mHeader;
    }

    public void setHeader(TextView textView) {
        this.mHeader = textView;
    }

    public TextView getDate() {
        return mDate;
    }

    public void setDate(TextView textView) {
        this.mDate = textView;
    }
}
