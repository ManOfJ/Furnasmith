package com.manofj.minecraft.moj_fsmith.asm

import scala.collection.JavaConversions.{ asScalaBuffer, asScalaIterator }

import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import org.objectweb.asm.Opcodes.{ ACONST_NULL, ALOAD, GETSTATIC, INVOKEVIRTUAL }
import org.objectweb.asm.tree.{ ClassNode, FieldInsnNode, InsnList, MethodInsnNode, MethodNode, VarInsnNode }
import org.objectweb.asm.{ ClassReader, ClassWriter, Type }

import net.minecraft.launchwrapper.IClassTransformer

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper.{ INSTANCE => remapper }

import com.manofj.minecraft.moj_fsmith.Furnasmith


/**
  * FurnaceRecipes クラスの書き換えを行うトランスフォーマー
  */
class FurnasmithTransformer
  extends IClassTransformer
{
  // transform 関数内で使用する定数群
  // 可読性重視のため命名規則を無視している
  private[ this ] val FurnaceRecipes    = "net.minecraft.item.crafting.FurnaceRecipes"
  private[ this ] val getSmeltingResult = "getSmeltingResult"
  private[ this ] val FurnasmithObj     = "com/manofj/minecraft/moj_fsmith/Furnasmith$"
  private[ this ] val MethodDescriptor  = {
    val unmapItemStack = Type.getObjectType( remapper.unmap( "net/minecraft/item/ItemStack" ) )
    Type.getMethodDescriptor( unmapItemStack, unmapItemStack )
  }


  /**
    * FurnaceRecipes.getSmeltingResult 関数内の return null という箇所を
    * return Furnasmith.getSmeltingResult( itemStack ) に書き換える作業を行う
    *
    * @param name クラスファイルから読み取ったそのままのクラス名
    * @param transformedName 上記 name パラメータをマッピングしたもの
    * @param basicClass クラスの情報をバイト配列化したもの
    * @return 書き換え後のデータ
    */
  override def transform( name:            String,
                          transformedName: String,
                          basicClass:      Array[ Byte ] ): Array[ Byte ] =
    transformedName match {
      case FurnaceRecipes =>
        def hooking( method: MethodNode ): Unit = {
          val instructions = method.instructions
          instructions.iterator foreach {
            case insnNode if insnNode.getOpcode == ACONST_NULL =>
              Furnasmith.log.info( s"Method $name.${ method.name }${ method.desc } " +
                s"Replacing ACONST_NULL with INVOKEVIRTUAL $FurnasmithObj.$getSmeltingResult" )

              val list = new InsnList
              list.add( new FieldInsnNode( GETSTATIC, FurnasmithObj, "MODULE$", s"L$FurnasmithObj;" ) )
              list.add( new VarInsnNode( ALOAD, 1 ) )
              list.add( new MethodInsnNode( INVOKEVIRTUAL, FurnasmithObj, getSmeltingResult, method.desc, false ) )

              instructions.insert( insnNode, list )
              instructions.remove( insnNode )
            case _ =>
          }
        }

        val classNode   = new ClassNode
        val classReader = new ClassReader( basicClass )
        classReader.accept( classNode, 0 )

        classNode.methods.foreach {
          case method if method.name == getSmeltingResult => hooking( method )
          case method if method.desc == MethodDescriptor  => hooking( method )
          case _                                          =>
        }

        val classWriter = new ClassWriter( COMPUTE_MAXS )
        classNode.accept( classWriter )
        classWriter.toByteArray
      case _  => basicClass
    }
}
