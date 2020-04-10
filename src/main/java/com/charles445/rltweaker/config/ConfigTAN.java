package com.charles445.rltweaker.config;

import net.minecraftforge.common.config.Config;

public class ConfigTAN
{
	@Config.Comment("Master switch for this mod compatibility")
	@Config.Name("ENABLED")
	@Config.RequiresMcRestart
	public boolean enabled = true;
	
	@Config.Comment("Fixes thirst drain on teleportation")
	@Config.Name("Teleport Thirst Fix")
	public boolean fixTeleportThirst = true;
	
	@Config.Comment("Exhaustion change in a tick for the teleport fix to kick in.")
	@Config.Name("Teleport Thirst Threshold")
	@Config.RangeDouble(min=1.0D, max=40.0D)
	public double teleportThirstThreshold = 8.0D;
	
	@Config.Comment("Regularly send players extra Thirst packets")
	@Config.Name("Send Extra Thirst Packets")
	public boolean sendExtraThirstPackets = true;
	
	@Config.Comment("How often extra Thirst packets get sent out, in ticks")
	@Config.Name("Thirst Packet Frequency")
	@Config.RangeInt(min=1)
	public int extraThirstPacketFrequency = 20;
	
}
