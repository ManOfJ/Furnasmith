package com.manofj.minecraft.moj_fsmith

import java.util.{ Map => JMap }

import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.{ MCVersion, TransformerExclusions }


/**
  * Furnasmith-Mod のローディングプラグイン
  * FurnasmithTransformer をロードするだけの存在
  */
@MCVersion( ForgeVersion.mcVersion )
@TransformerExclusions( Array( "com.manofj.minecraft.moj_fsmith." ) )
class FurnasmithPlugin
  extends
    IFMLLoadingPlugin
{
  override def getASMTransformerClass: Array[ String ] =
    Array( "com.manofj.minecraft.moj_fsmith.asm.FurnasmithTransformer" )

  override def injectData( data: JMap[ String, AnyRef ] ): Unit = {}

  override def getModContainerClass: String = null

  override def getAccessTransformerClass: String = null

  override def getSetupClass: String = null
}
