package com.fyp.mykarachi;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fyp.mykarachi.dummy.DummyContent;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

public class HomeScreenActivity extends AppCompatActivity implements NewsFeedFragment.OnListFragmentInteractionListener,
        UpdatesFragment.OnFragmentInteractionListener, AddUpdateFragment.OnFragmentInteractionListener, FragmentListener {

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

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // PERFORM ACTIONS ON SIGN-OUT
                    startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });


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
        bottomAppBar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_round_more_vert_24px));
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {

                    case "Search":
                        Toast.makeText(HomeScreenActivity.this, "Search Button Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case "Sign Out":
//                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
//                        if (prev != null) {
//                            ft.remove(prev);
//                        }
//                        ft.addToBackStack(null);
//                        DialogFragment dialogFragment = new SignOutDialogFragment();
//                        dialogFragment.show(ft, "dialog");
                        FirebaseAuth.getInstance().signOut();
                        break;
                }
                return HomeScreenActivity.super.onOptionsItemSelected(item);
            }
        });


        // SET MAP FRAGMENT TO BOTTOM APP BAR NAVIGATION BUTTON
        setTransitionOnMapExit();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Animation animation = AnimationUtils.loadAnimation(HomeScreenActivity.this, R.anim.fab_grow);
//                floatingActionButton.startAnimation(animation);

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
    public void setBottomAppBarTransition(String callingContext) {

        switch (callingContext) {
            case "onMapAttach":
                setTransitionOnMapEntry();
                break;

            case "onMapDetach":
                setTransitionOnMapExit();
                break;
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        RoundedBottomSheetDialogFragment roundedBottomMenu = RoundedBottomSheetDialogFragment.getInstance();
//        roundedBottomMenu.show(getSupportFragmentManager(), "Custom Bottom Sheet");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getTitle().toString()) {

            case "Search":
                Toast.makeText(this, "Search Button Clicked", Toast.LENGTH_SHORT).show();
                break;

            case "Sign Out":
                FirebaseAuth.getInstance().signOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTransitionOnMapExit() {

        bottomAppBar.setNavigationIcon(R.drawable.ic_round_map_24px);
        final Fragment mapFragment = new MapFragment();
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.HomeScreenCoordinatorLayout, mapFragment);
                fragmentTransaction.addToBackStack(mapFragment.toString());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        });

    }

    private void setTransitionOnMapEntry() {

        bottomAppBar.setNavigationIcon(R.drawable.ic_round_close_24px);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeScreenActivity.super.onBackPressed();
            }
        });

    }

}
