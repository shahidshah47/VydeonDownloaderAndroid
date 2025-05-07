package com.appdev360.jobsitesentry.util;

/**
 * Created by Abubaker on 01,May,2018
 */
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;

public class PanAndZoomListener implements OnTouchListener {

    private enum Anchor {
        NONE, LEFT, TOP, RIGHT, BOTTOM, LEFT_TOP, TOP_RIGHT, RIGHT_BOTTOM, LEFT_BOTTOM
    }

    private static final float SPACE_ZOOM = 10f;
    private static final float SPACE_DRAG = 5f;
    private static final float ZOOM_MAX = 3f;
    private static final String TAG = "PanAndZoomListener";
    // We can be in one of these 3 states
    static final int CLICK = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int NONE = 3;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    MarginLayoutParams oldParams;

   // LiveStreamingActivity liveStreamingActivity;
    View parentView;
    View child;

    public PanAndZoomListener(FrameLayout containter, View surfaceView) {

        this.parentView = containter;
        this.child = surfaceView;
    }

    public boolean onTouch(View view, MotionEvent event) {
        // Handle touch events here...
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                initDragInfo(view, event);
                Log.i(TAG, "======ACTION_DOWN - CLICK ======" + event.getActionIndex());
                mode = CLICK;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "======ACTION_POINTER_1_DOWN======");
                oldDist = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                Log.i(TAG, "========oldDist:" + oldDist + "=========");
                if (oldDist < SPACE_ZOOM) {
                    mode = NONE;
                } else {
                    mode = ZOOM;
                    initZoomInfo(view, event);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");

                if (mode == CLICK) {
                    Log.i(TAG, "mode= onTouchDitect");
                   // liveStreamingActivity.onTouchDitect();
                }
                mode = NONE;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == CLICK) {
                    if (spacing(start.x, start.y, event.getRawX(), event.getRawY()) >= SPACE_DRAG) {
                        mode = DRAG;
                    } else {
                        Log.i(TAG, "======ACTION_MOVE - CLICK======");
                        break;
                    }
                }

                if (mode == DRAG) {
                    Log.i(TAG, "======ACTION_MOVE - DRAG======");
                    doPan(event.getRawX() - start.x, event.getRawY() - start.y);
                    break;
                }

