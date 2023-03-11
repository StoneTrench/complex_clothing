package me.stonetrench.complex_clothing.items;

import me.stonetrench.complex_clothing.ComplexPattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ComplexPatternItem extends Item {
    private final ComplexPattern pattern;

    public ComplexPatternItem(ComplexPattern pattern, Properties properties) {
        super(properties);
        this.pattern = pattern;
    }

    public ComplexPattern getPattern() {
        return this.pattern;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(new TranslatableComponent(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }
}
