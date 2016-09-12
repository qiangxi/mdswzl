package com.lanma.lostandfound.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lanma.lostandfound.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class LookImageActivity extends BaseActivity {

    @Bind(R.id.imageViewPager)
    ViewPager mImageViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_image);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        ArrayList<String> imageList = getIntent().getStringArrayListExtra("ImageList");
        mImageViewPager.setAdapter(new SamplePagerAdapter(imageList));
    }

    public class SamplePagerAdapter extends PagerAdapter {
        private List<String> mList;

        public SamplePagerAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(container.getContext()).load(mList.get(position)).into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
