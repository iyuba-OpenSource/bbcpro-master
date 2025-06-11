package com.ai.bbcpro.ui.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

public class SpannableStringBuilderUtil {

    private final SpannableStringBuilder sb;
    private SpannableString currentSpan;

    public SpannableStringBuilderUtil() {
        this.sb = new SpannableStringBuilder();
    }

    /**
     * 获取设置完的字符串
     */
    public SpannableStringBuilder build() {
        if (currentSpan != null) {
            sb.append(currentSpan);
        }
        return sb;
    }

    /**
     * 添加一个待设置样式的字符串
     */
    public SpannableStringBuilderUtil append(CharSequence source) {
        if (currentSpan != null) {
            sb.append(currentSpan);
        }
        currentSpan = new SpannableString(source);
        return this;
    }

    /**
     * 给上一次append的进来的字符串设置样式
     */
    public SpannableStringBuilderUtil setSpan(CharacterStyle span) {
        if (currentSpan != null) {
            currentSpan.setSpan(span, 0, currentSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }
}
