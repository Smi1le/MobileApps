package android.rss;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;


public class FileManager {
    private String mFileName;
    private String mDelimiter;
    private Context mContext;

    FileManager(Context context, String fileName)
    {
        this.mContext = context;
        this.mFileName = fileName;
        this.mDelimiter = "◘";
    }

    ArrayList<RSSItem> readTasksFromFile() {
        ArrayList<RSSItem> items = new ArrayList<>();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new InputStreamReader(mContext.openFileInput(mFileName)));
            String str = "";
            while ((str = br.readLine()) != null) {
                String[] values = str.split(mDelimiter);
                RSSItem newTask = new RSSItem(values[0], values[1], values[2], values[3]);
                items.add(newTask);
            }
            return items;
        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            try{
                if (br != null) {
                    br.close();
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }

        return items;
    }

    public void writeTasksFromFile(RSSItem task){
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new OutputStreamWriter(mContext.openFileOutput(mFileName, mContext.MODE_APPEND)));
            String del = mDelimiter;
            bw.write(task.getTitle() + del + task.getDescription() + del + task.getPubDate() + del + task.getLink());
            bw.newLine();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            try{
                if (bw != null) {
                    bw.close();
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void rewriteTasksFromFile(ArrayList<RSSItem> items){
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new OutputStreamWriter(mContext.openFileOutput(mFileName, mContext.MODE_PRIVATE)));
            String del = mDelimiter;
            for(int i = 0; i != items.size(); ++i)
            {
                RSSItem task = items.get(i);
                bw.write(task.getTitle() + del + task.getDescription() + del + task.getPubDate() + del + task.getLink());
                if (i + 1 != items.size()) {
                    bw.newLine();
                }
            }
            bw.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }finally{
            try{
                if (bw != null) {
                    bw.close();
                }
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<RSSItem> append(ArrayList<RSSItem> listItems, ArrayList<RSSItem> newItems)
    {
        Collections.reverse(newItems);
        for(int i = 0; i != newItems.size(); ++i)
        {
            if (!isAvailable(listItems, newItems.get(i)))
            {
                writeTasksFromFile(newItems.get(i));
                listItems.add(0, newItems.get(i));
            }
        }
        return listItems;
    }

    public boolean isAvailable(ArrayList<RSSItem> listItems, RSSItem newItem)
    {
        for(int i = 0; i != listItems.size(); ++i)
        {
            if (listItems.get(i).getTitle().equals(newItem.getTitle()))
            {
                return true;
            }
        }
        return false;
    }

}
