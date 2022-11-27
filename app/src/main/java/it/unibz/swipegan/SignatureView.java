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

/**
 * Handles the logic related to drawing (and deleting) the signature and collecting the data related to the gesture.
 *
 * Overall structure based on: https://stackoverflow.com/questions/21846584/android-canvas-draw-by-finger
 */
public class SignatureView extends View {
    /**
     * Paint object holding the style and color information about how to draw geometries, text and bitmaps.
     */
    private Paint paint = new Paint();

    /**
     * Object encapsulating compound (multiple contour) geometric paths consisting of straight line segments, quadratic curves, and cubic curves.
     */
    private Path path = new Path();

    /**
     * Indicates whether the clear functionality has been called.
     */
    private boolean clearCalled = false;

    /**
     * Holds the set of X coordinates making up the signature points.
     */
    private ArrayList<Float> xLocations = null;

    /**
     * Holds the set of Y coordinates making up the signature points.
     */
    private ArrayList<Float> yLocations = null;

    /**
     * Holds the set of X velocities between the points making up the signature points.
     */
    private ArrayList<Float> xVelocityTranslation = null;

    /**
     * Holds the set of Y velocities between the points making up the signature points.
     */
    private ArrayList<Float> yVelocityTranslation = null;

    /**
     * Holds the set of individual screen area touch sizes.
     */
    private ArrayList<Float> sizes = null;

    /**
     * Stores the main activity object.
     */
    private MainActivity mainActivity;

    /**
     * Class constructor.
     * Sets up the style and colour information of the Paint object.
     *
     * @param context The context the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view. T
     */
    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    /**
     * Called when the visibility of the view or an ancestor of the view has changed.
     * Initializes the lists holding the signature information when the view is first called.
     *
     * @param changedView The view whose visibility changed. May be this or an ancestor view.
     * @param visibility The new visibility, one of VISIBLE, INVISIBLE or GONE.
     */
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

    /**
     * Implements the logic triggering the drawing process of the specified path using the specified paint (and style) and the canvas re-draw when the signature is cleared.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    @Override protected void onDraw(Canvas canvas) {
        if(this.clearCalled) {
            this.path = new Path();
            canvas.drawPath(path, paint);
            this.clearCalled = false;
        } else {
            canvas.drawPath(path, paint);
        }
    }

    /**
     * Handles touch screen motion events.
     * Updates the path object by initially setting the beginning of the next contour to the point (MotionEvent.ACTION_DOWN) and adding a line from the last point to the current one on each subsequent method call (MotionEvent.ACTION_MOVE).
     * Enables (MotionEvent.ACTION_DOWN) and disables (MotionEvent.ACTION_UP) sensor tracking by calling the relevant method from the main activity object.
     * Updates the signature information (X / Y coordinates, X / Y velocities and touch sizes) on each method call.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
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

    /**
     * Empties the lists containing the signature information (X / Y coordinates, X / Y velocities and touch sizes) and triggers a re-draw of the canvas.
     */
    public void clearPath() {
        this.xLocations.clear();
        this.yLocations.clear();
        this.xVelocityTranslation.clear();
        this.yVelocityTranslation.clear();
        this.sizes.clear();

        this.clearCalled = true;
        invalidate();
    }

    /**
     * Retrieves the set of X coordinate values of the signature gesture.
     *
     * @return The set of X coordinate values.
     */
    public ArrayList<Float> getXLocations() {
        return this.xLocations;
    }

    /**
     * Retrieves the set of Y coordinate values of the signature gesture.
     *
     * @return The set of Y coordinate values.
     */
    public ArrayList<Float> getYLocations() {
        return this.yLocations;
    }

    /**
     * Retrieves the set of velocity values for the X axis.
     *
     * @return The set of X velocity values.
     */
    public ArrayList<Float> getXVelocityTranslations() { return this.xVelocityTranslation; }

    /**
     * Retrieves the set of velocity values for the Y axis.
     *
     * @return The set of Y velocity values.
     */
    public ArrayList<Float> getYVelocityTranslations() { return this.yVelocityTranslation; }

    /**
     * Retrieves the set of individual screen area touch sizes.
     *
     * @return The set of touch sizes.
     */
    public ArrayList<Float> getSizes() { return this.sizes; }

    /**
     * Generates the summary statistics for the X velocity.
     *
     * @return DoubleSummaryStatistics object for the X velocity.
     */
    public DoubleSummaryStatistics getXVelocitySummaryStatistics() {
        return this.xVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
    }

    /**
     * Generates the summary statistics for the Y velocity.
     *
     * @return DoubleSummaryStatistics object for the Y velocity.
     */
    public DoubleSummaryStatistics getYVelocitySummaryStatistics() {
        return this.yVelocityTranslation.stream().mapToDouble(x -> (double) x).summaryStatistics();
    }

    /**
     * Setter for the main activity object.
     *
     * @param mainActivity The instance of the main activity class.
     */
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
