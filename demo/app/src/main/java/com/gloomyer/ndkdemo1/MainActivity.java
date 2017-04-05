package com.gloomyer.ndkdemo1;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();

    RecyclerView mRecyclerView;

    List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter mAdapter = new MyAdapter();
        initData();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 20;
                outRect.right = 20;
                outRect.top = 30;
            }
        });
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mDatas.add("item pos:" + i + "|| value:" + stringFromJNI());
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView pos;
        TextView value;
        Button top;
        Button bottom;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView;
            pos = (TextView) itemView.findViewById(R.id.pos);
            value = (TextView) itemView.findViewById(R.id.text);
            top = (Button) itemView.findViewById(R.id.top);
            bottom = (Button) itemView.findViewById(R.id.bottom);
        }
    }

    private class MyAdapter extends UpDownAdapter<ViewHolder> {

        int height = 0;

        @Override
        protected void swop(int oldPos, int newPos) {
            mDatas.add(
                    oldPos > newPos ? newPos : oldPos,
                    mDatas.remove(oldPos > newPos ? oldPos : newPos));
//            notifyDataSetChanged();
            notifyItemChanged(oldPos);
            notifyItemChanged(newPos);
        }

        @Override
        protected int getItemHeight() {
            return height;
        }

        @Override
        protected int getItemSpace() {
            return 30;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.pos.setText("pos:" + position);
            holder.value.setText(mDatas.get(position));
            holder.top.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            holder.bottom.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
            holder.top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (height == 0) {
                        height = holder.itemView.getHeight();
                    }
                    toUp(holder, position);
                }
            });

            holder.bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (height == 0) {
                        height = holder.itemView.getHeight();
                    }
                    toDown(holder, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }
    }

//    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(MainActivity.this)
//                    .inflate(R.layout.item, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.pos.setText("pos:" + position);
//            holder.value.setText(mDatas.get(position));
//            holder.top.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
//            holder.bottom.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
//
//            holder.top.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //执行向上动画
//                    //平移
//                    //获取两个item之间间距占一个item的百分比,间距为固定值30
//                    final AnimationSet set
//                            = new AnimationSet(true);
//
//                    final float value = 30.0f / holder.content.getHeight();
//                    TranslateAnimation translate
//                            = new TranslateAnimation(
//                            TranslateAnimation.RELATIVE_TO_SELF, 0,
//                            TranslateAnimation.RELATIVE_TO_SELF, 0,
//                            TranslateAnimation.RELATIVE_TO_SELF, 0,
//                            TranslateAnimation.RELATIVE_TO_SELF, -1 - value);
//                    translate.setFillAfter(true);
//                    translate.setDuration(300);
//                    set.addAnimation(translate);
//                    set.setFillAfter(true);
//                    set.setDuration(300);
//                    set.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                            holder.content.setAlpha(0.8f);
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            holder.content.setAlpha(1.0f);
//                            mDatas.add(position - 1, mDatas.remove(position));
//                            notifyItemChanged(position);
//                            notifyItemChanged(position - 1);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//
//
//                    //移动上一个!
//                    final LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//                    int firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
//
//
//                    Runnable run = new Runnable() {
//                        @Override
//                        public void run() {
//                            //找到上一个viewholder
//                            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//                            int last = position - firstVisibleItemPosition;
//                            final View view = mRecyclerView.getChildAt(last - 1);
//
//                            AnimationSet set1
//                                    = new AnimationSet(true);
//                            TranslateAnimation translate1
//                                    = new TranslateAnimation(
//                                    TranslateAnimation.RELATIVE_TO_SELF, 0,
//                                    TranslateAnimation.RELATIVE_TO_SELF, 0,
//                                    TranslateAnimation.RELATIVE_TO_SELF, 0,
//                                    TranslateAnimation.RELATIVE_TO_SELF, 1 + value);
//                            translate1.setFillAfter(true);
//                            translate1.setDuration(300);
//                            set1.addAnimation(translate1);
//                            set1.setFillAfter(true);
//                            set1.setDuration(300);
//                            set1.setAnimationListener(new Animation.AnimationListener() {
//                                @Override
//                                public void onAnimationStart(Animation animation) {
//                                    view.setAlpha(0.8f);
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animation animation) {
//                                    view.setAlpha(1.0f);
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animation animation) {
//
//                                }
//                            });
//
//                            holder.content.startAnimation(set);
//                            view.startAnimation(set1);
//                        }
//                    };
//
//                    if (firstVisibleItemPosition >= position) {
//                        //如果上下交换，作为当前上面的view不可见需要滑动到它完全可见为止
//                        mRecyclerView.scrollToPosition(position - 1);
//                        mRecyclerView.postDelayed(run, 100);
//                        //这里着重说明下，因为上述的滚动是耗时操作
//                        //如果需要执行这个那么后续操作需要延时，否则会nullpoint!
//                    } else {
//                        run.run();
//                    }
//
//
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDatas == null ? 0 : mDatas.size();
//        }
//    }
}
