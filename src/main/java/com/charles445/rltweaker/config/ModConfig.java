package com.charles445.rltweaker.config;

import com.charles445.rltweaker.RLTweaker;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Config(modid = RLTweaker.MODID)
public class ModConfig
{
	@Config.Comment("Server options")
	@Config.Name("Server")
	public static ServerConfig server = new ServerConfig();
	
	public static class ServerConfig
	{
		@Config.Comment("Minecraft tweaks, or anything that isn't mod specific")
		@Config.Name("Minecraft")
		public ConfigMinecraft minecraft = new ConfigMinecraft();
		
		@Config.Comment("Battle Towers tweaks")
		@Config.Name("Battle Towers")
		public ConfigBattleTowers battletowers = new ConfigBattleTowers();
		
		@Config.Comment("Recurrent Complex tweaks")
		@Config.Name("Recurrent Complex")
		public ConfigRecurrent recurrentcomplex = new ConfigRecurrent();
		
		@Config.Comment("Reskillable tweaks")
		@Config.Name("Reskillable")
		public ConfigReskillable reskillable = new ConfigReskillable();
		
		@Config.Comment("Roguelike Dungeons tweaks")
		@Config.Name("Roguelike Dungeons")
		public ConfigRoguelike roguelike = new ConfigRoguelike();
		
		@Config.Comment("Ruins tweaks")
		@Config.Name("Ruins")
		public ConfigRuins ruins = new ConfigRuins();
		
		@Config.Comment("So Many Enchantments tweaks")
		@Config.Name("So Many Enchantments")
		public ConfigSME somanyenchantments = new ConfigSME();
		
		@Config.Comment("Tough As Nails tweaks")
		@Config.Name("Tough As Nails")
		public ConfigTAN toughasnails = new ConfigTAN();
		
		@Config.Comment("Waystones tweaks")
		@Config.Name("Waystones")
		public ConfigWaystones waystones = new ConfigWaystones();
	}
	
	@Mod.EventBusSubscriber(modid = RLTweaker.MODID)
	private static class EventHandler
	{
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if(event.getModID().equals(RLTweaker.MODID))
			{
				ConfigManager.sync(RLTweaker.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
