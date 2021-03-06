package com.example.serba.snookertracker_1856482.models;

import java.io.Serializable;

/**
 * Created by serba on 03/02/2018.
 */

public abstract class APlayer implements Serializable {
    protected String name;
    protected int score;

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws EmptyValueException {
        if (name.isEmpty()) {
            throw new EmptyValueException("Name cannot be empty");
        }
    }

    public abstract void increaseScore(int amount);

    public abstract void resetScore();

    public abstract int getScore();

    public abstract APlayer getNextPlayer();

    @Override
    public String toString() {
        return "APlayer{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
