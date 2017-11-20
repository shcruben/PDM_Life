package com.iteso.ruben.proyectoversion1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityWelcome extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private MyViewPageAdapter myViewPageAdapter;
    private ImageView img;
    private int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }

        setContentView(R.layout.activity_welcome);

        viewPager   = findViewById(R.id.activity_welcome_view_pager);
        btnSkip     = findViewById(R.id.activity_welcome_btn_skip);
        btnNext     = findViewById(R.id.activity_welcome_btn_next);
        dotsLayout  = findViewById(R.id.activity_welcome_layoutDots);

        layouts = new int[]{
            R.layout.welcome_slide0,
            R.layout.welcome_slide1,
            R.layout.welcome_slide2,
            R.layout.welcome_slide3,
            R.layout.welcome_slide4
        };

        images = new int[]{
            R.id.slide1_img,
            R.id.slide1_img,
            R.id.slide2_img,
            R.id.slide3_img,
            R.id.slide4_img
        };

        addBottomDots(0);
        changeStatusBarColor();

        myViewPageAdapter = new MyViewPageAdapter();
        viewPager.setAdapter(myViewPageAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNextActivity();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checking for last page , if last page next activity will be launched
                int current = getItem(+1);
                if(current < layouts.length){
                    viewPager.setCurrentItem(current);
                }else{
                    launchNextActivity();
                }
            }
        });

    }

    private void addBottomDots(int currentPage){
        dots = new TextView[layouts.length];

        int[] colorsActive   = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();

        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            );
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void launchNextActivity(){
        Intent intent = new Intent(ActivityWelcome.this,
                HelloUpActivity.class);
        startActivity(intent);
        finish();
    }

    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if(position == layouts.length - 1){
                //last page
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            }else{
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }

            img = findViewById(images[position]);
            img.post(new ImgAnim());

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class MyViewPageAdapter extends PagerAdapter{
        private LayoutInflater inflater;

        public MyViewPageAdapter(){

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );

            View view = inflater.inflate(layouts[position],
                    container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public class ImgAnim implements Runnable{
        @Override
        public void run() {
            ((AnimationDrawable) img.getBackground()).start();
        }
    }

}