                if (mode == ZOOM) {
                    Log.i(TAG, "======ACTION_MOVE - ZOOM======");

                    float newDist = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    if (newDist >= SPACE_ZOOM) {
                        doZoom(newDist);
                    }
                }
                break;
        }
        return true; // indicate event was handled
    }

    private void initDragInfo(View view, MotionEvent event) {
        start.set(event.getRawX(), event.getRawY());
        // Initialize start motion info
        oldParams = new MarginLayoutParams((MarginLayoutParams) child.getLayoutParams());
        oldParams.width = child.getWidth();
        oldParams.height = child.getHeight();
    }

    // Caculate the space between two points
    private float spacing(float x0, float y0, float x1, float y1) {
        float x = x0 - x1;
        float y = y0 - y1;
        return (float) Math.sqrt(x * x + y * y);
    }

    // Calculate the mid point of the first two fingers
    private void initZoomInfo(View view, MotionEvent event) {
        midPoint.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);

        // Initialize start motion info
        oldParams = new MarginLayoutParams((MarginLayoutParams) child.getLayoutParams());
        oldParams.width = child.getWidth();
        oldParams.height = child.getHeight();
    }

    public void doZoom(float newDist) {
        Anchor anchor = Anchor.NONE;
        /*
        Log.i(TAG, "=======MID POINT: " + midPoint.x + "; " + midPoint.y + "============");
        Log.i(TAG, "=======CHILD VIEW WIDTH: " + child.getWidth() + "============");
        */

        MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();

        // Log.i(TAG, "=======CHILD VIEW LEFT MARGIN: " + childParams.leftMargin + "============");

        float scale = newDist / oldDist;

        int oriWidth = parentView.getWidth();
        int oriHeight = parentView.getHeight();
        float tempWidth = oldParams.width * scale;
        float tempHeight = tempWidth * oriHeight / oriWidth;

        if (tempWidth < oriWidth || tempWidth > oriWidth * ZOOM_MAX) {
            return;
        }

        childParams.width = (int) tempWidth;
        childParams.height = (int) tempHeight;

        float x1 = midPoint.x + oldParams.leftMargin;
        float x2 = oriWidth - x1;
        float y1 = midPoint.y + oldParams.topMargin;
        float y2 = oriHeight - y1;

        if (midPoint.x * scale <= x1) {
            anchor = Anchor.LEFT;
        } else if ((oldParams.width - midPoint.x) * scale <= x2) {
            anchor = Anchor.RIGHT;
        }

        if (midPoint.y * scale <= y1) {
            if (anchor == Anchor.LEFT) {
                anchor = Anchor.LEFT_TOP;
            } else if (anchor == Anchor.RIGHT) {
                anchor = Anchor.TOP_RIGHT;
            } else {
                anchor = Anchor.TOP;
            }
        } else if ((oldParams.height - midPoint.y) * scale <= y2) {
            if (anchor == Anchor.LEFT) {
                anchor = Anchor.LEFT_BOTTOM;
            } else if (anchor == Anchor.RIGHT) {
                anchor = Anchor.RIGHT_BOTTOM;
            } else {
                anchor = Anchor.BOTTOM;
            }
        }

        switch (anchor) {
            case BOTTOM:
                childParams.leftMargin = (int) (x1 - midPoint.x * scale);
                childParams.topMargin = (int) (oriHeight - tempHeight);
                break;
            case LEFT:
                childParams.leftMargin = 0;
                childParams.topMargin = (int) (y1 - midPoint.y * scale);
                break;
            case LEFT_BOTTOM:
                childParams.leftMargin = 0;
                childParams.topMargin = (int) (oriHeight - tempHeight);
                break;
            case LEFT_TOP:
                childParams.leftMargin = 0;
                childParams.topMargin = 0;
                break;
            case NONE:
                childParams.leftMargin = (int) (x1 - midPoint.x * scale);
                childParams.topMargin = (int) (y1 - midPoint.y * scale);
                break;
            case RIGHT:
                childParams.leftMargin = (int) (oriWidth - tempWidth);
                childParams.topMargin = (int) (y1 - midPoint.y * scale);
                break;
            case RIGHT_BOTTOM:
                childParams.leftMargin = (int) (oriWidth - tempWidth);
                childParams.topMargin = (int) (oriHeight - tempHeight);
                break;
            case TOP:
                childParams.leftMargin = (int) (x1 - midPoint.x * scale);
                childParams.topMargin = 0;
                break;
            case TOP_RIGHT:
                childParams.topMargin = 0;
                childParams.leftMargin = (int) (oriWidth - tempWidth);
                break;
            default:
                break;
        }


        child.setLayoutParams(childParams);
    }

    public void doPan(float panX, float panY) {
        Anchor anchor = Anchor.NONE;
        MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();

        if (oldParams.width <= parentView.getWidth()) {
            return;
        }

        if (oldParams.leftMargin + panX >= 0) {
            anchor = Anchor.LEFT;
        } else if (oldParams.leftMargin + panX + oldParams.width <= parentView.getWidth()) {
            anchor = Anchor.RIGHT;
        }

        if (oldParams.topMargin + panY >= 0) {
            if (anchor == Anchor.LEFT) {
                anchor = Anchor.LEFT_TOP;
            } else if (anchor == Anchor.RIGHT) {
                anchor = Anchor.TOP_RIGHT;
            } else {
                anchor = Anchor.TOP;
            }
        } else if (oldParams.topMargin + panY + oldParams.height <= parentView.getHeight()) {
            if (anchor == Anchor.LEFT) {
                anchor = Anchor.LEFT_BOTTOM;
            } else if (anchor == Anchor.RIGHT) {
                anchor = Anchor.RIGHT_BOTTOM;
            } else {
                anchor = Anchor.BOTTOM;
            }
        }

        switch (anchor) {
            case BOTTOM:
            case TOP:
                childParams.leftMargin = (int) (oldParams.leftMargin + panX);
                break;
            case LEFT:
            case RIGHT:
                childParams.topMargin = (int) (oldParams.topMargin + panY);
                break;
            case NONE:
                childParams.topMargin = (int) (oldParams.topMargin + panY);
                childParams.leftMargin = (int) (oldParams.leftMargin + panX);
                break;
            case LEFT_BOTTOM:
            case LEFT_TOP:
            case RIGHT_BOTTOM:
            case TOP_RIGHT:
                break;
            default:
                break;
        }
        child.setLayoutParams(childParams);

    }
}