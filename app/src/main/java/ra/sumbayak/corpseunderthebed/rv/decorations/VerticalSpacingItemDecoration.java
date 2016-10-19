package ra.sumbayak.corpseunderthebed.rv.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * {@link android.support.v7.widget.RecyclerView.ItemDecoration} that applies a
 * vertical and horizontal spacing between items of the target
 * {@link android.support.v7.widget.RecyclerView}.
 * 
 * Visit
 * https://github.com/lucasr/twoway-view/blob/master/layouts/src/main/java/org/lucasr/twowayview/widget/SpacingItemDecoration.java
 * for original code.
 */
public class VerticalSpacingItemDecoration extends RecyclerView.ItemDecoration {
    
    private final int mVerticalSpaceHeight;
    
    public VerticalSpacingItemDecoration (int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }
    
    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mVerticalSpaceHeight;
    }
}
