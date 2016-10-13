package com.example.accelerometer.accgame;

/**
 * Created by Sayli on 10/9/2016.
 */
public class Particle {
    //coefficient of restitution
    private static final float COR = 0.7f;

    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    //use acceleration values to calculate displacement of the particle along the X and Y axis
    public void updatePosition(float sx, float sy, float sz, long timestamp){
        float dt = (System.nanoTime()-timestamp)/1000000000.0f;

        mVelX += -sx * dt;
        mVelY += -sy * dt;

        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }

    //add logic to create a bounce effect when it collides with the boundary
    public void resolveCollisionWithBounds(float mHorizontalBounds, float mVerticalBounds){
        if (mPosX > mHorizontalBounds){
            mPosX = mHorizontalBounds;
            mVelX = -mVelX * COR;
        }
        else if (mPosX < -mHorizontalBounds){
            mPosX = -mHorizontalBounds;
            mVelX = -mVelX * COR;
        }
        if (mPosY > mVerticalBounds){
            mPosY = mVerticalBounds;
            mVelY = -mVelY * COR;
        }
        else if (mPosY < -mVerticalBounds){
            mPosY = -mVerticalBounds;
            mVelY = -mVelY * COR;
        }
    }
}
