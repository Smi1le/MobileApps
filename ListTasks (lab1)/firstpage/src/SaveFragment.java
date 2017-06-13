package com.example.firstpage;


import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;

public class SaveFragment extends Fragment {
    private ArrayList<Task> mModel;


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    public ArrayList<Task> getModel(){
        return  mModel;
    }

    public  void setModel(ArrayList<Task> model){
        mModel = model;
    }
}
