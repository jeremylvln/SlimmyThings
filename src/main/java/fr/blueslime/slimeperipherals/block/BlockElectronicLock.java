package fr.blueslime.slimeperipherals.block;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.init.ModBlocks;
import fr.blueslime.slimeperipherals.init.ModItems;
import fr.blueslime.slimeperipherals.tileentity.TileEntityElectronicLock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = SlimePeripherals.MODID)
public class BlockElectronicLock extends BlockPeripheral
{
    public static final PropertyEnum<BlockMagneticCardReader.EnumOrientation> ORIENTATION = BlockMagneticCardReader.ORIENTATION;
    public static final PropertyEnum<EnumState> STATE = PropertyEnum.create("state", EnumState.class);

    private static final AxisAlignedBB UP_X_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.25D, 0.6875D, 0.15625D, 0.75D);
    private static final AxisAlignedBB UP_Y_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.3125D, 0.75D, 0.15625D, 0.6875D);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.3125D, 0.25D, 1.0D, 0.6875D, 0.75D, 0.84375D);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.3125D, 0.15625D, 0.75D, 0.6875D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.25D, 0.0D, 0.6875D, 0.75D, 0.15625);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(1.0D, 0.25D, 0.3125D, 0.84375D, 0.75D, 0.6875D);
    private static final AxisAlignedBB DOWN_X_AABB = new AxisAlignedBB(0.3125D, 1.0D, 0.25D, 0.6875D, 0.84375D, 0.75D);
    private static final AxisAlignedBB DOWN_Y_AABB = new AxisAlignedBB(0.25D, 1.0D, 0.3125D, 0.75D, 0.84375D, 0.6875D);

    public BlockElectronicLock()
    {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(ORIENTATION, BlockMagneticCardReader.EnumOrientation.DOWN_NORTH)
                .withProperty(STATE, EnumState.IDLE));

        this.setHardness(0.5F);
        this.setSoundType(SoundType.METAL);
        this.setRegistryName(SlimePeripherals.MODID, "electronic_lock");
        this.setUnlocalizedName(SlimePeripherals.MODID + ".electronic_lock");
        this.setCreativeTab(SlimePeripherals.TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return true;

        TileEntityElectronicLock tileEntity = (TileEntityElectronicLock) worldIn.getTileEntity(pos);

        if (tileEntity == null)
            return true;

        if (playerIn.isSneaking())
            tileEntity.onPadMovement(playerIn, playerIn.getHeldItem(hand));
        else
            tileEntity.onPlayerInteract(playerIn, hitX, hitY, hitZ);

        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityElectronicLock();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(ORIENTATION))
        {
            case UP_NORTH:
            case UP_SOUTH:
                return UP_X_AABB;
            case UP_EAST:
            case UP_WEST:
                return UP_Y_AABB;
            case NORTH: return NORTH_AABB;
            case EAST: return EAST_AABB;
            case SOUTH: return SOUTH_AABB;
            case WEST: return WEST_AABB;
            case DOWN_NORTH:
            case DOWN_SOUTH:
                return DOWN_X_AABB;
            case DOWN_EAST:
            case DOWN_WEST:
                return DOWN_Y_AABB;
        }

        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(ORIENTATION, BlockMagneticCardReader.EnumOrientation.byFacings(facing, placer.getHorizontalFacing()));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ORIENTATION, BlockMagneticCardReader.EnumOrientation.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ORIENTATION).ordinal();
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, ORIENTATION, STATE);
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.ELECTRONIC_LOCK)
            if (event.getEntityPlayer().isSneaking() && event.getItemStack().getItem() == ModItems.ELECTRONIC_PAD)
                event.setUseBlock(Event.Result.ALLOW);
    }

    public enum EnumState implements IStringSerializable
    {
        IDLE("idle"),

        GRANTED("granted"),
        REJECTED("rejected");

        private final String name;

        EnumState(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
