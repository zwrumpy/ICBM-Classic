package icbm.zhapin.zhapin.ex;

import icbm.api.ICBM;
import icbm.zhapin.EFeiBlock;
import icbm.zhapin.zhapin.EZhaDan;
import icbm.zhapin.zhapin.EZhaPin;
import icbm.zhapin.zhapin.ZhaPin;

import java.util.List;

import mffs.api.ForcefieldProtected;
import mffs.api.IForceFieldBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.RecipeHelper;
import cpw.mods.fml.common.Loader;

public class ExHongSu extends ZhaPin
{
	private static final int MAX_TAKE_BLOCKS = 5;

	public ExHongSu(String name, int ID, int tier)
	{
		super(name, ID, tier);
		this.isMobile = true;
	}

	public void baoZhaQian(World worldObj, Vector3 position, Entity explosionSource)
	{
		if (!worldObj.isRemote)
		{
			worldObj.createExplosion(explosionSource, position.x, position.y, position.z, 5.0F, true);
		}
	}

	// Sonic Explosion is a procedural explosive
	@Override
	public boolean doBaoZha(World worldObj, Vector3 position, Entity explosionSource, int explosionMetadata, int callCount)
	{
		// Try to find and grab some blocks to orbit
		if (!worldObj.isRemote)
		{
			Vector3 currentPos;
			int blockID;
			int metadata;
			double dist;
			int takenBlocks = 0;

			for (int r = 1; r < this.getRadius(); r++)
			{
				for (int x = -r; x < r; x++)
				{
					for (int y = -r; y < r; y++)
					{
						for (int z = -r; z < r; z++)
						{
							dist = MathHelper.sqrt_double((x * x + y * y + z * z));

							if (dist > r || dist < r - 2)
								continue;

							currentPos = new Vector3(position.x + x, position.y + y, position.z + z);
							blockID = worldObj.getBlockId(currentPos.intX(), currentPos.intY(), currentPos.intZ());

							if (blockID == 0 || Block.blocksList[blockID] == null)
								continue;

							if (Loader.isModLoaded("ModularForceFieldSystem"))
							{
								if (ForcefieldProtected.BlockProtected(worldObj, currentPos.intX(), currentPos.intY(), currentPos.intZ(), null))
									continue;
							}

							if (Block.blocksList[blockID] instanceof IForceFieldBlock)
							{
								((IForceFieldBlock) Block.blocksList[blockID]).weakenForceField(worldObj, currentPos.intX(), currentPos.intY(), currentPos.intZ());
								continue;
							}

							if (Block.blocksList[blockID].getBlockHardness(worldObj, currentPos.intX(), currentPos.intY(), currentPos.intZ()) <= -1)
								continue;

							metadata = worldObj.getBlockMetadata(currentPos.intX(), currentPos.intY(), currentPos.intZ());

							worldObj.setBlockAndMetadataWithNotify(currentPos.intX(), currentPos.intY(), currentPos.intZ(), 0, 0, 2);

							if (Block.blocksList[blockID] instanceof BlockFluid)
								continue;

							if (worldObj.rand.nextFloat() > 0.8)
							{
								currentPos.add(0.5D);

								EFeiBlock entity = new EFeiBlock(worldObj, currentPos, blockID, metadata);
								worldObj.spawnEntityInWorld(entity);
								entity.yawChange = 50 * worldObj.rand.nextFloat();
								entity.pitchChange = 50 * worldObj.rand.nextFloat();
							}

							takenBlocks++;
							if (takenBlocks > MAX_TAKE_BLOCKS)
								break;
						}
						if (takenBlocks > MAX_TAKE_BLOCKS)
							break;
					}
					if (takenBlocks > MAX_TAKE_BLOCKS)
						break;
				}
				if (takenBlocks > MAX_TAKE_BLOCKS)
					break;
			}
		}

		// Make the blocks controlled by this red
		// matter orbit around it
		int radius = (int) this.getRadius();
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(position.x - radius, position.y - radius, position.z - radius, position.x + radius, position.y + radius, position.z + radius);
		List<Entity> allEntities = worldObj.getEntitiesWithinAABB(Entity.class, bounds);
		boolean explosionCreated = false;

		for (Entity entity : allEntities)
		{
			if (entity == explosionSource)
				continue;

			if (entity instanceof EntityPlayer)
			{
				if (((EntityPlayer) entity).capabilities.isCreativeMode)
					continue;
			}

			double xDifference = entity.posX - position.x;
			double yDifference = entity.posY - position.y;
			double zDifference = entity.posZ - position.z;

			int r = (int) this.getRadius();
			if (xDifference < 0)
				r = (int) -this.getRadius();

			entity.motionX -= (r - xDifference) * Math.abs(xDifference) * 0.0005;

			r = (int) this.getRadius();
			if (entity.posY > position.y)
				r = (int) -this.getRadius();
			entity.motionY += (r - yDifference) * Math.abs(yDifference) * 0.0012;

			r = (int) this.getRadius();
			if (zDifference < 0)
				r = (int) -this.getRadius();

			entity.motionZ -= (r - zDifference) * Math.abs(zDifference) * 0.0005;

			if (Vector3.distance(new Vector3(entity.posX, entity.posY, entity.posZ), position) < 4)
			{
				if (!explosionCreated && callCount % 5 == 0)
				{
					worldObj.createExplosion(explosionSource, entity.posX, entity.posY, entity.posZ, 3.0F, true);
					explosionCreated = true;
				}

				if (entity instanceof EntityLiving)
				{
					entity.fallDistance = 0;
				}
				else
				{
					if (entity instanceof EZhaPin)
					{
						worldObj.playSoundEffect(position.x, position.y, position.z, "icbm.explosion", 7.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

						if (worldObj.rand.nextFloat() > 0.85 && !worldObj.isRemote)
						{
							entity.setDead();
							return false;
						}

					}
					else if (entity instanceof EZhaDan)
					{
						((EZhaDan) entity).explode();
					}
					else
					{
						entity.setDead();
					}
				}
			}
		}

		worldObj.playSoundEffect(position.x, position.y, position.z, "icbm.redmatter", 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 1F);

		return true;
	}

	/**
	 * The interval in ticks before the next procedural call of this explosive
	 * 
	 * @return - Return -1 if this explosive does not need proceudral calls
	 */
	@Override
	public int proceduralInterval()
	{
		return 1;
	}

	@Override
	public void init()
	{
		RecipeHelper.addRecipe(new ShapedOreRecipe(this.getItemStack(), new Object[] { "AAA", "AEA", "AAA", 'E', fanWuSu.getItemStack(), 'A', "strangeMatter" }), this.getUnlocalizedName(), ICBM.CONFIGURATION, true);
	}

	@Override
	public float getRadius()
	{
		return 35;
	}

	@Override
	public double getEnergy()
	{
		return 4000;
	}
}
