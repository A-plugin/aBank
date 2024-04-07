package org.apo.abank.commands;

import net.milkbowl.vault.chat.Chat;
import org.apo.abank.A_bank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Command implements TabExecutor {
    A_bank aBank=A_bank.instance;
    public Command(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("bank");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (((Player) commandSender).hasPermission("Bank.bank")){
                Player p= (Player) commandSender;
                if (args[0].equalsIgnoreCase("list")) {
                    Map<String,Double> map=new HashMap();
                    for (Player pl: Bukkit.getOnlinePlayers()) {
                        double mo=aBank.economy.getBalance(pl);
                        map.put(pl.getName(), mo);
                    }
                    p.sendMessage("======§a[aBank]§f======");
                    for (int i=0; i<map.size();i++) {
                        // balance list
                    }

                }
                if (args[0].equalsIgnoreCase("loan")) {
                    String reg="^[\\d]*$";
                    if (Pattern.matches(reg, args[1])){
                        ItemStack loan=new ItemStack(Material.PAPER);
                        ItemMeta m= loan.getItemMeta();
                        m.setDisplayName("§a"+args[1]+"$");
                        loan.setItemMeta(m);
                        p.getInventory().addItem(loan);
                        p.sendMessage(ChatColor.GOLD+"[aBank] 은행에서 "+args[1]+"$를 대출했습니다.");
                        p.sendMessage(ChatColor.GOLD+"[aBank] 이자: "+aBank.getConfig().get("loan-per"));
                        double money= Double.parseDouble(args[1]);
                        money=money+((Integer.parseInt(aBank.getConfig().get("loan-per").toString()))/100);
                        aBank.getConfig().set(p.getUniqueId()+".loan", money);
                        aBank.saveConfig();
                    }else {
                        p.sendMessage(ChatColor.RED+"[aBank] NaN Money value");
                    }
                }
                if (args[0].equalsIgnoreCase("loanM")) {
                    String reg="^[\\d]*$";
                    if (Pattern.matches(reg, args[1])){
                        if (aBank.economy.getBalance(p)>=Integer.parseInt(args[1])){
                            double money = aBank.getConfig().getDouble(p.getUniqueId() + ".loan");
                            double mmoney = Double.parseDouble(args[1]);
                            money -= mmoney;
                            aBank.getConfig().set(p.getUniqueId() + ".loan", money);
                            aBank.saveConfig();
                            if (money <= 0) {
                                p.sendMessage(ChatColor.GOLD + "[aBank] 빚을 전부 갚았습니다.");
                            }
                        }
                    }else {
                        p.sendMessage(ChatColor.RED+"[aBank] NaN Money value");
                    }
                }
                if (args[0].equalsIgnoreCase("loanC")) {
                    p.sendMessage(ChatColor.GOLD+"[aBank] 빚: "+aBank.getConfig().get(p.getUniqueId()+".loan"));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length==1) {
            completions.add("list");
            completions.add("loan");
            completions.add("loanM");
            completions.add("loanC");
        }
        if (args.length==2){
            if (args[1].equalsIgnoreCase("loan")) {
                completions.add("100");
            }
        }
        return completions;
    }
}
