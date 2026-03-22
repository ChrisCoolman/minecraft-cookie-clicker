package com.cookie;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// This manages the input for the golden cookies
public class GoldenCookie {
    public List<Integer> sequence = new ArrayList<>();
    public int index = 0;
    public boolean active = false;
    private final Random rand = new Random();

    // up left down right
    private static final int[] KEYS = {GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D};

    public void startNew(int length) {
        sequence.clear();
        // Add random keys to sequence
        for(int i = 0; i < length; i++) {
            sequence.add(KEYS[rand.nextInt(KEYS.length)]);
        }
        index = 0;
        active = true;
    }

    public boolean input(int key) {
        // No golden cookie on screen
        if(!active){
            return false;
        }
        // go thru every key typed
        if(key == sequence.get(index)) {
            index+=1;
            // player wins
            if(index >= sequence.size()) {
                active = false;
                return true;
            }
        }
        else {
            // reset if player messes it up
            index = 0;
        }

        if(Cookie.cookieSecondsLeft <= 0) {
            active = false;
            return false;
        }
        return false;
    }
}
