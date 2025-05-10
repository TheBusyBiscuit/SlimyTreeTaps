package io.github.thebusybiscuit.slimytreetaps;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;

public class TreeTaps extends JavaPlugin implements SlimefunAddon {
	
	@Override
	public void onEnable() {
		Config cfg = new Config(this);
		
		if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("DEV - ")) {
			new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/SlimyTreeTaps/master").start();
		}
		
		new Metrics(this, 6138);
		
		SlimefunItemStack treeTap = new SlimefunItemStack("TREE_TAP", Material.WOODEN_HOE, "&6Tree Tap", getLore("Resin", cfg.getInt("resin-chance.standard")));
		SlimefunItemStack reinforcedTreeTap = new SlimefunItemStack("REINFORCED_TREE_TAP", Material.IRON_HOE, "&6Reinforced Tree Tap", getLore("Resin", cfg.getInt("resin-chance.reinforced")));
		SlimefunItemStack diamondTreeTap = new SlimefunItemStack("DIAMOND_TREE_TAP", Material.DIAMOND_HOE, "&bDiamond Tree Tap", getLore("Resin", cfg.getInt("resin-chance.diamond")));
		SlimefunItemStack treeScraper = new SlimefunItemStack("TREE_SCRAPER", Material.GOLDEN_SHOVEL, "&bTree Scraper", getLore("Amber", cfg.getInt("amber-chance")));

		clearAttributes(treeTap, reinforcedTreeTap, diamondTreeTap, treeScraper);
		
		SlimefunItemStack stickyResin = new SlimefunItemStack("STICKY_RESIN", Material.BROWN_DYE, "&6Sticky Resin", "", "&7Can be turned into Rubber");
		SlimefunItemStack rubber = new SlimefunItemStack("RUBBER", Material.FIREWORK_STAR, "&eRubber", "", "&7An alternative source of plastic");
		SlimefunItemStack rawPlastic = new SlimefunItemStack("RAW_PLASTIC", Material.PAPER, "&rRaw Plastic");
		
