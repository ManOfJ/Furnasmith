package manofj.com.github.moj_fsmith

import java.util.Collections
import java.io.{ File => JFile }

import com.google.common.collect.Lists
import net.minecraft.client.resources.I18n.{ format => i18n }
import net.minecraftforge.common.config.{ Property, ConfigElement, Configuration }
import net.minecraftforge.fml.client.config.IConfigElement
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


/**
  * Furnasmith-Mod のコンフィグハンドラ
  */
object FurnasmithConfigHandler {
  final val CONFIG_ID = Configuration.CATEGORY_GENERAL


  private[ this ] var configDirectory = Option.empty[ JFile ]

  private[ this ] var config = Option.empty[ Configuration ]

  private[ this ] var alOutput = false

  private[ this ] var rCondition = 50.0

  private[ this ] var kEnchantment = true


  // ログにアイテムの情報を出力するか否か
  private[ moj_fsmith ] def allow_log_output = alOutput

  // ツールが修復可能になる損耗度 ( 百分率 )
  private[ moj_fsmith ] def repair_condition = rCondition

  // 修復時にエンチャントを保持するか否か
  private[ moj_fsmith ] def keep_enchantment = kEnchantment


  // コンフィグの読み込み､変数への反映処理などを行う
  private[ this ] def syncConfig( load: Boolean ): Unit = config match {
    case Some( cfg ) =>
      if ( load && !cfg.isChild ) cfg.load()

      val order = Lists.newArrayList[ String ]
      var prop  = null: Property

      prop = cfg.get( CONFIG_ID, "allow_log_output", false )
      prop.comment = i18n( "moj_fsmith.config.allow_log_output.tooltip" )
      prop.setLanguageKey( "moj_fsmith.config.allow_log_output" )
      order add prop.getName
      alOutput = prop.getBoolean

      Furnasmith.log.debug( s"Bind allow_log_output: $alOutput" )

      prop = cfg.get( CONFIG_ID, "repair_condition", 50.0 )
      prop.comment = i18n( "moj_fsmith.config.repair_condition.tooltip" )
      prop.setLanguageKey( "moj_fsmith.config.repair_condition" )
      order add prop.getName
      rCondition = prop.getDouble

      Furnasmith.log.debug( s"Bind repair_condition: $rCondition" )

      prop = cfg.get( CONFIG_ID, "keep_enchantment", true )
      prop.comment = i18n( "moj_fsmith.config.keep_enchantment.tooltip" )
      prop.setLanguageKey( "moj_fsmith.config.keep_enchantment" )
      order add prop.getName
      kEnchantment = prop.getBoolean

      Furnasmith.log.debug( s"Bind keep_enchantment: $kEnchantment" )

      cfg.setCategoryPropertyOrder( CONFIG_ID, order )

      if ( cfg.hasChanged ) cfg.save()
    case None        =>
      Furnasmith.log.warn( "Config is not bind to FurnasmithConfigHandler." )
  }

  // コンフィグファイルの取得､ローカル変数へのバインドを行う
  private[ moj_fsmith ] def captureConfig( cfgDir: JFile ): Unit = {
    configDirectory = Option( cfgDir )
    config = configDirectory map { dir =>
      val cfgFile = new JFile( dir, s"$NAME.cfg" )
      new Configuration( cfgFile )
    }
    syncConfig( true )
  }

  private[ moj_fsmith ] def configElements = config match {
    case Some( cfg ) =>
      val cat = cfg.getCategory( CONFIG_ID )
      new ConfigElement( cat ).getChildElements
    case None        =>
      Furnasmith.log.warn( "Config is not bind to FurnasmithConfigHandler." )
      Collections.emptyList[ IConfigElement ]
  }

  // コンフィグディレクトリを返す
  def configDir = configDirectory

  @SubscribeEvent
  def onConfigChanged( evt: OnConfigChangedEvent ): Unit = {
    import evt._
    if ( modID == MOD_ID && configID == CONFIG_ID ) {
      syncConfig( false )
    }
  }
}
