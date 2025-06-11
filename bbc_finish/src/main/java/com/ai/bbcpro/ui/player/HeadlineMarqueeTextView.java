package com.ai.bbcpro.ui.player;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class HeadlineMarqueeTextView extends AppCompatTextView {
    public HeadlineMarqueeTextView(Context context) {
        super(context);
    }

    public HeadlineMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadlineMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isFocused() {
        return true;
    }
}
