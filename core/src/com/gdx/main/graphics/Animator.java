package com.gdx.main.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Animator {
	private Texture[] frames;
	private int currentFrame, totalFrames, duration, delay;
	private boolean playing, looping, current;
	
	public Animator(String folder, int count, int delay, boolean playing, boolean loop) {
		totalFrames = count;
		frames = new Texture[totalFrames];
		this.delay = delay;
		currentFrame = 0;
		duration = 0;
		this.playing = playing;
		looping = loop;
		current = false;
		
		for(int i = 0; i < frames.length; ++i) {
			frames[i] = new Texture(Gdx.files.internal(folder + "/" + i + ".png"), true);
			frames[i].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		}
	}
	
	public void start() {
        if (frames.length != 0) playing = true;
    }

    public void stop() {
        if (frames.length != 0) playing = false;
    }

    public void restart() {
        if (frames.length != 0 && !current) {
            currentFrame = 0;
            playing = true;
        } 
    }
    
    public void reset() {
        current = false;
    }
    
    public void setFrame(int frame) {
        currentFrame = frame;
    }
	
	public Texture getSprite() {
        return frames[currentFrame];
    }

    public void update() {
        if(playing) {
            ++duration;
            
            if (duration > delay) {
            	duration = 0;
                ++currentFrame;
                if (currentFrame > totalFrames - 1) {
                	if(looping)
                		currentFrame = 0;
                	else
                		currentFrame = totalFrames - 1;
                	
                	playing = looping;
                }
            }
        }

    	current = true;
    }
    
    public void dispose() {
    	for(int i = 0; i < frames.length; ++i)
			frames[i].dispose();
    }
}
