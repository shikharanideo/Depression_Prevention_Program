package com.example.bato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;


public class LaserBeamDestroyer extends View
{
	Paint mLaserPaint;
	DestroyerGame mDestroyer;
	int mStartX;
	int mStartY;
	int mStopX;
	int mStopY;
	int xRange;
	int yRange;
	int mHoldStopY;
	int mSmallBounds;
	int mLargeBounds;
	boolean hooked;
	boolean xBounds;
	BattleField mBattle;
	boolean mGameOver;
	int Position;
	RectF oval;
	int mLeftX; 
	int mTopY;
	int mRightX;
	int mBottomY;
	int mCenterX;
	int mCenterY;
	int xOfCenter;
	int yOfCenter;
	float offset;
	int mTextWidth;
	int mTextHeight;
	int negY;
	
	public LaserBeamDestroyer(Context context, int mPosition)
	{
		super(context);
		Position = mPosition;
		mDestroyer = (DestroyerGame) context;
		mLaserPaint = new Paint();
		//mLaserPaint.setStyle(Paint.Style.FILL);
		//mLaserPaint.setStrokeWidth(15);
		mLaserPaint.setColor(Color.parseColor("#FFFF99"));
		mLaserPaint.setAlpha(150);
		oval = new RectF();

	}
	
	@Override
	 protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
	     super.onSizeChanged(xNew, yNew, xOld, yOld);
	     mTextWidth = mDestroyer.mNeg.width;
	     mTextHeight = mDestroyer.mNeg.height;
		 yOfCenter = mTextHeight * Position;
		 //negY = mTextHeight/2;
		 xOfCenter = mDestroyer.mDestroyerShooter.mPosX + mTextWidth/2;
		 offset = offSet();
	}
	
	public float radius(int Posx, int Negx)
	{
		double A2 = Math.pow((Negx  - Posx), 2);
		double B2 = Math.pow((yOfCenter), 2);
		return (float) Math.sqrt(A2+B2);
	}
	
	public float startAngle(int x, int y)
	{
		return (float)(Math.atan2(x - xOfCenter, yOfCenter -y) * 180/Math.PI + 360 ) % 360;
	}
	
	public float endAngle(int x, int y)
	{
		return (float)((Math.atan2((x+mTextWidth/2)-xOfCenter, yOfCenter - y) * 180/Math.PI + 360 ) % 360)- 30;
	
	}
	
	public float offSet()
	{
		return (float)(Math.atan2(-xOfCenter, yOfCenter) * 180/Math.PI + 360 ) % 360;
	}
	
	
	@Override 
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		xOfCenter = mDestroyer.mDestroyerShooter.mNegX;
		Log.e("my pos x is", "" + mDestroyer.mDestroyerShooter.mPosX);
		float radius = radius(mDestroyer.mDestroyerShooter.mPosX, mDestroyer.mDestroyerShooter.mNegX);
		float startAngle = startAngle(mDestroyer.mDestroyerShooter.mPosX, mDestroyer.mDestroyerShooter.mNegX);
		float endAngle = endAngle(mDestroyer.mDestroyerShooter.mPosX, mDestroyer.mDestroyerShooter.mNegX);
		oval.set(xOfCenter - radius, yOfCenter - radius, xOfCenter + radius, yOfCenter + radius);
		canvas.drawArc(oval, -45f -(7.5f * Position), startAngle-endAngle, true, mLaserPaint);

	}

}
