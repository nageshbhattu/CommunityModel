/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */

package CorpusReader;

import cc.mallet.extract.StringSpan;
import cc.mallet.extract.StringTokenization;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import java.io.*;

import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;
import cc.mallet.util.CharSequenceLexer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replace the data string or string buffer with a lowercased version. 
 *  This can improve performance over TokenSequenceLowercase.
 */

public class CharacterSequence2SocialText extends Pipe implements Serializable {
    CharSequenceLexer lexer;
    Alphabet userAlphabet;
    public CharacterSequence2SocialText (CharSequenceLexer lexer, Alphabet userAlphabet)
	{
            super(new Alphabet(), null);
            this.lexer = lexer;
            this.userAlphabet = userAlphabet;    
	}

	public CharacterSequence2SocialText (String regex, Alphabet userAlphabet)
	{
            super(new Alphabet(), null);
            this.lexer = new CharSequenceLexer (regex);
            this.userAlphabet = userAlphabet;
	}

	public CharacterSequence2SocialText (Pattern regex, Alphabet userAlphabet)
	{
            super(new Alphabet(), null);
            this.lexer = new CharSequenceLexer (regex);
            this.userAlphabet = userAlphabet;
	}

	public CharacterSequence2SocialText (Alphabet userAlphabet)
	{
           	this (new CharSequenceLexer(),userAlphabet);
	}

    public Instance pipe (Instance carrier) {
        String line = (String) carrier.getData();
        Pattern pattern = Pattern.compile("(.*?)#\\s+#(.*)");
        Matcher mat  = pattern.matcher(line);
        
        
        if(mat.find()){
            int fromIndex = -1;
            String users = mat.group(1);
            Pattern emailPattern = Pattern.compile("([\\p{L}\\p{N}.]+@[\\p{L}\\p{N}.]+)");
            Matcher emailMatcher = emailPattern.matcher(users);
            if(emailMatcher.find()){
                fromIndex = userAlphabet.lookupIndex(emailMatcher.group(1));
            }
            ArrayList<Integer> toAddresses  = new ArrayList<>();
            while(emailMatcher.find()){
                toAddresses.add(userAlphabet.lookupIndex(emailMatcher.group(1)));
            }
            String text  = mat.group(2);
            CharSequence string = (CharSequence) text;
        
            lexer.setCharSequence (string);
            TokenSequence ts = new StringTokenization (string);
            while (lexer.hasNext()) {
                    lexer.next();
                    ts.add (new StringSpan (string, lexer.getStartOffset (), lexer.getEndOffset ()));
            }
            FeatureSequence ret =
			new FeatureSequence ((Alphabet)getDataAlphabet(), ts.size());
		for (int i = 0; i < ts.size(); i++) {
			ret.add (ts.get(i).getText());
		}
            Document document  = new Document(fromIndex, toAddresses,ret,userAlphabet);    
            carrier.setData(document);
            }else {
                System.out.println("Error in input string");
            }
        
        return carrier;
    }

	// Serialization 
	
	private static final long serialVersionUID = 1;
	private static final int CURRENT_SERIAL_VERSION = 0;
	
	private void writeObject (ObjectOutputStream out) throws IOException {
		out.writeInt (CURRENT_SERIAL_VERSION);
	}
	
	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		int version = in.readInt ();
	}

}
