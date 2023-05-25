package net.olegueyan.sbf4j.weight;

import net.olegueyan.sbf4j.math.MDArray;

import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractWeightInit implements Serializable
{
    @Serial
    private static final long serialVersionUID = 78L;

    public abstract String reference();
    public abstract MDArray initiate(int fanIn, int fanOut);
}