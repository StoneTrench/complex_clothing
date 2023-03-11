package me.stonetrench.complex_clothing.items;

import com.mojang.datafixers.util.Pair;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import me.stonetrench.complex_clothing.ComplexArmorMaterial;
import me.stonetrench.complex_clothing.ComplexPattern;
import me.stonetrench.complex_clothing.Complex_clothing;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class ComplexArmorItem extends ArmorItem implements ItemColor {
    private static final String PATTERN_LIST_KEY = "patterns";
    private static final String PATTERN_KEY = "key";
    private static final String COLOR_KEY = "color";

    public static final int DEFAULT_COLOR = 10511680;
    public static final int DEFAULT_COLOR_WHITE = 16777215;
    public static final Pair<ComplexPattern, Integer> DEFAULT_PAIR = new Pair<ComplexPattern, Integer>(ComplexPattern.BASE, DEFAULT_COLOR);

    public ComplexArmorItem(ComplexArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
        ColorHandlerRegistry.registerItemColors(this, this.asItem());
    }

    public static int getPatternCount(@NotNull ItemStack stack) {
        return stack.getOrCreateTag().getList(PATTERN_LIST_KEY, ListTag.TAG_COMPOUND).size();
    }

    @Contract("_, _, _ -> param1")
    public static @NotNull ItemStack addPattern(ItemStack stack, ComplexPattern pattern, int color) {
        if (
                getPatternCount(stack) == 0 &&
                        pattern != DEFAULT_PAIR.getFirst() &&
                        color != DEFAULT_PAIR.getSecond()
        )
            addPattern(stack, DEFAULT_PAIR.getFirst(), DEFAULT_PAIR.getSecond());

        CompoundTag element = new CompoundTag();
        element.putString(PATTERN_KEY, pattern.getId());
        element.putInt(COLOR_KEY, color);

        CompoundTag nbt = stack.getOrCreateTag();

        if (nbt.get(PATTERN_LIST_KEY) == null) nbt.put(PATTERN_LIST_KEY, new ListTag());
        nbt.getList(PATTERN_LIST_KEY, ListTag.TAG_COMPOUND).add(element);

        return stack;
    }

    @NotNull
    public static Pair<ComplexPattern, Integer> getPattern(@NotNull ItemStack stack, int index) {
        ListTag list = stack.getOrCreateTag().getList(PATTERN_LIST_KEY, ListTag.TAG_COMPOUND);

        if (index >= list.size() || index < 0) return DEFAULT_PAIR;

        CompoundTag element = (CompoundTag) list.get(index);

        if (element == null) return DEFAULT_PAIR;

        return new Pair<ComplexPattern, Integer>(ComplexPattern.byId(element.getString(PATTERN_KEY)), element.getInt(COLOR_KEY));
    }

    @Nullable
    public static Pair<ComplexPattern, Integer> removePattern(@NotNull ItemStack stack, int index) {
        ListTag list = stack.getOrCreateTag().getList(PATTERN_LIST_KEY, ListTag.TAG_COMPOUND);

        if (index >= list.size() || index < 0) return null;

        CompoundTag element = (CompoundTag) list.remove(index);

        if (element == null) return null;

        return new Pair<ComplexPattern, Integer>(ComplexPattern.byId(element.getString(PATTERN_KEY)), element.getInt(COLOR_KEY));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        int patternCount = getPatternCount(itemStack);

        for (int i = 0; i < patternCount; i++) {
            var pattern = getPattern(itemStack, i);

            var string = "tooltip." + Complex_clothing.MOD_ID + ".patterns.missing";
            if (pattern.getFirst() != null)
                string = "tooltip." + Complex_clothing.MOD_ID + ".patterns." + pattern.getFirst().getName();

            list.add(new TranslatableComponent(string).append(": #" + toHex(pattern.getSecond())).withStyle(ChatFormatting.GRAY));
        }
    }

    public static int blendColor(int k, List<DyeItem> colors) {
        int[] is = new int[3];
        int i = 0;
        int j = 0;

        float h;
        int n;

        float f = (float) (k >> 16 & 255) / 255.0F;
        float g = (float) (k >> 8 & 255) / 255.0F;
        h = (float) (k & 255) / 255.0F;
        i += (int) (Math.max(f, Math.max(g, h)) * 255.0F);
        is[0] += (int) (f * 255.0F);
        is[1] += (int) (g * 255.0F);
        is[2] += (int) (h * 255.0F);
        ++j;

        for (Iterator<DyeItem> colorIterator = colors.iterator(); colorIterator.hasNext(); ++j) {
            DyeItem dyeItem = colorIterator.next();
            float[] fs = dyeItem.getDyeColor().getTextureDiffuseColors();
            int l = (int) (fs[0] * 255.0F);
            int m = (int) (fs[1] * 255.0F);
            n = (int) (fs[2] * 255.0F);
            i += Math.max(l, Math.max(m, n));
            is[0] += l;
            is[1] += m;
            is[2] += n;
        }

        k = is[0] / j;
        int o = is[1] / j;
        int p = is[2] / j;
        h = (float) i / (float) j;
        float q = (float) Math.max(k, Math.max(o, p));
        k = (int) ((float) k * h / q);
        o = (int) ((float) o * h / q);
        p = (int) ((float) p * h / q);
        n = (k << 8) + o;
        n = (n << 8) + p;
        return n;
    }
    public static String toHex(int decimal) {
        int rem;
        StringBuilder hex = new StringBuilder();
        char[] hexchars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (decimal > 0) {
            rem = decimal % 16;
            hex.insert(0, hexchars[rem]);
            decimal = decimal / 16;
        }
        return hex.toString();
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return getPattern(stack, tintIndex).getSecond();
    }
}
