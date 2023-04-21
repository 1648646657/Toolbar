package com.lzx.test.XmlSaxParser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzx.test.R;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlParserActivity extends AppCompatActivity {

    private ArrayList<Person> persons;
    private TextView saxText;
    private Button saxBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser);
        saxText = findViewById(R.id.sax_text);
        saxBtn = findViewById(R.id.sax_btn);
        saxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    persons = xmlSaxParser();
                    saxText.setText(persons.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArrayList<Person> xmlSaxParser() throws Exception{
        InputStream is = getAssets().open("person.xml");
        SaxHelper sh = new SaxHelper();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(is,sh);
        is.close();
        return sh.getPersons();
    }
}