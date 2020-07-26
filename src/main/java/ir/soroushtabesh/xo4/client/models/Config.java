package ir.soroushtabesh.xo4.client.models;

public class Config {
    private String address = "127.0.0.1";
    private int port = 8000;

    public Config() {
    }

    public Config(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
