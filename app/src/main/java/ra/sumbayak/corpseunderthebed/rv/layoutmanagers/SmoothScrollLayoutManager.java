package ra.sumbayak.corpseunderthebed.rv.layoutmanagers;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class SmoothScrollLayoutManager extends LinearLayoutManager {
    
    private Float speed = 3.0f;
    
    public SmoothScrollLayoutManager (Context context) {
        super (context);
    }
    
    public SmoothScrollLayoutManager (Context context, int orientation, boolean reverseLayout) {
        
        super (context, orientation, reverseLayout);
    }
    
    public SmoothScrollLayoutManager (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
    }
    
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller =
            new LinearSmoothScroller (recyclerView.getContext ()) {
                private static final float MILLISECONDS_PER_INCH = 100f;
                
                @Override
                public PointF computeScrollVectorForPosition (int targetPosition) {
                    return SmoothScrollLayoutManager.this
                        .computeScrollVectorForPosition (targetPosition);
                }
                
                @Override
                protected float calculateSpeedPerPixel (DisplayMetrics displayMetrics) {
                    return speed;
                    //return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                }
            };
        linearSmoothScroller.setTargetPosition (position);
        startSmoothScroll (linearSmoothScroller);
    }
}