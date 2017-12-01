package com.example.user.qrcode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class QRActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView mSufaceView01;
    SurfaceHolder mSurfaceHolder01;
    Button genBtn;
    EditText inputET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*使应用全屏幕运行，不实用title bar*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        /*取得屏幕解析像素*/
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        /*以SurfaceView作为相机Preview之用*/
        mSufaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);

        /* 绑定SurfaceView，取得SurfaceHolder对象 */
        mSurfaceHolder01 = mSufaceView01.getHolder();

        /*Activity必须实现 SurfaceHolder.Callback*/
        mSurfaceHolder01.addCallback(QRActivity.this);

        genBtn = (Button) findViewById(R.id.genQR_btn);
        inputET = (EditText) findViewById(R.id.inputET);

        genBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputET.getText().toString() != "") {
                    AndroidQREncode(inputET.getText().toString(), 4);
                }
            }
        });

        inputET.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    public void AndroidQREncode(String strEncoding, int qrcodeVerison) {

        try {
            com.swetake.util.Qrcode testQrcode = new com.swetake.util.Qrcode();

        /* L,M,Q,H */
            testQrcode.setQrcodeErrorCorrect('M');

        /* N,A or other */
            testQrcode.setQrcodeEncodeMode('B');

        /* 0-20 */
            testQrcode.setQrcodeVersion(qrcodeVerison);

            //getBytes
            byte[] bytesEncoding = strEncoding.getBytes("utf-8");

            if (bytesEncoding.length > 0 && bytesEncoding.length < 120) {

                /*将字符串通过calQrcode函数转换成boolean数组*/

                boolean[][] bEncoding = testQrcode.calQrcode(bytesEncoding);

                /* 依据编码后的boolean数组，绘图*/
                drawQRCode(bEncoding, getResources().getColor(R.color.BLACK));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void drawQRCode(boolean[][] bRect, int colorFill) {
        /* test Canvas */
        int intPadding = 30;

        /* 欲在SurfaceView上绘图，需先lock锁定SurfaceHoldrer */
        Canvas mCanvas01 = mSurfaceHolder01.lockCanvas();

        /* 设置画布绘制颜色 */
        mCanvas01.drawColor(getResources().getColor(R.color.WHITE));

        /*创建画笔*/
        Paint paint = new Paint();

        /*设置画笔颜色及模式*/
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorFill);
        paint.setStrokeWidth(1.0F);

        /* 逐一加载2维boolean数组 */
        for (int i = 0; i < bRect.length; i++) {
            for (int j = 0; j < bRect.length; j++) {
                if (bRect[j][i]) {

                    /*依据数组值，绘出条形码方块 */
                    /* mCanvas01.drawRect(new Rect(intPadding + j * 3 + 2,intPadding + i *3
                    +2, intPadding +j * 3 +2 +3,intpadding +i*3+2+3),paint);
                     */
                    mCanvas01.drawRect(new Rect(intPadding + j * 3 + 2, intPadding + i * 3
                            + 2, intPadding + j * 3 + 2 + 3, intPadding + i * 3 + 2 + 3), paint);
                }
            }
        }
        mSurfaceHolder01.unlockCanvasAndPost(mCanvas01);
    }

    public void mMakeTextToast(String str,boolean isLong){
        if(isLong == true){
            Toast.makeText(QRActivity.this,str,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(QRActivity.this,str,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
