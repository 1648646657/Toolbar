package com.example.toolbar.XmlSaxParser;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SaxHelper extends DefaultHandler {

    private static final String TAG = "SaxHelper";

    private Person person;
    private ArrayList<Person> persons;
    private String tagName = null;

    @Override
    public void startDocument() throws SAXException {
//        super.startDocument();
        this.persons = new ArrayList<Person>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        super.startElement(uri, localName, qName, attributes);
        Log.i(TAG, "startElement: ");
        if(localName.equals("person")){
            person = new Person();
            person.setId(Integer.parseInt(attributes.getValue("id")));
            Log.i(TAG, "startElement: 开始处理Person元素");
        }
        tagName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
//        super.characters(ch, start, length);
        Log.i(TAG, "characters: ");
        if(tagName != null){
            String data = new String(ch,start,length);
            if(tagName.equals("name")){
                person.setName(data);
                Log.i(TAG, "characters: 开始处理name元素");
            }else if(tagName.equals("age")){
                person.setAge(data);
                Log.i(TAG, "characters: 开始处理age元素");
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        super.endElement(uri, localName, qName);
        Log.i(TAG, "endElement: ");
        if(localName.equals("person")){
            persons.add(person);
            person = null;
            Log.i(TAG, "endElement: 处理person元素结束");
        }
        tagName = null;
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.i(TAG, "endDocument: 读取到文档尾，xml解析结束");
    }

    public ArrayList<Person> getPersons(){
        return persons;
    }
}
