package net.lvtushiguang.trip.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import net.lvtushiguang.trip.R;

/**
 * 图片剧中TextView --mei
 * Created by PhoenixTree on 2017/7/18.
 */

public class DeprecatedCustomTextView extends TextView {

    public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

    private int mDrawableHeight, mDrawableWidth;

    private Drawable mDrawable;
    private Drawable mDrawableRight;
    private Drawable[] drs;

    private int mLocation;

    public DeprecatedCustomTextView(Context context) {
        this(context, null);
    }

    public DeprecatedCustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeprecatedCustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        mDrawableWidth = array.getDimensionPixelOffset(
                R.styleable.CustomTextView_drawable_width, 0);

        mDrawableHeight = array.getDimensionPixelOffset(
                R.styleable.CustomTextView_drawable_height, 0);

        mLocation = array.getInt(R.styleable.CustomTextView_drawable_location, LEFT);

        Drawable[] drawables = getCompoundDrawables();
        mDrawable = drawables[0];
        mDrawableRight = drawables[2];

        if (mDrawable instanceof StateListDrawable) {
            StateListDrawable stlDrawable = (StateListDrawable) mDrawable;
//            int[] states = stlDrawable.getStateCount();
            DrawableContainer.DrawableContainerState mDrawableContainerState =
                    (DrawableContainer.DrawableContainerState) stlDrawable.getConstantState();
            drs = mDrawableContainerState.getChildren();
            Log.e("TAG", "");
        }


//        mDrawable = array.getDrawable(R.styleable.CustomTextView_drawable);


        array.recycle();


        drawableDrawable();
    }

    /**
     * 绘制Drawable
     */
    private void drawableDrawable() {
        if (mDrawable != null) {
            Bitmap bitmap = null;

            if (mDrawable instanceof StateListDrawable) {
                StateListDrawable dr = (StateListDrawable) mDrawable;

                bitmap = ((BitmapDrawable) dr.getCurrent()).getBitmap();
            } else {
                bitmap = ((BitmapDrawable) mDrawable).getBitmap();
            }

            Drawable drawable = null;
            if (mDrawableWidth != 0 && mDrawableHeight != 0) {
                drawable = new BitmapDrawable(getResources(),
                        getBitmap(bitmap, mDrawableWidth, mDrawableHeight));

            } else {
                drawable = new BitmapDrawable(getResources(),
                        Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
                                bitmap.getHeight(), true));
            }

            switch (mLocation) {
                case LEFT:
                    if (drs != null) {
                        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                                drawable, null);
                    } else {
                        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                                drawable, null);
                    }
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
