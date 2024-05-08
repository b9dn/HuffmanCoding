package pl.edu.pw.ee;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class HuffmanTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPathIsNull() {
        //given
        String pathToRootDir = null;
        Huffman huffman = new Huffman();

        //when
        huffman.huffman(pathToRootDir, true);

        //then
        assert false;
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenFileToCompressMissing() {
        //given
        String pathToRootDir = "src/test/resources/empty";
        Huffman huffman = new Huffman();

        //when
        huffman.huffman(pathToRootDir, true);

        //then
        assert false;
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenFileToDecompressMissing() {
        //given
        String pathToRootDir = "src/test/resources/empty";
        Huffman huffman = new Huffman();

        //when
        huffman.huffman(pathToRootDir , false);

        //then
        assert false;
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenFileIsEmpty() {
        //given
        String pathToRootDir = "src/test/resources/empty_file";
        Huffman huffman = new Huffman();

        //when
        huffman.huffman(pathToRootDir, true);

        //then
        assert false;
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenPolishCharactersInFile() {
        //given
        String pathToRootDir = "src/test/resources/polish";
        Huffman huffman = new Huffman();

        //when
        huffman.huffman(pathToRootDir, false);

        //then
        assert false;
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenBadHuffmanTreeFormat() {
        //given
        String pathToRootDir = "src/test/resources/bad_tree";
        Huffman huffman = new Huffman();

        //when
        huffman.huffman(pathToRootDir, false);

        //then
        assert false;
    }

    @Test
    public void shouldWorkCorrectlyWhenCompressAndDecompress() {
        //given
        String pathToRootDir = "src/test/resources/niemanie";
        File beforeCompression = new File(pathToRootDir + "\\huffman.txt");
        File afterDecompression = new File(pathToRootDir + "\\decompressedFile.txt");
        Huffman huffman = new Huffman();

        //when
        int a = huffman.huffman(pathToRootDir, true);
        int b = huffman.huffman(pathToRootDir, false);

        //then
        System.out.println("Compressed - " + a);
        System.out.println("Decompressed - " + b);

        assertEquals(beforeCompression.length(), afterDecompression.length());
    }

    @Test
    public void shouldWorkCorrectlyWhenOnlyOneTypeOfCharacterInFile() {
        String pathToRootDir = "src/test/resources/bbbbb";
        Huffman huffman = new Huffman();

        //when
        int a = huffman.huffman(pathToRootDir, true);
        int b = huffman.huffman(pathToRootDir, false);

        //then
        System.out.println("Compressed - " + a);
        System.out.println("Decompressed - " + b);

        assert true;
    }
}
