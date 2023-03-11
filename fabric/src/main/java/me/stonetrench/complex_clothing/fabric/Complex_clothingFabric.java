package me.stonetrench.complex_clothing.fabric;

import me.stonetrench.complex_clothing.Complex_clothing;
import net.fabricmc.api.ModInitializer;

public class Complex_clothingFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Complex_clothing.init();
    }
}