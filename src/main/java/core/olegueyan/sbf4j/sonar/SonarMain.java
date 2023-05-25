package core.olegueyan.sbf4j.sonar;

import net.olegueyan.sbf4j.architecture.NeuralNetwork;
import net.olegueyan.sbf4j.impulse.HyperbolicTangentImpulse;
import net.olegueyan.sbf4j.impulse.SigmoidImpulse;
import net.olegueyan.sbf4j.layer.ActivationLayer;
import net.olegueyan.sbf4j.layer.DenseLayer;
import net.olegueyan.sbf4j.math.MDArray;
import net.olegueyan.sbf4j.minimizer.BinaryCrossEntropyMinimizer;
import net.olegueyan.sbf4j.minimizer.MeanAbsoluteErrorMinimizer;
import net.olegueyan.sbf4j.optimizer.GradientDescentOptimizer;
import net.olegueyan.sbf4j.weight.XavierNormalWeightInit;
import net.olegueyan.sbf4j.weight.XavierUniformWeightInit;

public class SonarMain
{
    public static void main(String[] args)
    {
        SonarDataSet sonarDataSet = new SonarDataSet();

        NeuralNetwork neuralNetwork = new NeuralNetwork.Builder()
                .withMinimizer(new MeanAbsoluteErrorMinimizer())
                .add(DenseLayer.create(60, 120, new XavierNormalWeightInit()))
                .add(new ActivationLayer(new HyperbolicTangentImpulse()))
                .add(DenseLayer.create(120, 1, new XavierNormalWeightInit()))
                .add(new ActivationLayer(new SigmoidImpulse()))
                .build();

        MDArray predicted = neuralNetwork.forward(sonarDataSet.testEntry());
        System.out.println(predicted);
        System.out.println("---------------------------");
        System.out.println(sonarDataSet.testTruth());

        System.out.println(new MeanAbsoluteErrorMinimizer().loss(predicted, sonarDataSet.testTruth()));

        System.out.println("###########################");

        neuralNetwork.train(sonarDataSet, 0.25, new GradientDescentOptimizer(), 4, 2500);

        System.out.println("###########################");

        MDArray predicted2 = neuralNetwork.forward(sonarDataSet.testEntry());
        System.out.println(predicted2);
        System.out.println("---------------------------");
        System.out.println(sonarDataSet.testTruth());

        System.out.println(new MeanAbsoluteErrorMinimizer().loss(predicted2, sonarDataSet.testTruth()));
    }
}