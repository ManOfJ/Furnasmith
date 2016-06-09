package com.manofj.minecraft.moj_fsmith


import com.google.common.collect.Lists

import net.minecraft.client.resources.I18n.{ format => i18n }

import net.minecraftforge.common.config.Property

import com.manofj.minecraft.moj_commons.config.ConfigGuiHandler


/**
  * Furnasmith-Mod のコンフィグハンドラ
  */
object FurnasmithConfigHandler
  extends ConfigGuiHandler
{

  private[ this ] var alOutput = false

  private[ this ] var rCondition = 50.0

  private[ this ] var kEnchantment = true


  // ログにアイテムの情報を出力するか否か
  private[ moj_fsmith ] def allow_log_output = alOutput

  // ツールが修復可能になる損耗度 ( 百分率 )
  private[ moj_fsmith ] def repair_condition = rCondition

  // 修復時にエンチャントを保持するか否か
  private[ moj_fsmith ] def keep_enchantment = kEnchantment


  override def modId: String = MOD_ID

  override def title: String = i18n( "moj_fsmith.config.gui.title" )

  override def syncConfig( load: Boolean ): Unit = {
    val cfg = config

    if ( load && !cfg.isChild ) cfg.load()

    val order = Lists.newArrayList[ String ]
    var prop  = null: Property

    prop = cfg.get( configId, "allow_log_output", false )
    prop.setComment( i18n( "moj_fsmith.config.allow_log_output.tooltip" ) )
    prop.setLanguageKey( "moj_fsmith.config.allow_log_output" )
    order add prop.getName
    alOutput = prop.getBoolean

    Furnasmith.log.debug( s"Bind allow_log_output: $alOutput" )

    prop = cfg.get( configId, "repair_condition", 50.0 )
    prop.setComment( i18n( "moj_fsmith.config.repair_condition.tooltip" ) )
    prop.setLanguageKey( "moj_fsmith.config.repair_condition" )
    order add prop.getName
    rCondition = prop.getDouble

    Furnasmith.log.debug( s"Bind repair_condition: $rCondition" )

    prop = cfg.get( configId, "keep_enchantment", true )
    prop.setComment( i18n( "moj_fsmith.config.keep_enchantment.tooltip" ) )
    prop.setLanguageKey( "moj_fsmith.config.keep_enchantment" )
    order add prop.getName
    kEnchantment = prop.getBoolean

    Furnasmith.log.debug( s"Bind keep_enchantment: $kEnchantment" )

    cfg.setCategoryPropertyOrder( configId, order )

    if ( cfg.hasChanged ) cfg.save()
  }
}
