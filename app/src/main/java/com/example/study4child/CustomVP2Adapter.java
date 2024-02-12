package com.example.study4child;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;

public class CustomVP2Adapter extends FragmentStateAdapter {
    private ArrayList<Fragment> pages;

    public CustomVP2Adapter(FragmentActivity activity) {
        super(activity);
        pages = new ArrayList<Fragment>();
    }

    public void addItem(Fragment fragment) {
        pages.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return pages.get(position);
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }
}