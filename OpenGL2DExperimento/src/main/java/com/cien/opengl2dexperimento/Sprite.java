/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cien.opengl2dexperimento;

import static org.lwjgl.opengl.GL33C.*;

/**
 *
 * @author Cien
 */
public class Sprite extends Body {

    public static final float GRAVITY = 800f;
    
    private int texture = 0;
    
    public Sprite() {
        
    }

    public void setTexture(int texture) {
        this.texture = texture;
        
        if (this.texture == 0) {
            return;
        }
        
        glBindTexture(GL_TEXTURE_2D, texture);
        
        int[] width = {0};
        glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH, width);
        
        int[] height = {0};
        glGetTexLevelParameteriv(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT, height);
        
        super.setWidth(width[0]);
        super.setHeight(height[0]);
    }
    
    public int getTexture() {
        return texture;
    }

    @Override
    public void update() {
        if (isStaticBody()) {
            return;
        }
        
        super.setSpeedY(super.getSpeedY() + (GRAVITY * Game.TPF * -1f));
    }

    @Override
    public void render() {
        if (this.texture == 0) {
            return;
        }
        
        SpriteRenderer.render(
                this.texture,
                (int) Math.floor(super.getX()),
                (int) Math.floor(super.getY())
        );
    }
    
}
