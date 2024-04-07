package org.apo.abank.commands;

import org.apo.abank.A_bank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PayCmd implements TabExecutor {
    A_bank aBank=A_bank.instance;
    public PayCmd(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("pay");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            if (((Player) commandSender).hasPermission("Bank.pay")){
                Player p= (Player) commandSender;
                if (args.length==2) {
                    String reg="^[\\d]*$";
                    if (Pattern.matches(reg,args[1])){
                        double money=aBank.economy.getBalance(p.getName());
                        if (money>=(Double.parseDouble(args[1]))){
                            Player pay = Bukkit.getPlayer(args[0]);
                            aBank.economy.depositPlayer(pay, Double.parseDouble(args[1]));
                            aBank.economy.withdrawPlayer(p, Double.parseDouble(args[1]));
                            p.sendMessage(ChatColor.GREEN + "[aBank] Pay " + args[1] + "$ -> " + pay.getName());
                        } else {
                            p.sendMessage(ChatColor.RED+"[aBank] 당신은 충분한 돈을 가지고 있지 않습니다!");
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
            for (Player p: Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
        }
        if (args.length==2) {
            completions.add("0");
            completions.add("10");
        }
        return completions;
    }
}
