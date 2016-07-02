package com.manofj.minecraft.moj_fsmith

import net.minecraftforge.common.ForgeVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions

import com.manofj.minecraft.moj_commons.collection.java.alias.JavaMap


// FurnasmithTransformer をロードするためのプラグイン
@MCVersion( ForgeVersion.mcVersion )
@TransformerExclusions( Array( "com.manofj.minecraft.moj_fsmith." ) )
class FurnasmithPlugin
  extends IFMLLoadingPlugin
{

  override def getASMTransformerClass: Array[ String ] =
    Array( "com.manofj.minecraft.moj_fsmith.asm.FurnasmithTransformer" )

  override def injectData( data: JavaMap[ String, AnyRef ] ): Unit = {}
  override def getModContainerClass: String = null
  override def getAccessTransformerClass: String = null
  override def getSetupClass: String = null

}
