package network.ycc.waterdog.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ScriptCustomEventPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.MainLogger;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class WaterdogAPI extends PluginBase {
  private static int playerCount;
  public static String[] serverList;
  public static Map<String, Integer> plCount = new HashMap<>();
  private static MainLogger log = Server.getInstance().getLogger();
  @Override
  public void onEnable(){
    this.getServer().getScheduler().scheduleRepeatingTask(this, new Updater(), 20, true);
    this.getServer().getPluginManager().registerEvents(new EventListener(), this);
  }
  public static int getPlayerCount(String server){
    return plCount.getOrDefault(server, 0);
  }
  public static void transferPlayer(Player p, String destination) {
    ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    DataOutputStream a = new DataOutputStream(out);
    try {
      a.writeUTF("Connect");
      a.writeUTF(destination);
      pk.eventName = "bungeecord:main";
      pk.eventData = out.toByteArray();
      p.dataPacket(pk);
    } catch (Exception e) {
      log.warning("Error while transferring ( PLAYER: " + p.getName() + " | DEST: " + destination + " )");
      log.logException(e);
    }
  }
  public static void transferOther(String name, String destination) {
    Player p = getRandomPlayer();
    if (p != null) {
      ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      DataOutputStream a = new DataOutputStream(out);
      try {
        a.writeUTF("ConnectOther");
        a.writeUTF(name);
        a.writeUTF(destination);
        pk.eventName = "bungeecord:main";
        pk.eventData = out.toByteArray();
        p.dataPacket(pk);
      } catch (Exception e) {
        log.warning("Error while transferring ( PLAYER: " + p.getName() + " | DEST: " + destination + " )");
        log.logException(e);
      }
    } else {
      log.info("Cannot execute transfer for player " + name + ": No player online!");
    }
  }
  public static Player getRandomPlayer() {
    if (!Server.getInstance().getOnlinePlayers().isEmpty()) {
      return Server.getInstance().getOnlinePlayers().values().iterator().next();
    } else {
      return null;
    }
  }
}
