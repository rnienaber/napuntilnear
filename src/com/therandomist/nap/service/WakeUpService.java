package com.therandomist.nap.service;

import android.content.Context;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;

public class WakeUpService {

    final AsyncPlayer ringtonePlayer = new AsyncPlayer("NAP_UNTIL_NEAR");
    final Uri ringtoneUri = Settings.System.DEFAULT_RINGTONE_URI;

    Vibrator vibrator;
    Context applicationContext;

    public WakeUpService(Context applicationContext, Vibrator vibrator) {
        this.applicationContext = applicationContext;
        this.vibrator = vibrator;
    }

    public void shush(){
        ringtonePlayer.stop();
        vibrator.cancel();
    }

    public void wakeMeUp(){
        vibrator.vibrate(500000);
        ringtonePlayer.play(applicationContext, ringtoneUri, true, AudioManager.STREAM_RING);
    }
}
