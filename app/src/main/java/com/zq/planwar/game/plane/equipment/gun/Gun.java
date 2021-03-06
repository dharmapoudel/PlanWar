package com.zq.planwar.game.plane.equipment.gun;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.zq.planwar.appearance.Appearance;
import com.zq.planwar.appearance.decorator.Decorator;
import com.zq.planwar.core.context.GameContext;
import com.zq.planwar.ext.IBounds;
import com.zq.planwar.ext.ILocation;
import com.zq.planwar.game.plane.equipment.Equipment;
import com.zq.planwar.game.plane.equipment.bullet.Bullet;
import com.zq.planwar.game.plane.equipment.bullet.BulletFactory;
import com.zq.planwar.game.plane.property.CollingTime;
import com.zq.planwar.game.plane.property.Level;
import com.zq.planwar.role.Role;
import com.zq.planwar.utils.Logger;
import com.zq.planwar.utils.RectFUtils;

/**
 * Created by zhangqiang on 2018/9/13.
 */
public class Gun extends Equipment implements ILocation, IBounds {

    private static final int INVALID_DATA = -1;

    private BulletFactory bulletFactory;
    private long lastShootTime = INVALID_DATA;
    private Level level;
    private CollingTime collingTime;
    private int minBulletCount = 1;
    private int maxBulletCount = 5;
    private int minBulletVelocity = 500;
    private int maxBulletVelocity = 1000;
    private Decorator[] decorators;

    public Gun(Decorator[] decorators,BulletFactory bulletFactory, Level level,  CollingTime collingTime) {
        super(null);
        this.decorators = decorators;
        this.bulletFactory = bulletFactory;
        this.level = level;
        this.collingTime = collingTime;
    }

    @Override
    protected void onDraw(Canvas canvas, GameContext gameContext) {
        super.onDraw(canvas, gameContext);
        if (bulletFactory != null) {
            long pastTime = gameContext.getPastTime();
            if (lastShootTime == INVALID_DATA) {

                shoot();
                lastShootTime = pastTime;
            } else {

                long currentCollingTime = getCurrentCollingTime();
                if (pastTime - lastShootTime > currentCollingTime) {

                    shoot();
                    lastShootTime = pastTime;
                }
            }
        }
        if (decorators != null) {
            for (Decorator decorator : decorators) {
                decorator.draw(canvas,gameContext,getBounds());
            }
        }
    }

    private void shoot() {

        final int bulletCount = getCurrentBulletCount();

        Bullet[] bullets = new Bullet[bulletCount];
        for (int i = 0; i < bulletCount; i++) {
            bullets[i] = bulletFactory.createBullet(this, bulletCount, i);
        }
        addChild(bullets);
        Logger.i(Gun.class, "shoot");
    }

    public float getShootX() {

        return getLocationX();
    }

    public float getShootY() {

        return getLocationY();
    }

    @Override
    public float getLocationX() {
        Role parentRole = getParentRole();
        if (parentRole instanceof ILocation) {
            return ((ILocation) parentRole).getLocationX();
        }
        return 0;
    }

    @Override
    public float getLocationY() {
        Role parentRole = getParentRole();
        if (parentRole instanceof ILocation) {
            return ((ILocation) parentRole).getLocationY();
        }
        return 0;
    }

    @NonNull
    @Override
    public RectF getBounds() {
        Role parentRole = getParentRole();
        if (parentRole instanceof IBounds) {
            return ((IBounds) parentRole).getBounds();
        }
        return RectFUtils.EMPTY;
    }

    private float getFactor() {
        return level.getFactor();
    }

    public long getCurrentCollingTime() {
        return collingTime.getCurrentCollingTime(getFactor());
    }

    public int getCurrentBulletCount() {

        return Math.max(minBulletCount, Math.round((maxBulletCount - minBulletCount) * getFactor() + minBulletCount));
    }

    public int getCurrentBulletVelocity(){
        return Math.max(minBulletVelocity,Math.round((maxBulletVelocity - minBulletVelocity) * getFactor()));
    }

    public Level getLevel() {
        return level;
    }

    public int getMinBulletCount() {
        return minBulletCount;
    }

    public void setMinBulletCount(int minBulletCount) {
        this.minBulletCount = minBulletCount;
    }

    public int getMaxBulletCount() {
        return maxBulletCount;
    }

    public void setMaxBulletCount(int maxBulletCount) {
        this.maxBulletCount = maxBulletCount;
    }

    public int getMinBulletVelocity() {
        return minBulletVelocity;
    }

    public void setMinBulletVelocity(int minBulletVelocity) {
        this.minBulletVelocity = minBulletVelocity;
    }

    public int getMaxBulletVelocity() {
        return maxBulletVelocity;
    }

    public void setMaxBulletVelocity(int maxBulletVelocity) {
        this.maxBulletVelocity = maxBulletVelocity;
    }
}
