package net.olegueyan.sbf4j.math;

import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/* Implementation of a multidimensional array */
public class MDArray implements Serializable
{
    @Serial
    private static final long serialVersionUID = 23456L;

    /*** Documentation ***/
    private int[] shape;
    private int size;
    private double[] data;

    public MDArray(int... shape)
    {
        this.shape = shape;
        this.size = this.calculateSize(this.shape);
        this.data = new double[this.size];
    }

    public MDArray(double[] array1D)
    {
        if (array1D.length == 0)
        {
            throw new IllegalArgumentException("Invalid array size");
        }

        this.shape = new int[]{array1D.length};
        this.size = this.calculateSize(this.shape);
        this.data = array1D;
    }

    public MDArray(double[][] array2D)
    {
        if (array2D.length == 0 || array2D[0].length == 0)
        {
            throw new IllegalArgumentException("Invalid array size");
        }

        int rows = array2D.length;
        int cols = array2D[0].length;

        for (int i = 1; i < array2D.length; i++)
        {
            if (array2D[i].length != cols)
            {
                throw new IllegalArgumentException("Sub-arrays must have the same length");
            }
        }

        this.shape = new int[] { rows, cols };
        this.size = this.calculateSize(this.shape);
        this.data = new double[this.size];

        // Flatten the 2D array and copy its values into this.data
        int index = 0;
        for (double[] doubles : array2D) {
            for (int j = 0; j < cols; j++) {
                this.data[index++] = doubles[j];
            }
        }
    }

    public MDArray(double[][][] array3D)
    {
        if (array3D.length == 0 || array3D[0].length == 0 || array3D[0][0].length == 0) {
            throw new IllegalArgumentException("Invalid array size");
        }

        int dim1 = array3D.length;
        int dim2 = array3D[0].length;
        int dim3 = array3D[0][0].length;

        for (int i = 1; i < array3D.length; i++) {
            if (array3D[i].length != dim2 || array3D[i][0].length != dim3) {
                throw new IllegalArgumentException("Sub-arrays must have the same dimensions");
            }
        }

        this.shape = new int[]{dim1, dim2, dim3};
        this.size = this.calculateSize(this.shape);
        this.data = new double[this.size];

        // Flatten the 3D array and copy its values into this.data
        int index = 0;
        for (double[][] doubles2D : array3D) {
            for (double[] doubles1D : doubles2D) {
                for (double value : doubles1D) {
                    this.data[index++] = value;
                }
            }
        }
    }

    public static MDArray filled(double value, int... shape)
    {
        MDArray mdArray = new MDArray(shape);
        mdArray.fill(value);
        return mdArray;
    }

    public static MDArray random(double origin, double bound, int... shape)
    {
        MDArray mdArray = new MDArray(shape);
        Random random = new Random();
        mdArray.map(value -> random.nextDouble(origin, bound));
        return mdArray;
    }

    public static MDArray zero(int... shape)
    {
        return new MDArray(shape);
    }

    public MDArray duplicate()
    {
        MDArray duplicateArray = new MDArray(this.shape);
        System.arraycopy(this.data, 0, duplicateArray.data, 0, this.size);
        return duplicateArray;
    }

    private int calculateSize(int... shape)
    {
        return IntStream.of(shape).reduce(1, (a, b) -> a * b);
    }

    public double get(int... indices)
    {
        int index = calculateIndex(indices);
        return data[index];
    }

    public void set(double value, int... indices)
    {
        int index = calculateIndex(indices);
        data[index] = value;
    }

    private int calculateIndex(int... indices)
    {
        if (indices.length != shape.length)
        {
            throw new IllegalArgumentException("Number of indices does not match the array shape.");
        }
        int index = 0;
        int stride = 1;
        for (int i = indices.length - 1; i >= 0; i--)
        {
            if (indices[i] < 0 || indices[i] >= shape[i])
            {
                throw new IndexOutOfBoundsException("Index out of bounds.");
            }
            index += indices[i] * stride;
            stride *= shape[i];
        }
        return index;
    }

