package net.olegueyan.sbf4j.impulse;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public abstract class AbstractImpulse implements Serializable
{
    @Serial
    private static final long serialVersionUID = 99L;

    public abstract String reference();
    public abstract void forward(@NotNull MDArray array);
    public abstract void backward(@NotNull MDArray array);
}