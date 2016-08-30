package com.miguo.live.model.getHandOutRedPacket;

/**
 * Created by Administrator on 2016/7/30.
 */
public class ModelHandOutRedPacket {

    private String red_packet_type;

    private String red_packet_amount;

    private String red_packets;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setRed_packet_type(String red_packet_type) {
        this.red_packet_type = red_packet_type;
    }

    public String getRed_packet_type() {
        return this.red_packet_type;
    }

    public void setRed_packet_amount(String red_packet_amount) {
        this.red_packet_amount = red_packet_amount;
    }

    public String getRed_packet_amount() {
        return this.red_packet_amount;
    }

    public void setRed_packets(String red_packets) {
        this.red_packets = red_packets;
    }

    public String getRed_packets() {
        return this.red_packets;
    }
}
