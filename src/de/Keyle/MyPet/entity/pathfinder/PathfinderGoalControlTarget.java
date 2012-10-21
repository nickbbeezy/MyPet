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

package de.Keyle.MyPet.entity.pathfinder;

import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.skill.skills.Behavior;
import de.Keyle.MyPet.util.MyPetUtil;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.PathfinderGoalTarget;
import org.bukkit.entity.Player;

public class PathfinderGoalControlTarget extends PathfinderGoalTarget
{
    private MyPet myPet;
    private EntityMyPet petEntity;
    private EntityLiving target;
    private float range;
    private PathfinderGoalControl controlPathfinderGoal;

    public PathfinderGoalControlTarget(MyPet myPet, PathfinderGoalControl controlPathfinderGoal, float range)
    {
        super(myPet.getPet().getHandle(), 32.0F, false);
        this.petEntity = myPet.getPet().getHandle();
        this.myPet = myPet;
        this.range = range;
        this.controlPathfinderGoal = controlPathfinderGoal;
    }

    /**
     * Checks whether this pathfinder should be activated
     */
    public boolean a()
    {
        if (controlPathfinderGoal.moveTo != null && petEntity.canMove())
        {
            if (myPet.getSkillSystem().hasSkill("Behavior"))
            {
                Behavior behaviorSkill = (Behavior) myPet.getSkillSystem().getSkill("Behavior");
                if (behaviorSkill.getLevel() > 0)
                {
                    if (behaviorSkill.getBehavior() == Behavior.BehaviorState.Friendly)
                    {
                        return false;
                    }
                }
            }
            for (Object entityObj : this.petEntity.world.a(EntityLiving.class, this.petEntity.boundingBox.grow((double) this.range, 4.0D, (double) this.range)))
            {
                Entity entity = (Entity) entityObj;
                EntityLiving entityLiving = (EntityLiving) entity;

                if (petEntity.at().canSee(entityLiving) && entityLiving != petEntity)
                {
                    if (entityLiving instanceof EntityPlayer)
                    {
                        Player targetPlayer = (Player) entityLiving.getBukkitEntity();
                        if (myPet.getOwner().equals(targetPlayer))
                        {
                            continue;
                        }
                        if (!MyPetUtil.canHurt(myPet.getOwner().getPlayer(), targetPlayer))
                        {
                            continue;
                        }
                    }
                    controlPathfinderGoal.stopControl();
                    this.target = entityLiving;
                    return true;
                }
            }
        }
        return false;
    }

    public void e()
    {
        petEntity.b(this.target);
        super.e();
    }
}