    public int[] getShape()
    {
        return this.shape;
    }

    public int getSize()
    {
        return this.size;
    }

    public double[] getData()
    {
        return this.data;
    }

    public void setData(double[] data)
    {
        if (this.data.length != data.length)
        {
            throw new RuntimeException("Invalid length of data");
        }
        this.data = data;
    }

    public MDArray flatten()
    {
        return new MDArray(this.data);
    }

    public void map(DoubleFunction<Double> function)
    {
        IntStream.range(0, this.size).forEach(index -> this.data[index] = function.apply(this.data[index]));
    }

    public void foreach(Consumer<int[]> consumer)
    {
        int[] indices = new int[this.getDimension()];
        coordinateRunRecursive(this.shape, indices, 0, consumer);
    }

    private void coordinateRunRecursive(int[] shape, int[] indices, int shapeIndex, Consumer<int[]> consumer)
    {
        if (shapeIndex == shape.length)
        {
            consumer.accept(indices);
        }
        else
        {
            int dimension = shape[shapeIndex];
            for (int i = 0; i < dimension; i++) {
                indices[shapeIndex] = i;
                coordinateRunRecursive(shape, indices, shapeIndex + 1, consumer);
            }
        }
    }

    public void fill(double value)
    {
        Arrays.fill(this.data, value);
    }

