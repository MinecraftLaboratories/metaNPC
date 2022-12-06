package su.metalabs.npc.mixins.common;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.DataInventory;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.justagod.cutter.invoke.Invoke;
import su.metalabs.lib.handlers.injection.SkillsHandler;
import su.metalabs.lib.handlers.skill.EnumSkill;
import su.metalabs.npc.common.roles.RoleAttackXp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(value = DataInventory.class, remap = false)
public abstract class MixinDataInventory {
    @Shadow(remap = false)
    public HashMap<Integer, ItemStack> items = new HashMap();
    @Shadow(remap = false)
    public HashMap<Integer, Integer> dropchance = new HashMap();

    @Shadow(remap = false)
    public HashMap<Integer, ItemStack> weapons = new HashMap();

    @Shadow(remap = false)
    public HashMap<Integer, ItemStack> armor = new HashMap();
    @Shadow(remap = false)
    public int minExp = 0;
    @Shadow(remap = false)
    public int maxExp = 0;
    @Shadow(remap = false)
    public int lootMode = 0;
    @Shadow(remap = false)
    private EntityNPCInterface npc;


    @Overwrite(remap = false)
    public void dropStuff(final Entity entity, final DamageSource damagesource) {
        if(entity instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer) damagesource.getEntity();
            if(this.npc.roleInterface instanceof RoleAttackXp) {
                RoleAttackXp role = (RoleAttackXp) this.npc.roleInterface;

                Invoke.server(() -> {
                    SkillsHandler.giveSkillXp(entityplayer.getCommandSenderName(), EnumSkill.ATTACK.getId(), role.getXp(), role.isIgnoreLimit());
                });
            }
        }

        final ArrayList<EntityItem> list = new ArrayList<EntityItem>();
        for (final int i : this.items.keySet()) {
            final ItemStack item = this.items.get(i);
            if (item == null) {
                continue;
            }
            int dchance = 100;
            if (this.dropchance.containsKey(i)) {
                dchance = this.dropchance.get(i);
            }
            final int chance = this.npc.worldObj.rand.nextInt(100) + dchance;
            if (chance < 100) {
                continue;
            }
            final EntityItem e = this.getEntityItem(item.copy());
            if (e == null) {
                continue;
            }
            list.add(e);
        }
        int enchant = 0;
        if (damagesource.getEntity() instanceof EntityPlayer) {
            enchant = EnchantmentHelper.getLootingModifier((EntityLivingBase) damagesource.getEntity());
        }
        if (!ForgeHooks.onLivingDrops(this.npc, damagesource, list, enchant, true, 0)) {
            for (final EntityItem item2 : list) {
                if (entity instanceof EntityPlayer && (this.lootMode == 1 || shouldDropToInventory(entity))) {
                    final EntityPlayer player = (EntityPlayer) entity;
                    item2.delayBeforeCanPickup = 2;
                    this.npc.worldObj.spawnEntityInWorld(item2);
                    final ItemStack stack = item2.getEntityItem();
                    final int j = stack.stackSize;
                    if (!player.inventory.addItemStackToInventory(stack)) {
                        continue;
                    }
                    this.npc.worldObj.playSoundAtEntity(item2, "random.pop", 0.2f, ((this.npc.getRNG().nextFloat() - this.npc.getRNG().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    player.onItemPickup(item2, j);
                    if (stack.stackSize > 0) {
                        continue;
                    }
                    item2.setDead();
                } else {
                    this.npc.worldObj.spawnEntityInWorld(item2);
                }
            }
        }
        int var1 = this.minExp;
        if (this.maxExp - this.minExp > 0) {
            var1 += this.npc.worldObj.rand.nextInt(this.maxExp - this.minExp);
        }
        while (var1 > 0) {
            final int var2 = EntityXPOrb.getXPSplit(var1);
            var1 -= var2;
            if (this.lootMode == 1 && entity instanceof EntityPlayer) {
                this.npc.worldObj.spawnEntityInWorld(new EntityXPOrb(entity.worldObj, entity.posX, entity.posY, entity.posZ, var2));
            } else {
                this.npc.worldObj.spawnEntityInWorld(new EntityXPOrb(this.npc.worldObj, this.npc.posX, this.npc.posY, this.npc.posZ, var2));
            }
        }
    }

    public boolean shouldDropToInventory(Entity entity) {
        if(!(entity instanceof EntityPlayer)) {
            return false;
        }

        if(!(this.npc.roleInterface instanceof RoleAttackXp)) {
            return false;
        }

        EntityPlayer player = (EntityPlayer) entity;
        RoleAttackXp role = (RoleAttackXp) this.npc.roleInterface;

        if(role.getAbilityId() == null) {
            return false;
        }

        String abilityId = role.getAbilityId();

        AtomicBoolean val = new AtomicBoolean(false);
        Invoke.server(() -> val.set(SkillsHandler.hasAbility(player.getCommandSenderName(), abilityId)));

        return val.get();
    }

    @Shadow
    public EntityItem getEntityItem(ItemStack itemstack) {
        if (itemstack == null) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(this.npc.worldObj, this.npc.posX, this.npc.posY - 0.30000001192092896 + (double) this.npc.getEyeHeight(), this.npc.posZ, itemstack);
            entityitem.delayBeforeCanPickup = 40;
            float f2 = this.npc.getRNG().nextFloat() * 0.5F;
            float f4 = this.npc.getRNG().nextFloat() * 3.141593F * 2.0F;
            entityitem.motionX = (double) (-MathHelper.sin(f4) * f2);
            entityitem.motionZ = (double) (MathHelper.cos(f4) * f2);
            entityitem.motionY = 0.20000000298023224;
            return entityitem;
        }
    }
}
