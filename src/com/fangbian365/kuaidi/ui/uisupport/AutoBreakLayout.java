package com.fangbian365.kuaidi.ui.uisupport;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 能根据子控件自动换行，同时把行空白空间平均分配到各行子控件中的容器
 * @author lruijun
 * 
 */
public class AutoBreakLayout extends FrameLayout {
	private final static int VIEW_MARGIN_CONST=6;  //按dp使用
	private int MarginPixelValue = 0;
	//===============================================================================
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		final int count = getChildCount();
		final int parentWidth = this.getMeasuredWidth(); // 保存当前容器的宽度值
		int row = 0; // which row lay you view relative to parent
		int curLeft = MarginPixelValue;
		int curRight = 0;
		int curTop = MarginPixelValue;
		int curRowHeight = 0; // 保存当前行的最大高度
		int curSpace = 0; // 保存当前行应伸展或缩小的宽度变化值
		int curCol = 1;
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i); // 得到子控件及宽、高值
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			if (row < rowList.size()) { // 获取到当前行的排版信息
				curRowHeight = rowList.get(row).maxHeight; // 获取到当前行的最大高度值
				curCol = rowList.get(row).cols;
				if (curCol > 0) {
					curSpace = rowList.get(row).space / curCol;
				}
			}
			curRight = curLeft + childWidth + MarginPixelValue; // 得到当前子控件的右边位置
			if (curRight > parentWidth) { // 如果当前控件超出了父控件宽度。则换行排放
				if (curCol == 1 && i == 0) { // 如果第一行同时只是一个子控件超过父控件了，则缩小放置
					child.layout(curLeft, curTop
							+ ((curRowHeight - childHeight) / 2), curLeft
							+ childWidth + curSpace, curTop
							+ ((curRowHeight - childHeight) / 2) + childHeight); // 让高度小于行最大高度的控件，垂直居中显
					curLeft = MarginPixelValue; // 复位左侧放置点到下一行最左边
					curTop += curRowHeight + MarginPixelValue; // 更新高度定位点为下一行的高度
					row++;
				} else {
					curLeft = MarginPixelValue; // 复位左侧放置点到下一行最左边
					curTop += curRowHeight + MarginPixelValue; // 更新高度定位点为下一行的高度
					curRight = curLeft + childWidth + MarginPixelValue; // 得到当前子控件的右边位置
					row++;
					if (row < rowList.size()) { // 获取到当前行的排版信息
						curRowHeight = rowList.get(row).maxHeight; // 获取到当前行的最大高度值
						curCol = rowList.get(row).cols;
						if (curCol > 0) {
							curSpace = rowList.get(row).space / curCol;
						}
					}
					if (curRight > parentWidth) { // 如果换行后，直接大于父控件宽度，说明当前子控件自己就宽过父控件，则直接按一行一个控件处理
						child.layout(curLeft, curTop
								+ ((curRowHeight - childHeight) / 2), curLeft
								+ childWidth + curSpace, curTop
								+ ((curRowHeight - childHeight) / 2)
								+ childHeight); // 让高度小于行最大高度的控件，垂直居中显
						curLeft = MarginPixelValue; // 复位左侧放置点到下一行最左边
						curTop += curRowHeight + MarginPixelValue; // 更新高度定位点为下一行的高度
						row++;
					} else {
						child.layout(curLeft, curTop
								+ ((curRowHeight - childHeight) / 2), curLeft
								+ childWidth + curSpace, curTop
								+ ((curRowHeight - childHeight) / 2)
								+ childHeight); // 让高度小于行最大高度的控件，垂直居中显
						curLeft = curLeft + childWidth + curSpace + MarginPixelValue; // 更新左侧放置点到新放控件后面
					}
				}
			} else { // 未超出，则直接排放位置
				child.layout(curLeft, curTop
						+ ((curRowHeight - childHeight) / 2), curLeft
						+ childWidth + curSpace, curTop
						+ ((curRowHeight - childHeight) / 2) + childHeight); // 让高度小于行最大高度的控件，垂直居中显
				curLeft = curLeft + childWidth + curSpace + MarginPixelValue; // 更新左侧放置点到新放控件后面
			}

		}
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);  //使用FrameLayout的测量方法，让子控件计算出自己的大小
		final int parentWidth = this.getMeasuredWidth();  //保存当前容器的宽度值
		final int count = getChildCount();
		if(initChildCount!=count||parentWidth!=initParentWidth){  //这样处理可以提高效率，不用在相同宽度和相同子控件时，一直重新排列
			initChildCount = count;
			initParentWidth = parentWidth;
	        rowList.clear();
			int curLeft = MarginPixelValue;
			int rowMaxHeight = 0;
			int totalHeight =MarginPixelValue;
			int rowChildCount=0;
			int childWidth = 0;
			int childHeight = 0;
			for(int i=0;i<count;i++){
				final View child = getChildAt(i);
				childWidth = child.getMeasuredWidth();
				childHeight = child.getMeasuredHeight();
				curLeft += (childWidth+MarginPixelValue);			
				rowChildCount++;
				if(curLeft>=parentWidth){
					if(rowChildCount<2){
						rowList.add(new RowInfo(rowChildCount, parentWidth-MarginPixelValue-childWidth-MarginPixelValue, childHeight));  //第一个子控件就超过父控件宽度时，直接缩小到同样宽度
						curLeft = MarginPixelValue;	
						rowChildCount =0;
						rowMaxHeight = 0;
					}else{
						rowList.add(new RowInfo(rowChildCount-1, parentWidth-(curLeft-childWidth-MarginPixelValue), rowMaxHeight));  //保存每一行的子控件数量，与父控件的空隙，最高的子控件高度
						curLeft = MarginPixelValue+childWidth+MarginPixelValue;	
						rowChildCount =1;
						rowMaxHeight = childHeight;
					}
					totalHeight += Math.max(rowMaxHeight, childHeight)+MarginPixelValue;
				}else{
					rowMaxHeight = Math.max(rowMaxHeight, childHeight);  //这个取值要保证在判断宽度的后面，防止把下一行的第一个的高度取到
				}
			}
			if(rowChildCount>0){
				rowList.add(new RowInfo(rowChildCount,parentWidth-curLeft, rowMaxHeight));   //此处减去二次View_Margin，不知道原理。需要再研究
				totalHeight +=rowMaxHeight+MarginPixelValue;
			}
			 setMeasuredDimension(resolveSize(parentWidth, widthMeasureSpec),resolveSize(totalHeight, heightMeasureSpec));			
		}else{
			 setMeasuredDimension(resolveSize(parentWidth, widthMeasureSpec),resolveSize(getRowTotalHeight(), heightMeasureSpec));	//此处要再设置一下高度。
		}
	}
	//==================================================================================
	private int getRowTotalHeight(){
		int ret = MarginPixelValue;
		for(RowInfo row:rowList){
			ret += row.maxHeight+MarginPixelValue;
		}
		return ret;
	}
	private int initChildCount = 0;
	private int initParentWidth = 0;
	private ArrayList<RowInfo>  rowList = new ArrayList<AutoBreakLayout.RowInfo>();
	private class RowInfo{
		public short cols;  //本行控件数量
		public short space; //本行空白宽度值，
		public short maxHeight; //本行控件的最大高度值
		private RowInfo(int cols, int space, int maxHeight) {
			super();
			this.cols = (short) cols;
			this.space = (short) space;
			this.maxHeight = (short) maxHeight;
		}
		
	}
	
	//==================================================================================
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    private int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
	
	private void init(Context context){
		
		MarginPixelValue = dip2px(context, VIEW_MARGIN_CONST);
	}
	public AutoBreakLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public AutoBreakLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public AutoBreakLayout(Context context) {
		super(context);
		init(context);
	}
}
