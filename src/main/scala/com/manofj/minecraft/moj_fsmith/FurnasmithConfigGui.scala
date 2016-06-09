package com.manofj.minecraft.moj_fsmith


import net.minecraft.client.gui.GuiScreen

import com.manofj.minecraft.moj_commons.config.{ ConfigGui, ConfigGuiFactory }


class FurnasmithConfigGui( parent: GuiScreen ) extends ConfigGui( parent, FurnasmithConfigHandler )
class FurnasmithGuiFactory extends ConfigGuiFactory[ FurnasmithConfigGui ]
