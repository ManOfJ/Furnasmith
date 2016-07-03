package com.manofj.minecraft.moj_fsmith.asm

import scala.collection.convert.WrapAsScala.asScalaBuffer
import scala.collection.convert.WrapAsScala.asScalaIterator

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.Opcodes.ACONST_NULL
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ARETURN
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.objectweb.asm.Opcodes.IRETURN
import org.objectweb.asm.Opcodes.SIPUSH
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode

import net.minecraft.launchwrapper.IClassTransformer

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper.{ INSTANCE => remapper }

import com.manofj.minecraft.moj_fsmith.Furnasmith


/**
  * FurnaceRecipes, TileEntityFurnace クラスの書き換えを行うトランスフォーマー
  */
class FurnasmithTransformer
  extends IClassTransformer
{
  // フックのハンドラオブジェクトの内部名称
  private[ this ] val FurnasmithHooks = "com/manofj/minecraft/moj_fsmith/FurnasmithHooks$"

  // 汎用の型オブジェクト､ディスクリプター
  private[ this ] val ItemStackType = Type.getObjectType( remapper.unmap( "net/minecraft/item/ItemStack" ) )
  private[ this ] val NullableDescriptor = "Ljavax/annotation/Nullable;"


  // transformFurnaceRecipes 関数内で使用する定数群
  private[ this ] val FurnaceRecipes = "net.minecraft.item.crafting.FurnaceRecipes"
  private[ this ] val getSmeltingResult = "getSmeltingResult"
  private[ this ] val getSmeltingResultDescriptor = Type.getMethodDescriptor( ItemStackType, ItemStackType )

  // FurnaceRecipes#getSmeltingResult の return null; を
  // return FurnasmithHooks.getSmeltingResult( item ); に書き換える
  private[ this ] def transformFurnaceRecipes( rawName:    String,
                                               mappedName: String,
                                               bytes:      Array[ Byte ] ): Array[ Byte ] = {
    val process: MethodNode => Unit = { method =>
      val insts = method.instructions
      insts.iterator foreach {
        case node: InsnNode if node.getOpcode == ACONST_NULL => node.getNext match {
        case next: InsnNode if next.getOpcode == ARETURN     =>
            Furnasmith.info( s"Method $rawName.${ method.name }${ method.desc } " +
              s"Replacing ACONST_NULL with INVOKEVIRTUAL $FurnasmithHooks.$getSmeltingResult" )

            val newCode = new InsnList
            newCode.add( new FieldInsnNode( GETSTATIC, FurnasmithHooks, "MODULE$", s"L$FurnasmithHooks;" ) )
            newCode.add( new VarInsnNode( ALOAD, 1 ) )
            newCode.add( new MethodInsnNode( INVOKEVIRTUAL, FurnasmithHooks, getSmeltingResult, method.desc, false ) )

            insts.insert( node, newCode )
            insts.remove( node )
          case _ =>
    } case _ => } }

    val classNode   = new ClassNode
    val classReader = new ClassReader( bytes )
    classReader.accept( classNode, 0 )

    classNode.methods.foreach {
      // メソッド名が一致する場合( 開発環境下のみ該当 )
      case method if method.name == getSmeltingResult => process( method )

      // メソッドのディスクリプションが一致する場合( プロダクション環境下ではこちらが該当 )
      case method if method.desc == getSmeltingResultDescriptor =>
        // FurnaceRecipes にて上記ディスクリプションに該当するメソッドは
        // 現状 getSmeltingResult のみだが一応､誤爆防止用にアノテーションをチェックする
        for { annotations <- Option( method.visibleAnnotations )
              annotation  <- Option( annotations.get( 0 ) )

          if annotation.desc == NullableDescriptor
        } process( method )

      // 対象のメソッドではない
      case _ => // 何もしない

    }

    val classWriter = new ClassWriter( COMPUTE_MAXS )
    classNode.accept( classWriter )
    classWriter.toByteArray
  }

  // transformTileEntityFurnace 関数内で使用する定数群
  private[ this ] val TileEntityFurnace = "net.minecraft.tileentity.TileEntityFurnace"
  private[ this ] val getCookTime = "getCookTime"
  private[ this ] val getCookTimeDescriptor = Type.getMethodDescriptor( Type.INT_TYPE, ItemStackType )

  // TileEntityFurnace#getCookTime の return 200; を
  // return FurnasmithHooks.getCookTime( item ); に書き換える
  private[ this ] def transformTileEntityFurnace( rawName:    String,
                                                  mappedName: String,
                                                  bytes:      Array[ Byte ] ): Array[ Byte ] = {
    val process: MethodNode => Unit = { method =>
      val insts = method.instructions
      insts.iterator foreach {
        case node: IntInsnNode if node.getOpcode == SIPUSH  => node.getNext match {
        case next: InsnNode    if next.getOpcode == IRETURN =>
            Furnasmith.info( s"Method $rawName.${ method.name }${ method.desc } " +
              s"Replacing SIPUSH with INVOKEVIRTUAL $FurnasmithHooks.$getCookTime" )

            val newCode = new InsnList
            newCode.add( new FieldInsnNode( GETSTATIC, FurnasmithHooks, "MODULE$", s"L$FurnasmithHooks;" ) )
            newCode.add( new VarInsnNode( ALOAD, 1 ) )
            newCode.add( new MethodInsnNode( INVOKEVIRTUAL, FurnasmithHooks, getCookTime, method.desc, false ) )

            insts.insert( node, newCode )
            insts.remove( node )
        case _ =>
    } case _ => } }

    val classNode   = new ClassNode
    val classReader = new ClassReader( bytes )
    classReader.accept( classNode, 0 )

    classNode.methods.foreach {
      // メソッド名が一致する場合( 開発環境下のみ該当 )
      case method if method.name == getCookTime => process( method )

      // メソッドのディスクリプションが一致する場合( プロダクション環境下ではこちらが該当 )
      case method if method.desc == getCookTimeDescriptor =>
        // TileEntityFurnace にて上記ディスクリプションに該当するメソッドは複数存在するため
        // 誤爆防止用にパラメータアノテーションをチェックする
        for { annotationLists <- Option( method.visibleParameterAnnotations )
              annotationList  <- Option( annotationLists( 0 ) )
              annotation      <- annotationList

          if annotation.desc == NullableDescriptor
        } { process( method ) }

      // 対象のメソッドではない
      case _ => // 何もしない
    }

    val classWriter = new ClassWriter( COMPUTE_MAXS )
    classNode.accept( classWriter )
    classWriter.toByteArray
  }


  override def transform( rawName:    String,
                          mappedName: String,
                          bytes:      Array[ Byte ] ): Array[ Byte ] =
    mappedName match {
      case FurnaceRecipes => transformFurnaceRecipes( rawName, mappedName, bytes )
      case TileEntityFurnace => transformTileEntityFurnace( rawName, mappedName, bytes )
      case _  => bytes
    }

}
