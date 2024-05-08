package pl.edu.pw.ee;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Huffman {

    private ArrayList<Node> listOfChars;
    private Node huffmanTreeRoot;
    private final char lastCharacter = (char)255;

    public Huffman() {
        this.listOfChars = new ArrayList<>();
        this.huffmanTreeRoot = null;
    }

    public int huffman(String pathToRootDir, boolean compress) {
        int ret = 0;
        if(pathToRootDir == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        if(compress) {
            ret = huffmanCompress(pathToRootDir);
        } else {
            ret = huffmanDecompress(pathToRootDir);
        }

        return ret;
    }

    private int huffmanCompress(String pathToRootDir) {
        int nmbOfBytes;
        String pathToFile = pathToRootDir + "\\huffman.txt";

        getListOfChars(pathToFile);
        makeHuffmanTree();
        getCodes();
        nmbOfBytes = makeCompressedFile(pathToRootDir);

        clear();

        return nmbOfBytes;
    }

    private int huffmanDecompress(String pathToRootDir) {
        int nmbOfChars = decompressFile(pathToRootDir);

        clear();

        return nmbOfChars;
    }

    private void getListOfChars(String pathToFile) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToFile));
            int ch;
            while((ch = br.read()) != -1) {
                addCharToList((char)ch);
            }
            listOfChars.add(new Node(lastCharacter));

            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file huffman.txt to compress");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCharToList(char c) throws IOException {
        checkChar(c);
        Node node = isInList(c);
        if(node != null) {
            node.frequencyUp();
        }
        else {
            listOfChars.add(new Node(c));
        }
    }

    private void checkChar(char c) throws IOException {
        if(c >= 255) {
            throw new IOException("Forbidden characters in the file");
        }
    }

    private Node isInList(char c) {
        for(Node node : listOfChars) {
            if(node.getCharacter() == c) {
                return node;
            }
        }
        return null;
    }

    private void makeHuffmanTree() {
        listOfChars.sort(Collections.reverseOrder());
        if(listOfChars.size() == 1) {
            Node parent = new Node(listOfChars.remove(0));
            listOfChars.add(parent);
        }
        while(listOfChars.size() > 1) {
            Node node1 = listOfChars.remove(listOfChars.size() - 1);
            Node node2 = listOfChars.remove(listOfChars.size() - 1);
            Node parent;
            if(node1.compareTo(node2) >= 0) {
                parent = new Node(node2, node1);
            }
            else {
                parent = new Node(node1, node2);
            }
            listOfChars.add(parent);
            listOfChars.sort(Collections.reverseOrder());
        }

        huffmanTreeRoot = listOfChars.remove(0);
    }

    private void getCodes() {
        if(!listOfChars.isEmpty()) {
            throw new RuntimeException("List should be empty");
        }
        String pathToNode = "";
        getCodes(huffmanTreeRoot, pathToNode);
    }

    private void getCodes(Node node, String pathToNode) {
        if(node == null) {
            return;
        }
        if(node.isNodeWithChar()) {
            node.setCode(pathToNode);
            listOfChars.add(node);
        }
        getCodes(node.getLeft(), pathToNode + "0");
        getCodes(node.getRight(), pathToNode + "1");
    }

    private int makeCompressedFile(String pathToRootDir) {
        try {
            int nmbOfBits = 0;
            String pathToCompressedFile = pathToRootDir + "\\compressedFile.huf";
            String pathToFile = pathToRootDir + "\\huffman.txt";
            BitWritter bitWritter = new BitWritter(pathToCompressedFile);
            BitReader bitReader = new BitReader(pathToFile);

            nmbOfBits += writeHuffmanTree(bitWritter);
            int ch;
            String code;
            while((ch = bitReader.nextByte()) != -1) {
                code = isInList((char)ch).getCode();
                bitWritter.writeCode(code);
                nmbOfBits += code.length();
            }
            code = isInList(lastCharacter).getCode();
            bitWritter.writeCode(code);
            nmbOfBits += code.length();

            bitWritter.close();
            bitReader.close();

            return (nmbOfBits % 8) == 0 ? nmbOfBits/8 : nmbOfBits/8 + 1;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file huffman.txt to compress");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int decompressFile(String pathToRootFile) {
        try {
            int nmbOfChars = 0;
            String pathToDecompressedFile = pathToRootFile + "\\decompressedFile.txt";
            String pathToCompressedFile = pathToRootFile + "\\compressedFile.huf";
            BitWritter bitWritter = new BitWritter(pathToDecompressedFile);
            BitReader bitReader = new BitReader(pathToCompressedFile);

            getHuffmanTree(bitReader);

            int b;
            Node actualNode = huffmanTreeRoot;
            while((b = bitReader.nextBit()) != -1) {
                if(b == 0) {
                    actualNode = actualNode.getLeft();
                }
                else if(b == 1) {
                    actualNode = actualNode.getRight();
                }
                if(actualNode.isNodeWithChar() && actualNode.getCharacter() != lastCharacter) {
                    bitWritter.writeByte((byte)actualNode.getCharacter());
                    nmbOfChars++;
                    actualNode = huffmanTreeRoot;
                }
                else if(actualNode.getCharacter() == lastCharacter) {
                    break;
                }
            }

            bitWritter.close();
            bitReader.close();

            return nmbOfChars;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file compressedFile.huf to decompress");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int writeHuffmanTree(BitWritter bitWritter) throws IOException {
        return writeHuffmanTree(huffmanTreeRoot, bitWritter);
    }

    private int writeHuffmanTree(Node node, BitWritter bitWritter) throws IOException {
        int nmbOfBits = 0;
        if(node == null) {
            return 0;
        }
        if(node.isNodeWithChar()) {
            bitWritter.writeBit(1);
            bitWritter.writeByte((byte)node.getCharacter());
            nmbOfBits += 9;
            return nmbOfBits;
        }
        else if(node != huffmanTreeRoot) {
            bitWritter.writeBit(0);
            nmbOfBits++;
        }

        nmbOfBits += writeHuffmanTree(node.getLeft(), bitWritter);
        nmbOfBits += writeHuffmanTree(node.getRight(), bitWritter);

        return nmbOfBits;
    }

    public void getHuffmanTree(BitReader bitReader) {
        try {
            getHuffmanTree(huffmanTreeRoot, bitReader, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getHuffmanTree(Node node, BitReader bitReader, boolean goLeft) throws IOException {
        int b = bitReader.nextBit();
        if(b == -1) {
            throw new IOException("Bad huffman tree format");
        }
        if(huffmanTreeRoot == null) {
            huffmanTreeRoot = new Node();
            if(b == 1) {
                huffmanTreeRoot.setChild(true, new Node((char)bitReader.nextByte()));
                getHuffmanTree(huffmanTreeRoot, bitReader, false);
            }
            else {
                huffmanTreeRoot.setChild(true, new Node());
                getHuffmanTree(huffmanTreeRoot.getLeft(), bitReader, true);
                getHuffmanTree(huffmanTreeRoot.getLeft(), bitReader, false);
                getHuffmanTree(huffmanTreeRoot, bitReader, false);
            }
            return;
        }
        if(b == 1) {
            node.setChild(goLeft, new Node((char)bitReader.nextByte()));
            return;
        }
        Node nextNode = new Node();
        node.setChild(goLeft, nextNode);

        getHuffmanTree(nextNode, bitReader, true);
        getHuffmanTree(nextNode, bitReader, false);
    }

    private void clear() {
        this.listOfChars.clear();
        this.huffmanTreeRoot = null;
    }
}
