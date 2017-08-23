package com.bokun.bkjcb.infomationmanage.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bokun.bkjcb.infomationmanage.Adapter.SimpleFragmentAdapter;
import com.bokun.bkjcb.infomationmanage.Fragment.MainFragment;
import com.bokun.bkjcb.infomationmanage.Fragment.SecondFragment;
import com.bokun.bkjcb.infomationmanage.Fragment.ThridFragment;
import com.bokun.bkjcb.infomationmanage.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        navigationView.setOnNavigationItemSelectedListener(this);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(SecondFragment.newInstance());
        fragments.add(MainFragment.newInstance());
        fragments.add(ThridFragment.newInstance());
        SimpleFragmentAdapter adapter = new SimpleFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentId = R.id.navigation_home;
                } else if (position == 1) {
                    currentId = R.id.navigation_nav;
                } else {
                    currentId = R.id.navigation_other;
                }
                navigationView.setSelectedItemId(currentId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_nav:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_other:
                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }
}
