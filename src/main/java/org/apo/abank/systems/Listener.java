package org.apo.abank.systems;

import net.milkbowl.vault.chat.Chat;
import org.apo.abank.A_bank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.util.Stack;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void loan(PlayerInteractEvent e) {
        Player p=e.getPlayer();
        Action a=e.getAction();
        if (a.equals(Action.RIGHT_CLICK_AIR)) {
            if (p.getInventory().getItemInMainHand().getType().equals(Material.PAPER)) {
                ItemStack i=p.getItemInHand();
                ItemMeta m= i.getItemMeta();
                if (m.getDisplayName().contains("$") && m.getDisplayName().startsWith("§")) {
                    e.setCancelled(true);
                    String mo= m.getDisplayName();
                    mo=mo.replace("$","");
                    mo=mo.replace("§a","");
                    int money= Integer.parseInt(mo);
                    A_bank aBank=A_bank.instance;
                    aBank.economy.depositPlayer(p,money);
                    p.sendMessage(ChatColor.GREEN+"[aBank] +"+money+"$");
                    p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }
        }
    }
}
