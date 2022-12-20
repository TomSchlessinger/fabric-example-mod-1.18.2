package net.myshampooisdrunk.hiddenisles.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.myshampooisdrunk.hiddenisles.entity.registry.EntityRegistry;
import net.myshampooisdrunk.hiddenisles.util.MathUtils;

import java.util.List;
import java.util.Optional;

public class TrocellateMissile extends PersistentProjectileEntity {

    private LivingEntity shooter;
    private int type;//type 0 does damage, type 1 does no damage

    public TrocellateMissile(LivingEntity owner, World world) {
        super(EntityRegistry.MISSILE, owner, world);
    }

    public TrocellateMissile(World world, double x, double y, double z, LivingEntity attacker, int type) {
        super(EntityRegistry.MISSILE, x, y, z, world);
        this.shooter = attacker;
        this.type = type;
    }

    public TrocellateMissile(EntityType type, World world) {
        super(type, world);
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
    }

    @Override
    public void tick(){
        super.tick();
        if (this.age >= 100) {
            this.remove(Entity.RemovalReason.DISCARDED);
        }

    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.world.isClient) {
            this.doDamage();
            this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 1.7F, false,
                    Explosion.DestructionType.NONE);
            this.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean hasNoGravity() {return false;}

    @Override
    public void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.world.isClient) {
            this.doDamage();
            this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 1.7F, false,
                    Explosion.DestructionType.NONE);
            this.remove(Entity.RemovalReason.DISCARDED);
        }
        this.setSound(SoundEvents.ENTITY_GENERIC_EXPLODE);
    }

    public void doDamage() {
        float q = 4.0F;
        int k = MathHelper.floor(this.getX() - (double) q - 1.0D);
        int l = MathHelper.floor(this.getX() + (double) q + 1.0D);
        int t = MathHelper.floor(this.getY() - (double) q - 1.0D);
        int u = MathHelper.floor(this.getY() + (double) q + 1.0D);
        int v = MathHelper.floor(this.getZ() - (double) q - 1.0D);
        int w = MathHelper.floor(this.getZ() + (double) q + 1.0D);
        List<Entity> list = this.world.getOtherEntities(this,
                new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
        Vec3d vec3d = new Vec3d(this.getX(), this.getY(), this.getZ());
        for (int x = 0; x < list.size(); ++x) {
            Entity entity = (Entity) list.get(x);
            double y = (double) (MathHelper.sqrt( (float) entity.squaredDistanceTo(vec3d)) / q);

            if (y <= 2.0D) {
                if (entity instanceof LivingEntity) {
                    if(this.type!=1){entity.damage(DamageSource.player((PlayerEntity) this.shooter), 15);}
                }
                this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625D), this.getZ(), 0.0F,
                        Explosion.DestructionType.NONE);
            }
            if(this.type==1){
                System.out.println(MathUtils.map(5.5,0,10,0,100));
                Vec3d posDiff = entity.getPos().subtract(this.getPos());//the difference in between the 2 positions
                Vec3d mappedPos = new Vec3d(//turns a vector which will go from -5 to 5, to a vector that goes from -2 to 2
                        MathUtils.map(posDiff.x,-4d,4d,-2d,2d),
                        MathUtils.map(posDiff.y,-4d,4d,-1.2,2d),
                        MathUtils.map(posDiff.z,-4d,4d,-2d,2d)
                );
                entity.setVelocity(mappedPos.x, mappedPos.y + 4 * mappedPos.y >= 0 ? 1:-1, mappedPos.z);
            }

        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE;
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

}
