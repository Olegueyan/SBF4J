package core.olegueyan.sbf4j.sonar;

import net.olegueyan.sbf4j.data.Dataset;
import net.olegueyan.sbf4j.manager.ResourcesManager;
import net.olegueyan.sbf4j.math.MDArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SonarDataSet implements Dataset
{
    private static final String SONAR_CSV_PATH = String.join("/", ResourcesManager.ResourcesPath, "sonar.all-data.csv");
    private final List<CSVRecord> csvRecords = getSonarCSV();

    @Override
    public @NotNull MDArray trainEntry()
    {
        MDArray mdArray = new MDArray(188, 60);
        for (var i = 0; i < 188; i++)
        {
            double[] doubles = new double[60];
            for (var j = 0; j < 60; j++)
            {
                doubles[j] = Double.parseDouble(csvRecords.get(i).get(j));
            }
            mdArray = mdArray.underReplace(i, new MDArray(doubles));
        }
        return mdArray;
    }

    @Override
    public @NotNull MDArray trainTruth()
    {
        MDArray mdArray = new MDArray(188, 1);
        for (var i = 0; i < 188; i++)
        {
            String type = csvRecords.get(i).get(60);
            double truth = Objects.equals(type, "R") ? 0 : 1;
            mdArray = mdArray.underReplace(i, new MDArray(new double[]{truth}));
        }
        return mdArray;
    }

    @Override
    public @NotNull MDArray testEntry()
    {
        MDArray mdArray = new MDArray(20, 60);
        int index = 0;
        for (var i = 188; i < 208; i++)
        {
            double[] doubles = new double[60];
            for (var j = 0; j < 60; j++)
            {
                doubles[j] = Double.parseDouble(csvRecords.get(i).get(j));
            }
            mdArray = mdArray.underReplace(index, new MDArray(doubles));
            index++;
        }
        return mdArray;
    }

    @Override
    public @NotNull MDArray testTruth()
    {
        MDArray mdArray = new MDArray(20, 1);
        int index = 0;
        for (var i = 188; i < 208; i++)
        {
            String type = csvRecords.get(i).get(60);
            double truth = Objects.equals(type, "R") ? 0 : 1;
            mdArray = mdArray.underReplace(index, new MDArray(new double[]{truth}));
            index++;
        }
        return mdArray;
    }

    @Override
    public int trainSize()
    {
        return 188;
    }

    @Override
    public int testSize()
    {
        return 20;
    }

    private List<CSVRecord> getSonarCSV()
    {
        try
        {
            Reader reader = Files.newBufferedReader(Paths.get(SONAR_CSV_PATH));
            CSVParser cvsParser = new CSVParser(reader, CSVFormat.DEFAULT);
            List<CSVRecord> records = cvsParser.getRecords();
            Collections.shuffle(records);
            return records;
        } catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
    }
}