		SlimefunItemStack rubberFactory = new SlimefunItemStack("RUBBER_FACTORY", Material.SMOKER, "&bRubber Factory", "", LoreBuilder.machine(MachineTier.ADVANCED, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &712 J/s");
		SlimefunItemStack resinExtractor = new SlimefunItemStack("RESIN_EXTRACTOR", Material.SMITHING_TABLE, "&cResin Extractor", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 1x", "&8\u21E8 &e\u26A1 &732 J/s");
		SlimefunItemStack resinExtractor2 = new SlimefunItemStack("RESIN_EXTRACTOR_2", Material.SMITHING_TABLE, "&cResin Extractor &7(&eII&7)", "", LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE), "&8\u21E8 &7Speed: 2x", "&8\u21E8 &e\u26A1 &756 J/s");
		
		SlimefunItemStack amber = new SlimefunItemStack("AMBER", "ac7f7b72fc3e733828fcccc0ca8278aca2633aa33a231c93a682d14ac54aa0c4", "&6Amber", "", "&eA hardened gem acquired from Resin");
		SlimefunItemStack amberBlock = new SlimefunItemStack("AMBER_BLOCK", Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16) ? Material.SHROOMLIGHT: Material.GLOWSTONE, "&6Block of Amber");
        
		SlimefunItemStack blueEnderPearl = new SlimefunItemStack("BLUE_ENDER_PEARL", "38be8abd66d09a58ce12d377544d726d25cad7e979e8c2481866be94d3b32f", "&9Blue Ender Pearl", "", "&7This item can be used to", "&7craft Magical Mirrors");
		SlimefunItemStack magicalMirror = new SlimefunItemStack("MAGICAL_MIRROR", Material.BLUE_STAINED_GLASS_PANE, "&9Magical Mirror &7(Unbound)", "", "&eRight Click &7to bind this Mirror", "&7to your current location.", "", "&7Place a bound mirror into an", "&7Item Frame and then click on", "&7that Item Frame to teleport.", "&7Teleports cost &b1 Ender Pearl");
        
		ItemGroup itemGroup = new ItemGroup(new NamespacedKey(this, "tree_taps"), CustomItemStack.create(treeTap.item(), "&6Slimy TreeTaps", "", "&a> Click to open"));
		RecipeType rubberFactoryType = new RecipeType(new NamespacedKey(this, "rubber_factory"), rubberFactory);
		
		new TreeTool(itemGroup, treeTap, cfg.getInt("resin-chance.standard"), stickyResin,
		new ItemStack[] {
				null, SlimefunItems.DAMASCUS_STEEL_INGOT.item(), new ItemStack(Material.OAK_LOG),
				SlimefunItems.DAMASCUS_STEEL_INGOT.item(), new ItemStack(Material.OAK_LOG), null,
				new ItemStack(Material.OAK_LOG), null, new ItemStack(Material.BOWL)
		}).register(this);
		
		new TreeTool(itemGroup, reinforcedTreeTap, cfg.getInt("resin-chance.reinforced"), stickyResin,
		new ItemStack[] {
				null, SlimefunItems.HARDENED_METAL_INGOT.item(), new ItemStack(Material.OAK_LOG),
				SlimefunItems.HARDENED_METAL_INGOT.item(), treeTap.item(), null,
				new ItemStack(Material.OAK_LOG), null, SlimefunItems.COBALT_INGOT.item()
		}).register(this);
		
		new TreeTool(itemGroup, diamondTreeTap, cfg.getInt("resin-chance.diamond"), stickyResin,
		new ItemStack[] {
				null, new ItemStack(Material.DIAMOND), new ItemStack(Material.OAK_LOG),
				new ItemStack(Material.DIAMOND), reinforcedTreeTap.item(), null,
				new ItemStack(Material.OAK_LOG), null, SlimefunItems.CARBONADO.item()
		}).register(this);
        
        new TreeTool(itemGroup, treeScraper, cfg.getInt("amber-chance"), amber,
        new ItemStack[] {
                null, new ItemStack(Material.GOLD_INGOT), null,
                new ItemStack(Material.GOLD_INGOT), treeTap.item(), null,
                null, null, SlimefunItems.BRONZE_INGOT.item()
        }).register(this);
		
		new SlimefunItem(itemGroup, stickyResin, new RecipeType(new NamespacedKey(this, "tree_tap"), treeTap),
		new ItemStack[] {
				null, null, null,
				null, new ItemStack(Material.OAK_LOG), null,
				null, null, null
		}).register(this);
		
		new RubberFactory(itemGroup, rubberFactory, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				null, SlimefunItems.HEATING_COIL.item(), null,
				SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.ELECTRIC_FURNACE_2.item(), SlimefunItems.SOLDER_INGOT.item(),
				SlimefunItems.SOLDER_INGOT.item(), SlimefunItems.MEDIUM_CAPACITOR.item(), SlimefunItems.SOLDER_INGOT.item()
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				registerRecipe(4, new ItemStack[] {CustomItemStack.create(stickyResin.item(), 2)}, new ItemStack[] {rubber.item()});
				registerRecipe(6, new ItemStack[] {CustomItemStack.create(rubber.item(), 2)}, new ItemStack[] {rawPlastic.item()});
				registerRecipe(10, new ItemStack[] {rawPlastic.item()}, new ItemStack[] {SlimefunItems.PLASTIC_SHEET.item()});
			}

			@Override
			public int getEnergyConsumption() {
				return 6;
			}

			@Override
			public int getSpeed() {
				return 1;
			}
			
		}.register(this);
		
