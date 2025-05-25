package com.aaryan7.dastakmobile.analysis;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.aaryan7.dastakmobile.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SalesAnalysisActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabTitles = {"Daily", "Weekly", "Monthly", "Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_analysis);
        
        // Set title
        setTitle("Sales Analysis");
        
        // Initialize views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        
        // Set up ViewPager with fragments
        SalesAnalysisPagerAdapter adapter = new SalesAnalysisPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles[position]);
        }).attach();
    }
    
    // ViewPager adapter
    private class SalesAnalysisPagerAdapter extends FragmentStateAdapter {
        
        public SalesAnalysisPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return the appropriate fragment based on position
            switch (position) {
                case 0:
                    return SalesAnalysisFragment.newInstance(SalesAnalysisFragment.PERIOD_DAILY);
                case 1:
                    return SalesAnalysisFragment.newInstance(SalesAnalysisFragment.PERIOD_WEEKLY);
                case 2:
                    return SalesAnalysisFragment.newInstance(SalesAnalysisFragment.PERIOD_MONTHLY);
                case 3:
                    return SalesAnalysisFragment.newInstance(SalesAnalysisFragment.PERIOD_YEARLY);
                default:
                    return SalesAnalysisFragment.newInstance(SalesAnalysisFragment.PERIOD_DAILY);
            }
        }

        @Override
        public int getItemCount() {
            return tabTitles.length;
        }
    }
}
