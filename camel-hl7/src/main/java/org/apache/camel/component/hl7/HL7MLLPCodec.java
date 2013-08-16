/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.hl7;

import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * HL7 MLLP codec.
 * <p/>
 * This codec supports encoding/decoding the HL7 MLLP protocol. It will use the
 * default markers for start and end combination:
 * <ul>
 * <li>0x0b (11 decimal) = start marker</li>
 * <li>0x0d (13 decimal = the \r char) = segment terminators</li>
 * <li>0x1c (28 decimal) = end 1 marker</li>
 * <li>0x0d (13 decimal) = end 2 marker</li>
 * </ul>
 * <p/>
 * The decoder is used for decoding from MLLP (bytes) to String. The String will
 * not contain any of the start and end markers.
 * <p/>
 * The encoder is used for encoding from String to MLLP (bytes). The String
 * should <b>not</b> contain any of the start and end markers, the encoder will
 * add these, and stream the string as bytes. Also the enocder will convert any
 * <tt>\n</tt> (line breaks) as segment terminators to <tt>\r</tt>.
 * <p/>
 * This codes supports charset encoding/decoding between bytes and String. The
 * JVM platform default charset is used, but the charset can be configued on
 * this codec using the setter method. The decoder will use the JVM platform
 * default charset for decoding, but the charset can be configued on the this
 * codec.
 */
public class HL7MLLPCodec
    implements ProtocolCodecFactory
{
    
    private final HL7MLLPConfig config = new HL7MLLPConfig();
    
    @Override
    public ProtocolDecoder getDecoder()
    {
        return new HL7MLLPDecoder(this.config);
    }
    
    @Override
    public ProtocolEncoder getEncoder()
    {
        return new HL7MLLPEncoder(this.config);
    }
    
    public void setCharset(Charset charset)
    {
        this.config.setCharset(charset);
    }
    
    public void setCharset(String charsetName)
    {
        this.config.setCharset(Charset.forName(charsetName));
    }
    
    public Charset getCharset()
    {
        return this.config.getCharset();
    }
    
    public boolean isConvertLFtoCR()
    {
        return this.config.isConvertLFtoCR();
    }
    
    public void setConvertLFtoCR(boolean convertLFtoCR)
    {
        this.config.setConvertLFtoCR(convertLFtoCR);
    }
    
    public char getStartByte()
    {
        return this.config.getStartByte();
    }
    
    public void setStartByte(char startByte)
    {
        this.config.setStartByte(startByte);
    }
    
    public char getEndByte1()
    {
        return this.config.getEndByte1();
    }
    
    public void setEndByte1(char endByte1)
    {
        this.config.setEndByte1(endByte1);
    }
    
    public char getEndByte2()
    {
        return this.config.getEndByte2();
    }
    
    public void setEndByte2(char endByte2)
    {
        this.config.setEndByte2(endByte2);
    }
    
    public boolean isValidate()
    {
        return this.config.isValidate();
    }
    
    public void setValidate(boolean validate)
    {
        this.config.setValidate(validate);
    }
    
}
