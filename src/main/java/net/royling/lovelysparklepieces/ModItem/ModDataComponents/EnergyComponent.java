package net.royling.lovelysparklepieces.ModItem.ModDataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record EnergyComponent(
        int currentEnergy, int maxEnergy
) implements IEnergyStorage {
    public static final Codec<EnergyComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("currentEnergy").forGetter(EnergyComponent::currentEnergy),
            Codec.INT.fieldOf("maxEnergy").forGetter(EnergyComponent::maxEnergy)
    ).apply(instance, EnergyComponent::new));

    // 充电方法
    public EnergyComponent charge(int amount) {
        int newEnergy = Math.min(currentEnergy + Math.max(amount, 0), maxEnergy);
        return new EnergyComponent(newEnergy, maxEnergy);
    }
    // 放电方法
    public EnergyComponent discharge(int amount) {
        int newEnergy = Math.max(currentEnergy - Math.max(amount, 0), 0);
        return new EnergyComponent(newEnergy, maxEnergy);
    }
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int accepted = Math.min(maxReceive, maxEnergy - currentEnergy);
        if (!simulate) {
            return this.charge(accepted).currentEnergy(); // 实际充电
        }
        return accepted; // 模拟充电
    }
    @Override
    public int extractEnergy(int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return currentEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
