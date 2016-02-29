package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.*;

public class DemoDialogue {

    private HashMap<String, Node> dialoguetracker;
    private Node currentnode;
    private String currentCharacter;
    private Document doc;

    public DemoDialogue(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList protaglist = doc.getElementsByTagName("protagonist");
            HashMap<String, Node> tracker = new HashMap<String, Node>();
            for (int i = 0; i < protaglist.getLength(); i++) {
                Node node = protaglist.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    String name = elem.getAttribute("name");
                    NodeList diags = elem.getElementsByTagName("dialogue");
                    if (diags.getLength() > 0) {
                        tracker.put(name, diags.item(0));
                    }
                }

            }
            dialoguetracker = tracker;

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setCharacter(String character) {
        currentCharacter = character;
        System.out.println(dialoguetracker);
        currentnode = dialoguetracker.get(character);
    }

    public boolean hasOptions() {
        if (currentnode.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) currentnode;
            NodeList nlist = elem.getElementsByTagName("options");
            if (nlist.getLength() > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public String getSpeakingCharacter() {
        Element elem = (Element) currentnode;
        String name = elem.getAttribute("speaker");
        return name;
    }

    public NodeList getDialogueOptionsNodes() {
        if (this.hasOptions() && currentnode.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) currentnode;
            NodeList nlist = elem.getElementsByTagName("options");
            Element optionelem = (Element) nlist.item(0);
            NodeList optionslist = optionelem.getElementsByTagName("option");
            return optionslist;
        }
        return null;
    }

    public ArrayList<String> getDialogueOptionsText() {
        NodeList options = getDialogueOptionsNodes();
        ArrayList<String> output = new ArrayList<String>();
        if (options != null) {
            for (int i = 0; i < options.getLength(); i++) {
                String optiontext = options.item(i).getTextContent();
                output.add(optiontext);
            }
        }
        return output;
    }

    public void selectOption(Node option) {
        Element elem = (Element) option;
        String nextid = elem.getAttribute("nextID");
        moveToNextDialogue(nextid);
    }

    public String getDialogueText() {
        Element elem = (Element) currentnode;
        NodeList nlist = elem.getElementsByTagName("text");
        if (nlist.getLength() > 0) {
            String text = nlist.item(0).getTextContent();
            System.out.println(text);
            return text;
        }
        return null;
    }

    public String getCurrentCharacter() {
        return currentCharacter;
    }

    public void moveToNextDialogue() {
        try {
            if (!this.hasOptions() && currentnode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) currentnode;
                Element text = (Element) elem.getElementsByTagName("text").item(0);
                String nextid = text.getAttribute("nextID");
                XPathFactory charxPathfactory = XPathFactory.newInstance();
                XPath charxpath = charxPathfactory.newXPath();
                XPathExpression charexpr;
                charexpr = charxpath.compile("//protagonist[@name=\"" + currentCharacter + "\"]");
                NodeList charnodes = (NodeList) charexpr.evaluate(doc, XPathConstants.NODESET);
                XPathFactory nextxPathfactory = XPathFactory.newInstance();
                XPath nextxpath = nextxPathfactory.newXPath();
                XPathExpression nextexpr;
                nextexpr = nextxpath.compile("//dialogue[@id=\"" + nextid + "\"]");
                NodeList nextnodes = (NodeList) nextexpr.evaluate(charnodes.item(0), XPathConstants.NODESET);
                currentnode = nextnodes.item(0);
            }
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void moveToNextDialogue(String nextID) {
        try {
            if (!this.hasOptions() && currentnode.getNodeType() == Node.ELEMENT_NODE) {
                XPathFactory charxPathfactory = XPathFactory.newInstance();
                XPath charxpath = charxPathfactory.newXPath();
                XPathExpression charexpr;
                charexpr = charxpath.compile("//protagonist[@name=\"" + currentCharacter + "\"]");
                NodeList charnodes = (NodeList) charexpr.evaluate(doc, XPathConstants.NODESET);
                XPathFactory nextxPathfactory = XPathFactory.newInstance();
                XPath nextxpath = nextxPathfactory.newXPath();
                XPathExpression nextexpr;
                nextexpr = nextxpath.compile("//dialogue[@id=\"" + nextID + "\"]");
                NodeList nextnodes = (NodeList) nextexpr.evaluate(charnodes.item(0), XPathConstants.NODESET);
                currentnode = nextnodes.item(0);
            }
        } catch (XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
