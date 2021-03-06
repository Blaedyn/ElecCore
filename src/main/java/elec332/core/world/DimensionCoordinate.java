package elec332.core.world;

import elec332.core.main.ElecCore;
import elec332.core.util.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 22-7-2016.
 */
public final class DimensionCoordinate implements INBTSerializable<NBTTagCompound> {

    public DimensionCoordinate(DimensionCoordinate dim){
        this(dim.dim, dim.pos);
    }

    public DimensionCoordinate(World world, BlockPos pos){
        this(WorldHelper.getDimID(world), pos);
    }

    public DimensionCoordinate(int dimension, BlockPos pos){
        this.pos = Validate.notNull(pos, "Cannot have a DimensionCoordinate with a null BlockPos!");
        this.dim = dimension;
    }

    private final BlockPos pos;
    private final int dim;

    @Nonnull
    public BlockPos getPos(){
        return pos;
    }

    public int getDimension(){
        return dim;
    }

    @Nullable
    public World getWorld(){
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
            World world = ElecCore.proxy.getClientWorld();
            if (WorldHelper.getDimID(world) == dim){
                return world;
            }
            return null;
        }
        return DimensionManager.getWorld(dim);
    }

    @Nullable
    public TileEntity getTileEntity(){
        return getTileEntity(getWorld());
    }

    @Nullable
    public TileEntity getTileEntity(World world){
        if (loaded(world)){
            return WorldHelper.getTileAt(world, pos);
        }
        return null;
    }

    @Nullable
    public IBlockState getBlockState(){
        return getBlockState(getWorld());
    }

    @Nullable
    public IBlockState getBlockState(World world){
        if (loaded(world)){
            return WorldHelper.getBlockState(world, pos);
        }
        return null;
    }

    public boolean isLoaded() {
        return loaded(getWorld());
    }

    private boolean loaded(World world){
        return world != null && WorldHelper.chunkLoaded(world, pos);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTHelper().addToTag(pos).addToTag(dim, "dim").serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        throw new UnsupportedOperationException();
    }

    public static DimensionCoordinate fromNBT(NBTTagCompound tag){
        NBTHelper nbt = new NBTHelper(tag);
        return new DimensionCoordinate(nbt.getInteger("dim"), nbt.getPos());
    }

    public static DimensionCoordinate fromTileEntity(TileEntity tile){
        return new DimensionCoordinate(tile.getWorld(), tile.getPos());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DimensionCoordinate && ((DimensionCoordinate) obj).pos.equals(pos) && ((DimensionCoordinate) obj).dim == dim;
    }

    @Override
    public int hashCode() {
        return 31 * pos.hashCode() + dim;
    }

    @Override
    public String toString() {
        return "[DimensionCoordinate: "+pos.toString() + " dim: "+dim+"]";
    }

}
