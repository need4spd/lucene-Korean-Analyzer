package com.tistory.devyongsik.analyzer;


import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.CharacterUtils;
import org.apache.lucene.util.CharacterUtils.CharacterBuffer;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 입력되는 문장을 읽어 Token으로 만들어 return
 * split 기준은 스페이스, 특수문자, 한글 / 영문,숫자
 *
 * @author 장용석, 2011.07.16 need4spd@naver.com
 */

public class KoreanCharacterTokenizer extends Tokenizer {

private Logger logger = LoggerFactory.getLogger(KoreanCharacterTokenizer.class);
	
	public KoreanCharacterTokenizer(Reader input) {
		super(input);
		charUtils = CharacterUtils.getInstance(Version.LUCENE_36);
	}
	
	/**
	 * Creates a new {@link CharTokenizer} instance
	 * 
	 * @param matchVersion
	 *          Lucene version to match See {@link <a href="#version">above</a>}
	 * @param input
	 *          the input to split up into tokens
	 */
	public KoreanCharacterTokenizer(Version matchVersion, Reader input) {
		super(input);
		charUtils = CharacterUtils.getInstance(matchVersion);
	}

	/**
	 * Creates a new {@link CharTokenizer} instance
	 * 
	 * @param matchVersion
	 *          Lucene version to match See {@link <a href="#version">above</a>}
	 * @param source
	 *          the attribute source to use for this {@link Tokenizer}
	 * @param input
	 *          the input to split up into tokens
	 */
	public KoreanCharacterTokenizer(Version matchVersion, AttributeSource source,
			Reader input) {
		super(source, input);
		charUtils = CharacterUtils.getInstance(matchVersion);
	}

	/**
	 * Creates a new {@link CharTokenizer} instance
	 * 
	 * @param matchVersion
	 *          Lucene version to match See {@link <a href="#version">above</a>}
	 * @param factory
	 *          the attribute factory to use for this {@link Tokenizer}
	 * @param input
	 *          the input to split up into tokens
	 */
	public KoreanCharacterTokenizer(Version matchVersion, AttributeFactory factory,
			Reader input) {
		super(factory, input);
		charUtils = CharacterUtils.getInstance(matchVersion);
	}

	// note: bufferIndex is -1 here to best-effort AIOOBE consumers that don't call reset()
	private int offset = 0, bufferIndex = -1, dataLen = 0, finalOffset = 0;
	private static final int MAX_WORD_LEN = 255;
	private static final int IO_BUFFER_SIZE = 4096;

	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
	private final PositionIncrementAttribute positionAtt = addAttribute(PositionIncrementAttribute.class);

	private final CharacterUtils charUtils;
	private final CharacterBuffer ioBuffer = CharacterUtils.newCharacterBuffer(IO_BUFFER_SIZE);

	/**
	 * Returns true iff a codepoint should be included in a token. This tokenizer
	 * generates as tokens adjacent sequences of codepoints which satisfy this
	 * predicate. Codepoints for which this is false are used to define token
	 * boundaries and are not included in tokens.
	 */
	protected boolean isTokenChar(int c) {
		return Character.isLetterOrDigit(c);
	}

	/**
	 * Called on each token character to normalize it before it is added to the
	 * token. The default implementation does nothing. Subclasses may use this to,
	 * e.g., lowercase tokens.
	 */
	protected int normalize(int c) {
		return Character.toLowerCase(c);
	}

	private int preChar = 0;

	private int preCharType = 99;
	private int nowCharType = 99;

	private final int DIGIT = 0; //숫자
	private final int KOREAN = 1; //한글
	private final int ALPHA = 2; //영어

	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		
		logger.debug("incrementToken");
		
		int length = 0;
		int start = -1; // this variable is always initialized
		int end = -1;
		char[] buffer = termAtt.buffer();
		while (true) {
			
			if (bufferIndex >= dataLen) {
				offset += dataLen;
				if(!charUtils.fill(ioBuffer, input)) { // read supplementary char aware with CharacterUtils
					dataLen = 0; // so next offset += dataLen won't decrement offset
					if (length > 0) {
						break;
					} else {
						finalOffset = correctOffset(offset);
						return false;
					}
				}
				dataLen = ioBuffer.getLength();
				bufferIndex = 0;
			}
			
			// use CharacterUtils here to support < 3.1 UTF-16 code unit behavior if the char based methods are gone
			final int c = charUtils.codePointAt(ioBuffer.getBuffer(), bufferIndex);
			final int charCount = Character.charCount(c);
			bufferIndex += charCount;

			if (isTokenChar(c)) {               // if it's a token char
				
				//전 문자와 현재 문자를 비교해서 속성이 다르면  분리해낸다.
				if (length > 0) {
					//이전문자의 속성 set
					if(Character.isDigit(preChar)) preCharType = this.DIGIT;
					else if(preChar < 127) preCharType = this.ALPHA;
					else preCharType = this.KOREAN;

					//현재문자의 속성set
					if(Character.isDigit(c)) nowCharType = this.DIGIT;
					else if(c < 127) nowCharType = this.ALPHA;
					else nowCharType = this.KOREAN;

					if(preCharType != nowCharType) { //앞뒤 Character가 서로 다른 형식
						bufferIndex--;

						//여기서 토큰을 하나 끊어야 함
						termAtt.setLength(length);
					    offsetAtt.setOffset(correctOffset(start), correctOffset(start+length));
					    typeAtt.setType("word");
					    positionAtt.setPositionIncrement(1);

						return true;
					}
				}
				
				preChar = c;
				
				if (length == 0) {                // start of token
					assert start == -1;
					start = offset + bufferIndex - charCount;
					end = start;
				} else if (length >= buffer.length-1) { // check if a supplementary could run out of bounds
					
					buffer = termAtt.resizeBuffer(2+length); // make sure a supplementary fits in the buffer
				
				}
				
				end += charCount;
				length += Character.toChars(normalize(c), buffer, length); // buffer it, normalized
				
				if (length >= MAX_WORD_LEN) // buffer overflow! make sure to check for >= surrogate pair could break == test
					break;
			
			} else if (length > 0) {            // at non-Letter w/ chars
				break;                           // return 'em
			}
		}

		termAtt.setLength(length);
		assert start != -1;
		offsetAtt.setOffset(correctOffset(start), finalOffset = correctOffset(end));
		typeAtt.setType("word");
	    positionAtt.setPositionIncrement(1);
	    
		return true;
	}

	@Override
	public final void end() {
		// set final offset
		offsetAtt.setOffset(finalOffset, finalOffset);
	}

	@Override
	public void reset() throws IOException {
		bufferIndex = 0;
		offset = 0;
		dataLen = 0;
		finalOffset = 0;
		ioBuffer.reset(); // make sure to reset the IO buffer!!
	}
}
