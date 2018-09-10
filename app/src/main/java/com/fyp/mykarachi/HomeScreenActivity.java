package com.fyp.mykarachi;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fyp.mykarachi.dummy.DummyContent;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class HomeScreenActivity extends AppCompatActivity implements NewsFeedFragment.OnListFragmentInteractionListener,
        UpdatesFragment.OnFragmentInteractionListener, AddUpdateFragment.OnFragmentInteractionListener {

    private static BottomAppBar bottomAppBar;
    private static ShimmerFrameLayout shimmerFrameLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;

    public static void stopShimmer() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

    public static void fabTransition() {
        int alignmentMode = bottomAppBar.getFabAlignmentMode();
        // XOR with "1" to inverse alignment
        bottomAppBar.setFabAlignmentMode(alignmentMode ^ 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        // Initialize fields
        tabLayout = findViewById(R.id.HomeScreenTabLayout);
        viewPager = findViewById(R.id.HomeScreenViewPager);
        bottomAppBar = findViewById(R.id.HomeScreenAppBar);
        floatingActionButton = findViewById(R.id.HomeScreenFab);
        shimmerFrameLayout = findViewById(R.id.ShimmerFrameLayout);

        viewPager.setAdapter(new HomeScreenFragmentPagerAdapter(getSupportFragmentManager(), HomeScreenActivity.this));

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_round_public_24px);
        tabLayout.getTabAt(0).setText("News Feed");

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_location_city_24px);
        tabLayout.getTabAt(1).setText("Updates");

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fabTransition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);

        // SET MAP FRAGMENT TO NAVIGATION BUTTON
        final Fragment mapFragment = new MapFragment();
        setSupportActionBar(bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.HomeScreenCoordinatorLayout, mapFragment);
                fragmentTransaction.addToBackStack(mapFragment.toString());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
                bottomAppBar.setNavigationIcon(R.drawable.ic_round_close_24px);
                bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HomeScreenActivity.super.onBackPressed();
                        bottomAppBar.setNavigationIcon(R.drawable.ic_round_map_24px);
                    }
                });

                // SET FAB BUTTON FUNCTIONALITY TO SEARCH HERE
            }
        });

        final Fragment addUpdateFragment = new AddUpdateFragment();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Animation animation = AnimationUtils.loadAnimation(HomeScreenActivity.this, R.anim.fab_grow);
//                floatingActionButton.startAnimation(animation);
//                RoundedBottomSheetDialogFragment roundedBottomMenu = RoundedBottomSheetDialogFragment.getInstance();
//                roundedBottomMenu.show(getSupportFragmentManager(), "Custom Bottom Sheet");
                Intent intent = new Intent(HomeScreenActivity.this, AddUpdateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        // Left blank. No functionality needed.
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Left blank. No functionality needed.
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        HomeScreenActivity.this.registerReceiver(new NetworkBroadcastReceiver(), intentFilter);

    }

//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }

}
