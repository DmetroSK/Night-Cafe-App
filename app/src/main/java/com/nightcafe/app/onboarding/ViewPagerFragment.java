package com.nightcafe.app.onboarding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.nightcafe.app.R;
import com.nightcafe.app.onboarding.screens.FirstScreenFragment;
import com.nightcafe.app.onboarding.screens.SecondScreenFragment;
import com.nightcafe.app.onboarding.screens.ThirdScreenFragment;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager,container,false);

//        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>(
//                FirstScreenFragment(),
//                SecondScreenFragment(),
//                ThirdScreenFragment());

  //      Adapter adapter = ViewPagerAdapter(fragmentList,requireActivity().getSupportFragmentManager(), Lifecycle);
        return view;
    }
}