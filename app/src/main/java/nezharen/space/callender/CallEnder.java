package nezharen.space.callender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallEnder extends BroadcastReceiver {

    private static final String TAG = "CallEnder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "CallEnder onReceive.");
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

        } else {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                if (!intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
                    return;
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.v(TAG, "Incoming number detected: " + number);
                if (DataBase.getInstance(context.getApplicationContext()).matchNumber(number)) {
                    try {
                        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                        telecomManager.endCall();
                        Log.v(TAG, "Block success: " + number);
                    } catch (SecurityException se) {
                        Log.e(TAG, "Block failed: " + number + "due to permission.");
                    } catch (Exception e) {
                        Log.e(TAG, "Block failed: " + number);
                        Log.d(TAG, e.getMessage());
                    }
                } else {
                    Log.v(TAG, "Pass: " + number);
                }
            } else {

            }
        }
    }
}
