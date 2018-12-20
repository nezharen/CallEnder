package nezharen.space.callender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

public class CallEnder extends BroadcastReceiver {

    private static final String TAG = "CallEnder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "CallEnder onReceive.");
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

        } else {
            String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.v(TAG, "Incoming number detected: " + number);

                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                Log.v(TAG, "Silent mode.");
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                try {
                    Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[])null);
                    getITelephonyMethod.setAccessible(true);
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyManager, (Object[])null);
                    iTelephony.endCall();
                    Log.v(TAG, "Block success: " + number);
                } catch (Exception e) {
                    Log.e(TAG, "Block failed: " + number);
                }
            } else {
                Log.v(TAG, "Normal mode.");
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        }
    }
}
