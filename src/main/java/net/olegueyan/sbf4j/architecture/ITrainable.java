package net.olegueyan.sbf4j.architecture;

import net.olegueyan.sbf4j.data.Dataflux;
import net.olegueyan.sbf4j.data.Dataset;
import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.optimizer.AbstractOptimizer;

public interface ITrainable
{
    void train(Dataflux dataflux, double learningRate, AbstractOptimizer optimizer, int batch, int epoch);
    void train(Dataset dataset, double learningRate, AbstractOptimizer optimizer, int batch, int epoch);
    void train(MDArray entry, MDArray truth, double learningRate, AbstractOptimizer optimizer, int batch, int epoch);
}