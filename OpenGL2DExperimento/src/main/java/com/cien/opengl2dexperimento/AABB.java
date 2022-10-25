/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cien.opengl2dexperimento;

/**
 *
 * @author Cien
 */
public class AABB {
    
    private float width;
    private float height;
    private float x;
    private float y;
    
    public AABB() {
        
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public boolean inCollision(AABB other) {
        float thisMinX = this.x;
        float thisMinY = this.y;
        float thisMaxX = this.x + this.width;
        float thisMaxY = this.y + this.height;
        
        float otherMinX = other.x;
        float otherMinY = other.y;
        float otherMaxX = other.x + other.width;
        float otherMaxY = other.y + other.height;
        
        return thisMinX <= otherMaxX &&
                thisMaxX >= otherMinX &&
                thisMinY <= otherMaxY &&
                thisMaxY >= otherMinY;
    }
}
