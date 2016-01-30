package com.example.maps;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

@SuppressLint("NewApi")
public class Class_custom_fragmet extends MapFragment {
	  private View mOriginalView;
	    private Class_map_wrapper_layout mMapWrapperLayout;

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        mOriginalView = super.onCreateView(inflater, container, savedInstanceState);

	        mMapWrapperLayout = new Class_map_wrapper_layout(getActivity());
	        mMapWrapperLayout.addView(mOriginalView);

	        return mMapWrapperLayout;
	}

	    @Override
	    public View getView() {
	        return mOriginalView;
	    }

	    public void setOnDragListener(Class_map_wrapper_layout.OnDragListener onDragListener) {
	        mMapWrapperLayout.setOnDragListener(onDragListener);
	    }
}
