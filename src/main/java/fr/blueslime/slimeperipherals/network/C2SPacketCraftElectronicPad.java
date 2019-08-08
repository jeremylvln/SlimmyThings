package fr.blueslime.slimeperipherals.network;

import fr.blueslime.slimeperipherals.inventory.ContainerElectronicPadDesigner;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class C2SPacketCraftElectronicPad implements IMessageHandler<C2SPacketCraftElectronicPad.Message, IMessage>
{
    @Override
    public IMessage onMessage(Message message, MessageContext ctx)
    {
        return null;
    }

    public static class Message implements IMessage
    {
        private UUID playerUUID;

        public Message() {}

        public Message(EntityPlayer player)
        {
            this.playerUUID = player.getUniqueID();
        }

        @Override
        public void fromBytes(ByteBuf byteBuf)
        {
            this.playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(this.playerUUID);

            if (player.openContainer instanceof ContainerElectronicPadDesigner)
                ((ContainerElectronicPadDesigner) player.openContainer).processCraft();
        }

        @Override
        public void toBytes(ByteBuf byteBuf)
        {
            ByteBufUtils.writeUTF8String(byteBuf, this.playerUUID.toString());
        }
    }
}
