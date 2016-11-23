package me.library.swipe;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by wangyt on 2016/10/26.
 * : dragHelper实现
 */
public class SwipeLayout extends FrameLayout {
    private static final float SCROLL_PERCENT = 0.2f;
    private ViewDragHelper mDragHelper;
    //上层滑动的view
    private View mTopView;
    //背景选显卡view
    private View mBgView;
    //背景选显卡view宽
    private int mBgWidth;
    //默认打开模式
    private SwipeMode mMode = SwipeMode.RIGHT;
    //默认状态
    protected SwipeState mState = SwipeState.CLOSE;

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void initView(View topView, View bgView) {
        this.mTopView = topView;
        this.mBgView = bgView;
        addView(createBgView(bgView));
        addView(topView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (widthMeasureSpec != 0) {
            mBgWidth = mBgView.getWidth();
        }
    }

    /**
     * 生成背景
     *
     * @param view
     * @return
     */
    private View createBgView(View view) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(mMode == SwipeMode.RIGHT ? Gravity.END : Gravity.START);
        linearLayout.addView(view);
        return linearLayout;
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 5.0f, new ViewDragCallBack());
        mDragHelper.setEdgeTrackingEnabled(mMode == SwipeMode.RIGHT ? ViewDragHelper.EDGE_RIGHT : ViewDragHelper.EDGE_LEFT);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mState == SwipeState.CLOSE)
            mBgView.setVisibility(View.GONE);
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 设置模式
     *
     * @param mode
     */
    public void setSwipeMode(SwipeMode mode) {
        this.mMode = mode;
        mDragHelper.setEdgeTrackingEnabled(mode == SwipeMode.RIGHT ? ViewDragHelper.EDGE_RIGHT : ViewDragHelper.EDGE_LEFT);
    }

    /**
     * 打开抽屉
     */
    public void open() {
        int newLeft = (mMode == SwipeMode.LEFT ? mBgWidth : -1 * mBgWidth);
        mState = SwipeState.OPEN;
        if (mDragHelper.smoothSlideViewTo(mTopView, newLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
        invalidate();
    }

    /**
     * 关闭抽屉
     */
    public void close() {
        mState = SwipeState.CLOSE;
        if (mDragHelper.smoothSlideViewTo(mTopView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
        invalidate();
    }

    private class ViewDragCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTopView;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mTopView, pointerId);
            if (mBgWidth != 0)
                mBgView.setVisibility(View.GONE);

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (left != 0) {
                if (mBgView.getVisibility() == View.GONE)
                    mBgView.setVisibility(View.VISIBLE);
            } else {
                if (mBgView.getVisibility() == View.VISIBLE)
                    mBgView.setVisibility(View.GONE);

            }
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (releasedChild != mTopView)
                return;
            int newLeft;
            if (mMode == SwipeMode.LEFT) {
                if (mTopView.getLeft() < (int) ((float) mBgWidth * SCROLL_PERCENT) || mState == SwipeState.OPEN) {
                    newLeft = 0;
                    mState = SwipeState.CLOSE;
                } else {
                    newLeft = mBgWidth;
                    mState = SwipeState.OPEN;
                }
            } else {
                if (mTopView.getLeft() > -(int) ((float) mBgWidth * SCROLL_PERCENT) || mState == SwipeState.OPEN) {
                    newLeft = 0;
                    mState = SwipeState.CLOSE;
                } else {
                    newLeft = -1 * mBgWidth;
                    mState = SwipeState.OPEN;
                }
            }
            if (mDragHelper.smoothSlideViewTo(mTopView, newLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
            }
            invalidate();
        }

        /**
         * 水平方向处理
         *
         * @param child 被拖动到view
         * @param left  移动到达的x轴的距离
         * @param dx    建议的移动的x距离
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mMode == SwipeMode.LEFT) {
                if (left > mBgWidth && dx > 0) return mBgWidth;
                if (left < 0 && dx < 0) return 0;
            } else {
                if (left > 0 && dx > 0) return 0;
                if (left < -mBgWidth && dx < 0) return -mBgWidth;
            }
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mTopView == child ? child.getWidth() : 0;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mTopView == child ? child.getHeight() : 0;
        }
    }

    /**
     * 状态枚举
     */
    public enum SwipeState {
        //打开状态
        OPEN,
        //关闭状态
        CLOSE
    }

    /**
     * 模式枚举
     */
    public enum SwipeMode {
        //左边按钮
        LEFT,
        //右边按钮
        RIGHT
    }
}