package net.myshampooisdrunk.hiddenisles.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ScaffoldingBlock;
import net.minecraft.util.math.*;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.World;

import java.util.Iterator;

import static net.minecraft.block.Blocks.*;
import static net.minecraft.world.gen.feature.Feature.isAir;

public class MathUtils {

    public static boolean isValidPosition(BlockPos block, TestableWorld world, World world2){
        boolean notSolid = false;
        if(!(world2.getBlockState(block).getMaterial().isSolid()) || (world2.getBlockState(block).getMaterial().isLiquid())){
            notSolid = true;
        }
        if(world2.getBlockState(block).equals(SCAFFOLDING.getDefaultState()) || world2.getBlockState(block).equals(LAVA.getDefaultState()) ){
            notSolid = false;
        }
        return isAir(world,block) || notSolid;
    }

    public static double map(double x, double in_min, double in_max, double out_min, double out_max) {//taken from arduino map function
        return ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
    }



}
