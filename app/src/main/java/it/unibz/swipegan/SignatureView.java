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

// Based on: https://stackoverflow.com/questions/21846584/android-canvas-draw-by-finger
public class SignatureView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean clearCalled = false;

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
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

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set a new starting point
                path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                // Connect the points
                path.lineTo(eventX, eventY);
                break;
            default:
                return false;
        }

        // Makes our view repaint and call onDraw
        invalidate();
        return true;
    }

    public void clearPath() {
        this.clearCalled = true;
        invalidate();
    }
}
