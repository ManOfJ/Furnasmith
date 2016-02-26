package com.manofj.minecraft.moj_fsmith

import java.util.{ Set => JSet }

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n

import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.{ RuntimeOptionCategoryElement, RuntimeOptionGuiHandler }
import net.minecraftforge.fml.client.config.GuiConfig

import com.manofj.minecraft.moj_fsmith.FurnasmithConfigHandler.{ CONFIG_ID, configElements }


/**
  * Furnasmith-Mod の GUI ファクトリ
  * FurnasmithConfigGui のクラスを提供するだけの存在
  */
class FurnasmithGuiFactory
  extends IModGuiFactory
{
  override def runtimeGuiCategories( ): JSet[ RuntimeOptionCategoryElement ] = null

  override def initialize( minecraftInstance: Minecraft ): Unit = {}

  override def getHandlerFor( element: RuntimeOptionCategoryElement ): RuntimeOptionGuiHandler = null

  override def mainConfigGuiClass( ): Class[ _ <: GuiScreen ] = classOf[ FurnasmithConfigGui ]
}

/**
  * Furnasmith-Mod のコンフィグ GUI
  * @param guiScreen 親画面
  */
class FurnasmithConfigGui( guiScreen: GuiScreen )
  extends
    GuiConfig( guiScreen,
      configElements,
      MOD_ID,
      CONFIG_ID,
      false,
      false,
      I18n.format( "moj_fsmith.config.gui.title" ) )
