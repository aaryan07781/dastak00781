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

public class ProfitAnalysisActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabTitles = {"Daily", "Weekly", "Monthly", "Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_analysis);
        
        // Set title
        setTitle("Profit Analysis");
        
        // Initialize views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        
        // Set up ViewPager with fragments
        ProfitAnalysisPagerAdapter adapter = new ProfitAnalysisPagerAdapter(this);
        viewPager.setAdapter(adapter);
        
        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles[position]);
        }).attach();
    }
    
    // ViewPager adapter
    private class ProfitAnalysisPagerAdapter extends FragmentStateAdapter {
        
        public ProfitAnalysisPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return the appropriate fragment based on position
            switch (position) {
                case 0:
                    return ProfitAnalysisFragment.newInstance(ProfitAnalysisFragment.PERIOD_DAILY);
                case 1:
                    return ProfitAnalysisFragment.newInstance(ProfitAnalysisFragment.PERIOD_WEEKLY);
                case 2:
                    return ProfitAnalysisFragment.newInstance(ProfitAnalysisFragment.PERIOD_MONTHLY);
                case 3:
                    return ProfitAnalysisFragment.newInstance(ProfitAnalysisFragment.PERIOD_YEARLY);
                default:
                    return ProfitAnalysisFragment.newInstance(ProfitAnalysisFragment.PERIOD_DAILY);
            }
        }

        @Override
        public int getItemCount() {
            return tabTitles.length;
        }
    }
}
