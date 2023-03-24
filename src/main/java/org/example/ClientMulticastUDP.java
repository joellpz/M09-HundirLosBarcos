package org.example;

import java.io.IOException;
import java.net.*;

public class ClientMulticastUDP {
    /* Client afegit al grup multicast SrvVelocitats.java que representa un velocímetre */

    private boolean continueRunning = true;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    NetworkInterface netIf;
    InetSocketAddress group;


    public ClientMulticastUDP(int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        //netIf = NetworkInterface.getByName("enp1s0");
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp, portValue);
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte[] receivedData = new byte[1024];

        socket.joinGroup(group, netIf);
        //System.out.printf("Connectat a %s:%d%n", group.getAddress(), group.getPort());

        while (continueRunning) {
            packet = new DatagramPacket(receivedData, receivedData.length);
            socket.setSoTimeout(5000);
            try {
                socket.receive(packet);
                continueRunning = getData(packet.getData(), packet.getLength());
            } catch (SocketTimeoutException e) {
                System.out.println("S'ha perdut la connexió amb el servidor.");
                continueRunning = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.leaveGroup(group, netIf);
        socket.close();
    }

    protected boolean getData(byte[] data, int lenght) {
        boolean ret = true;

        String s = new String(data, 0, lenght);

        //pintem velocimetre
        System.out.println(s);

        //if (v==1) ret=false;

        return ret;
    }

}
