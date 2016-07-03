package com.manofj.minecraft.moj_fsmith

import scala.reflect.ClassTag

import com.manofj.minecraft.moj_commons.util.ImplicitConversions.AnyExtension


private[ moj_fsmith ] object FurnasmithSettings {

  final val REPAIR_CONDITION_KEY = "repair_condition"
  final val REPAIR_CONDITION_DEFAULT = 50d

  final val KEEP_ENCHANTMENT_KEY = "keep_enchantment"
  final val KEEP_ENCHANTMENT_DEFAULT = true

  final val REPAIRABLE_FILEPATH_KEY = "repairable_filepath"
  final val REPAIRABLE_FILEPATH_DEFAULT = "user/repairable.txt"

  final val BLACKLIST_FILEPATH_KEY = "blacklist_filepath"
  final val BLACKLIST_FILEPATH_DEFAULT = "user/blacklist.txt"

  final val EXTRA_COOK_TIME_KEY = "extra_cook_time"
  final val EXTRA_COOK_TIME_DEFAULT = 0d


  // ツールが修復可能になる損耗度 ( 百分率 )
  private[ this ] var repairConditionOpt = Option.empty[ Double ]
  def repairCondition: Double = repairConditionOpt.getOrElse( REPAIR_CONDITION_DEFAULT )

  // 修復時にエンチャントを保持するか否か
  private[ this ] var keepEnchantmentOpt = Option.empty[ Boolean ]
  def keepEnchantment: Boolean = keepEnchantmentOpt.getOrElse( KEEP_ENCHANTMENT_DEFAULT )

  // 修復可能リストのファイルパス
  private[ this ] var repairableFilepathOpt = Option.empty[ String ]
  def repairableFilepath: String = repairableFilepathOpt.getOrElse( REPAIRABLE_FILEPATH_DEFAULT )

  // 修復不可能リストのファイルパス
  private[ this ] var blacklistFilepathOpt = Option.empty[ String ]
  def blacklistFilepath: String = blacklistFilepathOpt.getOrElse( BLACKLIST_FILEPATH_DEFAULT )

  // 追加精錬時間のベース値
  private[ this ] var extraCookTimeOpt = Option.empty[ Double ]
  def extraCookTime: Float = extraCookTimeOpt.getOrElse( EXTRA_COOK_TIME_DEFAULT ).toFloat


  // コンフィグの変更を設定値に反映する
  def reflectConfigChanges( data: Map[ String, Any ] ): Unit = {
    def value[ A : ClassTag ]( key: String ): A = data( key ).asInstanceOf[ A ]

    repairConditionOpt    = value[ Double ]( REPAIR_CONDITION_KEY ).?
    keepEnchantmentOpt    = value[ Boolean ]( KEEP_ENCHANTMENT_KEY ).?
    repairableFilepathOpt = value[ String ]( REPAIRABLE_FILEPATH_KEY ).?
    blacklistFilepathOpt  = value[ String ]( BLACKLIST_FILEPATH_KEY ).?
    extraCookTimeOpt      = value[ Double ]( EXTRA_COOK_TIME_KEY ).?

  }

}
