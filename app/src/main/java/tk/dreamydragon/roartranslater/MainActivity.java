package tk.dreamydragon.roartranslater;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity {
    public static ClipboardManager clipboard;
    public static Handler handler;
    private Intent intent;
    private myListener clickListener = new myListener();
    private mySwitch switchListener = new mySwitch();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // android.support.p003v7.app.AppCompatActivity, android.support.p000v4.app.FragmentActivity, android.support.p000v4.app.BaseFragmentActivityDonut, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new myHandler();
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        FloatView.ready(getApplicationContext(), getWindowManager());
        SharedPreferences mySp = getPreferences(0);
        char[] chars = {(char) mySp.getInt("F0", 22007), (char) mySp.getInt("F1", 21596), (char) mySp.getInt("F2", 21834), (char) mySp.getInt("F3", 126)};
        NormalTransTask.setChars(chars);
        RoarTransFloatService.setChars(chars);
        goMainView();
        this.intent = new Intent(this, RoarTransFloatService.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override
    // android.support.p003v7.app.AppCompatActivity, android.support.p000v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    // android.support.p000v4.app.FragmentActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goMainView() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_normal_mod).setOnClickListener(this.clickListener);
        findViewById(R.id.button_float_mod).setOnClickListener(this.clickListener);
        findViewById(R.id.button_setting).setOnClickListener(this.clickListener);
        findViewById(R.id.button_about).setOnClickListener(this.clickListener);
        findViewById(R.id.button_exit).setOnClickListener(this.clickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFloatServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("tk.dreamydragon.roartranslater.RoarTransFloatService".equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: classes.dex */
    class mySwitch implements CompoundButton.OnCheckedChangeListener {
        mySwitch() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton.getId() == R.id.switch_float_mod) {
                if (isChecked) {
                    MainActivity.this.startService(MainActivity.this.intent);
                } else {
                    MainActivity.this.stopService(MainActivity.this.intent);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class myListener implements View.OnClickListener {
        myListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.button_back) { /* 2131427412 */
                MainActivity.this.goMainView();
                return;
            } else if (id == R.id.button_normal_mod) { /* 2131427413 */
                MainActivity.this.setContentView(R.layout.normal_mod);
                MainActivity.this.findViewById(R.id.button_roar).setOnClickListener(MainActivity.this.clickListener);
                MainActivity.this.findViewById(R.id.button_word).setOnClickListener(MainActivity.this.clickListener);
                MainActivity.this.findViewById(R.id.button_copy).setOnClickListener(MainActivity.this.clickListener);
                MainActivity.this.findViewById(R.id.button_paste).setOnClickListener(MainActivity.this.clickListener);
                MainActivity.this.findViewById(R.id.button_clean).setOnClickListener(MainActivity.this.clickListener);
                MainActivity.this.findViewById(R.id.button_back).setOnClickListener(MainActivity.this.clickListener);
                return;
            } else if (id == R.id.button_float_mod) { /* 2131427414 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    }
                }
                if (!MainActivity.this.isFloatServiceRunning()) {
                    MainActivity.this.startService(MainActivity.this.intent);
                }
                Intent i = new Intent("android.intent.action.MAIN");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addCategory("android.intent.category.HOME");
                MainActivity.this.startActivity(i);
                return;
            } else if (id == R.id.button_setting) { /* 2131427415 */
                MainActivity.this.setContentView(R.layout.setting);
                SharedPreferences mySp = MainActivity.this.getPreferences(0);
                StringBuffer sb = new StringBuffer();
                sb.append(String.valueOf((char) mySp.getInt("F0", 22007)));
                sb.append(String.valueOf((char) mySp.getInt("F1", 21596)));
                sb.append(String.valueOf((char) mySp.getInt("F2", 21834)));
                sb.append(String.valueOf((char) mySp.getInt("F3", 126)));
                ((EditText) MainActivity.this.findViewById(R.id.editText_setting)).setText(sb.toString());
                MainActivity.this.findViewById(R.id.button_setting_back).setOnClickListener(MainActivity.this.clickListener);
                MainActivity.this.findViewById(R.id.button_setting_ok).setOnClickListener(MainActivity.this.clickListener);
                Switch floatSwitch = (Switch) MainActivity.this.findViewById(R.id.switch_float_mod);
                floatSwitch.setOnCheckedChangeListener(MainActivity.this.switchListener);
                if (MainActivity.this.isFloatServiceRunning()) {
                    floatSwitch.setChecked(true);
                    return;
                } else {
                    floatSwitch.setChecked(false);
                    return;
                }
            } else if (id == R.id.button_about) { /* 2131427416 */
                MainActivity.this.setContentView(R.layout.about);
                MainActivity.this.findViewById(R.id.button_back).setOnClickListener(MainActivity.this.clickListener);
                return;
            } else if (id == R.id.button_exit) { /* 2131427417 */
                MainActivity.this.finish();
                return;
            } else if (id == R.id.button_copy) { /* 2131427429 */
                EditText et3 = (EditText) MainActivity.this.findViewById(R.id.editText);
                NormalTransTask.copy(et3.getText().toString());
                return;
            } else if (id == R.id.button_paste) { /* 2131427430 */
                NormalTransTask.paste();
                return;
            } else if (id == R.id.button_clean) { /* 2131427431 */
                EditText et4 = (EditText) MainActivity.this.findViewById(R.id.editText);
                et4.setText("");
                return;
            } else if (id == R.id.button_roar) { /* 2131427432 */
                EditText et = (EditText) MainActivity.this.findViewById(R.id.editText);
                NormalTransTask.toBeast(et.getText().toString());
                return;
            } else if (id == R.id.button_word) { /* 2131427433 */
                EditText et2 = (EditText) MainActivity.this.findViewById(R.id.editText);
                NormalTransTask.fromBeast(et2.getText().toString());
                return;
            } else if (id == R.id.button_setting_back) { /* 2131427450 */
                MainActivity.this.goMainView();
                return;
            } else if (id == R.id.button_setting_ok) { /* 2131427451 */
                String s = ((EditText) MainActivity.this.findViewById(R.id.editText_setting)).getText().toString();
                if (s.length() == 4) {
                    char[] chars = s.toCharArray();
                    if (chars[0] != chars[1] && chars[0] != chars[2] && chars[0] != chars[3] && chars[1] != chars[2] && chars[1] != chars[3] && chars[2] != chars[3]) {
                        SharedPreferences.Editor edit = MainActivity.this.getPreferences(0).edit();
                        edit.putInt("F0", chars[0]);
                        edit.putInt("F1", chars[1]);
                        edit.putInt("F2", chars[2]);
                        edit.putInt("F3", chars[3]);
                        edit.commit();
                        NormalTransTask.setChars(chars);
                        RoarTransFloatService.setChars(chars);
                        Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.setting_suc), 0).show();
                        MainActivity.this.goMainView();
                        return;
                    }
                    Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.setting_err), 0).show();
                    return;
                }
                Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.setting_err), 0).show();
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
            switch (msg.what) {
                case -3:
                    Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.paste_err), 0).show();
                    return;
                case -2:
                    Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.copy_err), 0).show();
                    return;
                case -1:
                    Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.trans_err), 0).show();
                    return;
                case 0:
                default:
                    return;
                case 1:
                    EditText et = (EditText) MainActivity.this.findViewById(R.id.editText);
                    et.setText((String) msg.obj);
                    return;
                case 2:
                    Toast.makeText(MainActivity.this.getApplicationContext(), MainActivity.this.getString(R.string.copy_suc), 0).show();
                    return;
            }
        }
    }
}
