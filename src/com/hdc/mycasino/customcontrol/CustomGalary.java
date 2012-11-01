package com.hdc.mycasino.customcontrol;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class CustomGalary extends Gallery {

	Camera mCamera;

	public CustomGalary(Context context) {
		// super(context);
		// TODO Auto-generated constructor stub
		this(context, null);
	}

	public CustomGalary(Context context, AttributeSet a) {
		this(context, a, 0);
	}

	public CustomGalary(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mCamera = new Camera();
		setSpacing(0);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		// TODO Auto-generated method stub
		super.getChildStaticTransformation(child, t);
		final int mCenter = (getWidth() - getPaddingLeft() - getPaddingRight())
				/ 2 + getPaddingLeft();
		final int childCenter = child.getLeft() + child.getWidth() / 2;
		final int childWidth = child.getWidth();

		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		float rate = Math.abs((float) (mCenter - childCenter) / childWidth);

		mCamera.save();
		final Matrix matrix = t.getMatrix();
		float zoomAmount = (float) (rate * 200.0);
		mCamera.translate(0.0f, 0.0f, zoomAmount);
		mCamera.getMatrix(matrix);
		matrix.preTranslate(-(childWidth / 2), -(childWidth / 2));
		matrix.postTranslate((childWidth / 2), (childWidth / 2));
		mCamera.restore();

		return true;
	}

}
