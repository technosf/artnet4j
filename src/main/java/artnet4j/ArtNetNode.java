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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import artnet4j.packets.ArtPollReplyPacket;
import artnet4j.packets.ByteUtils;

public class ArtNetNode
{

    private static final Logger LOG = LoggerFactory.getLogger(ArtNetNode.class);

    protected final NodeStyle nodeStyle;

    private InetAddress ip;

    private int subSwitch;

    private int oemCode;

    private int nodeStatus;
    private NodeReportCode reportCode;

    private String shortName;
    private String longName;

    private int numPorts;
    private PortDescriptor[] ports;
    private byte[] dmxIns;
    private byte[] dmxOuts;


    /**
     * 
     */
    public ArtNetNode()
    {
        this(NodeStyle.ST_NODE);
    }


    /**
     * @param style
     */
    public ArtNetNode(NodeStyle style)
    {
        nodeStyle = style;
    }


    /**
     * @param source
     */
    public void extractConfig(ArtPollReplyPacket source)
    {
        setIPAddress(source.getIPAddress());
        subSwitch = source.getSubSwitch();
        oemCode = source.getOEMCode();
        nodeStatus = source.getNodeStatus();
        shortName = source.getShortName();
        longName = source.getLongName();
        ports = source.getPorts();
        numPorts = ports.length;
        reportCode = source.getReportCode();
        dmxIns = source.getDmxIns();
        dmxOuts = source.getDmxOuts();
        LOG.info("updated node config");
    }


    /**
     * @return the dmxIns
     */
    public byte[] getDmxIns()
    {
        return dmxIns;
    }


    /**
     * @return the dmxOuts
     */
    public byte[] getDmxOuts()
    {
        return dmxOuts;
    }


    /**
     * @return the ip
     */
    public InetAddress getIPAddress()
    {
        return ip;
    }


    /**
     * @return the longName
     */
    public String getLongName()
    {
        return longName;
    }


    /**
     * @return the nodeStatus
     */
    public int getNodeStatus()
    {
        return nodeStatus;
    }


    /**
     * @return the nodeStyle
     */
    public NodeStyle getNodeStyle()
    {
        return nodeStyle;
    }


    /**
     * @return the numPorts
     */
    public int getNumPorts()
    {
        return numPorts;
    }


    /**
     * @return the oemCode
     */
    public int getOemCode()
    {
        return oemCode;
    }


    /**
     * @return the ports
     */
    public PortDescriptor[] getPorts()
    {
        return ports;
    }


    /**
     * @return the reportCode
     */
    public NodeReportCode getReportCode()
    {
        return reportCode;
    }


    /**
     * @return the shortName
     */
    public String getShortName()
    {
        return shortName;
    }


    /**
     * @return
     */
    public int getSubNet()
    {
        return subSwitch;
    }


    /**
     * @return
     */
    public String getSubNetAsHex()
    {
        return ByteUtils.hex(subSwitch, 2);
    }


    /**
     * @param ip
     */
    public void setIPAddress(InetAddress ip)
    {
        this.ip = ip;
    }


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "node: " + nodeStyle + " " + ip + " " + longName + ", "
                + numPorts + " ports, subswitch: "
                + ByteUtils.hex(subSwitch, 2);
    }
}
