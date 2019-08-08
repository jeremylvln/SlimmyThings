package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.init.ModItems;
import fr.blueslime.slimeperipherals.item.ItemCard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class TileEntityRFIDAntenna extends TileEntityPeripheral
{
    private static final double RANGE_SQUARED = Math.pow(10.0D, 2);

    public TileEntityRFIDAntenna()
    {
        this.setHasEventQueue();
        this.computerMethodRegistry.register("scan", this::onMethodScan);
    }

    @Override
    public String getComputerName()
    {
        return "rfid_antenna";
    }

    @SuppressWarnings({ "unused "})

    private Object[] onMethodScan(Object[] args)
    {
        this.world.playerEntities.stream().filter(EntitySelectors.NOT_SPECTATING::apply).forEach(entityPlayer ->
        {
            double distanceSquared = entityPlayer.getDistanceSq(this.pos);

            if (distanceSquared < RANGE_SQUARED)
                TileEntityRFIDAntenna.this.pushPlayerCards(entityPlayer);
        });

        return new Object[0];
    }

    private void pushPlayerCards(EntityPlayer player)
    {
        player.inventory.mainInventory.stream()
                .filter(itemStack -> !itemStack.isEmpty() && itemStack.getItem() == ModItems.RFID_CARD).forEach((itemStack ->
                    this.pushEvent(new Object[] { player.getName(), ItemCard.getData(itemStack) })));
    }
}
