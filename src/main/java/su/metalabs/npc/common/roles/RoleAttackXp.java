package su.metalabs.npc.common.roles;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleInterface;

public class RoleAttackXp extends RoleInterface {
    @Getter
    @Setter
    int xp;

    @Getter
    @Setter
    boolean ignoreLimit;

    @Getter
    @Setter
    String abilityId;

    public RoleAttackXp(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("attack_xp", xp);
        nbtTagCompound.setBoolean("ignore_limit", ignoreLimit);

        if(abilityId != null) {
            nbtTagCompound.setString("ability_id", abilityId);
        }
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        xp = nbtTagCompound.getInteger("attack_xp");
        ignoreLimit = nbtTagCompound.getBoolean("ignore_limit");

        if(nbtTagCompound.hasKey("ability_id")) {
            abilityId = nbtTagCompound.getString("ability_id");
        }
    }


    @Override
    public void interact(EntityPlayer entityPlayer) {

    }
}
