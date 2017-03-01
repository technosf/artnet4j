/*
 * This file is part of artnet4j.
 * Copyright 2009 Karsten Schmidt (PostSpectacular Ltd.)
 * artnet4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * artnet4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with artnet4j. If not, see <http://www.gnu.org/licenses/>.
 */

package artnet4j;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import artnet4j.events.ArtNetServerEventAdapter;
import artnet4j.events.ArtNetServerListener;
import artnet4j.packets.AbstractArtNetPacket;
import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.PacketType;

public class ArtNet
{

    private static final Logger LOG = LoggerFactory.getLogger(ArtNet.class);

    protected static final long ARTPOLL_REPLY_TIMEOUT = 3000;

    protected static final String VERSION = "0.2.0";

    protected ArtNetServer server;
    protected ArtNetNodeDiscovery discovery;


    /**
     * 
     */
    public ArtNet()
    {
        LOG.info("Art-Net v{}", VERSION);
    }


    /**
     * @param l
     */
    public void addServerListener(ArtNetServerListener l)
    {
        server.addListener(l);
    }


    /**
     * @param packet
     */
    public void broadcastPacket(AbstractArtNetPacket packet)
    {
        server.broadcastPacket(packet);
    }


    /**
     * @return
     */
    public ArtNetNodeDiscovery getNodeDiscovery()
    {
        if (discovery == null)
        {
            discovery = new ArtNetNodeDiscovery(this);
        }
        return discovery;
    }


    /**
     * 
     */
    public void init()
    {
        server = new ArtNetServer();
        server.addListener(new ArtNetServerEventAdapter()
        {

            @Override
            public void artNetPacketReceived(AbstractArtNetPacket packet)
            {
                LOG.trace("packet received: {}", packet.getType());
                if (discovery != null
                        && packet.getType() == PacketType.ART_POLL_REPLY)
                {
                    discovery.discoverNode((ArtPollReplyPacket) packet);
                }
            }


            @Override
            public void artNetServerStarted(ArtNetServer artNetServer)
            {
                LOG.trace("server started callback");
            }


            @Override
            public void artNetServerStopped(ArtNetServer artNetServer)
            {
                LOG.info("server stopped");
            }
        });
    }


    /**
     * @param l
     */
    public void removeServerListener(ArtNetServerListener l)
    {
        server.removeListener(l);
    }


    /**
     * @param ip
     */
    public void setBroadCastAddress(String ip)
    {
        server.setBroadcastAddress(ip);
    }


    /**
     * Starts the Artnet client.
     *
     * @throws SocketException
     * @throws ArtNetException
     */
    public void start()
            throws SocketException, ArtNetException
    {
        start(null);
    }


    /**
     * Starts the Artnet client.
     *
     * @param networkAddress
     *            Network address to bind to
     * @throws SocketException
     * @throws ArtNetException
     */
    public void start(InetAddress networkAddress)
            throws SocketException, ArtNetException
    {
        if (server == null)
        {
            init();
        }
        server.start(networkAddress);
    }


    /**
     * @throws ArtNetException
     */
    public void startNodeDiscovery()
            throws ArtNetException
    {
        getNodeDiscovery().start();
    }


    /**
     * 
     */
    public void stop()
    {
        if (discovery != null)
        {
            discovery.stop();
        }
        if (server != null)
        {
            server.stop();
        }
    }


    /**
     * Sends the given packet to the specified Art-Net node.
     *
     * @param packet
     * @param node
     */
    public void unicastPacket(AbstractArtNetPacket packet, ArtNetNode node)
    {
        server.unicastPacket(packet, node.getIPAddress());
    }


    /**
     * Sends the given packet to the specified IP address.
     *
     * @param packet
     * @param adr
     */
    public void unicastPacket(AbstractArtNetPacket packet, InetAddress adr)
    {
        server.unicastPacket(packet, adr);
    }


    /**
     * Sends the given packet to the specified IP address.
     *
     * @param packet
     * @param adr
     */
    public void unicastPacket(AbstractArtNetPacket packet, String adr)
    {
        InetAddress targetAdress;
        try
        {
            targetAdress = InetAddress.getByName(adr);
            server.unicastPacket(packet, targetAdress);
        }
        catch (UnknownHostException e)
        {
            LOG.error(e.getMessage(), e);
        }
    }
}
