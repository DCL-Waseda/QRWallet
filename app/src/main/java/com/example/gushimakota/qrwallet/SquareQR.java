package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.util.AttributeSet;

import com.journeyapps.barcodescanner.CompoundBarcodeView;

/**
 * Created by gushimakota on 16/05/27.
 */
public class SquareQR extends CompoundBarcodeView {

    public SquareQR(Context context) {
        super(context);
    }
    public SquareQR(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }
    public SquareQR(Context context,AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = widthMeasureSpec;
        setMeasuredDimension(width,width);
    }

}
