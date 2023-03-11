package me.stonetrench.complex_clothing.forge;

import dev.architectury.platform.forge.EventBuses;
import me.stonetrench.complex_clothing.Complex_clothing;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Complex_clothing.MOD_ID)
public class Complex_clothingForge {
    public Complex_clothingForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Complex_clothing.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            Complex_clothing.init();
    }
}