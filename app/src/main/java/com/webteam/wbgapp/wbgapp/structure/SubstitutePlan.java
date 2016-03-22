package com.webteam.wbgapp.wbgapp.structure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by Deathlymad on 20.03.2016 .
 */
public class SubstitutePlan {
    private class SubstituteEntry
    {
        String grade, formselector, subject,room, commentary;
        String newSubject, newRoom;

        SubstituteEntry(Node n)
        {

        }
    }

    private List<SubstituteEntry> plan;

    public SubstitutePlan(Document xmlSubstitues) {
        Element e = xmlSubstitues.getDocumentElement();
        //TODO: Wenn wir Plan haben xD
    }

    public void getSubstitutionByClass()
    {

    }

    public void getSubstitutionByPlan()
    {

    }
}
