package uk.ac.cam.echo2016.dynademo;

import java.util.HashSet;

public class DemoDialogueNode {
    
    private String character;
    private String dialogue;
    
    private HashSet<DemoDialogueNode> nextOptions;
    
    public DemoDialogueNode(String chara, String dia) {
        this.character = chara;
        this.dialogue = dia;
    }
    
    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
    public String getDialogue() {
        return dialogue;
    }
    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }
    public HashSet<DemoDialogueNode> getNextOptions() {
        return nextOptions;
    }
    public void setNextOptions(HashSet<DemoDialogueNode> nextOptions) {
        this.nextOptions = nextOptions;
    }

    public void addNextOption(DemoDialogueNode next) {
        this.nextOptions.add(next);
    }



}
