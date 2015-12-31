package manofj.com.github.moj_fsmith

import java.util.{ Map => JMap }

import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.{ MCVersion, TransformerExclusions }


/**
  * Furnasmith-Mod のローディングプラグイン
  * FurnasmithTransformer をロードするだけの存在
  */
@MCVersion( ForgeVersion.mcVersion )
@TransformerExclusions( Array( "manofj.com.github.moj_fsmith." ) )
class FurnasmithPlugin
  extends
    IFMLLoadingPlugin
{
  override def getASMTransformerClass: Array[ String ] =
    Array( "manofj.com.github.moj_fsmith.asm.FurnasmithTransformer" )

  override def injectData( data: JMap[ String, AnyRef ] ): Unit = {}

  override def getModContainerClass: String = null

  override def getAccessTransformerClass: String = null

  override def getSetupClass: String = null
}