		new ResinExtractor(itemGroup, resinExtractor, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				null, diamondTreeTap.item(), null,
				SlimefunItems.GOLD_24K.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.GOLD_24K.item(),
				SlimefunItems.ELECTRIC_MOTOR.item(), rubberFactory.item(), SlimefunItems.ELECTRIC_MOTOR.item()
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				for (Material log : Tag.LOGS.getValues()) {
					if (!log.name().startsWith("STRIPPED_")) {
						registerRecipe(14, new ItemStack[] {new ItemStack(log, 8)}, new ItemStack[] {stickyResin.item()});
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

            @Override
			public int getCapacity() {
			    return 1024;
			}
			
		}.register(this);
		
		new ResinExtractor(itemGroup, resinExtractor2, RecipeType.ENHANCED_CRAFTING_TABLE,
		new ItemStack[] {
				SlimefunItems.REINFORCED_ALLOY_INGOT.item(), diamondTreeTap.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(),
				SlimefunItems.GOLD_24K.item(), SlimefunItems.CARBONADO.item(), SlimefunItems.GOLD_24K.item(),
				SlimefunItems.ELECTRIC_MOTOR.item(), resinExtractor.item(), SlimefunItems.ELECTRIC_MOTOR.item()
		}) {
			
			@Override
			public void registerDefaultRecipes() {
				for (Material log : Tag.LOGS.getValues()) {
					if (!log.name().startsWith("STRIPPED_")) {
						registerRecipe(6, new ItemStack[] {new ItemStack(log, 8)}, new ItemStack[] {stickyResin.item()});
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

            @Override
            public int getCapacity() {
                return 2048;
            }
			
		}.register(this);
		
		new SlimefunItem(itemGroup, rawPlastic, rubberFactoryType,
		new ItemStack[] {
				null, null, null,
				null, CustomItemStack.create(rubber.item(), 2), null,
				null, null, null
		}).register(this);

		new SlimefunItem(itemGroup, rubber, rubberFactoryType,
		new ItemStack[] {
				null, null, null,
				null, stickyResin.item(), null,
				null, null, null
		}).register(this);

        new SlimefunItem(itemGroup, amber, RecipeType.SMELTERY,
        new ItemStack[] {
                CustomItemStack.create(stickyResin.item(), 4), null, null,
                null, null, null,
                null, null, null
        }).register(this);

        new SlimefunItem(itemGroup, amberBlock, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {
                amber.item(), amber.item(), amber.item(),
                amber.item(), amber.item(), amber.item(),
                amber.item(), amber.item(), amber.item()
        }).register(this);
        
        new UnplaceableBlock(itemGroup, blueEnderPearl, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {
                new ItemStack(Material.LAPIS_BLOCK), amberBlock.item(), new ItemStack(Material.LAPIS_BLOCK),
                amberBlock.item(), new ItemStack(Material.ENDER_PEARL), amberBlock.item(),
                new ItemStack(Material.LAPIS_BLOCK), amberBlock.item(), new ItemStack(Material.LAPIS_BLOCK)
        }).register(this);
        
        MagicalMirror mirror = new MagicalMirror(this, itemGroup, magicalMirror, RecipeType.ENHANCED_CRAFTING_TABLE,
        new ItemStack[] {
                new ItemStack(Material.GLASS), amber.item(), new ItemStack(Material.GLASS),
                amber.item(), blueEnderPearl.item(), amber.item(),
                new ItemStack(Material.GLASS), amber.item(), new ItemStack(Material.GLASS)
        });
        mirror.register(this);

        Research treeTapsResearch = new Research(new NamespacedKey(this, "tree_taps"), 6789, "Tree Taps", 15);
		treeTapsResearch.addItems(treeTap.item(), reinforcedTreeTap.item(), diamondTreeTap.item(), stickyResin.item(), rubber.item(), rawPlastic.item());
		treeTapsResearch.register();

		Research automationResearch = new Research(new NamespacedKey(this, "rubber_automation"), 6790, "Automated Rubber", 20);
		automationResearch.addItems(rubberFactory.item(), resinExtractor.item(), resinExtractor2.item());
		automationResearch.register();

        Research amberResearch = new Research(new NamespacedKey(this, "amber"), 6791, "Amber", 20);
        amberResearch.addItems(treeScraper.item(), amber.item(), amberBlock.item());
        amberResearch.register();

        Research magicalMirrorResearch = new Research(new NamespacedKey(this, "magical_mirror"), 6792, "Magical Mirror", 30);
        magicalMirrorResearch.addItems(blueEnderPearl.item(), magicalMirror.item());
        magicalMirrorResearch.register();
        
        new MagicalMirrorListener(this, mirror);
	}

	private String[] getLore(String item, int chance) {
		return new String[] {
				"", 
				"&7Chance: &a" + chance + "%", 
				"&eRight Click any Log &7to harvest " + item
		};
	}

	private void clearAttributes(SlimefunItemStack... items) {
		for (SlimefunItemStack item : items) {
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			item.setItemMeta(meta);
		}
	}

	@Override
	public String getBugTrackerURL() {
		return "https://github.com/TheBusyBiscuit/Slimytreetaps/issues";
	}

	@Override
	public JavaPlugin getJavaPlugin() {
		return this;
	}

}
