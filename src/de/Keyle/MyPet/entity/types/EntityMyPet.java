/*
 * Copyright (C) 2011-2012 Keyle
 *
 * This file is part of MyPet
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyPet. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.types;

import de.Keyle.MyPet.skill.skills.Control;
import de.Keyle.MyPet.util.MyPetUtil;
import net.minecraft.server.*;
import org.bukkit.Location;

public abstract class EntityMyPet extends EntityTameableAnimal
{
    protected float e;
    protected boolean h;
    protected boolean g;
    protected float j;
    protected float i;
    public EntityLiving goalTarget = null;

    protected boolean isMyPet = false;
    protected MyPet myPet;

    // This Constructor should be never called!!!
    public EntityMyPet(World world)
    {
        super(world);
        MyPetUtil.getLogger().severe("Don't try to get a MyPet this way!");
        MyPetUtil.getDebugLogger().severe("Default Entity constructor called!!!");
    }

    public EntityMyPet(World world, MyPet myPet)
    {
        super(world);
        setMyPet(myPet);
        myPet.craftPet = (CraftMyPet) this.getBukkitEntity();
    }

    public boolean isMyPet()
    {
        return isMyPet;
    }

    public abstract void setMyPet(MyPet MPet);

    public abstract int getMaxHealth();

    /**
     * Is called when player rightclicks this MyPet
     * return:
     * true: there was a reaction on rightclick
     * false: no reaction on rightclick
     */
    public boolean c(EntityHuman entityhuman)
    {
        ItemStack itemStack = entityhuman.inventory.getItemInHand();

        if (isMyPet() && entityhuman.name.equalsIgnoreCase(this.getOwnerName()))
        {
            if (myPet.getSkillSystem().hasSkill("Control") && myPet.getSkillSystem().getSkill("Control").getLevel() > 0)
            {
                if (itemStack.id == Control.item.getId())
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Is called when a MyPet attemps to do damge to another entity
     */
    public boolean k(Entity entity)
    {
        int damage = 4 + (isMyPet() && myPet.getSkillSystem().hasSkill("Damage") ? myPet.getSkillSystem().getSkill("Damage").getLevel() : 0);

        return entity.damageEntity(DamageSource.mobAttack(this), damage);
    }

    public void setLocation(Location loc)
    {
        this.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
    }

    public void setHealth(int i)
    {
        if (i > this.getMaxHealth())
        {
            i = this.getMaxHealth();
        }
        this.health = i;
    }

    @Override
    public boolean isTamed()
    {
        return true;
    }

    @Override
    public void setTamed(boolean flag)
    {
    }

    public void setSitting(boolean flag)
    {
        this.d.a(flag);
        super.setSitting(flag);
    }

    public abstract org.bukkit.entity.Entity getBukkitEntity();

    public MyPet getMyPet()
    {
        return myPet;
    }

    //Unused changed Vanilla Methods ---------------------------------------------------------------------------------------

    /**
     * Returns the default sound of the MyPet
     */
    protected abstract String aQ();

    public EntityAnimal createChild(EntityAnimal entityanimal)
    {
        return null;
    }

    public boolean mate(EntityAnimal entityanimal)
    {
        return false;
    }

    /**
     * saves info about this entity to a NBTTagCompound
     */
    public void a(NBTTagCompound nbttagcompound)
    {
    }

    protected void bd()
    {
    }

    //Vanilla Methods ------------------------------------------------------------------------------------------------------

    public boolean aV()
    {
        return true;
    }

    protected void a()
    {
        super.a();
    }

    protected boolean e_()
    {
        return false;
    }

    protected boolean ba()
    {
        return false;
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    protected abstract String aR();

    /**
     * Returns the sound that is played when the MyPet dies
     */
    protected abstract String aS();

    /**
     * N.A.
     */
    public float aP()
    {
        return 0.4F;
    }

    protected int getLootId()
    {
        return -1;
    }

    /**
     * N.A.
     */
    public void d()
    {
        super.d();
        if (!this.world.isStatic && this.h && !this.g && !this.H() && this.onGround)
        {
            this.g = true;
            this.j = 0.0F;
            this.i = 0.0F;
            this.world.broadcastEntityEffect(this, (byte) 8);
        }
    }

    /**
     * N.A.
     */
    public void h_()
    {
        super.h_();
        if (this.b)
        {
            this.e += (1.0F - this.e) * 0.4F;
        }
        else
        {
            this.e += (0.0F - this.e) * 0.4F;
        }
        if (this.G())
        {
            this.h = true;
            this.g = false;
            this.j = 0.0F;
            this.i = 0.0F;
        }
    }

    public float getHeadHeight()
    {
        return this.length * 0.8F;
    }

    /**
     * N.A.
     */
    public int bf()
    {
        return this.isSitting() ? 20 : super.bf();
    }

    public boolean damageEntity(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getEntity();

        this.d.a(false);
        if (entity != null && !(entity instanceof EntityHuman) && !(entity instanceof EntityArrow))
        {
            i = (i + 1) / 2;
        }

        return super.damageEntity(damagesource, i);
    }

    /**
     * Checks weather an itemstack is eatable for the MyPet
     */
    public boolean b(ItemStack itemstack)
    {
        return itemstack != null && (Item.byId[itemstack.id] instanceof ItemFood && ((ItemFood) Item.byId[itemstack.id]).h());
    }

    /**
     * N.A.
     */
    public int bl()
    {
        return 8;
    }

    /**
     * N.A.
     */
    public void i(boolean flag)
    {
        this.b = flag;
    }
}