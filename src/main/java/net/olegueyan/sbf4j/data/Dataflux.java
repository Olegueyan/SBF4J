package net.olegueyan.sbf4j.data;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public interface Dataflux
{
    @NotNull MDArray trainEntry(int a, int b);
    @NotNull MDArray trainTruth(int a, int b);
    @NotNull MDArray testEntry(int a, int b);
    @NotNull MDArray testTruth(int a, int b);
    int trainSize();
    int testSize();
}