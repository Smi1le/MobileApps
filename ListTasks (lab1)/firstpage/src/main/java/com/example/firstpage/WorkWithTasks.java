package com.example.firstpage;

import java.util.ArrayList;

public class WorkWithTasks {
    private ArrayList<Task> mTasks;

    public WorkWithTasks(ArrayList<Task> tasks)
    {
        mTasks = tasks;
    }

    public void SelectAllTasks()
    {
        ChangeSelectionTasks(true);
    }

    public void UnselectAllTasks()
    {
        ChangeSelectionTasks(false);
    }

    private void ChangeSelectionTasks(Boolean value)
    {
        for(int i = 0; i != mTasks.size(); ++i)
        {
            mTasks.get(i).setChecked(value);
        }
    }

    public void DeleteAllTasks()
    {
        for(int i = mTasks.size() - 1; i != -1; --i)
        {
            if (mTasks.get(i).isChecked()) {
                mTasks.remove(i);
            }
        }
    }

    public void MarkAsDone(int pos)
    {
        Task task = mTasks.get(pos);
        task.setComplete(true);
        mTasks.add(task);
        mTasks.remove(pos);

    }
}
