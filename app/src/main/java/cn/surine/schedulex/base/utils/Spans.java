package cn.surine.schedulex.base.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;


public class Spans {
    private static Spans instance;
    private static int FLAGS = Spannable.SPAN_INCLUSIVE_EXCLUSIVE;
    private SpannableString spannable;
    private int start, end;


    private Spans(String src) {
        spannable = new SpannableString(src);
    }

    public static Spans with(String src) {
        return instance = new Spans(src);
    }

    public Spans scope(int start, int end) {
        this.start = start;
        this.end = end;
        return instance;
    }

    public Spans size(int sizeSp) {
        return size(sizeSp, start, end);
    }

    public Spans size(int sizeSp, int start, int end) {
        spannable.setSpan(new AbsoluteSizeSpan(sizeSp, true), start, end, FLAGS);
        return instance;
    }

    public Spans color(int color) {
        return color(color, start, end);
    }

    public Spans color(int color, int start, int end) {
        spannable.setSpan(new ForegroundColorSpan(color), start, end, FLAGS);
        return instance;
    }

    public Spans bold(int start, int end) {
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, FLAGS);
        return instance;
    }

    public Spans exclude(int start, int end) {
        spannable.setSpan(new StrikethroughSpan(), start, end, FLAGS);
        return instance;
    }

    public SpannableString toSpannable() {
        return spannable;
    }

    public int length() {
        return spannable.length();
    }
}
