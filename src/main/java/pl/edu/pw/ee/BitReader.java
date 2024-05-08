package pl.edu.pw.ee;

import java.io.*;

public class BitReader {

    private final DataInputStream dis;
    private int buffor;
    private int offset;

    public BitReader(String pathToFile) throws IOException {
        this.dis = new DataInputStream(new FileInputStream(pathToFile));
        if((this.buffor = dis.read()) == -1) {
            throw new IOException("File is empty");
        }
        this.offset = 8;
    }

    public int nextBit() throws IOException {
        if(!moveOffset()) {
            return -1;
        }
        return (buffor & (1 << offset)) != 0 ? 1 : 0;
    }

    public int nextByte() throws IOException {
        int ret = 0;
        for(int i = 7; i >= 0; i--) {
            if(!moveOffset()) {
                return -1;
            }
            if((buffor & (1 << offset)) != 0) {
                ret = ret | (1 << i);
            }
        }
        return (ret <= 255 && ret >= 0) ? ret : -1;
    }

    private boolean moveOffset() throws IOException {
        offset--;
        if(offset == -1) {
            if((buffor = dis.read()) == -1) {
                return false;
            }
            offset = 7;
        }
        return true;
    }

    public void close() throws IOException {
        this.dis.close();
    }
}
