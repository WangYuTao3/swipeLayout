package me.library.swipe;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangyt on 2016/10/24.
 * :帮助类 记录item,控制开关；
 */
public class SwipeHelper implements RecyclerView.OnItemTouchListener {

    private SwipeCallback mCallback;
    private SwipeLayout mCurSwipeLayout;

    public SwipeHelper(SwipeCallback callback) {
        mCallback = callback;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        int acton = MotionEventCompat.getActionMasked(e);
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (acton) {
            case MotionEvent.ACTION_DOWN:
                if (!inView(x, y)) {
                    closeSwipeLayout();
                }
                mCurSwipeLayout = mCallback.getSwipeLayout(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /**
     * 直接关闭当前swipeLayout
     */
    private void closeSwipeLayout() {
        if (mCurSwipeLayout != null) {
            mCurSwipeLayout.close();
            mCurSwipeLayout = null;
        }
    }

    /**
     * 判断是否在当前item里
     *
     * @param x
     * @param y
     * @return
     */
    private boolean inView(int x, int y) {
        if (mCurSwipeLayout == null)
            return false;
        int left = mCurSwipeLayout.getLeft();
        int top = mCurSwipeLayout.getTop();
        int right = mCurSwipeLayout.getRight();
        int bottom = mCurSwipeLayout.getBottom();
        Rect rect = new Rect(left, top, right, bottom);
        return rect.contains(x, y);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public static SwipeLayout getSwipeLayout(Context context, int topViewRes, int bgViewRes) {
        SwipeLayout swipeLayout = new SwipeLayout(context);
        View topView = LayoutInflater.from(context).inflate(topViewRes, swipeLayout, false);
        View bgView = LayoutInflater.from(context).inflate(bgViewRes, swipeLayout, false);
        swipeLayout.initView(topView, bgView);
        return swipeLayout;
    }
}