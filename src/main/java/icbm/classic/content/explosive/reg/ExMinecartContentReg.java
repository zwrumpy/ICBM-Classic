package icbm.classic.content.explosive.reg;

import icbm.classic.api.ICBMClassicAPI;
import icbm.classic.api.reg.content.IExMinecartRegistry;
import net.minecraft.util.ResourceLocation;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/7/19.
 */
public class ExMinecartContentReg extends ExplosiveContentRegistry implements IExMinecartRegistry
{
    public ExMinecartContentReg()
    {
        super(ICBMClassicAPI.EX_MINECART);
    }
}
