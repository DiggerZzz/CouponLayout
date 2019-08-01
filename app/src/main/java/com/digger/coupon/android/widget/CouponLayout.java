package com.digger.coupon.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.digger.coupon.android.R;
import com.digger.coupon.util.ScreenTool;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: DiggerZzz
 * Date: 2019/7/30 17:33
 * Desc:
 */
public class CouponLayout extends FrameLayout {

    //锯齿半径
    private int sawRadius;
    //锯齿间隔
    private int sawGap;
    //
    private Drawable drawable;
    //绘制位置
    private int gravity;
    //水平锯齿数量
    private int hSawCount;
    //绘制水平锯齿后的剩余空间
    private int hRemain;
    //垂直锯齿数量
    private int vSawCount;
    //绘制垂直锯齿后的剩余空间
    private int vRemain;

    private int width, height;

    private RectF srcRecF;
    private Paint paint;
    private PorterDuffXfermode xfermode;
    private List<Path> hSawPathList;
    private List<Path> vSawPathList;

    public CouponLayout(Context context) {
        this(context, null);
    }

    public CouponLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CouponLayout);
        sawRadius = ta.getDimensionPixelSize(R.styleable.CouponLayout_saw_radius, ScreenTool.dip2px(context, 3));
        sawGap = ta.getDimensionPixelSize(R.styleable.CouponLayout_saw_gap, 0);
        gravity = ta.getInt(R.styleable.CouponLayout_draw_gravity, DrawGravity.DRAW_GRAVITY_TOP);
        Drawable background = ta.getDrawable(R.styleable.CouponLayout_coupon_background);
        ta.recycle();

        srcRecF = new RectF();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);

        if(background == null) {
            setBackground(new ColorDrawable(0));
        } else {
            setBackground(background);
        }

        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        if(checkGravity(DrawGravity.DRAW_GRAVITY_TOP) || checkGravity(DrawGravity.DRAW_GRAVITY_BOTTOM)) {
            calcHSawCountAndRemain();
        }
        if(checkGravity(DrawGravity.DRAW_GRAVITY_LEFT) || checkGravity(DrawGravity.DRAW_GRAVITY_RIGHT)) {
            calcVSawCountAndRemain();
        }
        setSrcRect();
        initHSawPath();
        initVSawPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(srcRecF, null, Canvas.ALL_SAVE_FLAG);

        //绘制background drawable
        drawable.setBounds((int) srcRecF.left, (int) srcRecF.top, (int) srcRecF.right, (int) srcRecF.bottom);
        drawable.draw(canvas);

        paint.setXfermode(xfermode);
        //绘制水平方向锯齿
        for(Path path : hSawPathList) {
            canvas.drawPath(path, paint);
        }

        //绘制垂直方向锯齿
        for(Path path : vSawPathList) {
            canvas.drawPath(path, paint);
        }

        canvas.restore();
    }

    private boolean checkGravity(@DrawGravity int g) {
        return (gravity & g) == g;
    }

    /**
     * 计算水平锯齿个数和空余空间宽度
     */
    private void calcHSawCountAndRemain() {
        hSawCount = (int) ((float) (width - sawGap) / (float) (sawRadius * 2 + sawGap));
        hRemain = width - hSawCount * sawRadius * 2 - sawGap * (hSawCount - 1);
    }

    /**
     * 计算水平锯齿个数和空余空间宽度
     */
    private void calcVSawCountAndRemain() {
        vSawCount = (int) ((float) (height - sawGap) / (float) (sawRadius * 2 + sawGap));
        vRemain = height - vSawCount * sawRadius * 2 - sawGap * (vSawCount - 1);
    }

    /**
     * 设置srcRect
     */
    private void setSrcRect() {
        srcRecF.set(0, 0, width, height);
    }

    /**
     * 初始化水平方向锯齿path
     */
    private void initHSawPath() {
        int startX = hRemain / 2;

        if(hSawPathList == null) {
            hSawPathList = new ArrayList<>();
        } else {
            hSawPathList.clear();
        }

        for(int i = 0; i < hSawCount; i++) {
            int x = startX + i * (sawRadius * 2 + sawGap) + sawRadius;

            //顶部
            if(checkGravity(DrawGravity.DRAW_GRAVITY_TOP)) {
                Path path = new Path();
                path.addCircle(x, 0, sawRadius, Path.Direction.CCW);
                hSawPathList.add(path);
            }

            //底部
            if(checkGravity(DrawGravity.DRAW_GRAVITY_BOTTOM)) {
                Path path = new Path();
                path.addCircle(x, height, sawRadius, Path.Direction.CCW);
                hSawPathList.add(path);
            }
        }
    }

    /**
     * 初始化垂直方向锯齿path
     */
    private void initVSawPath() {
        int startY = vRemain / 2;

        if(vSawPathList == null) {
            vSawPathList = new ArrayList<>();
        } else {
            vSawPathList.clear();
        }

        for(int i = 0; i < vSawCount; i++) {
            int y = startY + i * (sawRadius * 2 + sawGap) + sawRadius;

            //左边
            if(checkGravity(DrawGravity.DRAW_GRAVITY_LEFT)) {
                Path path = new Path();
                path.addCircle(0, y, sawRadius, Path.Direction.CCW);
                vSawPathList.add(path);
            }

            //右边
            if(checkGravity(DrawGravity.DRAW_GRAVITY_RIGHT)) {
                Path path = new Path();
                path.addCircle(width, y, sawRadius, Path.Direction.CCW);
                vSawPathList.add(path);
            }
        }
    }

    /**
     * 设置背景
     * @param d
     */
    public void setBackground(Drawable d) {
        if(drawable == d)  {
            return;
        }

        drawable = d;
        invalidate();
    }

    /**
     * 设置背景
     * @param drawableRes
     */
    public void setBackgroundResource(@DrawableRes int drawableRes) {
        if(drawableRes != 0) {
            setBackground(getContext().getResources().getDrawable(drawableRes));
        }
    }

    /**
     * 设置背景
     * @param color
     */
    public void setBackgroundColor(@ColorInt int color) {
        if(drawable instanceof ColorDrawable) {
            ((ColorDrawable) drawable.mutate()).setColor(color);
        } else {
            setBackground(new ColorDrawable(color));
        }
    }

    @IntDef({DrawGravity.DRAW_GRAVITY_TOP, DrawGravity.DRAW_GRAVITY_BOTTOM,
            DrawGravity.DRAW_GRAVITY_LEFT, DrawGravity.DRAW_GRAVITY_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawGravity {
        int DRAW_GRAVITY_TOP = 0x1;
        int DRAW_GRAVITY_BOTTOM = 0x2;
        int DRAW_GRAVITY_LEFT = 0x4;
        int DRAW_GRAVITY_RIGHT = 0x8;
    }
}
