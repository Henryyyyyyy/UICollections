package me.henry.uicollections.circlemenu;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.henry.uicollections.R;


public class MyCircleMenuLayout extends ViewGroup {

	/**
	 * mRadius是我们整个View的宽度
	 */
	private int mRadius;
	/**
	 * 该容器内child item的默认尺寸
	 */
	private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
	/**
	 * 当每秒移动角度达到该值时，认为是快速移动
	 */
	private static final int FLINGABLE_VALUE = 300;

	/**
	 * 如果移动角度达到该值，则屏蔽点击
	 */
	private static final int NOCLICK_VALUE = 5;

	/**
	 * 当每秒移动角度达到该值时，认为是快速移动
	 */
	private int mFlingableValue = FLINGABLE_VALUE;

	/**
	 * 布局时的开始角度
	 */
	private double mStartAngle = 0;

	/**
	 * 菜单的个数
	 */
	private int mMenuItemCount;

	/**
	 * 检测按下到抬起时旋转的角度
	 */
	private float mTmpAngle;
	/**
	 * 检测按下到抬起时使用的时间
	 */
	private long mDownTime;

	/**
	 * 判断是否正在自动滚动
	 */
	private boolean isFling;

	private int mMenuItemLayoutId = R.layout.circle_menu_item;

	/**
	 * 每个item所在的角度
	 */
	private int[] childAngles;
	private ArrayList<View> itemViews = new ArrayList<View>();

	private ArrayList<ItemBean> itemBeans = new ArrayList<ItemBean>();
	/**
	 * 菜单总个数
	 */
	private int mMenuAllItemCount;
	/**
	 * item相差的度数
	 */
	private static final int Item_Delta_Degree = 30;
	private double exactStartAngle = 0;

	private static final int Default_Size = 8;
	/**
	 * 新加进来的item（排在最后）;
	 */
	private int afterPosition = 8;
	private int previousPosition = -1;
	private boolean isMove = false;
	private Handler handler = new Handler();

