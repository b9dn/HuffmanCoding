package pl.edu.pw.ee;

public class Node implements Comparable<Node> {

    private int frequency;
    private char character;
    private Node left;
    private Node right;
    private boolean isNodeWithChar;
    private String code;

    public Node(char character) {
        this.character = character;
        this.frequency = 1;
        this.left = null;
        this.right = null;
        this.isNodeWithChar = true;
    }

    public Node() {
        this.frequency = 0;
        this.left = null;
        this.right = null;
        this.isNodeWithChar = false;
    }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
        this.frequency = left.getFrequency() + right.getFrequency();
        this.isNodeWithChar = false;
    }

    public Node(Node left) {
        this.left = left;
        this.frequency = left.getFrequency();
        this.isNodeWithChar = false;
    }

    public void frequencyUp() {
        this.frequency++;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public char getCharacter() {
        return this.character;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }

    public void setChild(boolean left, Node child) {
        if (left) {
            this.left = child;
        } else {
            this.right = child;
        }
    }
    public boolean isNodeWithChar() {
            return isNodeWithChar;
    }
    @Override
    public int compareTo(Node node) {
        return frequency - node.frequency;
    }
}
