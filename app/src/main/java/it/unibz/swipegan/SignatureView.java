package it.unibz.swipegan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

// Based on: https://stackoverflow.com/questions/21846584/android-canvas-draw-by-finger
public class SignatureView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean clearCalled = false;

    private ArrayList<Float> xLocations = null;
    private ArrayList<Float> yLocations = null;
    private ArrayList<Float> xVelocityTranslation = null;
    private ArrayList<Float> yVelocityTranslation = null;

    private ArrayList<Float> sizes = null;

    private MainActivity mainActivity;

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == View.VISIBLE) {
            this.xLocations = new ArrayList<>();
            this.yLocations = new ArrayList<>();
            this.xVelocityTranslation = new ArrayList<>();
            this.yVelocityTranslation = new ArrayList<>();
            this.sizes = new ArrayList<>();
        }
    }

    @Override protected void onDraw(Canvas canvas) {
        if(this.clearCalled) {
            this.path = new Path();
            canvas.drawPath(path, paint);
            this.clearCalled = false;
        } else {
            canvas.drawPath(path, paint);
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        // Get the coordinates of the touch event.
        float eventX = event.getX();
        float eventY = event.getY();

        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mainActivity.setSensorsTracking(true);

                // Set a new starting point
                path.moveTo(eventX, eventY);

                this.xLocations.add(event.getX(pointerId));
                this.yLocations.add(event.getY(pointerId));
                this.sizes.add(event.getSize(pointerId));

                return true;
            case MotionEvent.ACTION_MOVE:
                // Connect the points
                path.lineTo(eventX, eventY);

                final int historySize = event.getHistorySize();
                final int pointerCount = event.getPointerCount();
                for (int h = 0; h < historySize; h++) {
                    for (int p = 0; p < pointerCount; p++) {
                        float newX = event.getHistoricalX(p, h);
                        float newY = event.getHistoricalY(p, h);
                        this.xVelocityTranslation.add(Math.abs(newX - this.xLocations.get(this.xLocations.size() - 1)));
                        this.yVelocityTranslation.add(Math.abs(newY - this.yLocations.get(this.yLocations.size() - 1)));
                        this.xLocations.add(newX);
                        this.yLocations.add(newY);
                    }
                }

                for (int p = 0; p < pointerCount; p++) {
                    float newX = event.getX(p);
                    float newY = event.getY(p);
                    this.xVelocityTranslation.add(Math.abs(newX - this.xLocations.get(this.xLocations.size() - 1)));
                    this.yVelocityTranslation.add(Math.abs(newY - this.yLocations.get(this.yLocations.size() - 1)));
                    this.xLocations.add(newX);
                    this.yLocations.add(newY);
                }

                this.sizes.add(event.getSize(pointerId));

                break;
            case MotionEvent.ACTION_UP:
                this.mainActivity.setSensorsTracking(false);

                float newX = event.getX(pointerId);
                float newY = event.getY(pointerId);
                this.xVelocityTranslation.add(Math.abs(newX - this.xLocations.get(this.xLocations.size() - 1)));
                this.yVelocityTranslation.add(Math.abs(newY - this.yLocations.get(this.yLocations.size() - 1)));
                this.xLocations.add(newX);
                this.yLocations.add(newY);

                break;
            default:
                return false;
        }

        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }

    public void clearPath() {
        this.xLocations.clear();
        this.yLocations.clear();
        this.xVelocityTranslation.clear();
        this.yVelocityTranslation.clear();
        this.sizes.clear();

        this.clearCalled = true;
        invalidate();
    }

    public ArrayList<Float> getXLocations() {
        return this.xLocations;
    }

    public ArrayList<Float> getYLocations() {
        return this.yLocations;
    }

    public ArrayList<Float> getXVelocityTranslations() { return this.xVelocityTranslation; }

    public ArrayList<Float> getYVelocityTranslations() { return this.yVelocityTranslation; }

    public ArrayList<Float> getSizes() { return this.sizes; }

    public DoubleSummaryStatistics getXVelocitySummaryStatistics() {
        return this.xVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
    }

    public DoubleSummaryStatistics getYVelocitySummaryStatistics() {
        return this.yVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
