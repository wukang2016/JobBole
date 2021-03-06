package simit.org.jobbole.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import simit.org.jobbole.adapter.MainViewPagerAdapter;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.fragment.BlogItemsFragment;
import simit.org.jobbole.fragment.FragmentFactory;
import simit.org.jobbole.utility.JobUtil;

public class MainActivity extends AppCompatActivity {
    private static final int CHANNEL_COUNT = 6;
    private static final int PAGE_OFFSET_LIMIT = 2;
    public static final int LOC_REQUEST_CODE = 1;
    // UI Components
    private DrawerLayout mNavDrawer;
    private NavigationView mNavView;
    private ActionBarDrawerToggle mToggle;
    private ViewPager mViewpager;
    // UI components in Naviagtion View
    private ImageView mUserPhoto;
    private TextView mUserName;
    // ViewPager Adapter
    private List<Fragment> fragments;
    private MainViewPagerAdapter mPagerAdapter;
    // page indicator
    private int curPage = 0;
    // curCityName
    private String cityName = "全国";
    private String cityTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // View initialization
        initViews();
    }

    /** init the UI components */
    private void initViews(){
        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        // CollapsingToolbarLayout
        //CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //mCollapsingToolbar.setTitle(getResources().getString(R.string.app_name));
        // DrawerLayout and Navigation View
        mNavDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mNavView = (NavigationView) findViewById(R.id.navigation_view);
        mToggle = new ActionBarDrawerToggle(this, mNavDrawer, mToolbar, R.string.app_name, R.string.app_name);
        mToggle.syncState();
        mNavDrawer.addDrawerListener(mToggle);
        //mNavDrawer.setDrawerListener(mToggle);
        // Navigation View
        setupNaviagtion();
        // ViewPager
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        // init Fragments
        fragments = initPagerFragments();
        mPagerAdapter.setFragments(fragments);
        // init tab titles from resourece
        String[] pageTitles = getResources().getStringArray(R.array.page_titles);
        mPagerAdapter.setPageTitles(pageTitles);
        // setup adapter
        mViewpager.setAdapter(mPagerAdapter);
        mViewpager.setOffscreenPageLimit(PAGE_OFFSET_LIMIT);
        // TabLayout
        TabLayout mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mTablayout.setupWithViewPager(mViewpager);
        //
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPage = position;
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date_menu, menu);
        if(curPage != CHANNEL_COUNT - 1){
            MenuItem loc = menu.findItem(R.id.action_loc);
            loc.setVisible(false);
            MenuItem loctext = menu.findItem(R.id.action_loctext);
            loctext.setVisible(false);
        }else {
            MenuItem loctext = menu.findItem(R.id.action_loctext);
            loctext.setTitle(cityName);
            //invalidateOptionsMenu();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_loc){
            // 跳转到CityActivity
            Intent intent = new Intent(this, CityActivity.class);
            startActivityForResult(intent, LOC_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && requestCode == LOC_REQUEST_CODE){
            if(curPage == JobboleConstants.DATE){
                String cityName = data.getStringExtra("cityName");
                String cityTag = data.getStringExtra("cityTag");

                BlogItemsFragment fragment = (BlogItemsFragment)fragments.get(curPage);
                fragment.updateCity(cityTag);
                //
                if(!TextUtils.isEmpty(cityName)){
                    this.cityName = cityName;
                    this.cityTag = cityTag;
                    invalidateOptionsMenu();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCityTag(){
        return cityTag;
    }

    /** initial the Navigation View */
    private void setupNaviagtion() {
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                /** TODO write navigation operation here */

                return false;
            }
        });
        //
        View header = mNavView.getHeaderView(0);
        mUserPhoto = (ImageView) header.findViewById(R.id.user_photo);
        mUserName = (TextView) header.findViewById(R.id.user_name);
        mUserPhoto.setImageDrawable(JobUtil.createCircleDrawable(this, R.mipmap.img103));
    }

    /** initial View Pager Fragments */
    private List<Fragment> initPagerFragments(){
        List<Fragment> fragments = new ArrayList<>();

        /** TODO initial the view pager fragments */
        for(int i = 0; i < CHANNEL_COUNT; i++){
            Fragment frag = FragmentFactory.createFragment(i);

            fragments.add(frag);
        }

        return  fragments;
    }
}
