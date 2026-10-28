package com.example.heart;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {
    static { System.loadLibrary("heart"); }
    static native void nInit();
    static native void nResize(int w, int h);
    static native void nDraw();
    static native void nTouch(float x, float y);

    private GLSurfaceView view;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        view = new GLSurfaceView(this);
        view.setEGLContextClientVersion(2);
        view.setRenderer(new GLSurfaceView.Renderer() {
            public void onSurfaceCreated(GL10 g, EGLConfig c) { nInit(); }
            public void onSurfaceChanged(GL10 g, int w, int h) { nResize(w, h); }
            public void onDrawFrame(GL10 g) { nDraw(); }
        });
        view.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE) {
                final float x = e.getX(), y = e.getY();
                view.queueEvent(() -> nTouch(x, y));
            }
            return true;
        });
        setContentView(view);
    }
    @Override protected void onPause()  { super.onPause();  view.onPause(); }
    @Override protected void onResume() { super.onResume(); view.onResume(); }
}
