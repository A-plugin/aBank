package org.apo.abank.commands;

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
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class EcoCmd implements TabExecutor {
    A_bank aBank=A_bank.instance;
    public EcoCmd(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("ebank");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (((Player) commandSender).hasPermission("Bank.eco")){
                Player p= (Player) commandSender;
                if (true){
                    String reg="^[\\d]*$";
                    if (args[1]==null) return false;
                    if (args[0].equalsIgnoreCase("CheckPaper")) {
                        if (args[1]!=null) {
                            if (Pattern.matches(reg, args[1])) {
                                if (aBank.economy.getBalance(p.getName())>=Double.parseDouble(args[1])) {
                                    aBank.economy.withdrawPlayer(p,Double.parseDouble(args[1]));
                                    ItemStack pa=new ItemStack(Material.PAPER);
                                    ItemMeta pam= pa.getItemMeta();
                                    pam.setDisplayName("§a"+args[1]+"$");
                                    pam.setLore(Collections.singletonList(ChatColor.GOLD+p.getName()+"'s 수표"));
                                    pa.setItemMeta(pam);
                                    p.getInventory().addItem(pa);
                                } else {
                                    p.sendMessage(ChatColor.RED+"[aBank] 당신은 충분한 돈을 가지고 있지 않습니다!");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED+"[aBank] NaN Money value");
                            }
                        }
                    }
                    if (args[2]==null) return false;
                    if (Pattern.matches(reg, args[2])){
                        Player t=Bukkit.getPlayer(args[1]);
                        if (p.hasPermission("Bank.ecop")){
                            if (args[0].equalsIgnoreCase("set")) {
                                aBank.economy.withdrawPlayer(t, aBank.economy.getBalance(t.getName()));
                                aBank.economy.depositPlayer(t, Double.parseDouble(args[2]));
                                p.sendMessage(ChatColor.GREEN + "[aBank] " + t.getName() + "의 잔고를 " + args[2] + "으(로) 설정했습니다.");
                            }
                            if (args[0].equalsIgnoreCase("take")) {
                                aBank.economy.withdrawPlayer(t, Double.parseDouble(args[2]));
                                p.sendMessage(ChatColor.GREEN + "[aBank] " + t.getName() + "의 잔고에서 " + args[2] + "을(를) 빼앗았습니다.");
                            }
                            if (args[0].equalsIgnoreCase("add")) {
                                aBank.economy.depositPlayer(t, Double.parseDouble(args[2]));
                                p.sendMessage(ChatColor.GREEN + "[aBank] " + t.getName() + "의 잔고에 " + args[2] + "을(를) 추가했습니다.");
                            }
                        }
                        if (args[0].equalsIgnoreCase("bal")) {
                            if (!(args[1]==null)){
                                p.sendMessage(ChatColor.GREEN + "[aBank] " + t.getName() + "님의 잔고는 " + aBank.economy.getBalance(t.getName())  + "$ 입니다.");
                            } else {
                                p.sendMessage(ChatColor.GREEN + "[aBank] " + p.getName() + "님의 잔고는 " + PlaceholderAPI.setPlaceholders(p,"%vault_eco_balance_fixed%") +"$ 입니다.");
                            }
                        }

                    } else {
                        p.sendMessage(ChatColor.RED+"[aBank] NaN Money value");
                    }
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
            completions.add("add");
            completions.add("take");
            completions.add("set");
            completions.add("bal");
            completions.add("CheckPaper");
        }
        if (args.length==2) {
            for (Player p: Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
        }
        if (args.length==3) {
            completions.add("0");
            completions.add("10");
        }
        return completions;
    }
}
