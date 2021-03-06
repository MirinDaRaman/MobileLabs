package com.example.android.mobilecourse;
import android.os.Bundle;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabFragments();
    }

    public void initTabFragments() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        //tabMovies = findViewById(R.id.movie_tab);
        //tabTab2 = findViewById(R.id.tab_tab2);
        //TabItem tabProfile = findViewById(R.id.profile_tab);
        viewPager = findViewById(R.id.viewPager);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    public ViewPager getViewPager(){
        return viewPager;
    }
}