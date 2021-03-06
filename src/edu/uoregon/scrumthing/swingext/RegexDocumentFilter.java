package edu.uoregon.scrumthing.swingext;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RegexDocumentFilter extends DocumentFilter {

    private String pattern;

    public RegexDocumentFilter(String pat) {
        pattern = pat;
    }

    @Override
    public void replace(FilterBypass fb, int offs, int length,
        String str, AttributeSet a) throws BadLocationException {

        String text = fb.getDocument().getText(0,
                fb.getDocument().getLength());
        if (str == null || str.isEmpty()) return;
        text += str;
        if (str.matches(pattern)) {   //  "^[0-9]+[.]?[0-9]{0,}$"
            super.replace(fb, offs, length, str, a);
        } else {
        	Toolkit.getDefaultToolkit().beep();
        }

    }

    @Override
    public void insertString(FilterBypass fb, int offs, String str,
            AttributeSet a) throws BadLocationException {

        String text = fb.getDocument().getText(0,
                fb.getDocument().getLength());
        if (str == null || str.isEmpty()) return;
        text += str;
        if (str.matches(pattern)) {
            super.insertString(fb, offs, str, a);
        } else {
        	Toolkit.getDefaultToolkit().beep();
        }
    }
}