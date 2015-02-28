/*
 * Copyright 2012 mfizz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mfizz.ruby.marshal;

/*
 * #%L
 * mfz-ruby-marshal
 * %%
 * Copyright (C) 2012 mfizz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.mfizz.ruby.types.RubyArray;
import com.mfizz.ruby.types.RubyBignum;
import com.mfizz.ruby.types.RubyBoolean;
import com.mfizz.ruby.types.RubyFixnum;
import com.mfizz.ruby.types.RubyHash;
import com.mfizz.ruby.types.RubyObject;
import com.mfizz.ruby.types.RubyString;
import com.mfizz.ruby.types.RubySymbol;
import com.mfizz.ruby.types.RubyType;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for unmarshalling bytes from Ruby serialized objects into 
 * a graph of useful Java objects.
 * 
 * The protocol was derived by trial and error as well as from the JRuby
 * project. 
 * 
 * https://github.com/jruby/jruby/blob/master/src/org/jruby/runtime/marshal/UnmarshalStream.java
 */
public class Unmarshaller {
    private static final Logger logger = LoggerFactory.getLogger(Unmarshaller.class);
    
    static public RubyType unmarshalRubyType(InputStream is) throws IOException {
        int type = readUnsignedByte(is);
        //logger.info("read type: " + (char)type + " (0x" + HexUtil.toHexString((byte)type) + ")");
        
        //Object result = null;
        
        // don't handle link types...
        //IRubyObject result = null;
        //if (cache.isLinkType(type)) {
        //    result = cache.readLink(this, type);
        //    if (callProc && runtime.is1_9()) return doCallProcForLink(result, type);
        //} else {
            RubyType result = unmarshalRubyTypeDirectly(type, is);
        //}

        //result.setTaint(taint);
        //result.setUntrusted(untrust);
            
        //logger.info("class: " + (result != null ? result.getClass() : "nil") + " value: " + result);

        return result;
    }
    
    
    static public RubyType unmarshalRubyTypeDirectly(int type, InputStream is) throws IOException {
        switch (type) {
            //case 'I':
                /**
                MarshalState childState = new MarshalState(true);
                rubyObj = unmarshalObject(childState);
                if (childState.isIvarWaiting()) {
                    defaultVariablesUnmarshal(rubyObj);
                }
                return rubyObj;
                */
            case '0' :
                return null;
            case 'T' :
                return new RubyBoolean(true);
            case 'F' :
                return new RubyBoolean(false);
            case '"' :
                return new RubyString(readRubyStringAsBytes(is));
            case 'i' :
                // this is just an integer, but of a specific type (so that it can be marshalled again)
                return new RubyFixnum(readRubyInt(is));
            //case 'f' :
            //    rubyObj = RubyFloat.unmarshalFrom(this);
            //    break;
            //case '/' :
            //    rubyObj = RubyRegexp.unmarshalFrom(this);
            //    break;
            case ':' :
                // a symbol is actually just a string in java
                return new RubySymbol(readRubyStringAsUTF8String(is));
            case '[' :
                // an array of objects
                return unmarshalRubyArray(is);
            case '{' :
                 // a hash of objects
                return unmarshalRubyHash(is);
            //case '}' :
                // "hashdef" object, a hash with a default???
            //    rubyObj = RubyHash.unmarshalFrom(this, true);
            //    break;
            //case 'c' :
            //    rubyObj = RubyClass.unmarshalFrom(this);
            //    break;
            //case 'm' :
            //    rubyObj = RubyModule.unmarshalFrom(this);
            //    break;
            /**
            case 'e':
                RubySymbol moduleName = (RubySymbol) unmarshalObject();
                RubyModule tp = null;
                try {
                    tp = runtime.getClassFromPath(moduleName.asJavaString());
                } catch (RaiseException e) {
                    if (runtime.getModule("NameError").isInstance(e.getException())) {
                        throw runtime.newArgumentError("undefined class/module " + moduleName.asJavaString());
                    } 
                    throw e;
                }

                rubyObj = unmarshalObject();

                tp.extend_object(rubyObj);
                tp.callMethod(runtime.getCurrentContext(),"extended", rubyObj);
                break;
                */
            case 'l' :
                return new RubyBignum(readBigInteger(is));
            //case 'S' :
            //    rubyObj = RubyStruct.unmarshalFrom(this);
            //    break;
            case 'o' :
                // will return a RubyObject
                return unmarshalRubyObject(is);
            //case 'u' :
            //    rubyObj = userUnmarshal(state);
            //    break;
            //case 'U' :
            //    rubyObj = userNewUnmarshal();
            //    break;
            //case 'C' :
            //    rubyObj = uclassUnmarshall();
            //    break;
            default :
                throw new UnsupportedOperationException("Unable to handle type value: " + type + " or known as [" + (char)type + "]");
        }

        /**
        if (runtime.is1_9()) {
            if (callProc) {
                return doCallProcForObj(rubyObj);
            }
        } else if (type != ':') {
            // call the proc, but not for symbols
            doCallProcForObj(rubyObj);
        }
        
        return rubyObj;
        */
    }
    

