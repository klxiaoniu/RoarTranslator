package tk.dreamydragon.roartranslater;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.HashMap;

/* loaded from: classes.dex */
public class FloatView {
    public static final String MOD_FLOAT_BUTTON = "float button";
    public static final String MOD_FLOAT_WINDOW = "float window";
    public static final String MOD_OUTSIDE_TOUCH_HIDE = "outside touch hide";
    private static Context context;
    private static final HashMap<String, FloatView> map = new HashMap<>();
    public static int scrHeight;
    public static int scrWidth;

    /* renamed from: wm */
    private static WindowManager f21wm;
    public Runnable clickEvent;
    public Runnable closeEvent;
    public Limiter limiter;

    /* renamed from: lp */
    private WindowManager.LayoutParams f22lp;
    private View view;
    private int systemBarHeight = 0;
    private boolean isShow = false;
    public boolean resizeHeight = true;
    public boolean resizeWidth = true;
    public boolean resizeDisplay = false;

    public FloatView(int layoutID) {
        readyFloatView();
        setView(layoutID);
    }

    public FloatView(View view) {
        readyFloatView();
        setView(view);
    }

    public FloatView(int X, int Y, int width, int height, int layoutID) {
        readyFloatView();
        setView(layoutID);
        setX(X);
        setY(Y);
        setWidth(width);
        setHeight(height);
    }

    public FloatView(int X, int Y, int width, int height, View view) {
        readyFloatView();
        setView(view);
        setX(X);
        setY(Y);
        setWidth(width);
        setHeight(height);
    }

    public static void ready(Context ctxt, WindowManager winm) {
        context = ctxt;
        DisplayMetrics dm = new DisplayMetrics();
        f21wm = winm;
        f21wm.getDefaultDisplay().getMetrics(dm);
        scrHeight = dm.heightPixels;
        scrWidth = dm.widthPixels;
    }

    public static FloatView getFromTag(String tag) {
        if (!map.containsKey(tag)) {
            return null;
        }
        FloatView f = map.get(tag);
        return f;
    }

    public void addToTag(String tag) {
        map.put(tag, this);
    }

    public boolean isShowing() {
        return this.isShow;
    }

    private void readyFloatView() {
        this.f22lp = new WindowManager.LayoutParams();
        this.f22lp.gravity = 51;
        this.f22lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        this.f22lp.format = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.f22lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            this.f22lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        this.limiter = new Limiter();
    }

    public WindowManager.LayoutParams getLayoutParams() {
        return this.f22lp;
    }

    public View getView() {
        return this.view;
    }

    public WindowManager getWindowManager() {
        return f21wm;
    }

    public Context getContext() {
        return context;
    }

