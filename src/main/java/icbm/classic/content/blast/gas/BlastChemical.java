package icbm.classic.content.blast.gas;

import icbm.classic.ICBMClassic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3i;

public class BlastChemical extends BlastGasBase
{
    public static final DamageSource CHEMICAL_DAMAGE = new DamageSource("icbm.chemical");

    public static final int DURATION = 20 * 30; //TODO move to config

    public static final float red = 0.8f, green = 0.8f, blue = 0;


    public BlastChemical()
    {
        super(DURATION, false);
    }

    @Override
    protected boolean canEffectEntities()
    {
        return true;
    }

    @Override
    protected boolean canGasEffect(EntityLivingBase entity)
    {
        return super.canGasEffect(entity) && !entity.isEntityInvulnerable(CHEMICAL_DAMAGE);
    }

    @Override
    protected void applyEffect(final EntityLivingBase entity, final int hitCount)
    {
        ICBMClassic.poisonous_potion.poisonEntity(location.toPos(), entity);
        if (hitCount > 20)
        {
            entity.attackEntityFrom(CHEMICAL_DAMAGE, (hitCount - 10f) / 10);
        }
    }

    @Override
    protected float getParticleColorRed(final Vec3i pos)
    {
        return red;
    }

    @Override
    protected float getParticleColorGreen(final Vec3i pos)
    {
        return green;
    }

    @Override
    protected float getParticleColorBlue(final Vec3i pos)
    {
        return blue;
    }
}
