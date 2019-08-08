package fr.blueslime.slimeperipherals.integration.computer;

public interface IComputerIntegration
{
    Object[] invokeComputerMethod(int index, Object[] args) throws NoSuchMethodException;
    String[] getComputerMethods();
    String getComputerName();
}
