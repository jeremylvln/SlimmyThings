package fr.blueslime.slimmythings.integration.computer;

public interface IComputerIntegration
{
    Object[] invokeComputerMethod(int index, Object[] args) throws NoSuchMethodException;
    String[] getComputerMethods();
    String getComputerName();
}
