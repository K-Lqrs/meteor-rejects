package anticope.rejects.modules;

import anticope.rejects.MeteorRejectsAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class AntiCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> log = sgGeneral.add(new BoolSetting.Builder()
            .name("log")
            .description("Logs when crash packet detected.")
            .defaultValue(false)
            .build()
    );

    public AntiCrash() {
        super(MeteorRejectsAddon.CATEGORY, "anti-crash", "Attempts to cancel packets that may crash the client.");
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.packet instanceof ExplosionS2CPacket packet) {
            if (packet.center().x > 30_000_000 || packet.center().y > 30_000_000 || packet.center().z > 30_000_000 ||
                    packet.center().x < -30_000_000 || packet.center().y < -30_000_000 || packet.center().z < -30_000_000) {
                cancel(event);
            }
        } else if (event.packet instanceof ParticleS2CPacket packet) {
            if (packet.getCount() > 100_000) { // 新しい方法でパーティクル数を確認
                cancel(event);
            }
        } else if (event.packet instanceof PlayerPositionLookS2CPacket packet) {
            if (packet.change().position().x > 30_000_000 || packet.change().position().y > 30_000_000 || packet.change().position().z > 30_000_000 ||
                    packet.change().position().x < -30_000_000 || packet.change().position().y < -30_000_000 || packet.change().position().z < -30_000_000) {
                cancel(event);
            }
        } else if (event.packet instanceof EntityVelocityUpdateS2CPacket packet) {
            if (packet.getVelocityX() > 30_000_000 || packet.getVelocityY() > 30_000_000 || packet.getVelocityY() > 30_000_000 ||
                    packet.getVelocityX() < -30_000_000 || packet.getVelocityY() < -30_000_000 || packet.getVelocityZ() < -30_000_000) {
                cancel(event);
            }
        }
    }


    private void cancel(PacketEvent.Receive event) {
        if (log.get()) warning("Server attempts to crash you");
        event.cancel();
    }
}
