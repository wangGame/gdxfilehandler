package com.musiceffect;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;

public class MusicVolumIn extends Action {

    private Music target;
    private boolean complete;
    private boolean began;
    private float end;
    private float time;
    private float duration;
    private Interpolation interpolation;
    private boolean reverse;
    private float start;

    public MusicVolumIn(Music sound){
        this.target = sound;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void begin(){
        target.play();
        target.setVolume(0);
        began = true;
        start = 0;
        end =1;
    }

    public void reset () {
        super.reset();
        target.play();
        target.setVolume(0);
        began = true;
        start = 0;
        end = 1;
    }

    @Override
    public boolean act(float delta) {
        if (complete) return true;
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            if (!began) {
                begin();
                began = true;
            }
            time += delta;
            complete = time >= duration;
            float percent;
            if (complete)
                percent = 1;
            else {
                percent = time / duration;
                if (interpolation != null) percent = interpolation.apply(percent);
            }
            update(reverse ? 1 - percent : percent);
            if (complete) end();
            return complete;
        } finally {
            setPool(pool);
        }
    }

    public void end(){

    }

    public void update(float v){
        target.setVolume(start + (end - start) * v);
    }
}
