package me.stonetrench.complex_clothing;

import me.stonetrench.complex_clothing.items.ComplexArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public enum ComplexPattern {
    BASE("base", "bs", new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}, false),
    BUTTONS("buttons", "bt", new EquipmentSlot[]{EquipmentSlot.CHEST}),
    RIM("rim", "rm", new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST}),
    OUTLINE("outline", "ol", new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}),
    NOSE("nose", "ns", new EquipmentSlot[]{EquipmentSlot.HEAD}),
    SHOULDERS("shoulders", "sh", new EquipmentSlot[]{EquipmentSlot.CHEST});

    private final String name;
    private final String id;
    private final EquipmentSlot[] canBePutOn;
    private final boolean hasItem;

    private static final ComplexPattern[] VALUES = values();
    public static final int COUNT = VALUES.length;

    ComplexPattern(String name, String id, EquipmentSlot[] canBePutOn, boolean hasItem) {
        this.name = name;
        this.id = id;
        this.canBePutOn = canBePutOn;
        this.hasItem = hasItem;
    }

    ComplexPattern(String name, String id, EquipmentSlot[] canBePutOn) {
        this.name = name;
        this.id = id;
        this.canBePutOn = canBePutOn;
        this.hasItem = true;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public boolean canPutOn(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof ComplexArmorItem complexArmorItem) {
            for (EquipmentSlot equipmentSlot : this.canBePutOn) {
                if (equipmentSlot == complexArmorItem.getSlot()) return true;
            }
        }

        return false;
    }

    public boolean isHasItem() {
        return this.hasItem;
    }


    @Nullable
    public static ComplexPattern byId(String name) {
        for (int i = 0; i < COUNT; ++i) {

            ComplexPattern pattern = VALUES[i];
            if (pattern.id.equals(name)) {
                return pattern;
            }
        }

        return null;
    }
}
