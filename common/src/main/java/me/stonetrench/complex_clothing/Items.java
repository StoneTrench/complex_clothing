package me.stonetrench.complex_clothing;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.stonetrench.complex_clothing.items.ComplexArmorItem;
import me.stonetrench.complex_clothing.items.ComplexPatternItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class Items {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Complex_clothing.MOD_ID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Complex_clothing.MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> ITEM_GROUP_ICON = ITEMS.register("item_group_icon", () ->
            new Item(new Item.Properties()));
    public static final CreativeModeTab ITEM_GROUP = CreativeTabRegistry.create(
            new ResourceLocation(Complex_clothing.MOD_ID, "item_group"),
            () -> new ItemStack(ITEM_GROUP_ICON.getOrNull())
    );

    public static final ComplexArmorMaterial COMPLEX_ARMOR_MATERIAL = new ComplexArmorMaterial();

    public static final RegistrySupplier<Item> COMPLEX_HELMET = ITEMS.register("complex_helmet", () ->
            new ComplexArmorItem(COMPLEX_ARMOR_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().tab(ITEM_GROUP)));
    public static final RegistrySupplier<Item> COMPLEX_CHESTPLATE = ITEMS.register("complex_chestplate", () ->
            new ComplexArmorItem(COMPLEX_ARMOR_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().tab(ITEM_GROUP)));
    public static final RegistrySupplier<Item> COMPLEX_LEGGINGS = ITEMS.register("complex_leggings", () ->
            new ComplexArmorItem(COMPLEX_ARMOR_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().tab(ITEM_GROUP)));
    public static final RegistrySupplier<Item> COMPLEX_BOOTS = ITEMS.register("complex_boots", () ->
            new ComplexArmorItem(COMPLEX_ARMOR_MATERIAL, EquipmentSlot.FEET, new Item.Properties().tab(ITEM_GROUP)));


    public static final RegistrySupplier<SimpleRecipeSerializer<ComplexArmorDyeRecipe>> COMPLEX_ARMOR_DYE_RECIPE_SPECIAL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            "complex_crafting_armor_dye",
            () -> new SimpleRecipeSerializer<>(ComplexArmorDyeRecipe::new)
    );

    public static void Register() {
        RECIPE_SERIALIZERS.register();

        for (ComplexPattern value : ComplexPattern.values()) {
            if (value.isHasItem()) {
                ITEMS.register("pattern_" + value.getName(), () ->
                        new ComplexPatternItem(value, new Item.Properties().tab(ITEM_GROUP))
                );
            }
        }

        ITEMS.register();
    }
}
