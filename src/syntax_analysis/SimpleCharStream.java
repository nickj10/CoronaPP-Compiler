package syntax_analysis;/* Generated By:JavaCC: Do not edit this line. SimpleCharStream.java Version 6.0 */
/* JavaCCOptions:STATIC=true,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
/**
 * An implementation of interface CharStream, where the stream is assumed to
 * contain only ASCII characters (without unicode processing).
 */

public class SimpleCharStream
{
/** Whether parser is static. */
  public static final boolean staticFlag = true;
  static int bufsize;
  static int available;
  static int tokenBegin;
/** Position in buffer. */
  static public int bufpos = -1;

  static protected java.io.Reader inputStream;

  static protected char[] buffer;
  static protected int maxNextCharInd = 0;
  static protected int inBuf = 0;
  static protected int tabSize = 8;
  static protected boolean trackLineColumn = false;

  static public void setTabSize(int i) { tabSize = i; }
  static public int getTabSize() { return tabSize; }


  static protected void ExpandBuff(boolean wrapAround)
  {
    char[] newbuffer = new char[bufsize + 2048];

    try
    {
      if (wrapAround)
      {
        System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
        System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
        buffer = newbuffer;

        maxNextCharInd = (bufpos += (bufsize - tokenBegin));
      }
      else
      {
        System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
        buffer = newbuffer;

        maxNextCharInd = (bufpos -= tokenBegin);
      }
    }
    catch (Throwable t)
    {
      throw new Error(t.getMessage());
    }


    bufsize += 2048;
    available = bufsize;
    tokenBegin = 0;
  }

  static protected void FillBuff() throws java.io.IOException
  {
    if (maxNextCharInd == available)
    {
      if (available == bufsize)
      {
        if (tokenBegin > 2048)
        {
          bufpos = maxNextCharInd = 0;
          available = tokenBegin;
        }
        else if (tokenBegin < 0)
          bufpos = maxNextCharInd = 0;
        else
          ExpandBuff(false);
      }
      else if (available > tokenBegin)
        available = bufsize;
      else if ((tokenBegin - available) < 2048)
        ExpandBuff(true);
      else
        available = tokenBegin;
    }

    int i;
    try {
      if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1)
      {
        inputStream.close();
        throw new java.io.IOException();
      }
      else
        maxNextCharInd += i;
      return;
    }
    catch(java.io.IOException e) {
      --bufpos;
      backup(0);
      if (tokenBegin == -1)
        tokenBegin = bufpos;
      throw e;
    }
  }

/** Start. */
  static public char BeginToken() throws java.io.IOException
  {
    tokenBegin = -1;
    char c = readChar();
    tokenBegin = bufpos;

    return c;
  }

/** Read a character. */
  static public char readChar() throws java.io.IOException
  {
    if (inBuf > 0)
    {
      --inBuf;

      if (++bufpos == bufsize)
        bufpos = 0;

      return buffer[bufpos];
    }

    if (++bufpos >= maxNextCharInd)
      FillBuff();

    char c = buffer[bufpos];

    return c;
  }

  @Deprecated
  /**
   * @deprecated
   * @see #getEndColumn
   */

  static public int getColumn() {
    return -1;
  }

  @Deprecated
  /**
   * @deprecated
   * @see #getEndLine
   */

  static public int getLine() {
    return -1;
  }

  /** Get token end column number. */
  static public int getEndColumn() {
    return -1;
  }

  /** Get token end line number. */
  static public int getEndLine() {
    return -1;
  }

  /** Get token beginning column number. */
  static public int getBeginColumn() {
    return -1;
  }

  /** Get token beginning line number. */
  static public int getBeginLine() {
    return -1;
  }

