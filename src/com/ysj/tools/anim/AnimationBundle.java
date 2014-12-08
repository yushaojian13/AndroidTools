package com.ysj.tools.anim;

import java.util.ArrayList;

public class AnimationBundle extends ArrayList<Tweener> {
    private static final long serialVersionUID = 0xA84D78726F127468L;
    private boolean mSuspended;

    public void start() {
        if (mSuspended) return; // ignore attempts to start animations
        final int count = size();
        for (int i = 0; i < count; i++) {
            Tweener anim = get(i);
            anim.animator.start();
        }
    }

    public void cancel() {
        final int count = size();
        for (int i = 0; i < count; i++) {
            Tweener anim = get(i);
            anim.animator.cancel();
        }
        clear();
    }

    public void stop() {
        final int count = size();
        for (int i = 0; i < count; i++) {
            Tweener anim = get(i);
            anim.animator.end();
        }
        clear();
    }

    public void setSuspended(boolean suspend) {
        mSuspended = suspend;
    }
}
