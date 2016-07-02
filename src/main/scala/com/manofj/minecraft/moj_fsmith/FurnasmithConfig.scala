package com.manofj.minecraft.moj_fsmith

import com.google.common.collect.Lists

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n.{ format => i18n }

import net.minecraftforge.common.config.Property

import com.manofj.minecraft.moj_commons.config.ConfigGui
import com.manofj.minecraft.moj_commons.config.ConfigGuiFactory
import com.manofj.minecraft.moj_commons.config.ConfigGuiHandler
import com.manofj.minecraft.moj_commons.util.ImplicitConversions.AnyExtension


object FurnasmithConfigHandler
  extends ConfigGuiHandler
{

  private[ this ] def languageKey( propKey: String ): String = s"$modId.config.$propKey"
  private[ this ] def comment( propKey: String ): String = i18n( languageKey( propKey ) + ".tooltip" )

  override val modId: String = Furnasmith.modId


  override def title: String = i18n( "moj_fsmith.config.gui.title" )

  override def syncConfig( load: Boolean ): Unit = {
    import com.manofj.minecraft.moj_fsmith.FurnasmithSettings._

    val cfg = config
    var prop = null: Property
    val data = Map.newBuilder[ String, Any ]

    if ( load && !cfg.isChild ) cfg.load()

    cfg.setCategoryPropertyOrder( configId, Lists.newArrayList[ String ] )

    // 廃止されたコンフィグ項目の削除
    cfg.getCategory( configId ) << { cat =>
      if ( cat.containsKey( "allow_log_output" ) ) cat.remove( "allow_log_output" )
    }

    prop = cfg.get( configId, REPAIR_CONDITION_KEY, REPAIR_CONDITION_DEFAULT )
    prop.setComment( comment( REPAIR_CONDITION_KEY ) )
    prop.setLanguageKey( languageKey( REPAIR_CONDITION_KEY ) )
    data += REPAIR_CONDITION_KEY -> prop.getDouble

    prop = cfg.get( configId, KEEP_ENCHANTMENT_KEY, KEEP_ENCHANTMENT_DEFAULT )
    prop.setComment( comment( KEEP_ENCHANTMENT_KEY ) )
    prop.setLanguageKey( languageKey( KEEP_ENCHANTMENT_KEY ) )
    data += KEEP_ENCHANTMENT_KEY -> prop.getBoolean

    prop = cfg.get( configId, REPAIRABLE_FILEPATH_KEY, REPAIRABLE_FILEPATH_DEFAULT )
    prop.setComment( comment( REPAIRABLE_FILEPATH_KEY ) )
    prop.setLanguageKey( languageKey( REPAIRABLE_FILEPATH_KEY ) )
    prop.requiresMcRestart
    data += REPAIRABLE_FILEPATH_KEY -> prop.getString

    prop = cfg.get( configId, BLACKLIST_FILEPATH_KEY, BLACKLIST_FILEPATH_DEFAULT )
    prop.setComment( comment( BLACKLIST_FILEPATH_KEY ) )
    prop.setLanguageKey( languageKey( BLACKLIST_FILEPATH_KEY ) )
    prop.requiresMcRestart
    data += BLACKLIST_FILEPATH_KEY -> prop.getString

    if ( cfg.hasChanged ) cfg.save()

    FurnasmithSettings.reflectConfigChanges( data.result )
  }
}

class FurnasmithConfigGui( parent: GuiScreen ) extends ConfigGui( parent, FurnasmithConfigHandler )
class FurnasmithGuiFactory extends ConfigGuiFactory[ FurnasmithConfigGui ]
