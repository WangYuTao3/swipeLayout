package me.swipelayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.library.swipe.SwipeCallback;
import me.library.swipe.SwipeHelper;
import me.library.swipe.SwipeLayout;

public class DemoActivity extends AppCompatActivity implements SwipeCallback {

    RecyclerView mList;
    ImageView mImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
    }

    private void initView() {
        mList = (RecyclerView) findViewById(R.id.recyList);
        mList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mList.addOnItemTouchListener(new SwipeHelper(this));
        mImgView = new ImageView(this);
        mImgView.setImageResource(R.mipmap.ic_launcher);
        mImgView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 280));
        mList.setAdapter(new DemoAdapter());
    }

    @Override
    public SwipeLayout getSwipeLayout(float x, float y) {
        View view = mList.findChildViewUnder(x, y);
        if (view instanceof SwipeLayout) {
            return (SwipeLayout) view;
        }
        return null;
    }
}
