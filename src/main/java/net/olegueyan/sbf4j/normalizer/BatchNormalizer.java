package net.olegueyan.sbf4j.normalizer;

import net.olegueyan.sbf4j.math.MDArray;
import org.jetbrains.annotations.NotNull;

public class BatchNormalizer extends AbstractNormalizer
{
    @Override
    public String reference()
    {
        return "batch-n";
    }

    /*
    def batch_normalization(X, epsilon=1e-8):
        # Calculer la moyenne et la variance sur l'ensemble du batch
    mean = np.mean(X, axis=0)
    variance = np.var(X, axis=0)

        # Normaliser les données
        X_normalized = (X - mean) / np.sqrt(variance + epsilon)

    return X_normalized  */

    @Override
    public MDArray normalize(@NotNull MDArray mdArray)
    {
        double mean = mdArray.mean();
        System.out.println(mdArray.std());
        return null;
    }
}