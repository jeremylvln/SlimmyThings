package fr.blueslime.slimeperipherals.block;

import fr.blueslime.slimeperipherals.tileentity.TileEntityPeripheral;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPeripheral extends Block
{
    BlockPeripheral(Material materialIn)
    {
        super(materialIn);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityPeripheral))
            return;

        ((TileEntityPeripheral) tileEntity).setOwner(placer.getUniqueID());
    }
}
