package me.henry.uicollections.seekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import me.henry.uicollections.R;

/**
 * Created by Henry on 2016/10/11.
 */
public class HenryCustomSeekBar extends View {
    //一定要matchparent
    private  final String TAG="LEDCustomSeekBar";
    private int viewHeight,viewWidth;
    private  float thumbRadius;//thumb的半径
    private float thumbX,thumbY;
    private  float dotRadius;//dot的半径
    private ArrayList<Float> dotXs=new ArrayList<Float>();
    private  int adjestmentFactor;//预留空间
    private  int paddingTop;//预留空间
    private Paint dotPaint;
    private Paint thumbPaint;
    private Paint linePaint;
    private Paint textPaint;
    private String[] text={"-5","-4","-3","-2","-1","0","+1","+2","+3","+4","+5"
    };
    private int textSize;
    private float dotnum=8;//刻度数量
    private float dotDistance=1.0f/(dotnum-1.0f)*1.0f;//两点之间的距离百分比
    private Rect textRect;
    private float changeX=70000;//原始值
    private OnProgressSelect onProgressSelect;
    private Bitmap rabbit,tortoise;
    public boolean isGetProgressFromOutside=true;
    public HenryCustomSeekBar(Context context) {
        this(context,null);
    }

    public HenryCustomSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HenryCustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textRect=new Rect();
        thumbRadius=  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        dotRadius= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4f, getResources().getDisplayMetrics());
        adjestmentFactor= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        paddingTop= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3, getResources().getDisplayMetrics());
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        thumbPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        thumbPaint.setColor(0xffffffff);//白色thumb
        linePaint.setColor(0xffffffff);//白色线
        textPaint.setColor(0xffffffff);//灰色字体
        dotPaint.setColor(0xffffffff);//白色点

        linePaint.setStrokeWidth(2.0f);

        textPaint.setTextSize(textSize);
        textPaint.setStrokeWidth(1);
        textPaint.getTextBounds("+0",0,"+0".length(),textRect);
        rabbit=((BitmapDrawable)getResources().getDrawable(R.drawable.icon_rabbit)).getBitmap();
        tortoise=((BitmapDrawable)getResources().getDrawable(R.drawable.icon_tortoise)).getBitmap();

    }
