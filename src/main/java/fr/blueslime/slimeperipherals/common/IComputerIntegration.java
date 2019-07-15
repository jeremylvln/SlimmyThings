package fr.blueslime.slimeperipherals.common;

public interface IComputerIntegration
{
    Object[] invokeComputerMethod(int index, Object[] args) throws NoSuchMethodException;
    String[] getComputerMethods();
    String getComputerName();
}
