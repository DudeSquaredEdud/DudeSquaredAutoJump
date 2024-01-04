package com.dudesquared.autojump.autojumpclient;

import com.dudesquared.autojump.AutoJump;
import com.fox2code.foxloader.loader.ClientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.src.client.player.EntityPlayerSP;
import net.minecraft.src.game.block.Material;
import net.minecraft.src.game.level.World;
import org.lwjgl.input.Keyboard;

public class AutoJumpClient extends AutoJump implements ClientMod {

    @Override
    public void onInit() {
        System.out.println("Autojump Initializing...");
    }

    EntityPlayerSP Ourplayer;
    World Ourworld;
    int zCheck = 0;
    int xCheck = 0;
    int direction = 0;
    Material blockMaterial = null;
    int blockID;

    public void onTick() {
        //Super Jump
        try{
            Ourplayer = Minecraft.getInstance().thePlayer;
            Ourworld = Minecraft.getInstance().theWorld;
            direction = (int) (((Ourplayer.rotationYaw%360)+45))/90;
            // 0 = +z, 2 = -x, 4 = -z, 6 = +x
            switch(direction){
                case 4:
                case 0:     zCheck = 0;
                    xCheck = 0;
                    break;
                case 1:     zCheck = -1;
                    xCheck = -1;
                    break;
                case 2:     zCheck = -2;
                    xCheck = 0;
                    break;
                case 3:     zCheck = -1;
                    xCheck = 1;

                    break;
            }
            blockMaterial = Ourworld.getBlockMaterial((int) Ourplayer.posX+xCheck, (int) Ourplayer.posY-1, (int) Ourplayer.posZ+zCheck);
            blockID = Ourworld.getBlockId((int) Ourplayer.posX+xCheck, (int) Ourplayer.posY-1, (int) Ourplayer.posZ+zCheck);
            if(
                    Keyboard.isKeyDown( Minecraft.theMinecraft.gameSettings.keyBindForward.keyCode)
                            && Ourplayer.onGround
                            && (
                            !Ourplayer.isSneaking()
                                    || !Ourplayer.isJumping
                                    || !Ourplayer.isDead
                                    || !Ourplayer.isPlayerSleeping()
                                    || !Ourplayer.isInWeb
                    )
                            &&
                            (       //there is space for the head to fit
                                    Ourworld.isAirBlock((int) Ourplayer.posX+xCheck, (int) Ourplayer.posY+1, (int) Ourplayer.posZ+zCheck)
                            )
                            &&
                            (       //there is a space to jump into
                                    Ourworld.isAirBlock((int) Ourplayer.posX + xCheck, (int) Ourplayer.posY, (int) Ourplayer.posZ+zCheck)
                            )
                            &&
                            (       //There is a block to stand on
                                    !Ourworld.isAirBlock((int) Ourplayer.posX+xCheck, (int) Ourplayer.posY-1, (int) Ourplayer.posZ+zCheck)
                                            && (blockMaterial != Material.lava && blockMaterial != Material.water && blockMaterial != Material.chair && blockMaterial != Material.cobweb && blockMaterial != Material.moveableCircuit && blockMaterial != Material.quicksand && blockMaterial != Material.circuits)
                                            &&  Ourworld.isBlockOpaqueCube((int) Ourplayer.posX+xCheck, (int) Ourplayer.posY-1, (int) Ourplayer.posZ+zCheck)
                                            && Ourworld.isBlockNormalCube2_do((int) Ourplayer.posX+xCheck, (int) Ourplayer.posY-1, (int) Ourplayer.posZ+zCheck)
                            )
            ) {
                Ourplayer.motionY = .5;
            }
        } catch (Exception ignored){} // No u
    }

}

