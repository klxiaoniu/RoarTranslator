package tk.dreamydragon.roartranslater;

import android.content.ClipData;
import android.os.Message;

/* loaded from: classes.dex */
public class NormalTransTask {

    /* renamed from: ch */
    private static char[] f27ch = {22007, 21596, 21834, '~'};
    private static boolean doing;

    public static void setChars(char[] newChars) {
        f27ch = newChars;
    }

    public static void toBeast(final String text) {
        if (!doing) {
            doing = true;
            Thread toBest = new Thread(new Runnable() { // from class: tk.dreamydragon.roartranslater.NormalTransTask.1
                @Override // java.lang.Runnable
                public void run() {
                    Message msg = Message.obtain();
                    try {
                        TxtToBeast.setBeastChars(NormalTransTask.f27ch[0], NormalTransTask.f27ch[1], NormalTransTask.f27ch[2], NormalTransTask.f27ch[3]);
                        String str = TxtToBeast.toBeast(text);
                        if (!str.equals("")) {
                            msg.obj = "" + NormalTransTask.f27ch[3] + NormalTransTask.f27ch[1] + NormalTransTask.f27ch[0] + str + NormalTransTask.f27ch[2];
                            msg.what = 1;
                        } else {
                            msg.what = -1;
                        }
                    } catch (Exception e) {
                        msg.what = -1;
                    }
                    MainActivity.handler.sendMessage(msg);
                    boolean unused = NormalTransTask.doing = false;
                }
            });
            toBest.start();
        }
    }

    public static void fromBeast(final String text) {
        if (!doing) {
            doing = true;
            Thread fromBest = new Thread(new Runnable() { // from class: tk.dreamydragon.roartranslater.NormalTransTask.2
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
                        if (!str.equals("")) {
                            msg.obj = str;
                            msg.what = 1;
                        } else {
                            msg.what = -1;
                        }
                    } catch (Exception e) {
                        msg.what = -1;
                    }
                    MainActivity.handler.sendMessage(msg);
                    boolean unused = NormalTransTask.doing = false;
                }
            });
            fromBest.start();
        }
    }

    public static void copy(final String text) {
        Thread copy = new Thread(new Runnable() { // from class: tk.dreamydragon.roartranslater.NormalTransTask.3
            @Override // java.lang.Runnable
            public void run() {
                Message msg = Message.obtain();
                try {
                    ClipData cd = ClipData.newPlainText("Beast", text);
                    MainActivity.clipboard.setPrimaryClip(cd);
                    msg.what = 2;
                } catch (Exception e) {
                    msg.what = -2;
                }
                MainActivity.handler.sendMessage(msg);
            }
        });
        copy.run();
    }

    public static void paste() {
        Message msg = Message.obtain();
        if (MainActivity.clipboard.hasPrimaryClip() && MainActivity.clipboard.getPrimaryClipDescription().hasMimeType("text/plain")) {
            try {
                ClipData cd = MainActivity.clipboard.getPrimaryClip();
                ClipData.Item item = cd.getItemAt(0);
                msg.what = 1;
                msg.obj = item.getText();
                MainActivity.handler.sendMessage(msg);
                return;
            } catch (Exception e) {
                msg.what = -3;
                MainActivity.handler.sendMessage(msg);
                return;
            }
        }
        msg.what = -3;
        MainActivity.handler.sendMessage(msg);
    }
}
