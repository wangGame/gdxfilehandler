package com.musiceffect;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;

public class MusicVolumOut extends Action {
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
    
    public MusicVolumOut(Sound sound){
        this.target = sound;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void begin(){
        targetId = target.play();
        target.setVolume(targetId,0);
        began = true;
        start = 1;
        end = 0;
    }

    public void reset () {
        super.reset();
        targetId = target.play();
        target.setVolume(targetId,0);
        began = true;
        start = 1;
        end = 0;
    }
    
    @Override
    public boolean act(float delta) {
        if (complete) return true;
        try {
            if (!began) {
                begin();
                began = true;
            }
            time += delta;
            complete = time >= duration;
            float percent =0;
            if (complete)
                percent = 1;
            else {
                if (time>end - duration)
                percent = time / duration;
                if (interpolation != null) percent = interpolation.apply(percent);
            }
            update(reverse ? 1 - percent : percent);
            if (complete) end();
            return complete;
        } finally {
      
        }
    }
    
    public void end(){
        targetId = -1;
    }
    
    public void update(float v){
        if (targetId != -1) {
            target.setVolume(targetId, start - (start - end) * v);
        }
    }
}
