package com.location.reminder;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.location.reminder.util.Constants;

public class BaseFragment extends Fragment {

    public SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);
    }

    public Typeface getFontAwesomeTf() {
        return Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
    }
}
