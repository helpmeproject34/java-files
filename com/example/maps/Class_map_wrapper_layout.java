package com.example.maps;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class Class_map_wrapper_layout extends FrameLayout {
	public interface OnDragListener {
        public void onDrag(MotionEvent motionEvent);
    }

    private OnDragListener mOnDragListener;

    public Class_map_wrapper_layout(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnDragListener != null) {
            mOnDragListener.onDrag(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }
}
