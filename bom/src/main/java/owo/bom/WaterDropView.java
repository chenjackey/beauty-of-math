package owo.bom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WaterDropView extends View {
    private Paint mPaint = new Paint();
    Bitmap mMemBitmap;
    private Canvas mMemCanvas;
    private Path mPath = new Path();
    private Handler mHandler;
    private static final int INTERVAL = 100;

    public WaterDropView(Context context) {
        super(context);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(23.0f);
        mMemBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        mMemCanvas = new Canvas(mMemBitmap);
        mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAngle -= 0.01;
                mrOffset += 1;
                postInvalidate();
                mHandler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }

    private int width = 800;
    private int height = 800;

    private void drawHeart(Canvas canvas) {
        double offset = 100;
        double centerX = width / 2 + offset;
        double centerY = height / 2 + offset;
        double leftX = 0 + offset;
        double leftY = height / 2 + offset;
        double rightX = width + offset;
        double rightY = height / 2 + offset;
        double topX = width / 2 + offset;
        double topY = 0 + offset;
        double bottomX = width / 2 + offset;
        double bottomY = height + offset;
        double angle = Math.PI / 4;

        drawArrowBR(canvas, 50, angle, leftX, leftY, rightX, rightY);
        drawArrowBR(canvas, 50, angle, bottomX, bottomY, topX, topY);
        canvas.drawText("O", (float) centerX, (float) centerY, mPaint);
        canvas.drawText("Y", (float) topX, (float) topY, mPaint);
        canvas.drawText("X", (float) rightX, (float) rightY, mPaint);
    }

    private void drawArrowBR(Canvas canvas, double r, double angle, double xs, double ys, double xe, double ye) {
        double angle2 = Math.atan2(ye - ys, xe - xs);
        angle2 = -angle2;
        double x2 = xe - r * Math.cos(angle + angle2);
        double y2 = ye + r * Math.sin(angle + angle2);

        double x3 = xe - r * Math.cos(angle2 - angle);
        double y3 = ye + r * Math.sin(angle2 - angle);


        drawLine(canvas, xs, ys, xe, ye, mPaint);
        drawLine(canvas, x2, y2, xe, ye, mPaint);
        drawLine(canvas, x3, y3, xe, ye, mPaint);
    }

    private void drawWaterDrop(Canvas canvas, double angle, double offset1) {
        double R = 200;
        double r = 50;
        double xR = 100;
        double yR = 0;
        double xr = 150 + mrOffset;
        double yr = 0;
        double l = R / Math.cos(mAngle);
        double xa = 0;
        double ya = 0;
        double xb = 0;
        double yb = 0;
        double xc = 0;
        double yc = 0;
        double xd = 0;
        double yd = 0;
        double xCross = 0;
        double yCross = 0;

        if (l <= (R + r)) {
            xa = xR + R * Math.cos(mAngle);
            ya = yR + R * Math.sin(mAngle);
            xb = xr + r * Math.cos(mAngle);
            yb = yr + r * Math.sin(mAngle);
            xc = xa;
            yc = -ya;
            xd = xb;
            yd = -yb;
        } else {
            xa = xR + R * Math.cos(mAngle);
            ya = yR + R * Math.sin(mAngle);
            xb = xr - r * Math.cos(mAngle);
            yb = yr + r * Math.sin(mAngle);
            xc = xa;
            yc = -ya;
            xd = xb;
            yd = -yb;
            xCross = xR + R / Math.cos(mAngle);
            yCross = yR;
        }
        xR += offset1;
        yR += offset1;
        xr += offset1;
        yr += offset1;
        double offset = 0;
        xa += offset;
        ya += offset;
        xb += offset;
        yb += offset;
        xc += offset;
        yc += offset;
        xd += offset;
        yd += offset;
        xCross += offset;
        yCross += offset;
        double xS = xR - R + offset;
        double yS = yR + offset;
        double xE = xr + r + offset;
        double yE = yr + offset;
        canvas.drawCircle((float) xR, (float) yR, (float) R, mPaint);
        canvas.drawCircle((float) xr, (float) yr, (float) r, mPaint);
        if (l <= (R + r)) {
            Path path = makeBezier(new Point[]{
                    new Point((int) xS, (int) yS),
                    new Point((int) xa, (int) ya),
                    new Point((int) xb, (int) yb),
                    new Point((int) xE, (int) yE),
                    new Point((int) xd, (int) yd),
                    new Point((int) xc, (int) yc),
                    new Point((int) xS, (int) yS),

            });
            canvas.drawPath(path, mPaint);
        } else {
            Path path = makeBezier(new Point[]{
                    new Point((int) xS, (int) yS),
                    new Point((int) xa, (int) ya),
                    new Point((int) xCross, (int) yCross),
                    new Point((int) xc, (int) yc),
                    new Point((int) xS, (int) yS),
                    new Point((int) xE, (int) yE),
                    new Point((int) xd, (int) yd),
                    new Point((int) xc, (int) yc),
                    new Point((int) xS, (int) yS),

            });
            canvas.drawPath(path, mPaint);
            path = makeBezier(new Point[]{
                    new Point((int) xE, (int) yE),
                    new Point((int) xb, (int) yb),
                    new Point((int) xCross, (int) yCross),
                    new Point((int) xd, (int) yd),

            });
            canvas.drawPath(path, mPaint);
        }


    }

    private Path mTmpPath = new Path();

    private Path makeBezier(Point[] controlPoints) {
        mTmpPath.reset();
        List<Point> points = Bezier.points(controlPoints);
        Point pre = points.get(0);
        mTmpPath.moveTo(pre.x, pre.y);
        for (int i = 1; i < points.size(); ++i) {
            Point cur = points.get(i);
            mTmpPath.quadTo(pre.x, pre.y, cur.x, cur.y);
            pre = cur;
        }
        return mTmpPath;
    }

    private void drawLine(Canvas canvas, double x1, double y1, double x2, double y2, Paint paint) {
        x1 = Math.abs(x1);
        y1 = Math.abs(y1);
        x2 = Math.abs(x2);
        y2 = Math.abs(y2);
        canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, paint);
    }

    private double mAngle = Math.PI / 2;
    private double mrOffset = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMemCanvas.drawColor(Color.BLACK);
        drawHeart(mMemCanvas);
        drawWaterDrop(mMemCanvas, mAngle, 100 + width / 2);
        canvas.drawBitmap(mMemBitmap, 0, 0, null);
    }
}
