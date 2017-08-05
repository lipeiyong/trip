package net.lvtushiguang.trip.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.widget.TextView;

import net.lvtushiguang.trip.R;
import net.lvtushiguang.trip.util.SizeUtils;

import java.lang.reflect.Method;

/**
 * 图片剧中TextView
 * Created by PhoenixTree on 2017/7/18.
 */

public class CustomTextView extends TextView {

    /**
     * StateListDrawable中Drawable列表
     */
    private Drawable[] drawables;

    public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

    private int mHeight, mWidth;

    private int mLocation;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        mWidth = array.getDimensionPixelOffset(
                R.styleable.CustomTextView_drawable_width, 0);

        mHeight = array.getDimensionPixelOffset(
                R.styleable.CustomTextView_drawable_height, 0);

        mLocation = array.getInt(R.styleable.CustomTextView_drawable_location, LEFT);

        array.recycle();

        Drawable[] dras = getCompoundDrawables();
        if (dras != null) {
            switch (mLocation) {
                case LEFT:
                    if (dras[0] instanceof StateListDrawable) {
                        drawables = getDrawables((StateListDrawable) dras[0]);
                    }
                    break;
                case TOP:
                    if (dras[1] instanceof StateListDrawable) {
                        drawables = getDrawables((StateListDrawable) dras[1]);
                    }
                    break;
                case RIGHT:
                    if (dras[2] instanceof StateListDrawable) {
                        drawables = getDrawables((StateListDrawable) dras[2]);
                    }
                    break;
                case BOTTOM:
                    if (dras[3] instanceof StateListDrawable) {
                        drawables = getDrawables((StateListDrawable) dras[3]);
                    }
                    break;
            }
        }

        //压缩
        if (drawables != null) {
            for (int i = 0; i < drawables.length; i++) {
                if (drawables[i] != null) {
                    Bitmap bitmap = getBitmap(((BitmapDrawable)
                            drawables[i]).getBitmap(), mWidth, mHeight);
                    drawables[i] = new BitmapDrawable(getResources(), bitmap);
                } else {
                    break;
                }
            }
            drawableDrawable(drawables[1]);
        }
    }

    /**
     * 通过反射获取Drawable列表
     *
     * @param sld
     * @return
     */
    private Drawable[] getDrawables(StateListDrawable sld) {
        Drawable.ConstantState cs = sld.getConstantState();
        Drawable[] dras = null;
        try {
            // 通过反射调用getChildren方法获取xml文件中写的drawable数组
            Method method = cs.getClass().getMethod("getChildren", new Class[0]);
            method.setAccessible(true);
            Object obj = method.invoke(cs, new Object[]{});
            dras = (Drawable[]) obj;
        } catch (Exception e) {
            Log.e("TAG", "error:" + e.toString());
        }
        return dras;
    }

    /**
     * 绘制Drawable
     */
    private void drawableDrawable(Drawable drawable) {
        if (drawable != null) {
            switch (mLocation) {
                case LEFT:
                    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null);
                    break;
                case TOP:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
                            null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            drawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            null, drawable);
                    break;
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (drawables != null) {
            int[] states = getDrawableState();
            if (states.length == 4) {
                //默认状态
                drawableDrawable(drawables[1]);
            } else if (states.length == 5) {
                //pressed
                drawableDrawable(drawables[0]);
            }
        }
    }

    /**
     * 缩放图片
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     */
    private Bitmap getBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 获取TextView的Drawable对象，返回的数组长度应该是4，对应左上右下
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawable = drawables[0];
            if (drawable != null) {
                // 当左边Drawable的不为空时，测量要绘制文本的宽度
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawable.getIntrinsicWidth();
                // 计算总宽度（文本宽度 + drawablePadding + drawableWidth）
                float bodyWidth = textWidth + drawablePadding + drawableWidth;
                // 移动画布开始绘制的X轴
                canvas.translate((getWidth() - bodyWidth) / 2, 0);
            } else if ((drawable = drawables[1]) != null) {
                // 否则如果上边的Drawable不为空时，获取文本的高度
                Rect rect = new Rect();
                getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(), rect);
                float textHeight = rect.height();
                int drawablePadding = getCompoundDrawablePadding();
                int drawableHeight = drawable.getIntrinsicHeight();
                // 计算总高度（文本高度 + drawablePadding + drawableHeight）
                float bodyHeight = textHeight + drawablePadding + drawableHeight;
                // 移动画布开始绘制的Y轴
                canvas.translate(0, (getHeight() - bodyHeight) / 2);
            }
        }
        super.onDraw(canvas);
    }
}
