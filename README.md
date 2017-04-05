### 概述

这是一个实现了RecyclerView的item上下交换动画的封装adapter

### 效果图:

![](http://gloomyer.com/img/img/recyclerview_item_change_demo.gif)



### 说明

效果图没有展示向下交换

1.<font color='red'>首先你得有一个RecyclerView，并且必须是LinearLayoutManager,

LinelayoutManager的ReverseLayout必须是false"

并且所有的item高度必须一致！</font>

2.继承自这个UpDownAdapter

3.实现createViewHolder、bindViewHolder、getItemCount 常规的三个方法

4.实现swop(int oldPos,int newPos);//交换item的实际逻辑

5.实现getItemHeight();//返回一个item的真实高度

6.实现getItemSpace();//item之间的间距

7.合适的地方调用toTp(holder, position);//holder， 当前的pos

toDown(holder, position);//holder, 当前的坐标





如果没有看懂，可以跑一边demo.