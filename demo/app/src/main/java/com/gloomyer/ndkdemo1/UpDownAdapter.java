package com.gloomyer.ndkdemo1;

import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * 带上下移动动画的myadapter
 */
public abstract class UpDownAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    @Override
    @CallSuper
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null ||
                !(layoutManager instanceof LinearLayoutManager)) {
            throw new RuntimeException("附加的RecyclerView的LayoutManager必须是LinearLayoutManager!");
        }

        mRecyclerView = recyclerView;
        mLayoutManager = (LinearLayoutManager) layoutManager;
        if (mLayoutManager.getReverseLayout())
            throw new RuntimeException("LinelayoutManager的ReverseLayout必须是false");
    }

    /**
     * 向上移动
     *
     * @param holder
     * @param position
     */
    protected final void toUp(final T holder, final int position) {
        if (position == 0)//如果是0将没有任何意义
            return;

        Runnable run = new Runnable() {
            @Override
            public void run() {
                //自己
                float space = getItemSpace() * 1.0f / getItemHeight();
                AnimationSet set1 = new AnimationSet(true);
                TranslateAnimation translate1
                        = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, -1 - space);
                translate1.setFillAfter(true);
                translate1.setDuration(300);
                set1.addAnimation(translate1);
                set1.setFillAfter(true);
                set1.setDuration(300);
                set1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        holder.itemView.setAlpha(0.8f);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.itemView.setAlpha(1.0f);
                        swop(position, position - 1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                //寻找上一个
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                int last = position - firstVisibleItemPosition;
                final View view = mRecyclerView.getChildAt(last - 1);
                AnimationSet set2 = new AnimationSet(true);
                TranslateAnimation translate2
                        = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 1 + space);
                translate2.setFillAfter(true);
                translate2.setDuration(300);
                set2.addAnimation(translate2);
                set2.setFillAfter(true);
                set2.setDuration(300);
                set2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (view != null)
                            view.setAlpha(0.8f);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (view != null)
                            view.setAlpha(1.0f);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                holder.itemView.startAnimation(set1);
                if (view != null)
                    view.startAnimation(set2);
            }
        };

        int firstCompletelyVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisibleItemPosition >= position) {
            //移动
            mLayoutManager.scrollToPosition(position - 1);//移动到上一个
            mRecyclerView.postDelayed(run, 50);
        } else {
            run.run();
        }


    }

    /**
     * 向下移动
     *
     * @param holder
     * @param position
     */
    protected final void toDown(final T holder, final int position) {
        if (position == getItemCount() - 1)
            return;//如果是最后一条，将毫无意义
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //自己
                float space = getItemSpace() * 1.0f / getItemHeight();
                AnimationSet set1 = new AnimationSet(true);
                TranslateAnimation translate1
                        = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 1 + space);
                translate1.setFillAfter(true);
                translate1.setDuration(300);
                set1.addAnimation(translate1);
                set1.setFillAfter(true);
                set1.setDuration(300);
                set1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        holder.itemView.setAlpha(0.8f);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.itemView.setAlpha(1.0f);
                        swop(position, position + 1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                //寻找上一个
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                int last = position - firstVisibleItemPosition;
                final View view = mRecyclerView.getChildAt(last + 1);
                AnimationSet set2 = new AnimationSet(true);
                TranslateAnimation translate2
                        = new TranslateAnimation(
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, 0,
                        TranslateAnimation.RELATIVE_TO_SELF, -1 - space);
                translate2.setFillAfter(true);
                translate2.setDuration(300);
                set2.addAnimation(translate2);
                set2.setFillAfter(true);
                set2.setDuration(300);
                set2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if (view != null)
                            view.setAlpha(0.8f);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (view != null)
                            view.setAlpha(1.0f);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                holder.itemView.startAnimation(set1);
                if (view != null)
                    view.startAnimation(set2);
            }
        };


        int lastCompletelyVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
        int firstCompletelyVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        int last = position + 1;
        if (last > firstCompletelyVisibleItemPosition && last <= lastCompletelyVisibleItemPosition) {
            //在可见范围之内!
            run.run();
        } else {
            //不在可见范围之内!
            mRecyclerView.scrollToPosition(last);
//            mLayoutManager.scrollToPosition(firstCompletelyVisibleItemPosition
//                    + (last - lastCompletelyVisibleItemPosition));
            mRecyclerView.postDelayed(run, 80);
        }
    }

    /**
     * 交换逻辑
     */
    protected abstract void swop(int oldPos, int newPos);

    /**
     * 获取item的实际高度
     *
     * @return
     */
    protected abstract int getItemHeight();

    /**
     * 获取两个item之间的间距
     *
     * @return
     */
    protected abstract int getItemSpace();
}
