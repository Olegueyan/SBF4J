package net.olegueyan.sbf4j.manager;

import java.io.*;

public class ResourcesManager
{
    public static final String ResourcesPath = String.join("/", "src", "main", "resources");

    public static final String ParametersPath = String.join("/", ResourcesPath, "parameters");

    public static <T extends Serializable> void save(T object, String path)
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path)))
        {
            out.writeObject(object);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T load(String path) throws Exception
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
        return (T) in.readObject();
    }
}