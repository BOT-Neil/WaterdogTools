package network.ycc.waterdog.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.ScriptCustomEventPacket;
import cn.nukkit.scheduler.NukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Updater extends NukkitRunnable {
  public Updater(){}
  @Override
  public void run(){
    this.fetchServers();
    if (WaterdogAPI.serverList != null){
      for (String sl : WaterdogAPI.serverList){
        this.fetchPlayerCount(sl);
      }
    }

  }
  private void fetchServers(){
    Player p = WaterdogAPI.getRandomPlayer();
    if (p != null){
      ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      DataOutputStream a = new DataOutputStream(out);
      try {
        a.writeUTF("GetServers");
        pk.eventName = "bungeecord:main";
        pk.eventData = out.toByteArray();
        p.dataPacket(pk);
      } catch (Exception e) {
        Server.getInstance().getLogger().logException(e);
      }
    }
  }
  private void fetchPlayerCount(String server){
    Player p = WaterdogAPI.getRandomPlayer();
    if (p != null){
      ScriptCustomEventPacket pk = new ScriptCustomEventPacket();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      DataOutputStream a = new DataOutputStream(out);
      try {
        a.writeUTF("PlayerCount");
        a.writeUTF(server);
        pk.eventName = "bungeecord:main";
        pk.eventData = out.toByteArray();
        p.dataPacket(pk);
      } catch (Exception e) {
        Server.getInstance().getLogger().logException(e);
      }
    }
  }
}
