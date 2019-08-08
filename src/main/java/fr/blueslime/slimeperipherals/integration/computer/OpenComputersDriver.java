package fr.blueslime.slimeperipherals.integration.computer;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;

public class OpenComputersDriver extends DriverSidedTileEntity
{
    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing)
    {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof IComputerIntegration)
            return new OpenComputersManagedEnvironment((IComputerIntegration) tileEntity);
        return null;
    }

    @Override
    public Class<?> getTileEntityClass()
    {
        return IComputerIntegration.class;
    }

    public class OpenComputersManagedEnvironment extends AbstractManagedEnvironment implements NamedBlock, ManagedPeripheral
    {
        private final IComputerIntegration tileEntity;

        OpenComputersManagedEnvironment(IComputerIntegration tileEntity)
        {
            this.tileEntity = tileEntity;
            this.setNode(Network.newNode(this, Visibility.Network).withComponent(this.preferredName(), Visibility.Network).create());
        }

        @Override
        public String[] methods()
        {
            return this.tileEntity.getComputerMethods();
        }

        @Override
        public Object[] invoke(String method, Context context, Arguments args) throws Exception
        {
            return this.tileEntity.invokeComputerMethod(Arrays.asList(this.methods()).indexOf(method), args.toArray());
        }

        @Override
        public String preferredName()
        {
            return this.tileEntity.getComputerName();
        }

        @Override
        public int priority()
        {
            return 4;
        }
    }
}
