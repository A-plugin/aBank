package org.apo.abank;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.apo.abank.commands.Command;
import org.apo.abank.commands.EcoCmd;
import org.apo.abank.commands.PayCmd;
import org.apo.abank.systems.Listener;
import org.bukkit.Bukkit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class A_bank extends JavaPlugin {

    public static A_bank instance;
    public Economy economy=null;
    File file = new File(this.getDataFolder(),"economy.yml");
    FileConfiguration Fconfig = YamlConfiguration.loadConfiguration(file);
    Configuration config=getConfig();
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance=this;
        if (setupEconomy() && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Command(this);
            new EcoCmd(this);
            new PayCmd(this);
            this.getServer().getPluginManager().registerEvents(new Listener(), this);
            this.getLogger().info("Bank Plugin made by.APO2073");
            saveConfig();
        } else {
            getLogger().warning("Vault plugin또는 PlaceHolderAPI 플러그인이 없습니다!\n");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false; // Vault 플러그인이 없음
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false; // 경제 서비스 등록 실패
        }
        economy = rsp.getProvider();
        return economy != null;
    }
        @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Bank Plugin Disable");
    }
}
