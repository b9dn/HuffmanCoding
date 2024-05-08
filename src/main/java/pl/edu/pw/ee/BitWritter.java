package pl.edu.pw.ee;

import java.io.*;

public class BitWritter {

    private final DataOutputStream dos;
    private int buffor;
    private int offset;

    public BitWritter(String pathToFile) throws FileNotFoundException {
        this.dos = new DataOutputStream(new FileOutputStream(pathToFile));
        this.buffor = 0;
        this.offset = 8;
    }

    public void writeBit(int bit) throws IOException {
        if(bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Argument bit should be 0 or 1");
        }
        moveOffset();
        buffor = buffor | (bit << offset);
    }

    public void writeByte(byte b) throws IOException {
        for(int i = 7; i >= 0; i--) {
            if((b & (1 << i)) != 0) {
                writeBit(1);
            }
            else {
                writeBit(0);
            }
        }
    }

    public void writeCode(String code) throws IOException {
        for(int i = 0; i < code.length(); i++) {
            writeBit(Character.getNumericValue(code.charAt(i)));
        }
    }

    private void moveOffset() throws IOException {
        offset--;
        if(offset == -1) {
            dos.write(buffor);
            buffor = 0;
            offset = 7;
        }
    }

    public void close() throws IOException {
        if(offset != 8) {
            dos.write(buffor);
            offset = 0;
        }
        this.dos.close();
    }
}
