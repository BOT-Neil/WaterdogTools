package network.ycc.waterdog.nukkit;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ScriptCustomEventPacket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class EventListener implements Listener {
  public EventListener(){ }
  @EventHandler
  public void serverList(DataPacketReceiveEvent e){
    if (e.getPlayer() == null || !e.getPacket().toString().startsWith("ScriptCustomEventPacket")) {
      return;
    }
    ScriptCustomEventPacket scep = ((ScriptCustomEventPacket) e.getPacket());
    DataInputStream in = new DataInputStream(new ByteArrayInputStream(scep.eventData));
    try {
      String subChannel = in.readUTF();
      if (subChannel.equals("GetServers")) {
        WaterdogAPI.serverList = in.readUTF().split(", ");
      }
    }
    catch (EOFException ex) {
      // Do nothing.
    } catch (IOException ex) {
      // This should never happen.
      ex.printStackTrace();
    }
  }
  @EventHandler
  public void playerCount(DataPacketReceiveEvent e){
    if (e.getPlayer() == null || !e.getPacket().toString().startsWith("ScriptCustomEventPacket")) {
      return;
    }
    ScriptCustomEventPacket scep = ((ScriptCustomEventPacket) e.getPacket());
    DataInputStream in = new DataInputStream(new ByteArrayInputStream(scep.eventData));
    try {
      String subChannel = in.readUTF();
      if (subChannel.equals("PlayerCount")) {
        String server = in.readUTF();
        if (in.available() > 0) {
          int online = in.readInt();
          WaterdogAPI.plCount.put(server, online);
        }
      }
    }
    catch (EOFException ex) {
      // Do nothing.
    } catch (IOException ex) {
      // This should never happen.
      ex.printStackTrace();
    }
  }
}
