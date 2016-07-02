package com.manofj.minecraft.moj_fsmith

import scala.collection.convert.WrapAsScala.asScalaSet
import scala.language.existentials

import net.minecraft.item.Item
import net.minecraft.item.ItemBlock

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

import com.manofj.minecraft.moj_commons.logging.LoggerLikeMod
import com.manofj.minecraft.moj_commons.util.ImplicitConversions.AnyExtension
import com.manofj.minecraft.moj_commons.util.MinecraftMod


@Mod( modid       = Furnasmith.modId,
      name        = Furnasmith.modName,
      version     = Furnasmith.modVersion,
      guiFactory  = Furnasmith.guiFactory,
      modLanguage = "scala" )
object Furnasmith
  extends MinecraftMod
  with    LoggerLikeMod
{

  final val modId      = "@modid@"
  final val modName    = "Furnasmith"
  final val modVersion = "@version@"
  final val guiFactory = "com.manofj.minecraft.moj_fsmith.FurnasmithGuiFactory"


  @EventHandler
  def preInit( evt: FMLPreInitializationEvent ): Unit = {
    import com.manofj.minecraft.moj_commons.config.javaFile2ForgeConfig

    FurnasmithConfigHandler.captureConfig( evt.getSuggestedConfigurationFile )
    MinecraftForge.EVENT_BUS.register( FurnasmithConfigHandler )

  }

  @EventHandler
  def postInit( evt: FMLPostInitializationEvent ): Unit = {
    import net.minecraft.client.resources.I18n.{ format => i18n }
    import net.minecraft.item.Item.{ REGISTRY => itemRegistry }

    import com.manofj.minecraft.moj_commons.util.ImplicitConversions.BooleanExtension

    // ログにアイテムの情報を出力
    debug( "*" * 64 )
    debug( "Item Name,Class Name,SuperClass Name,Resource Location" )
    itemRegistry.getKeys.foreach { itemRegistry.getObject( _ ) match {
      case block: ItemBlock => // 何もしない
      case item: Item =>
        val displayName = i18n( s"${ item.getUnlocalizedName }.name" )
        val itemClass = item.getClass
        val superClass = ( itemClass != classOf[ Item ] ) ?>
                            itemClass.getSuperclass.?     !>
                            Option.empty[ Class[ _ ] ]
        val registryName = item.getRegistryName

        debug( s"$displayName,${ itemClass.getName },${ superClass.fold("*")(_.getName) },${ registryName.toString }" )
    } }
    debug( "*" * 64 )

    FurnasmithHooks.setupConditions()

  }

}
