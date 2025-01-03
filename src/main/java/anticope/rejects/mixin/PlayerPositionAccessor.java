package anticope.rejects.mixin;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.entity.player.PlayerPosition;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerPosition.class)
public interface PlayerPositionAccessor {
    @Accessor("pitch")
    float getPitch();

    @Accessor("yaw")
    float getYaw();

    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("pitch")
    void setPitch(float pitch);
}
