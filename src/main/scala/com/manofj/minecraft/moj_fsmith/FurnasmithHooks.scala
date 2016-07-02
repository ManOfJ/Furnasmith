package com.manofj.minecraft.moj_fsmith

import java.net.URI

import scala.collection.convert.WrapAsScala.asScalaIterator
import scala.collection.mutable.{ ArrayBuffer => MutableBuffer }
import scala.collection.mutable.{ WeakHashMap => MutableWeakHashMap }
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.apache.commons.io.FileUtils

import net.minecraft.item.Item
import net.minecraft.item.ItemStack

import net.minecraftforge.fml.common.Loader

import com.manofj.minecraft.moj_commons.io.java.alias.JavaFile
import com.manofj.minecraft.moj_commons.minecraft.item.ImplicitConversions.ItemStackExtension
import com.manofj.minecraft.moj_commons.util.ImplicitConversions.AnyExtension


object FurnasmithHooks {
  import com.manofj.minecraft.moj_fsmith.{ FurnasmithSettings => settings }

  // アイテムの修復 可/不可 条件
  private[ this ] type Condition = ItemStack => Boolean

  // getSmeltingResult がコールされるたびに ItemStack の
  // インスタンスが生成されるのを防ぐため､弱参照のマップでキャッシュする
  private[ this ] val resultCache = MutableWeakHashMap.empty[ ItemStack, ItemStack ]


  private[ this ] val repairableConditions = MutableBuffer( { item: ItemStack =>
    // アイテムオブジェクトが null でなく､メソッド isRepairable が true を返すなら修復可能とする
    item.getItem.?.exists( _.isRepairable )
  } )

  private[ this ] val unrepairableConditions = MutableBuffer( { item: ItemStack =>
    // アイテムの損耗率がコンフィグで指定された値を下回る場合は修復不可能とする
    ( 100F / ( item.getMaxDamage.toFloat / item.getItemDamage ) ) < settings.repairCondition
  } )


  // 指定されたアイテムが修復可能であるか評価する
  // アイテムが `null` でなく､修復不可能条件のいずれにも一致せず
  // 修復可能条件のいずれかに一致する場合に `true` を返す
  private[ this ] def isItemRepairable( item: ItemStack ): Boolean =
    item.?.isDefined &&
    unrepairableConditions.forall( !_( item ) ) &&
    repairableConditions.exists( _( item ) )


  // コンフィグにて指定された外部ファイルから修復可能･不可能リストを設定する
  private[ moj_fsmith ] def setupConditions(): Unit =
    Map( settings.repairableFilepath -> repairableConditions,
         settings.blacklistFilepath  -> unrepairableConditions )
      .foreach { case ( filepath, conditions ) =>
        val configDirUri = Loader.instance.getConfigDir.toURI
        val filepathUri = new URI( s"file:///$filepath" )

        new JavaFile( configDirUri.resolve( filepathUri ) ) match {
          case x if !x.isFile => Furnasmith.warn( s"File not found: ${ x.getAbsolutePath }" )
          case file =>
            Furnasmith.debug( s"Load a conditions from ${ file.getAbsolutePath }" )

            val conditionFormat = "^\\s*+(CL|RL)\\s*+([\\w.:]++)\\s*+$".r
            FileUtils.lineIterator( file, "UTF-8" ).foreach {

              // クラス指定の条件
              case conditionFormat( "CL", data ) =>
                Try( Class.forName( data ) ) match {
                  case Success( clazz ) =>
                    conditions += { item => clazz.isAssignableFrom( item.getClass ) }
                    Furnasmith.debug( s"Condition loaded: Type = CL, Value = $data" )

                  case Failure( e ) =>
                    Furnasmith.warn( s"Class name is invalid: $data" )
                }

              // リソースロケーション指定の条件
              case conditionFormat( "RL", data ) =>
                Item.getByNameOrId( data ) match {
                  case item: Item =>
                    conditions += { _.getItem == item }
                    Furnasmith.debug( s"Condition loaded: Type = RL, Value = $data" )

                  case _ =>
                    Furnasmith.warn( s"Resource location is invalid: $data" )
                }

              // 書式外の文字列
              case _ => // 何もしない

      } } }


  /**
    * 指定されたアイテムが修復可能であれば修復後のアイテムを返す
    * アイテムが `null` あるいは修復不可能なアイテムの場合は `null` を返す
    */
  def getSmeltingResult( item: ItemStack ): ItemStack =
    if ( !isItemRepairable( item ) ) null
    else {
      // キャッシュに対応するアイテムがあればそれを返す
      // なければ新規に生成してキャッシュに追加
      resultCache.getOrElseUpdate( item, {
        Furnasmith.trace( s"Cache loading, Item hash:${ item.hashCode }" )

        item.duplicate( damage = 0 ) << { copy =>
          // コンフィグの条件により､エンチャントを消去する
          if ( copy.hasTagCompound && settings.keepEnchantment )
            copy.getTagCompound.removeTag( "ench" )

      } } )
    }



}
