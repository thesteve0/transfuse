package org.androidtransfuse.integrationTest.saveState;

import android.os.AsyncTask;
import android.os.SystemClock;
import org.androidtransfuse.event.EventManager;

import javax.inject.Inject;

public class BackgroundAsyncTask extends AsyncTask<Object, Integer, Object> {

    private static final int SLEEP_STEPS = 100;
    private static final int SLEEP_TIME = 200;

    @Inject
    private EventManager eventManager;
    private boolean running = false;
    private boolean paused = true;

    @Override
    protected Object doInBackground(Object... params) {
        int progress = 0;
        running = true;
        setPaused(false);
        while (progress < SLEEP_STEPS) {
            if(!paused){
                progress++;
                eventManager.trigger(new ProgressEvent(progress));
            }
            SystemClock.sleep(SLEEP_TIME);
            if(isCancelled()){
                return null;
            }
        }
        eventManager.trigger(new ResetEvent());
        running = false;
        setPaused(true);
        return null;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        eventManager.trigger(new PauseEvent());
    }
}