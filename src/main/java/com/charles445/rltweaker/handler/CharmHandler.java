package com.charles445.rltweaker.handler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.charles445.rltweaker.RLTweaker;
import com.charles445.rltweaker.config.ModConfig;
import com.charles445.rltweaker.util.ErrorUtil;
import com.charles445.rltweaker.util.ModNames;
import com.charles445.rltweaker.util.ReflectUtil;
import com.google.common.base.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class CharmHandler
{
	public static Map<String, EnumEnchantmentType> enchantmentTypes = new HashMap<>();
	
	public CharmHandler()
	{
		if(ModConfig.server.charm.fixIncorrectItemEnchantments)
			fixIncorrectItemEnchantments();
		
		if(ModConfig.server.charm.fixSalvageTrade)
			fixSalvageTrade();
		
		//No event bus registration yet
	}
	
	private void fixIncorrectItemEnchantments()
	{
		//Fix for enchantment types
		
		fixEnchantmentWithPredicate("curse_break", item -> false); //Book only
		fixEnchantmentWithPredicate("homing", item -> item == Items.IRON_HOE || item == Items.DIAMOND_HOE || item == Items.GOLDEN_HOE); //Specific vanilla hoes
		fixEnchantmentWithPredicate("magnetic", item -> item == Items.SHEARS || item instanceof ItemTool); //All tools, shears
	}
	
	private void fixEnchantmentWithType(String enchantName, EnumEnchantmentType type)
	{
		Enchantment enchant = getEnchantmentByName(enchantName);
		if(enchant == null)
		{
			ErrorUtil.logSilent("Charm Missing Enchantment "+enchantName);
			RLTweaker.logger.warn("Couldn't find Charm enchantment: "+enchantName);
		}
		else
		{
			enchant.type = type;
		}
	}
	
	private void fixEnchantmentWithPredicate(String enchantName, Predicate<Item> delegate)
	{
		Enchantment enchant = getEnchantmentByName(enchantName);
		if(enchant == null)
		{
			ErrorUtil.logSilent("Charm Missing Enchantment "+enchantName);
			RLTweaker.logger.warn("Couldn't find Charm enchantment: "+enchantName);
		}
		else
		{
			String typeName = "RLTweaker Charm "+enchantName;
			EnumEnchantmentType type = EnumHelper.addEnchantmentType(typeName, delegate);
			if(type==null)
			{
				ErrorUtil.logSilent("Charm addEnchantmentType "+typeName);
				RLTweaker.logger.error("Found Charm enchantment but addEnchantmentType failed: "+typeName);
			}
			else
			{
				enchantmentTypes.put(typeName, type);
				enchant.type = type;
				RLTweaker.logger.info("Replaced "+enchantName+" enchantment type with "+typeName);
			}
		}
	}
	
	@Nullable
	private Enchantment getEnchantmentByName(String name)
	{
		return Enchantment.REGISTRY.getObject(new ResourceLocation(ModNames.CHARM, name));
	}
	
	private void fixSalvageTrade()
	{
		Enchantment salvage = getEnchantmentByName("salvage");
		if(salvage == null)
		{
			//Salvage is disabled
			try
			{
				Object o_VillagerRegistry_INSTANCE = ReflectUtil.findField(VillagerRegistry.class, "INSTANCE").get(null);
				RegistryNamespaced<ResourceLocation, VillagerProfession> REGISTRY = (RegistryNamespaced<ResourceLocation, VillagerProfession>) ReflectUtil.findField(VillagerRegistry.class, "REGISTRY").get(o_VillagerRegistry_INSTANCE);
				
				VillagerProfession smithProfession = REGISTRY.getObject(new ResourceLocation("minecraft:smith"));
				
				VillagerCareer genericSmith = smithProfession.getCareer(0);
				VillagerCareer weaponSmith = smithProfession.getCareer(1);
				VillagerCareer toolSmith = smithProfession.getCareer(2);
				
				Field f_VillagerCareer_trades = ReflectUtil.findField(VillagerCareer.class, "trades");
				
				removeAllSalvages((List<List<ITradeList>>) f_VillagerCareer_trades.get(genericSmith));
				removeAllSalvages((List<List<ITradeList>>) f_VillagerCareer_trades.get(weaponSmith));
				removeAllSalvages((List<List<ITradeList>>) f_VillagerCareer_trades.get(toolSmith));
				
			}
			catch(Exception e)
			{
				RLTweaker.logger.error("Failed to remove Salvage trades", e);
				ErrorUtil.logSilent("Charm Salvage Trade Removal");
			}
		}
	}
	
	private void removeAllSalvages(List<List<ITradeList>> trades)
	{
		//svenhjol.charm.world.feature.VillagerTrades$SalvageTrade
		for(List<ITradeList> tradeLevel : trades)
		{
			Iterator<ITradeList> iterator = tradeLevel.iterator();
			while(iterator.hasNext())
			{
				ITradeList tradeList = iterator.next();
				
				if(tradeList.getClass().getName().equals("svenhjol.charm.world.feature.VillagerTrades$SalvageTrade"))
				{
					iterator.remove();
					RLTweaker.logger.info("Removed a charm salvage trade");
				}
			}
		}
	}
}