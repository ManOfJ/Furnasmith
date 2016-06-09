package com.manofj.minecraft

/**
  * Furnasmith-Mod のルートパッケージオブジェクト
  * 外部から参照可能な基本情報を定義しておく
  */
package object moj_fsmith {
  final val MOD_ID      = "@modid@"
  final val NAME        = "Furnasmith"
  final val VERSION     = "@version@"
  final val UPDATE_JSON = "http://manofj.com/minecraft/update?v=Furnasmith"
  final val LANGUAGE    = "scala"
  final val GUI_FACTORY = "com.manofj.minecraft.moj_fsmith.FurnasmithGuiFactory"
}
