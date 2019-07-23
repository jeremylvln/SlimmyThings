package fr.blueslime.slimeperipherals.integration.computer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ComputerMethodRegistry
{
    private final Map<String, Function<Object[], Object[]>> bindings = new LinkedHashMap<>();
    private String[] keysArray;
    private boolean dirty;

    public void register(String methodName, Function<Object[], Object[]> callback)
    {
        this.bindings.put(methodName, callback);
        this.dirty = true;
    }

    public void remove(String methodName)
    {
        this.bindings.remove(methodName);
    }

    public boolean has(String methodName)
    {
        return this.bindings.containsKey(methodName);
    }

    public Object[] invokeMethod(int index, Object[] args) throws NoSuchMethodException
    {
        if (index < 0 || index > this.bindings.size())
            throw new NoSuchMethodException();

        if (this.dirty)
            this.bakeKeysArray();

        return this.bindings.get(this.keysArray[index]).apply(args);
    }

    public String[] getMethods()
    {
        if (this.dirty)
            this.bakeKeysArray();

        return this.keysArray;
    }

    public Map<String, Function<Object[], Object[]>> getBindings()
    {
        return this.bindings;
    }

    private void bakeKeysArray()
    {
        this.keysArray = new String[this.bindings.size()];
        int i = 0;

        for (String key : this.bindings.keySet())
        {
            this.keysArray[i] = key;
            i += 1;
        }

        this.dirty = false;
    }
}