/** Backup a number of characters. */
  static public void backup(int amount) {

    inBuf += amount;
    if ((bufpos -= amount) < 0)
      bufpos += bufsize;
  }

  /** Constructor. */
  public SimpleCharStream(java.io.Reader dstream, int startline,
  int startcolumn, int buffersize)
  {
    if (inputStream != null)
      throw new Error("\n   ERROR: Second call to the constructor of a static SimpleCharStream.\n" +
      "       You must either use ReInit() or set the JavaCC option STATIC to false\n" +
      "       during the generation of this class.");
    inputStream = dstream;

    available = bufsize = buffersize;
    buffer = new char[buffersize];
  }

  /** Constructor. */
  public SimpleCharStream(java.io.Reader dstream, int startline,
                          int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }

  /** Constructor. */
  public SimpleCharStream(java.io.Reader dstream)
  {
    this(dstream, 1, 1, 4096);
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader dstream, int startline,
  int startcolumn, int buffersize)
  {
    inputStream = dstream;

    if (buffer == null || buffersize != buffer.length)
    {
      available = bufsize = buffersize;
      buffer = new char[buffersize];
    }
    tokenBegin = inBuf = maxNextCharInd = 0;
    bufpos = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader dstream, int startline,
                     int startcolumn)
  {
    ReInit(dstream, startline, startcolumn, 4096);
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader dstream)
  {
    ReInit(dstream, 1, 1, 4096);
  }
  /** Constructor. */
  public SimpleCharStream(java.io.InputStream dstream, String encoding, int startline,
  int startcolumn, int buffersize) throws java.io.UnsupportedEncodingException
  {
    this(encoding == null ? new java.io.InputStreamReader(dstream) : new java.io.InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
  }

  /** Constructor. */
  public SimpleCharStream(java.io.InputStream dstream, int startline,
  int startcolumn, int buffersize)
  {
    this(new java.io.InputStreamReader(dstream), startline, startcolumn, buffersize);
  }

  /** Constructor. */
  public SimpleCharStream(java.io.InputStream dstream, String encoding, int startline,
                          int startcolumn) throws java.io.UnsupportedEncodingException
  {
    this(dstream, encoding, startline, startcolumn, 4096);
  }

  /** Constructor. */
  public SimpleCharStream(java.io.InputStream dstream, int startline,
                          int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }

  /** Constructor. */
  public SimpleCharStream(java.io.InputStream dstream, String encoding) throws java.io.UnsupportedEncodingException
  {
    this(dstream, encoding, 1, 1, 4096);
  }

  /** Constructor. */
  public SimpleCharStream(java.io.InputStream dstream)
  {
    this(dstream, 1, 1, 4096);
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream dstream, String encoding, int startline,
                          int startcolumn, int buffersize) throws java.io.UnsupportedEncodingException
  {
    ReInit(encoding == null ? new java.io.InputStreamReader(dstream) : new java.io.InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream dstream, int startline,
                          int startcolumn, int buffersize)
  {
    ReInit(new java.io.InputStreamReader(dstream), startline, startcolumn, buffersize);
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream dstream, String encoding) throws java.io.UnsupportedEncodingException
  {
    ReInit(dstream, encoding, 1, 1, 4096);
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream dstream)
  {
    ReInit(dstream, 1, 1, 4096);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream dstream, String encoding, int startline,
                     int startcolumn) throws java.io.UnsupportedEncodingException
  {
    ReInit(dstream, encoding, startline, startcolumn, 4096);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream dstream, int startline,
                     int startcolumn)
  {
    ReInit(dstream, startline, startcolumn, 4096);
  }
  /** Get token literal value. */
  static public String GetImage()
  {
    if (bufpos >= tokenBegin)
      return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
    else
      return new String(buffer, tokenBegin, bufsize - tokenBegin) +
                            new String(buffer, 0, bufpos + 1);
  }

  /** Get the suffix. */
  static public char[] GetSuffix(int len)
  {
    char[] ret = new char[len];

    if ((bufpos + 1) >= len)
      System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
    else
    {
      System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0,
                                                        len - bufpos - 1);
      System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
    }

    return ret;
  }

  /** Reset buffer when finished. */
  static public void Done()
  {
    buffer = null;
  }
}
/* JavaCC - OriginalChecksum=d0197e3faf6da570e5276ad49515364e (do not edit this line) */