    static public int readUnsignedByte(InputStream is) throws IOException {
        int result = is.read();
        if (result == -1) {
            throw new EOFException("Unexpected end of stream");
        }
        return result;
    }

    static public byte readSignedByte(InputStream is) throws IOException {
        int b = readUnsignedByte(is);
        if (b > 127) {
            return (byte) (b - 256);
        }
        return (byte)b;
    }

    static public String readRubyStringAsUTF8String(InputStream is) throws IOException {
        byte[] buffer = readRubyStringAsBytes(is);
        // supposedly strings in ruby are considered to be raw/ISO-8859-1? (seem's potentially wrong!)
        // seems like we're getting bad strings w/ ISO-8859-1 -- trying UTF-8
        return new String(buffer, "UTF-8");
    }
    
    static public byte[] readRubyStringAsBytes(InputStream is) throws IOException {
        int length = readRubyIntAsJavaInt(is);
        byte[] buffer = new byte[length];

        int readLength = 0;
        while (readLength < length) {
            int read = is.read(buffer, readLength, length - readLength);

            if (read == -1) {
                throw new IOException("marshal data too short");
            }
            readLength += read;
        }

        return buffer;
    }

    static public int readRubyIntAsJavaInt(InputStream is) throws IOException {
        return (int)readRubyInt(is);
    }
    
    static public long readRubyInt(InputStream is) throws IOException {
        int c = readSignedByte(is);
        if (c == 0) {
            return 0;
        } else if (5 < c && c < 128) {
            return c - 5;
        } else if (-129 < c && c < -5) {
            return c + 5;
        }
        long result;
        if (c > 0) {
            result = 0;
            for (int i = 0; i < c; i++) {
                result |= (long) readUnsignedByte(is) << (8 * i);
            }
        } else {
            c = -c;
            result = -1;
            for (int i = 0; i < c; i++) {
                result &= ~((long) 0xff << (8 * i));
                result |= (long) readUnsignedByte(is) << (8 * i);
            }
        }
        return result;
    }
    
    static public BigInteger readBigInteger(InputStream is) throws IOException {
        // what a convoluted way to serialize a big integer (gotta love ruby)
        boolean positive = readUnsignedByte(is) == '+';
        int shortLength = readRubyIntAsJavaInt(is);

        // BigInteger required a sign byte in incoming array
        byte[] digits = new byte[shortLength * 2 + 1];

        for (int i = digits.length - 1; i >= 1; i--) {
            digits[i] = readSignedByte(is);
        }

        BigInteger value = new BigInteger(digits);
        if (!positive) {
            value = value.negate();
        }

        return value;
    }

    static public RubyArray unmarshalRubyArray(InputStream is) throws IOException {
        int size = readRubyIntAsJavaInt(is);
        
        RubyType[] array = new RubyType[size];

        for (int i = 0; i < size; i++) {
            array[i] = unmarshalRubyType(is);
        }

        return new RubyArray(array);
    }
    
