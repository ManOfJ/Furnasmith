package manofj.com.github.moj_fsmith

import scala.collection.JavaConversions.asScalaSet
import scala.collection.mutable.{ WeakHashMap => MutableWeakHashMap }
import scala.language.existentials

import manofj.com.github.moj_fsmith.FurnasmithConfigHandler.keep_enchantment
import manofj.com.github.moj_fsmith.FurnasmithExtractor.{ Blacklist, Repairable }
import net.minecraft.item.{ Item, ItemBlock, ItemStack }
import net.minecraft.util.{ ResourceLocation, StatCollector }
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{ FMLPostInitializationEvent, FMLPreInitializationEvent }
import net.minecraftforge.fml.common.registry.GameData
import net.minecraftforge.fml.common.{ FMLCommonHandler, Mod }
import org.apache.logging.log4j.{ LogManager, Logger }


/**
  * Furnasmith の Mod オブジェクト
  * 各種設定を行うほか、指定されたアイテムスタックが
  * 修理可能であるか評価する関数を定義しておく
  */
@Mod( modid       = MOD_ID,
      name        = NAME,
      version     = VERSION,
      modLanguage = LANGUAGE,
      guiFactory  = GUI_FACTORY )
object Furnasmith {
  private[ this ] var loggerOpt = Option.empty[ Logger ]

  // getSmeltingResult がコールされるたびに ItemStack の
  // インスタンスが生成されるのを防ぐため､弱参照のマップでキャッシュする
  private[ this ] val resultCache = MutableWeakHashMap.empty[ ItemStack, ItemStack ]

  // ロガーのゲッタ
  def log = loggerOpt getOrElse LogManager.getLogger( MOD_ID )

  /**
    * FurnaceRecipes の getSmeltingResult の最後に追加される処理
    * パラメータ item に対応するレシピがない場合､こちらの処理が呼ばれる
    *
    * パラメータ item が null ではなく､修復付加条件に当てはまるアイテムでもない場合は
    * 修復可能条件に当てはまるか確認｡ 条件を満たしていればダメージ値がゼロの同アイテムを返す
    * @param item かまどに入れられたアイテム
    * @return 諸条件を満たしていれば 'ダメージ値がゼロの同アイテム' そうでなければ null を返す
    */
  def getSmeltingResult( item: ItemStack ): ItemStack = item match {
    case Blacklist()  => null
    case Repairable() =>
      // キャッシュに対応するアイテムがあればそれを返す
      // なければ新規に生成してキャッシュに追加
      resultCache.getOrElseUpdate( item, {
        Furnasmith.log.trace( s"Cache loading. Item hash:${ item.hashCode }" )
        val result = item.copy()
        result.setItemDamage( 0 )
        // コンフィグの条件により､エンチャントを消去する
        if ( result.hasTagCompound && !keep_enchantment )
          result.getTagCompound.removeTag( "ench" )
        result
      })
    case _ => null
  }

  @EventHandler
  def preInit( evt: FMLPreInitializationEvent ): Unit = {
    loggerOpt = Option( evt.getModLog )

    FurnasmithConfigHandler.captureConfig( evt.getModConfigurationDirectory )
    FMLCommonHandler.instance.bus.register( FurnasmithConfigHandler )
  }

  @EventHandler
  def postInit( evt: FMLPostInitializationEvent ): Unit = {
    if ( FurnasmithConfigHandler.allow_log_output ) {
      import GameData.{ getItemRegistry => itemRegistry }
      import StatCollector.{ translateToLocal => i18n }

      // すべてのブロックではないアイテムの情報をログに出力
      log.info( "*" * 64 )
      log.info( "Item Name,Class Name,SuperClass Name,Resource Location" )
      itemRegistry.getKeys foreach {
        case loc: ResourceLocation => itemRegistry.getObject( loc ) match {
          case block: ItemBlock => // skip
          case item: Item       =>
            val iName  = i18n( s"${ item.getUnlocalizedName }.name" )
            val clazz  = item.getClass
            val sClass = clazz.getSuperclass
            log.info( s"$iName,${ clazz.getName },${ sClass.getName },${ loc.toString }" )
        }
        case any => // skip
      }
      log.info( "*" * 64 )
    }

    // 外部ファイルから修復可能･不可能リストを設定する
    FurnasmithConfigHandler.configDir foreach { dir =>
      Blacklist.loadConditions( dir )
      Repairable.loadConditions( dir )
    }
  }
}
