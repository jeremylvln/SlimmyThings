package fr.blueslime.slimeperipherals.integration.computer;

public class ComputerEvent
{
    private final Object[] payload;
    private int ttl;

    public ComputerEvent(Object[] payload, int ttl)
    {
        this.payload = payload;
        this.ttl = ttl;
    }

    public int decrTTL()
    {
        return this.ttl -= 1;
    }

    public Object[] getPayload()
    {
        return this.payload;
    }

    public int getTTL()
    {
        return this.ttl;
    }
}
