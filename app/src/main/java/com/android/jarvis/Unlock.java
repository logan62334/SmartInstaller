package com.android.jarvis;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by mafei on 2017/12/26.
 */

public class Unlock extends AppCompatActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Set window flags to unlock screen. This works on most devices by itself.
    Window window = this.getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

    // some devices needs waking up screen first before disable keyguard
    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
    PowerManager.WakeLock screenLock = powerManager.newWakeLock(
        PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,
        getLocalClassName());
    screenLock.acquire();

    if (screenLock.isHeld()) {
      screenLock.release();
    }

    // On most other devices, using the KeyguardManager + the permission in
    // AndroidManifest.xml will do the trick
    KeyguardManager mKeyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
    if (mKeyGuardManager.inKeyguardRestrictedInputMode()) {
      KeyguardManager.KeyguardLock keyguardLock =
          mKeyGuardManager.newKeyguardLock(getLocalClassName());
      keyguardLock.disableKeyguard();
    }
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    moveTaskToBack(true);
  }

  @Override
  protected void onStop() {
    super.onStop();
    finish();
  }
}

