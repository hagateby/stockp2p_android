package com.stockp2p.common.view.test;

import com.stockp2p.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;



public class test_StickNavLayoutActivity extends FragmentActivity
{
	private String[] mTitles = new String[] { "简介", "评价", "相关" };
	private SimpleViewPagerIndicator mIndicator;
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private TabFragment[] mFragments = new TabFragment[mTitles.length];

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_sticknavlayout);

		initViews();
		initDatas();
		initEvents();
	}

	private void initEvents()
	{
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				mIndicator.scroll(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{

			}
		});

	}

	private void initDatas()
	{
		mIndicator.setTitles(mTitles);

		for (int i = 0; i < mTitles.length; i++)
		{
			mFragments[i] = (TabFragment) TabFragment.newInstance(mTitles[i]);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mTitles.length;
			}

			@Override
			public Fragment getItem(int position)
			{
				return mFragments[position];
			}

		};

		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
	}

	private void initViews()
	{
		mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
		mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
	}

}