private Matrix matrix;
    float scaleWidth;
    float scaleHeight;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG,"onMeasure");
        //   Log.e("haha","Measuredwidth="+getMeasuredWidth()+".....Measuredheight="+getMeasuredHeight());


        viewHeight=getMeasuredHeight();
        viewWidth=getMeasuredWidth();

        setMeasuredDimension(getMeasuredWidth(),(int)(thumbRadius+adjestmentFactor+textRect.height()+paddingTop)*2);
          Log.e("haha","viewWidth="+viewWidth+".....viewHeight="+viewHeight);
        //缩放乌龟---------------
         matrix=new Matrix();
         scaleWidth =  thumbRadius*2 / rabbit.getWidth();
         scaleHeight = thumbRadius*2/ rabbit.getHeight();
        matrix.postScale(scaleWidth,scaleHeight);
        rabbit= Bitmap.createBitmap(rabbit,0,0,rabbit.getWidth(),rabbit.getHeight(),matrix,true);
        //缩放兔子------------
        matrix=new Matrix();
        scaleWidth =  thumbRadius*2 / tortoise.getWidth();
        scaleHeight = thumbRadius*2/ tortoise.getHeight();
        matrix.postScale(scaleWidth,scaleHeight);
        tortoise= Bitmap.createBitmap(tortoise,0,0,tortoise.getWidth(),tortoise.getHeight(),matrix,true);



        distance = dotDistance*(viewWidth-2*thumbRadius);
    }


    Rect rect;
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        float yLine=(float)( thumbRadius+adjestmentFactor+textRect.height()+paddingTop);
        dotXs.clear();
        //canvas.drawColor(0xffe06666);
        Log.e(TAG,"onDraw");
         Log.e("haha","width="+viewWidth+".....height="+viewHeight);

        canvas.drawLine(thumbRadius,yLine,viewWidth-thumbRadius,yLine,linePaint);
        //画点点

            for (int i = 0; i <dotnum ; i++) {
                if (i==0) {
                    canvas.drawBitmap(tortoise, thumbRadius+i*dotDistance*(viewWidth-2*thumbRadius)-tortoise.getWidth()/2,yLine-tortoise.getHeight()/2, new Paint());
                }else if (i==dotnum-1) {
                    canvas.drawBitmap(rabbit, thumbRadius+i*dotDistance*(viewWidth-2*thumbRadius)-rabbit.getWidth()/2,yLine-rabbit.getHeight()/2, new Paint());

                }else {
                    canvas.drawCircle(thumbRadius+i*dotDistance*(viewWidth-2*thumbRadius),yLine,dotRadius,dotPaint);

                }
                dotXs.add(thumbRadius+i*dotDistance*(viewWidth-2*thumbRadius));
            }


        //画text--预留变量
        rect=new Rect();
        textPaint.getTextBounds(text[5],0,text[5].length(),rect);
        //画thumb--预留变量
        if (changeX==70000) {
            //初始化
            Log.e("eco","111");
            canvas.drawCircle(dotXs.get(5),yLine,thumbRadius,thumbPaint);
            thumbX=dotXs.get(5);
            thumbY=yLine;
            canvas.drawText("03",dotXs.get(5)-rect.width()/2.0f,yLine-thumbRadius-paddingTop,textPaint);
        }else {
            Log.e("eco","2222");
            canvas.drawCircle(changeX,yLine,thumbRadius,thumbPaint);
            thumbX=changeX;
            thumbY=yLine;
            canvas.drawText(switchText(thumbX),changeX-rect.width()/2.0f,yLine-thumbRadius-paddingTop,textPaint);

        }
       // if (isGetProgressFromOutside==true){
          //  onProgressSelect.onReceiveData();//显示获取数据之后的进度
     //   }
    }
    boolean isTouchThumb=false;
    private float distance;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((x>=thumbRadius)&&(x<(viewWidth-thumbRadius))) {

                    if (isInThumb(x, y)) {
                        isTouchThumb=true;
                    }else {
                        isTouchThumb=false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float outputvalue=0;
                if ((x>=thumbRadius)&&(x<(viewWidth-thumbRadius))) {
                    if (isTouchThumb) {

                        outputvalue=changeX;
                        changeX=judgePointX(x);

                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isTouchThumb) {

                    if ((onProgressSelect!=null)) {
                        onProgressSelect.onSelect(switchText(changeX));
                    }
                }
                break;
            default:
                break;
        }

        return true;
    }

    public float judgePointX(float mx){
        float previous=mx;
        float after=mx;
        for (Float x : dotXs) {
            if (mx>x) {
                previous=x;
            }else if(mx<x){
                after=x;
                break;
            }else {
                return mx;
            }
        }
        if (Math.abs(thumbX-mx)>=(distance/2.0)) {
            if (Math.abs(previous-mx)< Math.abs(after-mx)) {
                return previous;
            }else {
                return after;
            }
        }else {
            return thumbX;
        }


    }

    public String switchText(float x){
        if (x==dotXs.get(0)) {
            return "08";
        }
        if (x==dotXs.get(1)) {
            return "07";
        }
        if (x==dotXs.get(2)) {
            return "06";
        }
        if (x==dotXs.get(3)) {
            return "05";
        }
        if (x==dotXs.get(4)) {
            return "04";
        }
        if (x==dotXs.get(5)) {
            return "03";
        }
        if (x==dotXs.get(6)) {
            return "02";
        }
        if (x==dotXs.get(7)) {
            return "01";
        }
       else {
            return "05";
        }


    }
    public float switchThumbX(String value){
        if (value.equals("08")) {
            return dotXs.get(0);
        }
        if (value.equals("07")) {
            return dotXs.get(1);
        }
        if (value.equals("06")) {
            return dotXs.get(2);
        }
        if (value.equals("05")) {
            return dotXs.get(3);
        }
        if (value.equals("04")) {
            return dotXs.get(4);
        }
        if (value.equals("03")) {
            return dotXs.get(5);
        }
        if (value.equals("02")) {
            return dotXs.get(6);
        }
        if (value.equals("01")) {
            return dotXs.get(7);
        }

       else {
            return dotXs.get(3);
        }



    }
    public boolean isInThumb(float x,float y){
        if (Math.sqrt(Math.pow(Math.abs(thumbX-x),2)+ Math.pow(Math.abs(thumbY-y),2))<=thumbRadius) {
            return true;
        }else {
            return false;
        }

    }
    public void refreshState(String value){

        thumbX=switchThumbX(value);
        changeX=switchThumbX(value);
        invalidate();
        isGetProgressFromOutside=false;

    }
    public void setOnProgressSelectListener(OnProgressSelect l) {
        onProgressSelect = l;
    }

    public interface OnProgressSelect {
        //public void onReceiveData();
        public void onSelect(String value);
    }

}
