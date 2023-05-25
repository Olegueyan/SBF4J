package net.olegueyan.sbf4j.layer;

import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.optimizer.AbstractOptimizer;
import net.olegueyan.sbf4j.weight.AbstractWeightInit;
import org.jetbrains.annotations.NotNull;

public class DenseLayer implements Layer
{
    private final int fanIn;
    private final int fanOut;

    private MDArray weight;
    private MDArray bias;

    private MDArray input;

    public DenseLayer(int fanIn, int fanOut)
    {
        this.fanIn = fanIn;
        this.fanOut = fanOut;
    }

    public DenseLayer(MDArray weight, MDArray bias)
    {
        if (weight.getDimension() != 2 || bias.getDimension() != 2)
        {
            throw new RuntimeException("Invalid weight or bias MDArray | Need 2D Array !");
        }
        this.fanIn = weight.getShape()[0];
        this.fanOut = weight.getShape()[1];
        this.weight = weight;
        this.bias = bias;
    }

    public static DenseLayer create(int fanIn, int fanOut, AbstractWeightInit weightInit)
    {
        MDArray weight = weightInit.initiate(fanIn, fanOut);
        MDArray bias = weightInit.initiate(1, fanOut);
        return new DenseLayer(weight, bias);
    }

    @Override
    public MDArray forward(@NotNull MDArray entry)
    {
        this.input = entry;
        return entry.dot(this.weight).eachLineBroadcast(this.bias.flatten());
    }

    @Override
    public MDArray backward(@NotNull MDArray gradient, AbstractOptimizer optimizer, double learningRate)
    {
        MDArray inputErr = gradient.dot(this.weight.transpose());
        MDArray weightErr = this.input.transpose().dot(gradient);

        this.weight = this.weight.sub(weightErr.mul(learningRate));
        this.bias = this.bias.sub(gradient.mul(learningRate));

        return inputErr;
    }

    public int getFanIn()
    {
        return this.fanIn;
    }

    public int getFanOut()
    {
        return this.fanOut;
    }
}