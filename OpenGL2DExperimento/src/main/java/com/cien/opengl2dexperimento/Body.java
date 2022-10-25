/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cien.opengl2dexperimento;

/**
 *
 * @author Cien
 */
public class Body extends AABB {

    private boolean staticBody = true;
    private float speedX = 0f;
    private float speedY = 0f;
    
    public Body() {
        
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }
    
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public boolean isStaticBody() {
        return staticBody;
    }

    public void setStaticBody(boolean staticBody) {
        this.staticBody = staticBody;
    }
    
    public void update() {
        
    }
    
    public void willCollideWith(Body body, boolean xAxis) {
        if (this.staticBody) {
            return;
        }
        
        if (xAxis) {
            this.speedX = 0f;
        } else {
            this.speedY = 0f;
        }
    }
    
    public void move() {
        if (this.staticBody) {
            return;
        }
        
        super.setX(super.getX() + (this.speedX * Game.TPF));
        super.setY(super.getY() + (this.speedY * Game.TPF));
    }
    
    public void render() {
        
    }
    
}