	public MyCircleMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 无视padding
		setPadding(0, 0, 0, 0);
	}

	/**
	 * 设置布局的宽高，并策略menu item宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Log.e("123", "onmeasure");
		int resWidth = 0;
		int resHeight = 0;

		/**
		 * 根据传入的参数，分别获取测量模式和测量值
		 */
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		/**
		 * 如果宽或者高的测量模式非精确值
		 */
		if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
			// 主要设置为背景图的高度
			resWidth = getSuggestedMinimumWidth();
			// 如果未设置背景图片，则设置为屏幕宽高的默认值
			// getDefaultWidth(),屏幕宽高的默认值
			resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

			resHeight = getSuggestedMinimumHeight();// 获取view的height或者图片height的最大值
			// 如果未设置背景图片，则设置为屏幕宽高的默认值
			resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
		} else {
			// 如果都设置为精确值，则直接取小值；
			resWidth = resHeight = Math.min(width, height);
		}

		setMeasuredDimension(resWidth, resHeight);

		// 获得半径
		mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

		// menu item数量
		final int count = getChildCount();
		// Log.e("123", "count=" + count);
		childAngles = new int[count];
		// menu item尺寸
		int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
		// menu item测量模式
		int childMode = MeasureSpec.EXACTLY;

		// 迭代测量
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child.getVisibility() == GONE) {
				continue;
			}

			// 计算menu item的尺寸；以及和设置好的模式，去对item进行测量
			int makeMeasureSpec = -1;

			makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);

			child.measure(makeMeasureSpec, makeMeasureSpec);
		}

	}

	/**
	 * MenuItem的点击事件接口
	 *
	 * @author zhy
	 *
	 */
	public interface OnMenuItemClickListener {
		void itemClick(View view, int pos);

		void itemLongClick(View view, int pos);

		void itemSelected(ItemBean itemBean, boolean isZero);

	}

	/**
	 * MenuItem的点击事件接口
	 */
	private OnMenuItemClickListener mOnMenuItemClickListener;

	/**
	 * 设置MenuItem的点击事件接口
	 *
	 * @param mOnMenuItemClickListener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
		this.mOnMenuItemClickListener = mOnMenuItemClickListener;
	}

	/**
	 * 设置menu item的位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int layoutRadius = mRadius;
		// Laying out the child views
		final int childCount = getChildCount();
		// Log.e("123", "childCount=" + childCount);

		int left, top;
		// menu item 的尺寸
		int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

		// 根据menu item的个数，计算角度

		float angleDelay = Item_Delta_Degree;
		mStartAngle = exactStartAngle;
		// 遍历去设置menuitem的位置
		for (int i = 0; i < childCount; i++) {

			final View child = getChildAt(i);

			if (child.getVisibility() == GONE) {
				continue;
			}

			mStartAngle %= 360;

			// 计算，中心点到menu item中心的距离
			float tmp = layoutRadius / 2f - cWidth / 2;

			// tmp cosa 即menu item中心点的横坐标
			left = layoutRadius / 2 + (int) Math.round(tmp * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);
			// tmp sina 即menu item的纵坐标
			top = layoutRadius / 2 + (int) Math.round(tmp * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * cWidth);

			child.layout(left, top, left + cWidth, top + cWidth);

			// 如果是0度(右边为0度)
			TextView tv = (TextView) getChildAt(i).findViewById(R.id.id_circle_menu_item_text);
			ImageView iv = (ImageView) getChildAt(i).findViewById(R.id.id_circle_menu_item_image);
			LayoutParams params = iv.getLayoutParams();
			Log.e("123", "i=" + i);
			Log.e("123", "mStartAngle=" + mStartAngle);
			if (mStartAngle == 0) {
				tv.setTextColor(0xffffffff);
				params.width = dp2px(getContext(), 50);
				params.height = dp2px(getContext(), 50);
				iv.setLayoutParams(params);
				iv.setImageResource(itemBeans.get(previousPosition + i + 1).getResImgOn());

				mOnMenuItemClickListener.itemSelected(itemBeans.get(previousPosition + i + 1), true);
			} else {
				params.width = dp2px(getContext(), 40);
				params.height = dp2px(getContext(), 40);
				iv.setLayoutParams(params);
				tv.setTextColor(0xffA07548);
				iv.setImageResource(itemBeans.get(previousPosition + i + 1).getResImgOff());
				mOnMenuItemClickListener.itemSelected(itemBeans.get(previousPosition + i + 1), false);
			}

			// 每次滑动都设置每个item所在的角度

			childAngles[i] = (int) mStartAngle;
			// 叠加尺寸
			mStartAngle += angleDelay;

		}

	}

	/**
	 * 记录上一次的x，y坐标
	 */
	private float mLastX;
	private float mLastY;
	/**
	 * 记录上一次的x，y坐标,用于记录幅度的
	 */
	private float testLastX;
	private float testLastY;
	/**
	 * 自动滚动的Runnable
	 */
	private AutoFlingRunnable mFlingRunnable;

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		// 用于控制拉动幅度的xy;
		float testX = event.getX();
		float testY = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isMove = false;
				Log.e("123", "ACTION_DOWN");
				testLastX = testX;
				testLastY = testY;
				mLastX = x;
				mLastY = y;
				mDownTime = System.currentTimeMillis();
				mTmpAngle = 0;

				// 如果当前已经在快速滚动
				if (isFling) {
					// 移除快速滚动的回调
					removeCallbacks(mFlingRunnable);
					isFling = false;
					return true;
				}

				break;
			case MotionEvent.ACTION_MOVE:

				if (itemBeans.size() == 0) {
					break;
				}
				/**
				 * 获得开始的角度
				 */
				float start = getAngle(mLastX, mLastY);
				/**
				 * 获得当前的角度
				 */
				float end = getAngle(x, y);

				float testStart = getAngle(testLastX, testLastY);
				float testEnd = getAngle(testX, testY);

				if (Math.abs(testStart - testEnd) > 1) {

					isMove = true;
				} else {

					isMove = false;
				}

				// 如果是一、四象限，则直接end-start，角度值都是正值
				// 计算转过的角度
				if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
					exactStartAngle += end - start;
					mTmpAngle += end - start;
				} else
				// 二、三象限，色角度值是付值
				{
					exactStartAngle += start - end;
					mTmpAngle += start - end;
				}
				/**
				 * 如果 0<item数量<4,逆时针旋转，当最后一个item到0度的时候，就不给它旋转
				 *
				 */
				int value = -30 * (itemBeans.size() - 1);
				if (getChildCount() > 0 && getChildCount() < 4 && ((start - end) > 0) && (exactStartAngle < value)) {
					exactStartAngle = value;
				}
				/**
				 * 当最后一个item到0度时，不能再逆时针拉动 因为遮住一半，12象限，所以只需要考虑start-end的情况
				 * 条件一：getChildCount()<=4 条件二：逆时针转动 条件三：第一个item到-90度，不可以再小
				 */
				if ((getChildCount() <= 4) && ((start - end) > 0) && (exactStartAngle < -90)) {
					exactStartAngle = -90;
				}
				/**
				 * 当第一个item到0度时，不能再顺时针拉动 因为遮住一半，12象限，所以只需要考虑start-end的情况 条件二：顺时针转动
				 * 条件三：第一个item到0度
				 */

				if (((start - end) < 0) && (exactStartAngle > 0)) {
					exactStartAngle = 0;
				}

				// 销毁view与addView
				Log.e("123", "exactStartAngle=" + exactStartAngle);
				if (exactStartAngle < -120) {
					anticlockwiseCorrectPosition();
				}
				if ((exactStartAngle > -90) && ((start - end) < 0) && (exactStartAngle < 0)) {
					clockwiseCorrectPosition();
				}

				requestLayout();

				mLastX = x;
				mLastY = y;

				break;
			case MotionEvent.ACTION_UP:
				isMove = false;
				handler.removeCallbacks(r);
				if (itemBeans.size() == 0) {
					break;
				}
				Log.e("123", "ACTION_UP");

				for (int i = 0; i < childAngles.length; i++) {
					Log.e("123", "childAngles*****" + i + "*****" + childAngles[i]);
				}
				float mod = Math.abs(childAngles[0] % 30);
				Log.e("123", "mod=" + mod);
				if (childAngles[0] < 0) {
					if (mod < 15) {
						exactStartAngle = childAngles[0] + mod;
					} else {
						exactStartAngle = childAngles[0] - (30 - mod);
					}
				} else {
					if (mod < 15) {
						exactStartAngle = childAngles[0] - mod;
					} else {
						exactStartAngle = childAngles[0] + (30 - mod);
					}
				}

				Log.e("123", "exactStartAngle=" + exactStartAngle);
				ImageView iv;

				requestLayout();

				// 如果当前旋转角度超过NOCLICK_VALUE屏蔽点击
				if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
					return true;
				}

				break;
		}
		return super.dispatchTouchEvent(event);
	}

	private void clockwiseCorrectPosition() {
		Log.e("zengjin", "clockwise'previousPosition=" + previousPosition);

		if (previousPosition >= 0) {
			for (int i = 0; i < getChildCount(); i++) {
				if (getChildAt(i).equals(itemViews.get(previousPosition))) {

					return;
				}
			}
			addView(itemViews.get(previousPosition), 0);
			exactStartAngle = exactStartAngle - 30;
			if (previousPosition > -1) {
				previousPosition--;
			}
		}
		if (getChildCount() > Default_Size) {
			removeViewAt(getChildCount() - 1);
			afterPosition--;
		}
	}
	public void anticlockwiseCorrectPosition() {

		if (afterPosition < itemViews.size()) {

			for (int i = 0; i < getChildCount(); i++) {
				if (getChildAt(i).equals(itemViews.get(afterPosition))) {
					return;
				}
			}
			addView(itemViews.get(afterPosition));
			afterPosition++;
		}
		exactStartAngle = exactStartAngle + 30;
		removeViewAt(0);
		previousPosition++;
	}

	/**
	 * 主要为了action_down时，返回true
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	/**
	 * 根据触摸的位置，计算角度
	 *
	 * @param xTouch
	 * @param yTouch
	 * @return
	 */
	private float getAngle(float xTouch, float yTouch) {
		double x = xTouch - (mRadius / 2d);
		double y = yTouch - (mRadius / 2d);
		return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
	}

	/**
	 * 根据当前位置计算象限
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	private int getQuadrant(float x, float y) {
		int tmpX = (int) (x - mRadius / 2);
		int tmpY = (int) (y - mRadius / 2);
		if (tmpX >= 0) {
			return tmpY >= 0 ? 4 : 1;
		} else {
			return tmpY >= 0 ? 3 : 2;
		}

	}

	/**
	 * 设置菜单条目的图标和文本 beans必须大于等于8
	 *
	 * @param
	 */
	public void setMenuItemIconsAndTexts(ArrayList<ItemBean> AllBeans) {
		itemBeans = AllBeans;

		// 初始化mMenuCount
		mMenuItemCount = Default_Size;

		addMenuItems();

	}

	/**
	 * 设置MenuItem的布局文件，必须在setMenuItemIconsAndTexts之前调用
	 *
	 * @param mMenuItemLayoutId
	 */
	public void setMenuItemLayoutId(int mMenuItemLayoutId) {
		this.mMenuItemLayoutId = mMenuItemLayoutId;
	}
	Runnable r = new Runnable() {
		@Override
		public void run() {
			if (!isMove) {
				mOnMenuItemClickListener.itemLongClick(null, 1);
			}

		}
	};
	/**
	 * 添加菜单项
	 */
	private void addMenuItems() {
		LayoutInflater mInflater = LayoutInflater.from(getContext());

		/**
		 * 根据用户设置的参数，初始化view
		 */
		for (int i = 0; i < itemBeans.size(); i++) {
			final int j = i;
			View view = mInflater.inflate(mMenuItemLayoutId, this, false);
			ImageView iv = (ImageView) view.findViewById(R.id.id_circle_menu_item_image);
			TextView tv = (TextView) view.findViewById(R.id.id_circle_menu_item_text);

			if (iv != null) {
				iv.setVisibility(View.VISIBLE);
				iv.setImageResource(itemBeans.get(i).getResImgOff());
				iv.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(final View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							handler.postDelayed(r, 1500);
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							handler.removeCallbacks(r);
						}
						return true;
					}
				});

				iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (mOnMenuItemClickListener != null) {
							mOnMenuItemClickListener.itemClick(v, j);
						}
					}
				});
			}
			if (tv != null) {
				tv.setVisibility(View.VISIBLE);
				tv.setText(itemBeans.get(i).getTxt());
			}

			// 添加view到容器中
			if (i < Default_Size) {

				addView(view);
			}
			itemViews.add(view);
		}

	}

	/**
	 * 如果每秒旋转角度到达该值，则认为是自动滚动
	 *
	 * @param mFlingableValue
	 */
	public void setFlingableValue(int mFlingableValue) {
		this.mFlingableValue = mFlingableValue;
	}

	/**
	 * 获得默认该layout的尺寸
	 *
	 * @return
	 */
	private int getDefaultWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
	}

	/**
	 * 自动滚动的任务
	 *
	 * @author zhy
	 *
	 */
	private class AutoFlingRunnable implements Runnable {

		private float angelPerSecond;

		public AutoFlingRunnable(float velocity) {
			this.angelPerSecond = velocity;
		}

		public void run() {
			// 如果小于20,则停止
			if ((int) Math.abs(angelPerSecond) < 20) {
				isFling = false;
				return;
			}
			isFling = true;
			// 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
			mStartAngle += (angelPerSecond / 30);
			// 逐渐减小这个值
			angelPerSecond /= 1.0666F;
			postDelayed(this, 30);
			// 重新布局
			requestLayout();
		}
	}

	public int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
				.getDisplayMetrics());
	}

}
