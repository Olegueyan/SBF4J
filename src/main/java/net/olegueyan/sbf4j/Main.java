package net.olegueyan.sbf4j;

import net.olegueyan.sbf4j.architecture.NeuralNetwork;
import net.olegueyan.sbf4j.impulse.SigmoidImpulse;
import net.olegueyan.sbf4j.layer.ActivationLayer;
import net.olegueyan.sbf4j.layer.DenseLayer;
import net.olegueyan.sbf4j.logger.Sbf4jFormatter;
import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.minimizer.BinaryCrossEntropyMinimizer;
import net.olegueyan.sbf4j.minimizer.MeanSquaredErrorMinimizer;
import net.olegueyan.sbf4j.normalizer.BatchNormalizer;
import net.olegueyan.sbf4j.optimizer.GradientDescentOptimizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    public static final Logger logger = configurateMainLogger();

    public static void main(String[] args)
    {
        MDArray w1 = new MDArray(new double[][]{{0, 0}, {1, 1}});
        MDArray w2 = new MDArray(new double[][]{{0}, {1}});

        MDArray b1 = new MDArray(new double[][]{{0, 0}});
        MDArray b2 = new MDArray(new double[][]{{0}});

        NeuralNetwork nn = new NeuralNetwork(new LinkedList<>()
        {{
            add(new DenseLayer(w1, b1));
            add(new ActivationLayer(new SigmoidImpulse()));
            add(new DenseLayer(w2, b2));
            add(new ActivationLayer(new SigmoidImpulse()));
        }}, new BinaryCrossEntropyMinimizer());

        MDArray entry = new MDArray(new double[][]{{2, 3}, {9, 2}, {1, 3}, {3, 8}});
        MDArray truth = new MDArray(new double[][]{{0}, {1}, {0}, {1}});

        // MDArray a1 = new MDArray(new double[][]{{2, 3, 4}, {1, 9, 2}});
        // MDArray a2 = new MDArray(new double[][]{{9, 1, 2}, {1, 0, 2}});

        MDArray a1 = new MDArray(2, 2, 3);
        a1.set(1, 0, 0, 0);
        a1.set(2, 0, 0, 1);
        a1.set(1, 0, 0, 2);
        a1.set(9, 0, 1, 0);
        a1.set(3.2, 0, 1, 1);
        a1.set(4, 0, 1, 2);
        a1.set(2.1, 1, 0, 0);
        a1.set(98, 1, 0, 1);
        a1.set(4.8, 1, 0, 2);
        a1.set(2.3, 1, 1, 0);
        a1.set(8.7, 1, 1, 1);
        a1.set(3, 1, 1, 2);
        MDArray a2 = new MDArray(new double[]{1, 2, 3});

        System.out.println(nn.forward(entry));
        nn.train(entry, truth, 0.25, new GradientDescentOptimizer(), 1, 1);
        System.out.println(nn.forward(entry));

        System.out.println(new BatchNormalizer().normalize(new MDArray(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}})));
    }

    private static Logger configurateMainLogger()
    {
        try
        {
            Logger logger = Logger.getLogger(Main.class.getName());
            logger.setUseParentHandlers(false);
            Sbf4jFormatter formatter = new Sbf4jFormatter();
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            logger.setLevel(Level.ALL);
            consoleHandler.setLevel(Level.ALL);
            logger.addHandler(consoleHandler);
            return logger;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}