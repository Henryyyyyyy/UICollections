package me.henry.uicollections.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {
	private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private Paint paint;
	private int cellWidth;// 格子的宽
	private int cellHeight;// 格子的高
	private int touchindex = -1;
	private onTouchIndexListener ontouchIndexListener;

	/**
	 * 绘制原点是格子的左下角
	 * 
	 * @param context
	 * @param attrs
	 */
	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	private void init() {
		paint = new Paint();
		paint.setColor(Color.parseColor("#ffffff"));
		// typeface.bold不能用.
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(18);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// getmeasure。。的方法就是获得你画的view的长宽，预判，一定要在ondraw方法调用
		super.onDraw(canvas);
		if (cellWidth == 0) {
			cellWidth = getMeasuredWidth();
		}
		if (cellHeight == 0) {
			cellHeight = getMeasuredHeight() / indexArr.length;
		}
		for (int i = 0; i < indexArr.length; i++) {
			float x = cellWidth / 2 - paint.measureText(indexArr[i]) / 2;
			// 高不能用measuretext
			Rect bounds = new Rect();
			// 只要执行完，bounds就有数据了,这个方法就是要获得一串text的边界值，例如love...的边界,(从第几个位置开始算a)等等
			paint.getTextBounds(indexArr[i], 0, indexArr[i].length(), bounds);

			float y = cellHeight / 2 + bounds.height() + i * cellHeight;

			paint.setColor(i == touchindex ? Color.parseColor("#666666")
					: Color.parseColor("#ffffff"));

			canvas.drawText(indexArr[i], x, y, paint);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 如果把break去掉，那就是说，down和move都走同一段逻辑
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if (isSameIndex(y / cellHeight)) {
				break;
			}
			touchindex = y / cellHeight;
			// 安全性检查
			if (touchindex >= 0 && touchindex < indexArr.length) {

				String word = indexArr[touchindex];
				if (ontouchIndexListener != null) {
					ontouchIndexListener.onTouchIndex(word);
				}
			}

			break;
		case MotionEvent.ACTION_UP:
	

			// 用于改变颜色时重置
			touchindex = -1;
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}

	private boolean isSameIndex(int currentIndex) {
		return touchindex == currentIndex;
	}

	// getset方法是在主activity中调用的，quickindexbar.seton...listerner
	public onTouchIndexListener getOntouchIndexListener() {
		return ontouchIndexListener;
	}

	public void setOntouchIndexListener(
			onTouchIndexListener ontouchIndexListener) {
		this.ontouchIndexListener = ontouchIndexListener;
	}

	public interface onTouchIndexListener {

		void onTouchIndex(String index);

	
	}

}
