package com.souneffect;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;

public class SoundVolumIn extends Action {
    private long targetId;
    private Sound target;
    private boolean complete;
    private boolean began;
    private float end;
    private float time;
    private float duration;
    private Interpolation interpolation;
    private boolean reverse;
    private float start;

    public SoundVolumIn(Sound sound){
        this.target = sound;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void begin(){
        targetId = target.play();
        target.setVolume(targetId,0);
        began = true;
        start = 0;
        end =1;
    }

    public void reset () {
        super.reset();
        targetId = target.play();
        target.setVolume(targetId,0);
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
        targetId = -1;
    }

    public void update(float v){
        if (targetId != -1) {
            target.setVolume(targetId, start + (end - start) * v);
        }
    }
}
