package icbm.classic.content.entity.mobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 12/31/2018.
 */
public class EntityXmasZombie extends EntityXmasMob
{
    public EntityXmasZombie(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.6F);
    }

    @Override
    public boolean isOnTeam(Entity entity)
    {
        return entity instanceof EntityZombie;
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySkeleton.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityXmasSkeleton.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityXmasSnowman.class, true));
    }

    @Override
    public float getEyeHeight()
    {
        return 1.24F;
    }
}
