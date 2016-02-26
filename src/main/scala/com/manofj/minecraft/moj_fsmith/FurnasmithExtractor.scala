package com.manofj.minecraft.moj_fsmith

import java.io.{ File => JFile }

import scala.collection.JavaConversions.asScalaIterator
import scala.collection.mutable.{ Buffer => MutableBuffer }

import org.apache.commons.io.FileUtils

import net.minecraft.item.{ Item, ItemStack }

import com.manofj.minecraft.moj_fsmith.FurnasmithConfigHandler.repair_condition


/**
  * Furnasmith-Mod が使用する
  * match 式で使用する抽出子
  */
object FurnasmithExtractor {

  /**
    * ItemStack を受け取り Boolean を返す抽出子のトレイト
    * 内部に PartialFunction[ ItemStack, Boolean ] の集合を持っており
    * この集合のいずれかの条件に当てはまる場合､抽出子は true を返す
    */
  sealed trait ConditionList {
    type ItemCondition = PartialFunction[ ItemStack, Boolean ]

    private[ this ] val format = "^\\s*+(CL|RL)\\s*+([\\w.:]++)\\s*+$".r

    /**
      * ディレクトリ %dir%/user 配下の %listFileName% ファイルを取得する
      * ファイルが見つからない場合は dir の親ディレクトリを新たな dir として再帰的に検索する
      * @param dir 最初に検索を行うディレクトリ
      * @return 最初に見つかったデータファイルを内包する Option, 見つからなければ None
      */
    private[ this ] def discoverDataFile( dir: JFile ): Option[ JFile ] = {
      val childDir = new JFile( dir, "user" )
      val result = new JFile( childDir, listFileName )

      Furnasmith.log.debug( s"Find condition data file: ${ result.getAbsolutePath }" )

      if ( result.isFile ) Some( result )
      else Option( dir.getParentFile ) match {
        case Some( parent ) => discoverDataFile( parent )
        case None           => None
      }
    }


    protected[ this ] val conditions: MutableBuffer[ ItemCondition ]

    // このリストの条件が記述された外部ファイルの名前 ( 拡張子を含む )
    protected[ this ] val listFileName: String


    def unapply( item: ItemStack ): Boolean = Option( item ) match {
      case Some( is ) => conditions.exists( _( is ) )
      case None       => false
    }

    def addCondition( condition: ItemCondition ): Unit = { conditions += condition }

    def loadConditions( cfgDir: JFile ): Unit = {
      def findClass( cl: String ): Option[ Class[ _ ] ] =
        try { Option( Class.forName( cl ) ) }
        catch { case e: ClassNotFoundException =>
          Furnasmith.log.warn( s"Illegal class name: $cl" )
          None
        }

      def findItem( rl: String ): Option[ Item ] =
        Item.getByNameOrId( rl ) match {
          case item: Item => Option( item )
          case _ =>
            Furnasmith.log.warn( s"Illegal resource location: $rl" )
            None
        }

      discoverDataFile( cfgDir ) match {
        case Some( file ) =>
          val listName = this.getClass.getSimpleName

          Furnasmith.log.debug( s"Load the $listName conditions from ${ file.getAbsolutePath }" )

          val sizeBefore = conditions.size

          FileUtils.lineIterator( file, "utf-8" ) foreach {
            case format( "CL", data ) => findClass( data ) foreach { clazz =>
              Furnasmith.log.debug( s"Loaded condition: Type = ClassName, Value = $data" )
              addCondition { case is => clazz.isAssignableFrom( is.getItem.getClass ) }
            }
            case format( "RL", data ) => findItem( data ) foreach { item =>
              Furnasmith.log.debug( s"Loaded condition: Type = ResourceLocation, Value = $data" )
              addCondition { case is => is.getItem == item }
            }
            case _ => // skip
          }

          val sizeAfter = conditions.size

          Furnasmith.log.debug( s"$listName has loaded ${ sizeAfter - sizeBefore } condition(s)." )
        case None =>
          Furnasmith.log.debug( s"$listFileName is not found." )
      }
    }
  }


  /**
    * 修復不可能リスト
    * このリストの条件は修復可能リストよりも優先される
    * このリストの条件に一致するアイテムは修復不可能であると判断される
    */
  object Blacklist extends ConditionList {

    override protected[ this ] val conditions = MutableBuffer[ ItemCondition ] {
      // アイテムの損耗率がコンフィグで指定された値を下回る場合は修復不可能とする
      case is => ( 100F / ( is.getMaxDamage.toFloat / is.getItemDamage ) ) < repair_condition
    }

    override protected[ this ] val listFileName = "blacklist.txt"

  }

  /**
    * 修復可能リスト
    * このリストの条件に一致するアイテムは修復可能であると判断される
    */
  object Repairable extends ConditionList {

    override protected[ this ] val conditions = MutableBuffer[ ItemCondition ] {
      // アイテムのインスタンス関数 isRepairable が true を返すなら修復可能とする
      case is => is.getItem.isRepairable
    }

    override protected[ this ] val listFileName = "repairable.txt"

  }
}
