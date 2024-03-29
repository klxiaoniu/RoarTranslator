package tk.dreamydragon.roartranslater;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

/* loaded from: classes.dex */
public class RoarTransFloatService extends Service {
    private static myListener clickListener;
    public static ClipboardManager clipboard;
    private static boolean doing;
    private static FloatView floatIcon;
    private static FloatView floatWindow;
    public static Handler handler;
    private Runnable clickEvent = new Runnable() { // from class: tk.dreamydragon.roartranslater.RoarTransFloatService.1
        @Override // java.lang.Runnable
        public void run() {
            Message.obtain();
            if (RoarTransFloatService.clipboard.hasPrimaryClip() && RoarTransFloatService.clipboard.getPrimaryClipDescription().hasMimeType("text/plain")) {
                try {
                    ClipData cd = RoarTransFloatService.clipboard.getPrimaryClip();
                    ClipData.Item item = cd.getItemAt(0);
                    if (!item.toString().equals(RoarTransFloatService.lastCopy)) {
                        String str = item.getText().toString();
                        RoarTransFloatService.fromBeast(str);
                    }
                } catch (Exception e) {
                }
            }
            RoarTransFloatService.floatIcon.hide();
            RoarTransFloatService.floatWindow.show();
        }
    };
    private Runnable closeEvent = new Runnable() { // from class: tk.dreamydragon.roartranslater.RoarTransFloatService.2
        @Override // java.lang.Runnable
        public void run() {
            RoarTransFloatService.floatWindow.hide();
            RoarTransFloatService.floatIcon.show();
        }
    };

    /* renamed from: et */
    private EditText f30et;