    public void setMod(String Mod) {
        if (Mod.equals(MOD_FLOAT_WINDOW)) {
            this.f22lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            refreshView();
        }
        if (Mod.equals(MOD_FLOAT_BUTTON)) {
            this.f22lp.flags = 40;
            this.view.setOnTouchListener(new MoveListener());
            refreshView();
        }
        if (Mod.equals(MOD_OUTSIDE_TOUCH_HIDE)) {
            this.f22lp.flags = 0;
            this.view.setOnTouchListener(new View.OnTouchListener() { // from class: tk.dreamydragon.roartranslater.FloatView.1
                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View p1, MotionEvent event) {
                    Rect rect = new Rect();
                    FloatView.this.view.getGlobalVisibleRect(rect);
                    if (event.getAction() == 0 && !rect.contains((int) event.getX(), (int) event.getY())) {
                        FloatView.this.hide();
                        if (FloatView.this.closeEvent != null) {
                            p1.post(FloatView.this.closeEvent);
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
            });
            refreshView();
        }
    }

    public void refreshView() {
        if (this.isShow) {
            f21wm.updateViewLayout(this.view, this.f22lp);
        }
    }

    public void setAlpha(float alpha) {
        this.f22lp.alpha = alpha;
        refreshView();
    }

    public void setFlags(int flags) {
        this.f22lp.flags = flags;
        refreshView();
    }

    public void setFormat(int format) {
        this.f22lp.format = format;
        refreshView();
    }

    public void setHeight(int height) {
        this.f22lp.height = height;
        this.limiter.limit();
    }

    public void setMoveView(View moveView) {
        moveView.setOnTouchListener(new MoveListener());
    }

    public void setResizeView(View resizeView) {
        resizeView.setOnTouchListener(new ResizeListener());
    }

    public void setHideView(View v) {
        v.setOnClickListener(new View.OnClickListener() { // from class: tk.dreamydragon.roartranslater.FloatView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View p1) {
                FloatView.this.hide();
                if (FloatView.this.closeEvent != null) {
                    p1.post(FloatView.this.closeEvent);
                }
            }
        });
    }

    public void setMaxSize(int width, int height) {
        this.limiter.maxWidth = width;
        this.limiter.maxHeight = height;
    }

    public void setType(int type) {
        this.f22lp.type = type;
        refreshView();
    }

    public void setMinSize(int width, int height) {
        this.limiter.minWidth = width;
        this.limiter.minHeight = height;
    }

    public void setView(int layotID) {
        this.view = LayoutInflater.from(context).inflate(layotID, (ViewGroup) null);
    }

    public void setView(View vi) {
        this.view = vi;
    }

    public void setWidth(int width) {
        this.f22lp.width = width;
        this.limiter.limit();
    }

    public void setX(int x) {
        this.f22lp.x = x;
        this.limiter.limit();
    }

    public void setY(int y) {
        this.f22lp.y = y;
        this.limiter.limit();
    }

    public void setSystembarHeight(int height) {
        this.systemBarHeight = height;
    }

    public void show() {
        if (!this.isShow) {
            f21wm.addView(this.view, this.f22lp);
            this.isShow = true;
            this.limiter.limit();
        }
    }

    public void hide() {
        if (this.isShow) {
            f21wm.removeView(this.view);
            this.isShow = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Limiter {
        public boolean isLimitingSize;
        public boolean isLimitinhPosition;
        public int maxHeight;
        public int maxWidth;
        public int minHeight;
        public int minWidth;

        private Limiter() {
            this.isLimitingSize = true;
            this.isLimitinhPosition = true;
            this.maxHeight = FloatView.scrHeight;
            this.maxWidth = FloatView.scrWidth;
            this.minHeight = 0;
            this.minWidth = 0;
        }

        public void limit() {
            if (this.isLimitingSize) {
                if (FloatView.this.f22lp.width < this.minWidth) {
                    FloatView.this.f22lp.width = this.minWidth;
                }
                if (FloatView.this.f22lp.height < this.minHeight) {
                    FloatView.this.f22lp.height = this.minHeight;
                }
                if (FloatView.this.f22lp.width > this.maxWidth) {
                    FloatView.this.f22lp.width = this.maxWidth;
                }
                if (FloatView.this.f22lp.height > this.maxHeight) {
                    FloatView.this.f22lp.height = this.maxHeight;
                }
                if (FloatView.this.f22lp.width < 0) {
                    FloatView.this.f22lp.width = 0;
                }
                if (FloatView.this.f22lp.height < 0) {
                    FloatView.this.f22lp.height = 0;
                }
                if (FloatView.this.f22lp.width > FloatView.scrWidth) {
                    FloatView.this.f22lp.width = FloatView.scrWidth;
                }
                if (FloatView.this.f22lp.height > FloatView.scrHeight - FloatView.this.systemBarHeight) {
                    FloatView.this.f22lp.height = FloatView.scrHeight - FloatView.this.systemBarHeight;
                }
            }
            if (this.isLimitinhPosition) {
                if (FloatView.this.f22lp.x < 0) {
                    FloatView.this.f22lp.x = 0;
                }
                if (FloatView.this.f22lp.y < FloatView.this.systemBarHeight) {
                    FloatView.this.f22lp.y = FloatView.this.systemBarHeight;
                }
                if (FloatView.this.f22lp.x + FloatView.this.f22lp.width > FloatView.scrWidth) {
                    FloatView.this.f22lp.x = FloatView.scrWidth - FloatView.this.f22lp.width;
                }
                if (FloatView.this.f22lp.y + FloatView.this.f22lp.height > FloatView.scrHeight) {
                    FloatView.this.f22lp.y = FloatView.scrHeight - FloatView.this.f22lp.height;
                }
            }
            FloatView.this.refreshView();
        }
    }

    /* loaded from: classes.dex */
    private class MoveListener implements View.OnTouchListener {

        /* renamed from: X */
        float f23X;

        /* renamed from: Y */
        float f24Y;
        float lastX;
        float lastY;
        float startX;
        float startY;

        private MoveListener() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View p1, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    this.startX = event.getRawX();
                    this.startY = event.getRawY();
                    this.lastX = this.startX;
                    this.lastY = this.startY;
                    return true;
                case 1:
                    if (Math.abs(this.lastX - this.startX) < 5.0f && Math.abs(this.lastY - this.startY) < 5.0f) {
                        FloatView.this.f22lp.x = (int) ((FloatView.this.f22lp.x + this.startX) - this.lastX);
                        FloatView.this.f22lp.y = (int) ((FloatView.this.f22lp.y + this.startY) - this.lastY);
                        click(p1);
                    }
                    FloatView.this.limiter.limit();
                    return true;
                case 2:
                    this.f23X = event.getRawX();
                    this.f24Y = event.getRawY();
                    FloatView.this.f22lp.x += (int) (this.f23X - this.lastX);
                    FloatView.this.f22lp.y += (int) (this.f24Y - this.lastY);
                    FloatView.this.refreshView();
                    this.lastX = this.f23X;
                    this.lastY = this.f24Y;
                    return true;
                default:
                    return true;
            }
        }

        public void click(View v) {
            if (FloatView.this.clickEvent != null) {
                v.post(FloatView.this.clickEvent);
            }
        }
    }

    /* loaded from: classes.dex */
    private class ResizeListener implements View.OnTouchListener {

        /* renamed from: X */
        float f25X;

        /* renamed from: Y */
        float f26Y;
        float lastX;
        float lastY;

        private ResizeListener() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View p1, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    this.lastX = event.getRawX();
                    this.lastY = event.getRawY();
                    return true;
                case 1:
                    FloatView.this.limiter.limit();
                    return true;
                case 2:
                    this.f25X = event.getRawX();
                    this.f26Y = event.getRawY();
                    if (FloatView.this.resizeWidth) {
                        FloatView.this.f22lp.width += (int) (this.f25X - this.lastX);
                    }
                    if (FloatView.this.resizeHeight) {
                        FloatView.this.f22lp.height += (int) (this.f26Y - this.lastY);
                    }
                    if (FloatView.this.resizeDisplay) {
                        FloatView.this.refreshView();
                    }
                    this.lastX = this.f25X;
                    this.lastY = this.f26Y;
                    return true;
                default:
                    return true;
            }
        }
    }
}
