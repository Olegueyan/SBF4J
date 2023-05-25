package net.olegueyan.sbf4j.brain;

import net.olegueyan.sbf4j.architecture.NeuralNetwork;

import java.util.Queue;

public interface IBrain
{
    Queue<NeuralNetwork> neuralSequence();
}