    public MDArray log()
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(Math::log);
        return mdArray;
    }

    public MDArray log(double offset)
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(value -> Math.log(value + offset));
        return mdArray;
    }

    public MDArray exp()
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(Math::exp);
        return mdArray;
    }

    public MDArray exp(double offset)
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(value -> Math.exp(value + offset));
        return mdArray;
    }

    public MDArray transpose()
    {
        int[] transposedDimension = new int[this.shape.length];
        for (int i = 0; i < shape.length; i++)
        {
            transposedDimension[i] = shape[shape.length - 1 - i];
        }
        MDArray transposedArray = new MDArray(transposedDimension);
        int[] indices = new int[this.shape.length];
        int[] transposedIndices = new int[shape.length];
        for (int i = 0; i < size; i++)
        {
            calculateIndices(i, indices);
            for (int j = 0; j < shape.length; j++)
            {
                transposedIndices[j] = indices[shape.length - 1 - j];
            }
            double value = get(indices);
            transposedArray.set(value, transposedIndices);
        }
        return transposedArray;
    }

    private void calculateIndices(int index, int[] indices)
    {
        int stride = 1;
        for (int i = shape.length - 1; i >= 0; i--)
        {
            indices[i] = (index / stride) % shape[i];
            stride *= shape[i];
        }
    }

    public MDArray dot(MDArray other)
    {
        int[] thisDimension = this.getShape();
        int[] otherDimension = other.getShape();

        if (thisDimension.length == 1 && otherDimension.length == 1) {
            // Dot product of two 1-D arrays
            if (thisDimension[0] != otherDimension[0]) {
                throw new IllegalArgumentException("Array dimensions do not match for dot product.");
            }
            MDArray result = new MDArray(1);
            double dotProduct = 0;
            for (int i = 0; i < this.size; i++) {
                dotProduct += this.data[i] * other.get(i);
            }
            result.set(dotProduct, 0);
            return result;
        } else if (thisDimension.length == 2 && otherDimension.length == 2) {
            // Matrix multiplication
            if (thisDimension[1] != otherDimension[0]) {
                throw new IllegalArgumentException("Array dimensions do not match for matrix multiplication.");
            }
            int[] resultDimension = {thisDimension[0], otherDimension[1]};
            MDArray result = new MDArray(resultDimension);

            for (int i = 0; i < thisDimension[0]; i++) {
                for (int j = 0; j < otherDimension[1]; j++) {
                    double sum = 0;
                    for (int k = 0; k < thisDimension[1]; k++) {
                        sum += this.get(i, k) * other.get(k, j);
                    }
                    result.set(sum, i, j);
                }
            }
            return result;
        } else if (otherDimension.length == 1) {
            // Product of sum over the last axis of this and the 1-D array
            int lastAxis = thisDimension.length - 1;
            if (thisDimension[lastAxis] != otherDimension[0]) {
                throw new IllegalArgumentException("Array dimensions do not match for dot product.");
            }
            int[] resultDimension = new int[thisDimension.length - 1];
            System.arraycopy(thisDimension, 0, resultDimension, 0, thisDimension.length - 1);
            MDArray result = new MDArray(resultDimension);

            for (int i = 0; i < this.size; i += thisDimension[lastAxis]) {
                double sum = 0;
                for (int j = 0; j < thisDimension[lastAxis]; j++) {
                    sum += this.data[i + j] * other.get(j);
                }
                int[] indices = new int[thisDimension.length - 1];
                int factor = 1;
                for (int k = thisDimension.length - 2; k >= 0; k--) {
                    indices[k] = (i / factor) % thisDimension[k];
                    factor *= thisDimension[k];
                }
                result.set(sum, indices);
            }
            return result;
        } else if (thisDimension.length >= 1 && otherDimension.length >= 2) {
            // Product of sum over the last axis of this and the second-last axis of other
            int lastAxisThis = thisDimension.length - 1;
            int lastAxisOther = otherDimension.length - 1;
            if (thisDimension[lastAxisThis] != otherDimension[lastAxisOther - 1]) {
                throw new IllegalArgumentException("Array dimensions do not match for dot product.");
            }
            int[] resultDimension = new int[thisDimension.length - 1 + otherDimension.length - 2];
            System.arraycopy(thisDimension, 0, resultDimension, 0, thisDimension.length - 1);
            System.arraycopy(otherDimension, 0, resultDimension, thisDimension.length - 1, otherDimension.length - 2);
            MDArray result = new MDArray(resultDimension);

            int[] indicesThis = new int[thisDimension.length - 1];
            int[] indicesOther = new int[otherDimension.length - 2];
            int factorThis = 1;
            int factorOther = 1;
            for (int k = thisDimension.length - 2; k >= 0; k--) {
                indicesThis[k] = (this.size / factorThis) % thisDimension[k];
                factorThis *= thisDimension[k];
            }
            for (int k = otherDimension.length - 3; k >= 0; k--) {
                indicesOther[k] = (other.getSize() / factorOther) % otherDimension[k];
                factorOther *= otherDimension[k];
            }

            for (int i = 0; i < this.size; i += thisDimension[lastAxisThis]) {
                for (int j = 0; j < other.getSize(); j += otherDimension[lastAxisOther - 1]) {
                    double sum = 0;
                    for (int k = 0; k < thisDimension[lastAxisThis]; k++) {
                        System.out.println(Arrays.toString(indicesOther));
                        //sum += this.data[i + k] * other.get(indicesOther, j + k);
                    }
                    int[] indices = new int[resultDimension.length];
                    System.arraycopy(indicesThis, 0, indices, 0, indicesThis.length);
                    System.arraycopy(indicesOther, 0, indices, indicesThis.length, indicesOther.length);
                    result.set(sum, indices);
                }
            }
            return result;
        } else
        {
            throw new IllegalArgumentException("Invalid dot product operation.");
        }
    }

    public MDArray add(MDArray other)
    {
        if (this.getDimension() != other.getDimension())
        {
            throw new RuntimeException("The MDArrays need to have the same dimension !");
        }
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(this.get(ints) + other.get(ints), ints));
        return out;
    }

    public MDArray add(double number)
    {
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(out.get(ints) + number, ints));
        return out;
    }

    public MDArray eachLineBroadcast(MDArray mdArray)
    {
        if (mdArray.getDimension() != 1)
        {
            throw new RuntimeException("Must be a 1D array !");
        }
        if (this.shape[this.getDimension() - 1] != mdArray.shape[0])
        {
            throw new RuntimeException("Invalid shape !");
        }
        MDArray output = this.duplicate();
        output.foreach(ints ->
        {
            double valA = output.get(ints);
            double valB = mdArray.get(ints[output.getDimension() - 1]);
            output.set(valA + valB, ints);
        });
        return output;
    }

    public MDArray sub(MDArray other)
    {
        if (this.getDimension() != other.getDimension())
        {
            throw new RuntimeException("The MDArrays need to have the same dimension !");
        }
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(this.get(ints) - other.get(ints), ints));
        return out;
    }

    public MDArray sub(double number)
    {
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(out.get(ints) - number, ints));
        return out;
    }

    public MDArray div(MDArray other)
    {
        if (this.getDimension() != other.getDimension())
        {
            throw new RuntimeException("The MDArrays need to have the sambe dimension !");
        }
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(this.get(ints) / other.get(ints), ints));
        return out;
    }

    public MDArray div(double number)
    {
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(out.get(ints) / number, ints));
        return out;
    }

    public MDArray mul(MDArray other)
    {
        if (this.getDimension() != other.getDimension())
        {
            throw new RuntimeException("The MDArrays need to have the sambe dimension !");
        }
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(this.get(ints) * other.get(ints), ints));
        return out;
    }

    public MDArray mul(double number)
    {
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(out.get(ints) * number, ints));
        return out;
    }

    public MDArray pow(double power)
    {
        MDArray out = this.duplicate();
        out.foreach(ints -> out.set(Math.pow(this.get(ints), power), ints));
        return out;
    }

    @Serial
    private void writeObject(@NotNull ObjectOutputStream out) throws Exception
    {
        out.defaultWriteObject();
        out.writeObject(this.shape);
        out.writeInt(this.size);
        out.writeObject(this.shape);
    }

    @Serial
    private void readObject(@NotNull ObjectInputStream in) throws Exception
    {
        in.defaultReadObject();
        this.shape = (int[]) in.readObject();
        this.size = in.readInt();
        this.data = (double[]) in.readObject();
    }

    public double sum()
    {
        return DoubleStream.of(this.data).sum();
    }

    public double min()
    {
        return DoubleStream.of(this.data).min().orElseThrow();
    }

    public double max()
    {
        return DoubleStream.of(this.data).max().orElseThrow();
    }

    public boolean hasUncountable()
    {
        return Arrays.stream(data).anyMatch(Double::isNaN) || Arrays.stream(data).anyMatch(Double::isInfinite);
    }

    public double mean()
    {
        return this.sum() / this.size;
    }

    public double std()
    {
        double mean = this.mean();
        double sumSquaredDiff = Arrays.stream(data)
                .map(d -> Math.pow(d - mean, 2))
                .sum();
        return Math.sqrt(sumSquaredDiff / size);
    }

    public MDArray absolute()
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(Math::abs);
        return mdArray;
    }

    public MDArray negative()
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(value -> - value);
        return mdArray;
    }

    @SuppressWarnings("all")
    public MDArray clip(double min, double max)
    {
        MDArray mdArray = this.duplicate();
        mdArray.map(value ->
        {
            if (value < min)
            {
                return min;
            }
            else if (value > max)
            {
                return max;
            }
            else
            {
                return value;
            }
        });
        return mdArray;
    }

    public MDArray underExtract(int pos)
    {
        if (this.getDimension() == 1)
        {
            throw new IllegalArgumentException("Cannot extract a subArray from a 1D array !");
        }
        if (pos >= this.shape[0])
        {
            throw new IllegalArgumentException("Invalid sub array index !");
        }
        int[] newShape = new int[this.getDimension() - 1];
        System.arraycopy(this.shape, 1, newShape, 0, this.getDimension() - 1);
        MDArray output = new MDArray(newShape);
        this.coordinateRunRecursive(newShape, new int[newShape.length], 0, ints ->
        {
            int[] indices = concatenateArrays(new int[]{pos}, ints);
            output.set(this.get(indices), ints);
        });
        return output;
    }

    public MDArray underReplace(int pos, MDArray subArray)
    {
        if (pos >= this.shape[0])
        {
            throw new IllegalArgumentException("Invalid sub array index !");
        }
        int[] newShape = new int[this.getDimension() - 1];
        System.arraycopy(this.shape, 1, newShape, 0, this.getDimension() - 1);
        if (!Arrays.equals(newShape, subArray.getShape()))
        {
            throw new IllegalArgumentException("Invalid sub array shape !");
        }
        MDArray output = this.duplicate();
        subArray.foreach(ints ->
        {
            int[] indices = concatenateArrays(new int[]{pos}, ints);
            output.set(subArray.get(ints), indices);
        });
        return output;
    }

    public void fragment(int targetDim, Consumer<MDArray> consumer)
    {
        if (targetDim == 0 || targetDim > this.getDimension())
        {
            throw new IllegalArgumentException("Invalid target shape.");
        }
        if (targetDim == this.getDimension())
        {
            consumer.accept(this);
        }
        else if (targetDim == this.getDimension() - 1)
        {
            for (var i = 0; i < this.shape[0]; i++)
            {
                MDArray temp = this.underExtract(i);
                consumer.accept(temp);
                this.underReplace(i, temp);
            }
        }
        else
        {
            throw new IllegalArgumentException("You can fragment only at N - 1 shape");
        }
    }

    public ArrayList<MDArray> split(int split)
    {
        if (this.shape[0] % split != 0)
        {
            throw new RuntimeException("Invalid split number!");
        }
        ArrayList<MDArray> mdArrays = new ArrayList<>();
        int[] subshape = Arrays.copyOf(this.shape, this.getDimension());
        subshape[0] = subshape[0] / split;
        int dataSize = calculateSize(subshape);
        int startIndex = 0;
        int endIndex = dataSize;
        for (int i = 0; i < split; i++)
        {
            MDArray subarray = new MDArray(subshape);
            subarray.setData(Arrays.copyOfRange(this.data, startIndex, endIndex));
            mdArrays.add(subarray);
            startIndex = endIndex;
            endIndex += dataSize;
        }
        return mdArrays;
    }

    public int getDimension()
    {
        return this.shape.length;
    }

    @Override
    public String toString()
    {
        return "MDArray " + this.getDimension() + "D" + " - " + Arrays.toString(this.shape) +
                " - " + "Size : " + this.size + "\n" +
                toStringRecursive(new int[shape.length], 0);
    }

    private @NotNull String toStringRecursive(int[] indices, int depth)
    {
        StringBuilder sb = new StringBuilder();
        if (depth == shape.length - 1)
        {
            sb.append("[");
            for (int i = 0; i < shape[depth]; i++) {
                indices[depth] = i;
                sb.append(String.format("%.5f", data[calculateIndex(indices)]));
                if (i < shape[depth] - 1) {
                    sb.append(" ");
                }
            }
            sb.append("]");
        }
        else
        {
            sb.append("[");
            for (int i = 0; i < shape[depth]; i++)
            {
                indices[depth] = i;
                sb.append(toStringRecursive(indices, depth + 1));
                if (i < shape[depth] - 1)
                {
                    sb.append("\n");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }

    private static int[] concatenateArrays(int[]... arrays)
    {
        int totalSize = 0;
        for (int[] array : arrays)
        {
            totalSize += array.length;
        }
        int[] result = new int[totalSize];
        int currentIndex = 0;
        for (int[] array : arrays)
        {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }
        return result;
    }
}