    /* renamed from: ch */
    private static char[] f29ch = {22007, 21596, 21834, '~'};
    private static String lastCopy = "WTF";

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        handler = new myHandler();
        clickListener = new myListener();
        floatWindow = FloatView.getFromTag("window");
        floatIcon = FloatView.getFromTag("icon");
        if ((floatWindow == null) | (floatIcon == null)) {
            floatWindow = new FloatView(R.layout.float_window);
            floatIcon = new FloatView(R.layout.image_float_button);
            floatWindow.setHeight((int) (FloatView.scrHeight * 0.5d));
            floatWindow.setWidth((int) (FloatView.scrWidth * 0.5d));
            int dp52 = getResources().getDimensionPixelSize(R.dimen.dp52);
            floatIcon.setHeight(dp52);
            floatIcon.setWidth(dp52);
            floatWindow.setMod(FloatView.MOD_FLOAT_WINDOW);
            floatIcon.setMod(FloatView.MOD_FLOAT_BUTTON);
//            floatIcon.getLayoutParams().type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//            floatWindow.getLayoutParams().type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            floatWindow.setMoveView(floatWindow.getView().findViewById(R.id.text_float_title));
            floatWindow.setHideView(floatWindow.getView().findViewById(R.id.image_hide));
            floatWindow.setResizeView(floatWindow.getView().findViewById(R.id.layout_resize));
            floatWindow.closeEvent = this.closeEvent;
            floatIcon.clickEvent = this.clickEvent;
            floatWindow.setMinSize((int) (FloatView.scrWidth * 0.3d), (int) (FloatView.scrHeight * 0.3d));
            floatWindow.getView().findViewById(R.id.button_float_clean).setOnClickListener(clickListener);
            floatWindow.getView().findViewById(R.id.button_float_roar).setOnClickListener(clickListener);
            floatWindow.getView().findViewById(R.id.button_float_word).setOnClickListener(clickListener);
            floatWindow.getView().findViewById(R.id.button_float_copy).setOnClickListener(clickListener);
            floatWindow.getView().findViewById(R.id.button_float_paste).setOnClickListener(clickListener);
            floatWindow.addToTag("window");
            floatIcon.addToTag("icon");
        }
        this.f30et = (EditText) floatWindow.getView().findViewById(R.id.editText_float);
        floatIcon.show();
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (floatWindow.isShowing()) {
            floatWindow.hide();
        }
        if (floatIcon.isShowing()) {
            floatIcon.hide();
        }
    }

    public static void setChars(char[] newChars) {
        f29ch = newChars;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void toBeast(final String text) {
        if (!doing) {
            doing = true;
            Thread toBest = new Thread(new Runnable() { // from class: tk.dreamydragon.roartranslater.RoarTransFloatService.3
                @Override // java.lang.Runnable
                public void run() {
                    Message msg = Message.obtain();
                    try {
                        TxtToBeast.setBeastChars(RoarTransFloatService.f29ch[0], RoarTransFloatService.f29ch[1], RoarTransFloatService.f29ch[2], RoarTransFloatService.f29ch[3]);
                        String str = TxtToBeast.toBeast(text);
                        if (!str.isEmpty()) {
                            msg.obj = "" + RoarTransFloatService.f29ch[3] + RoarTransFloatService.f29ch[1] + RoarTransFloatService.f29ch[0] + str + RoarTransFloatService.f29ch[2];
                            msg.what = 1;
                        } else {
                            msg.what = -1;
                        }
                    } catch (Exception e) {
                        msg.what = -1;
                    }
                    RoarTransFloatService.handler.sendMessage(msg);
                    boolean unused = RoarTransFloatService.doing = false;
                }
            });
            toBest.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fromBeast(final String text) {
        if (!doing) {
            doing = true;
            Thread fromBest = new Thread(new Runnable() { // from class: tk.dreamydragon.roartranslater.RoarTransFloatService.4
                @Override // java.lang.Runnable
                public void run() {
                    Message msg = Message.obtain();
                    try {
                        String text2 = "";
                        if (text.length() > 4) {
                            char[] tmpch = {text.charAt(2), text.charAt(1), text.charAt(text.length() - 1), text.charAt(0)};
                            text2 = text.substring(3, text.length() - 1);
                            TxtToBeast.setBeastChars(tmpch[0], tmpch[1], tmpch[2], tmpch[3]);
                        }
                        String str = TxtToBeast.fromBeast(text2);
                        if (!str.isEmpty()) {
                            msg.obj = str;
                            msg.what = 1;
                        } else {
                            msg.what = -1;
                        }
                    } catch (Exception e) {
                        msg.what = -1;
                    }
                    RoarTransFloatService.handler.sendMessage(msg);
                    boolean unused = RoarTransFloatService.doing = false;
                }
            });
            fromBest.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void copy(final String text) {
        Thread copy = new Thread(new Runnable() { // from class: tk.dreamydragon.roartranslater.RoarTransFloatService.5
            @Override // java.lang.Runnable
            public void run() {
                Message msg = Message.obtain();
                try {
                    ClipData cd = ClipData.newPlainText("Beast", text);
                    RoarTransFloatService.clipboard.setPrimaryClip(cd);
                    msg.what = 2;
                } catch (Exception e) {
                    msg.what = -2;
                }
                RoarTransFloatService.handler.sendMessage(msg);
            }
        });
        copy.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void paste() {
        Message msg = Message.obtain();
        if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType("text/plain")) {
            try {
                ClipData cd = clipboard.getPrimaryClip();
                ClipData.Item item = cd.getItemAt(0);
                msg.what = 1;
                msg.obj = item.getText();
                handler.sendMessage(msg);
                return;
            } catch (Exception e) {
                msg.what = -3;
                handler.sendMessage(msg);
                return;
            }
        }
        msg.what = -3;
        handler.sendMessage(msg);
    }

    /* loaded from: classes.dex */
    class myListener implements View.OnClickListener {
        myListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.button_float_paste) { /* 2131427421 */
                RoarTransFloatService.paste();
                return;
            } else if (id == R.id.button_float_word) { /* 2131427422 */
                RoarTransFloatService.fromBeast(RoarTransFloatService.this.f30et.getText().toString());
                return;
            } else if (id == R.id.button_float_clean) { /* 2131427423 */
                RoarTransFloatService.this.f30et.setText("");
                return;
            } else if (id == R.id.button_float_roar) { /* 2131427424 */
                RoarTransFloatService.toBeast(RoarTransFloatService.this.f30et.getText().toString());
                return;
            } else if (id == R.id.button_float_copy) { /* 2131427425 */
                RoarTransFloatService.copy(RoarTransFloatService.this.f30et.getText().toString());
                return;
            }
            return;
        }
    }

    /* loaded from: classes.dex */
    class myHandler extends Handler {
        myHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -3:
                    Toast.makeText(RoarTransFloatService.this.getApplicationContext(), RoarTransFloatService.this.getString(R.string.paste_err), 0).show();
                    return;
                case -2:
                    Toast.makeText(RoarTransFloatService.this.getApplicationContext(), RoarTransFloatService.this.getString(R.string.copy_err), 0).show();
                    return;
                case -1:
                    Toast.makeText(RoarTransFloatService.this.getApplicationContext(), RoarTransFloatService.this.getString(R.string.trans_err), 0).show();
                    return;
                case 0:
                default:
                    return;
                case 1:
                    RoarTransFloatService.this.f30et.setText((String) msg.obj);
                    String unused = RoarTransFloatService.lastCopy = (String) msg.obj;
                    return;
                case 2:
                    Toast.makeText(RoarTransFloatService.this.getApplicationContext(), RoarTransFloatService.this.getString(R.string.copy_suc), 0).show();
                    return;
            }
        }
    }
}
