package fr.blueslime.slimeperipherals.block;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.init.ModItems;
import fr.blueslime.slimeperipherals.tileentity.TileEntityMagneticCardReader;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMagneticCardReader extends BlockPeripheral
{
    public static final PropertyEnum<EnumOrientation> ORIENTATION = PropertyEnum.create("facing", EnumOrientation.class);
    public static final PropertyEnum<EnumState> STATE = PropertyEnum.create("state", EnumState.class);

    private static final AxisAlignedBB UP_X_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.25D, 0.625D, 0.15625D, 0.75D);
    private static final AxisAlignedBB UP_Y_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.375D, 0.75D, 0.15625D, 0.625D);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375D, 0.25D, 1.0D, 0.625D, 0.75D, 0.84375D);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.375D, 0.15625D, 0.75D, 0.625D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375D, 0.25D, 0.0D, 0.625D, 0.75D, 0.15625);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(1.0D, 0.25D, 0.375D, 0.84375D, 0.75D, 0.625D);
    private static final AxisAlignedBB DOWN_X_AABB = new AxisAlignedBB(0.375D, 1.0D, 0.25D, 0.625D, 0.84375D, 0.75D);
    private static final AxisAlignedBB DOWN_Y_AABB = new AxisAlignedBB(0.25D, 1.0D, 0.375D, 0.75D, 0.84375D, 0.625D);

    public BlockMagneticCardReader()
    {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ORIENTATION, EnumOrientation.DOWN_NORTH).withProperty(STATE, EnumState.IDLE));

        this.setHardness(0.5F);
        this.setSoundType(SoundType.METAL);
        this.setRegistryName(SlimePeripherals.MODID, "magnetic_card_reader");
        this.setUnlocalizedName(SlimePeripherals.MODID + ".magnetic_card_reader");
        this.setCreativeTab(SlimePeripherals.TAB);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (playerIn.isSneaking() || worldIn.isRemote)
            return false;

        ItemStack itemStack = playerIn.getHeldItem(hand);

        if (!itemStack.isEmpty() && itemStack.getItem() == ModItems.MAGNETIC_CARD)
        {
            TileEntityMagneticCardReader tileEntity = (TileEntityMagneticCardReader) worldIn.getTileEntity(pos);

            if (tileEntity != null)
                tileEntity.onCardSwipe(playerIn, itemStack);
        }

        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityMagneticCardReader();
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
        return this.getDefaultState().withProperty(ORIENTATION, EnumOrientation.byFacings(facing, placer.getHorizontalFacing()));
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
        return this.getDefaultState().withProperty(ORIENTATION, EnumOrientation.byMetadata(meta));
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

    public enum EnumOrientation implements IStringSerializable
    {
        DOWN_NORTH("down_north", true),
        DOWN_EAST("down_east", true),
        DOWN_SOUTH("down_south", true),
        DOWN_WEST("down_west", true),
        NORTH("north", false),
        EAST("east", false),
        SOUTH("south", false),
        WEST("west", false),
        UP_NORTH("up_north", true),
        UP_EAST("up_east", true),
        UP_SOUTH("up_south", true),
        UP_WEST("up_west", true);

        private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[values().length];
        private final String name;
        private final boolean horizontal;

        EnumOrientation(String name, boolean horizontal)
        {
            this.name = name;
            this.horizontal = horizontal;
        }

        public boolean isHorizontal()
        {
            return this.horizontal;
        }

        public String toString()
        {
            return this.name;
        }

        public static EnumOrientation byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;

            return META_LOOKUP[meta];
        }

        public static EnumOrientation byFacings(EnumFacing clickedSide, EnumFacing entityFacing)
        {
            switch (clickedSide)
            {
                case DOWN:
                    switch (entityFacing)
                    {
                        case NORTH: return DOWN_NORTH;
                        case EAST: return DOWN_EAST;
                        case SOUTH: return DOWN_SOUTH;
                        case WEST: return DOWN_WEST;
                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case UP:
                    switch (entityFacing)
                    {
                        case NORTH: return UP_NORTH;
                        case EAST: return UP_EAST;
                        case SOUTH: return UP_SOUTH;
                        case WEST: return UP_WEST;
                        default:
                            throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
                    }

                case NORTH:
                    return NORTH;
                case SOUTH:
                    return SOUTH;
                case WEST:
                    return WEST;
                case EAST:
                    return EAST;
                default:
                    throw new IllegalArgumentException("Invalid facing: " + clickedSide);
            }
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (EnumOrientation facing : values())
                META_LOOKUP[facing.ordinal()] = facing;
        }
    }

    public enum EnumState implements IStringSerializable
    {
        IDLE("idle"),
        WAITING_CARD("waiting_card"),
        WAITING_CARD_WRITE("waiting_card_write"),
        BUSY("busy"),
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