    static public RubyHash unmarshalRubyHash(InputStream is) throws IOException {
        int size = readRubyIntAsJavaInt(is);
        
        Map<RubyType,RubyType> hash = new LinkedHashMap<RubyType,RubyType>();
        //logger.info("size of hash: {}", size);
        
        for (int i = 0; i < size; i++) {
            //result.fastASetCheckString(input.getRuntime(), input.unmarshalObject(), input.unmarshalObject());
            RubyType key = unmarshalRubyType(is);
            //logger.info("parsed key: {}", key);
            RubyType value = unmarshalRubyType(is);
            //logger.info("parsed value: {}", value);
            
            hash.put(key, value);
        }
        
        return new RubyHash(hash);
    }
    
    static public RubyObject unmarshalRubyObject(InputStream is) throws IOException {
        Object objectName = unmarshalRubyType(is);
        
        // name of object should be a symbol? (not totally sure)
        if (!(objectName instanceof RubySymbol)) {
            throw new IOException("Expected symbol for object name but was something else intead");
        }
        
        RubyObject ro = new RubyObject((RubySymbol)objectName);
        
        // unmarshal its variables!
        unmarshalRubyObjectVariables(ro, is);
        
        return ro;
    }

    
    static public void unmarshalRubyObjectVariables(RubyObject ro, InputStream is) throws IOException {
        int count = readRubyIntAsJavaInt(is);
        //logger.info("going to parse {} variables in object", count);
        
        //RubyClass cls = object.getMetaClass().getRealClass();
        for (int i = count; --i >= 0; ) {
            // name of variable (should always be a symbol)
            Object name = unmarshalRubyType(is);
            
            if (!(name instanceof RubySymbol)) {
                throw new IOException("Expected only symbols for names of variables");
            }
            
            if (i == 0) { // first ivar
                /**
                if (runtime.is1_9()
                        && (object instanceof RubyString || object instanceof RubyRegexp)
                        && count >= 1) { // 1.9 string encoding
                    EncodingCapable strObj = (EncodingCapable)object;

                    if (key.asJavaString().equals(MarshalStream.SYMBOL_ENCODING_SPECIAL)) {
                        // special case for USASCII and UTF8
                        if (unmarshalObject().isTrue()) {
                            strObj.setEncoding(UTF8Encoding.INSTANCE);
                        } else {
                            strObj.setEncoding(USASCIIEncoding.INSTANCE);
                        }
                        continue;
                    } else if (key.asJavaString().equals("encoding")) {
                        // read off " byte for the string
                        read();
                        ByteList encodingName = unmarshalString();
                        Entry entry = runtime.getEncodingService().findEncodingOrAliasEntry(encodingName);
                        if (entry == null) {
                            throw runtime.newArgumentError("invalid encoding in marshaling stream: " + encodingName);
                        }
                        Encoding encoding = entry.getEncoding();
                        strObj.setEncoding(encoding);
                        continue;
                    } // else fall through as normal ivar
                }
                */
            }
            
            // value of variable
            RubyType value = unmarshalRubyType(is);
            
            ro.put((RubySymbol)name, value);

            //logger.info("variable name: {} with value: {}", name, value);
            //cls.getVariableAccessorForWrite(name).set(object, value);
        }
    }
    
    /**
    private IRubyObject uclassUnmarshall() throws IOException {
        RubySymbol className = (RubySymbol)unmarshalObject(false);

        RubyClass type = (RubyClass)runtime.getClassFromPath(className.asJavaString());

        // singleton, raise error
        if (type.isSingleton()) throw runtime.newTypeError("singleton can't be loaded");

        // All "C" marshalled objects descend from core classes, which are all at least RubyObject
        RubyObject result = (RubyObject)unmarshalObject();

        // if result is a module or type doesn't extend result's class...
        if (result.getMetaClass() == runtime.getModule() || !type.isKindOfModule(result.getMetaClass())) {
            // if allocators do not match, error
            // Note: MRI is a bit different here, and tests TYPE(type.allocate()) != TYPE(result)
            if (type.getAllocator() != result.getMetaClass().getRealClass().getAllocator()) {
                throw runtime.newArgumentError("dump format error (user class)");
            }
        }

        result.setMetaClass(type);

        return result;
    }
    */
    
}
