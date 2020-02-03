package io.github.thebusybiscuit.slimytreetaps;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.bstats.bukkit.Metrics;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.utils.MachineTier;
import me.mrCookieSlime.Slimefun.utils.MachineType;

public class TreeTaps extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Config cfg = new Config(this);
		
		if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
			new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/SlimyTreeTaps/master").start();
		}
		
		new Metrics(this);
		
		SlimefunItemStack treeTap = new SlimefunItemStack("TREE_TAP", Material.WOODEN_HOE, "&6Tree Tap", getLore(cfg.getInt("resin-chance.standard")));
		SlimefunItemStack reinforcedTreeTap = new SlimefunItemStack("REINFORCED_TREE_TAP", Material.IRON_HOE, "&6Reinforced Tree Tap", getLore(cfg.getInt("resin-chance.reinforced")));
		SlimefunItemStack diamondTreeTap = new SlimefunItemStack("DIAMOND_TREE_TAP", Material.DIAMOND_HOE, "&bDiamond Tree Tap", getLore(cfg.getInt("resin-chance.diamond")));

		clearAttributes(treeTap, reinforcedTreeTap, diamondTreeTap);
		
		SlimefunItemStack stickyResin = new SlimefunItemStack("STICKY_RESIN", Material.BROWN_DYE, "&6Sticky Resin", "", "&7Can be smelted into Rubber");
		SlimefunItemStack rubber = new SlimefunItemStack("RUBBER", Material.FIREWORK_STAR, "&eRubber", "", "&7An alternative source of plastic");
		SlimefunItemStack rawPlastic = new SlimefunItemStack("RAW_PLASTIC", Material.PAPER, "&rRaw Plastic");
		SlimefunItemStack rubberFactory = new SlimefunItemStack("RUBBER_FACTORY", Material.SMOKER, "&bRubber Factory", "", MachineTier.ADVANCED.and(MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &712 J/s");
		SlimefunItemStack resinExtractor = new SlimefunItemStack("RESIN_EXTRACTOR", Material.SMITHING_TABLE, "&cResin Extractor", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &732 J/s");
		SlimefunItemStack resinExtractor2 = new SlimefunItemStack("RESIN_EXTRACTOR_2", Material.SMITHING_TABLE, "&cResin Extractor &7(&eII&7)", "", MachineTier.END_GAME.and(MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &756 J/s");
		
		Category category = new Category(new CustomItem(treeTap, "&6Slimy TreeTaps", "", "&a> Click to open"));
		
		new TreeTap(category, treeTap, cfg.getInt("resin-chance.standard"), stickyResin,
		new ItemStack[] {
				null, SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.OAK_LOG),
				SlimefunItems.DAMASCUS_STEEL_INGOT, new ItemStack(Material.OAK_LOG), null,
				new ItemStack(Material.OAK_LOG), null, new ItemStack(Material.BOWL)
		}).register();
		
		new TreeTap(category, reinforcedTreeTap, cfg.getInt("resin-chance.reinforced"), stickyResin,
		new ItemStack[] {
				null, SlimefunItems.HARDENED_METAL_INGOT, new ItemStack(Material.OAK_LOG),
				SlimefunItems.HARDENED_METAL_INGOT, treeTap, null,
				new ItemStack(Material.OAK_LOG), null, SlimefunItems.COBALT_INGOT
		}).register();
		
		new TreeTap(category, diamondTreeTap, cfg.getInt("resin-chance.diamond"), stickyResin,
		new ItemStack[] {
				null, new ItemStack(Material.DIAMOND), new ItemStack(Material.OAK_LOG),
				new ItemStack(Material.DIAMOND), reinforcedTreeTap, null,
				new ItemStack(Material.OAK_LOG), null, SlimefunItems.CARBONADO
		}).register();
		
		new SlimefunItem(category, stickyResin, new RecipeType(treeTap),
		new ItemStack[] {
				null, null, null,
				null, new ItemStack(Material.OAK_LOG), null,
				null, null, null
		}).register();
		
		new RubberFactory(category, rubberFactory, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				null, SlimefunItems.HEATING_COIL, null,
				SlimefunItems.SOLDER_INGOT, SlimefunItems.ELECTRIC_FURNACE_2, SlimefunItems.SOLDER_INGOT,
				SlimefunItems.SOLDER_INGOT, SlimefunItems.MEDIUM_CAPACITOR, SlimefunItems.SOLDER_INGOT
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				registerRecipe(4, new ItemStack[] {new CustomItem(stickyResin, 2)}, new ItemStack[] {rubber});
				registerRecipe(6, new ItemStack[] {new CustomItem(rubber, 2)}, new ItemStack[] {rawPlastic});
				registerRecipe(10, new ItemStack[] {rawPlastic}, new ItemStack[] {SlimefunItems.PLASTIC_SHEET});
			}

			@Override
			public int getEnergyConsumption() {
				return 6;
			}

			@Override
			public int getSpeed() {
				return 1;
			}
			
		}.registerChargeableBlock(256);
		
		new ResinExtractor(category, resinExtractor, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				null, diamondTreeTap, null,
				SlimefunItems.GOLD_24K, SlimefunItems.CARBONADO, SlimefunItems.GOLD_24K,
				SlimefunItems.ELECTRIC_MOTOR, rubberFactory, SlimefunItems.ELECTRIC_MOTOR
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				for (Material log : Tag.LOGS.getValues()) {
					if (!log.name().startsWith("STRIPPED_")) {
						registerRecipe(14, new ItemStack[] {new ItemStack(log, 8)}, new ItemStack[] {stickyResin});
					}
				}
			}

			@Override
			public int getEnergyConsumption() {
				return 16;
			}

			@Override
			public int getSpeed() {
				return 1;
			}
			
		}.registerChargeableBlock(1024);
		
		new ResinExtractor(category, resinExtractor2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				SlimefunItems.REINFORCED_ALLOY_INGOT, diamondTreeTap, SlimefunItems.REINFORCED_ALLOY_INGOT,
				SlimefunItems.GOLD_24K, SlimefunItems.CARBONADO, SlimefunItems.GOLD_24K,
				SlimefunItems.ELECTRIC_MOTOR, resinExtractor, SlimefunItems.ELECTRIC_MOTOR
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				for (Material log : Tag.LOGS.getValues()) {
					if (!log.name().startsWith("STRIPPED_")) {
						registerRecipe(6, new ItemStack[] {new ItemStack(log, 8)}, new ItemStack[] {stickyResin});
					}
				}
			}

			@Override
			public int getEnergyConsumption() {
				return 28;
			}

			@Override
			public int getSpeed() {
				return 2;
			}
			
		}.registerChargeableBlock(2048);
		
		new SlimefunItem(category, rawPlastic, new RecipeType(rubberFactory),
		new ItemStack[] {
				null, null, null,
				null, new CustomItem(rubber, 2), null,
				null, null, null
		}).register();

		new SlimefunItem(category, rubber, new RecipeType(rubberFactory),
			new ItemStack[] {
				null, null, null,
				null, new CustomItem(stickyResin), null,
				null, null, null
			}).register();

		Slimefun.registerResearch(new Research(6789, "Tree Taps", 15), treeTap, reinforcedTreeTap, diamondTreeTap, stickyResin, rubber, rawPlastic);
		Slimefun.registerResearch(new Research(6790, "Automated Rubber", 20), rubberFactory, resinExtractor, resinExtractor2);
	}

	private String[] getLore(int chance) {
		return new String[] {
				"", 
				"&7Chance: &a" + chance + "%", 
				"&eRight Click any Log &7to harvest Resin"
		};
	}

	private void clearAttributes(SlimefunItemStack... items) {
		for (SlimefunItemStack item : items) {
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
		}
	}

}
