package bean;

import android.animation.Animator;

import java.util.ArrayList;

public class LyricsProgressDetailsBean{
    public ArrayList<Animator> getAnimatorArrayList() {
        return animatorArrayList;
    }

    public void setAnimatorArrayList(ArrayList<Animator> animatorArrayList) {
        this.animatorArrayList = animatorArrayList;
    }

    private ArrayList<Animator> animatorArrayList;

        public long getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(long waitTime) {
            this.waitTime = waitTime;
        }

        private long waitTime;
    }