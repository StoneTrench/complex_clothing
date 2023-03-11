package me.stonetrench.complex_clothing;

import me.stonetrench.complex_clothing.items.ComplexArmorItem;
import me.stonetrench.complex_clothing.items.ComplexPatternItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ArmorDyeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ComplexArmorDyeRecipe extends ArmorDyeRecipe {

    public ComplexArmorDyeRecipe(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, @NotNull Level level) {
        ArrayList<DyeItem> dyes = new ArrayList<>();
        ItemStack result = ItemStack.EMPTY;
        ItemStack pattern = ItemStack.EMPTY;

        for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack currentStack = craftingContainer.getItem(i);
            if (!currentStack.isEmpty()) {
                Item item = currentStack.getItem();

                if (item instanceof ComplexArmorItem) {
                    if (!result.isEmpty()) {
                        return false;
                    }

                    result = currentStack.copy();
                } else if (item instanceof ComplexPatternItem){
                    pattern = currentStack.copy();
                } else if (item instanceof DyeItem){
                    dyes.add((DyeItem)item);
                }  else
                    return false;
            }
        }

        return !result.isEmpty() && !dyes.isEmpty() && (pattern.isEmpty() || ((ComplexPatternItem)pattern.getItem()).getPattern().canPutOn(result));
    }
    @Override
    public @NotNull ItemStack assemble(CraftingContainer craftingContainer) {
        ArrayList<DyeItem> dyes = new ArrayList<>();
        ItemStack result = ItemStack.EMPTY;
        ItemStack pattern = ItemStack.EMPTY;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack currentStack = craftingContainer.getItem(i);
            if (!currentStack.isEmpty()) {
                Item item = currentStack.getItem();

                if (item instanceof ComplexArmorItem) {
                    if (!result.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    result = currentStack.copy();
                } else if (item instanceof ComplexPatternItem) {
                    pattern = currentStack.copy();
                } else if (item instanceof DyeItem) {
                    dyes.add((DyeItem) item);
                } else
                    return ItemStack.EMPTY;
            }
        }

        if (!result.isEmpty() && !dyes.isEmpty()) {
            if (pattern.isEmpty()) {
                int index = ComplexArmorItem.getPatternCount(result) - 1;

                if (index < 0){
                    return ComplexArmorItem.addPattern(result, ComplexArmorItem.DEFAULT_PAIR.getFirst(), ComplexArmorItem.blendColor(ComplexArmorItem.DEFAULT_COLOR_WHITE, dyes));
                }
                else {
                    var p = ComplexArmorItem.removePattern(result, index);
                    if (p == null) return ItemStack.EMPTY;
                    return ComplexArmorItem.addPattern(result, p.getFirst(), ComplexArmorItem.blendColor(p.getSecond(), dyes));
                }
            } else if (((ComplexPatternItem) pattern.getItem()).getPattern().canPutOn(result))
                return ComplexArmorItem.addPattern(result, ((ComplexPatternItem) pattern.getItem()).getPattern(), ComplexArmorItem.blendColor(ComplexArmorItem.DEFAULT_COLOR_WHITE, dyes));
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return i * j >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Items.COMPLEX_ARMOR_DYE_RECIPE_SPECIAL_RECIPE_SERIALIZER.get();
    }
}
