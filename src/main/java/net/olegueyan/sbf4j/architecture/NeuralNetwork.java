package net.olegueyan.sbf4j.architecture;

import net.olegueyan.sbf4j.Main;
import net.olegueyan.sbf4j.data.Dataflux;
import net.olegueyan.sbf4j.data.Dataset;
import net.olegueyan.sbf4j.layer.Layer;
import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.minimizer.AbstractMinimizer;
import net.olegueyan.sbf4j.optimizer.AbstractOptimizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class NeuralNetwork implements ITrainable
{
    private final LinkedList<Layer> layers;
    private final AbstractMinimizer minimizer;

    public NeuralNetwork(LinkedList<Layer> layers, AbstractMinimizer minimizer)
    {
        this.layers = layers;
        this.minimizer = minimizer;
    }

    public MDArray forward(MDArray entry)
    {
        MDArray out = entry;
        for (Layer layer : layers)
        {
            out = layer.forward(out);
        }
        return out;
    }

    @Override
    public void train(Dataflux dataflux, double learningRate, AbstractOptimizer optimizer, int batch, int epoch)
    {

    }

    @Override
    public void train(Dataset dataset, double learningRate, AbstractOptimizer optimizer, int batch, int epoch)
    {
        MDArray entry = dataset.trainEntry();
        MDArray truth = dataset.trainTruth();
        ArrayList<MDArray> splitEntry = entry.split(batch);
        ArrayList<MDArray> splitTruth = truth.split(batch);
        for (var i = 0; i < epoch; i++)
        {
            Main.logger.info("[Epochs] -> " + (i + 1) + "/" + epoch);
            for (var j = 0; j < batch; j++)
            {
                MDArray output = this.forward(splitEntry.get(j));
                MDArray gradient = this.minimizer.minimize(output, splitTruth.get(j));
                Collections.reverse(layers);
                for (Layer layer : layers)
                {
                    gradient = layer.backward(gradient, optimizer, learningRate);
                }
                Collections.reverse(layers);
                Main.logger.info("[Batch] -> " + (j + 1) + "/" + batch);
            }
            double loss = this.minimizer.loss(this.forward(entry), truth);
            String formattedNumber = String.format("%.20f", loss);
            Main.logger.info("Log Loss : " + formattedNumber);
        }
    }

    @Override
    public void train(MDArray entry, MDArray truth, double learningRate, AbstractOptimizer optimizer, int batch, int epoch)
    {
        ArrayList<MDArray> splitEntry = entry.split(batch);
        ArrayList<MDArray> splitTruth = truth.split(batch);
        for (var i = 0; i < epoch; i++)
        {
            Main.logger.info("[Epochs] -> " + (i + 1) + "/" + epoch);
            for (var j = 0; j < batch; j++)
            {
                MDArray output = this.forward(splitEntry.get(j));
                MDArray gradient = this.minimizer.minimize(output, splitTruth.get(j));
                Collections.reverse(layers);
                for (Layer layer : layers)
                {
                    gradient = layer.backward(gradient, optimizer, learningRate);
                }
                Collections.reverse(layers);
                Main.logger.info("[Batch] -> " + (j + 1) + "/" + batch);
            }
            double loss = this.minimizer.loss(this.forward(entry), truth);
            String formattedNumber = String.format("%.20f", loss);
            Main.logger.info("Log Loss : " + formattedNumber);
        }
    }

    public void validate()
    {

    }

    public static class Builder
    {
        // private final NeuralNetConfig neuralNetConfig;

        private final LinkedList<Layer> layers;
        private AbstractMinimizer minimizer;

        public Builder()
        {
            this.layers = new LinkedList<>();
        }

        public Builder add(Layer layer)
        {
            this.layers.add(layer);
            return this;
        }

        public Builder withMinimizer(AbstractMinimizer minimizer)
        {
            this.minimizer = minimizer;
            return this;
        }

        public NeuralNetwork build()
        {
            if (minimizer == null)
            {
                throw new RuntimeException("The minimizer is missing !");
            }
            return new NeuralNetwork(this.layers, minimizer);
        }
    }
}