/*
 * Created on 03.12.2014
 *
 * Copyright(c) 1995 - 2014 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.mobile.driver;

import java.util.Arrays;

/**
 * Direction in SeeTest.
 * <p/>
 * Useful for swiping. Note the unclear semantics of the direction:
 * Does swiping left mean the finger goes left, or the swipe reveals the left side? SeeTest uses the latter, i find the first more
 * intuitive.
 *
 * @link https://docs.experitest.com/display/public/SA/Swipe
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    INSIDE;

    @Override
    public String toString() {
        String name = this.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public static Direction getByOffset(int offsetX, int offsetY) {
        if (offsetX < 0) {
            return RIGHT;
        }
        if (offsetX > 0) {
            return LEFT;
        }
        if (offsetY < 0) {
            return DOWN;
        }
        if (offsetY > 0) {
            return UP;
        }
        throw new IllegalArgumentException("Offset [" + offsetX + ", " + offsetY + "] not valid for Direction enum. Values: " + Arrays.toString(values()));
    }

    public static Direction fromString(String directionString) {
        for (Direction directionEnum : values()) {
            if (directionString.equalsIgnoreCase(directionEnum.name())) {
                return directionEnum;
            }
        }
        throw new IllegalArgumentException(directionString + " not valid for Direction enum. Values: " + Arrays.toString(values()));
    }

    public static Direction random() {
        return values()[Math.max(3, (int) (Math.random() * 4))];
    }

    public static Direction randomVertical() {
        return Math.random() > 0.5d ? UP : DOWN;
    }

    public static Direction randomHorizontal() {
        return Math.random() > 0.5d ? LEFT : RIGHT;
    }

    public boolean isVertical() {
        return this == DOWN || this == UP;
    }
}