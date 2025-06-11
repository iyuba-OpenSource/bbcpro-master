package com.ai.bbcpro.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;

public class IyuButton extends androidx.appcompat.widget.AppCompatButton {

    private Animation mAnimationAlpha;

    public IyuButton(Context context) {
        super(context);
    }
    public IyuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IyuButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getBackground().setAlpha(100);
                invalidate();
                // mAnimationAlpha=new AlphaAnimation(1.0f,0.5f);
                // mAnimationAlpha.setDuration(1000);
                // startAnimation(mAnimationAlpha);
                // mAnimationAlpha.setAnimationListener(new AnimationListener() {
                //
                // @Override
                // public void onAnimationStart(Animation animation) {
                //
                // }
                //
                // @Override
                // public void onAnimationRepeat(Animation animation) {
                //
                // }
                //
                // @Override
                // public void onAnimationEnd(Animation animation) {
                // getBackground().setAlpha(100);
                // invalidate();
                // }
                // });

                break;
            case MotionEvent.ACTION_UP:
                getBackground().setAlpha(255);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

}

