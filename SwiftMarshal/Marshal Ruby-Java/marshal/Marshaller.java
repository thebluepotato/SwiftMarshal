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
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Map;

/**
 * Mostly learned from:
 * https://github.com/jruby/jruby/blob/master/src/org/jruby/runtime/marshal/MarshalStream.java
 * 
 * @author mfizz
 */
public class Marshaller {
    
    /**
    public void dumpObject(OutputStream os, Object value) throws IOException {
        writeAndRegister(value);

        depth--;
        if (depth == 0) {
            out.flush(); // flush afer whole dump is complete
        }
    }
    */
    
    /**
    private void writeAndRegister(IRubyObject value) throws IOException {
        if (cache.isRegistered(value)) {
            cache.writeLink(this, value);
        } else {
            value.getMetaClass().smartDump(this, value);
        }
    }
    */
    
    /**
    public void registerLinkTarget(IRubyObject newObject) {
        if (shouldBeRegistered(newObject)) {
            cache.register(newObject);
        }
    }

    public void registerSymbol(String sym) {
        cache.registerSymbol(sym);
    }

    static boolean shouldBeRegistered(IRubyObject value) {
        if (value.isNil()) {
            return false;
        } else if (value instanceof RubyBoolean) {
            return false;
        } else if (value instanceof RubyFixnum) {
            return ! isMarshalFixnum((RubyFixnum)value);
        }
        return true;
    }
    */

    /**
    private static boolean isMarshalFixnum(RubyFixnum fixnum) {
        return fixnum.getLongValue() <= RubyFixnum.MAX_MARSHAL_FIXNUM && fixnum.getLongValue() >= RubyFixnum.MIN_MARSHAL_FIXNUM;
    }
    */

    

    /**
    private List<Variable<Object>> getVariables(IRubyObject value) throws IOException {
        List<Variable<Object>> variables = null;
        if (value instanceof CoreObjectType) {
            int nativeTypeIndex = ((CoreObjectType)value).getNativeTypeIndex();
            
            if (nativeTypeIndex != ClassIndex.OBJECT && nativeTypeIndex != ClassIndex.BASICOBJECT) {
                if (shouldMarshalEncoding(value) || (
                        !value.isImmediate()
                        && value.hasVariables()
                        && nativeTypeIndex != ClassIndex.CLASS
                        && nativeTypeIndex != ClassIndex.MODULE
                        )) {
                    // object has instance vars and isn't a class, get a snapshot to be marshalled
                    // and output the ivar header here

                    variables = value.getVariableList();

                    // write `I' instance var signet if class is NOT a direct subclass of Object
                    write(TYPE_IVAR);
                }
                RubyClass type = value.getMetaClass();
                switch(nativeTypeIndex) {
                case ClassIndex.STRING:
                case ClassIndex.REGEXP:
                case ClassIndex.ARRAY:
                case ClassIndex.HASH:
                    type = dumpExtended(type);
                    break;
                }

                if (nativeTypeIndex != value.getMetaClass().index && nativeTypeIndex != ClassIndex.STRUCT) {
                    // object is a custom class that extended one of the native types other than Object
                    writeUserClass(value, type);
                }
            }
        }
        return variables;
    }
    */

    /**
    public static String getPathFromClass(RubyModule clazz) {
        String path = clazz.getName();
        
        if (path.charAt(0) == '#') {
            String classOrModule = clazz.isClass() ? "class" : "module";
            throw clazz.getRuntime().newTypeError("can't dump anonymous " + classOrModule + " " + path);
        }
        
        RubyModule real = clazz.isModule() ? clazz : ((RubyClass)clazz).getRealClass();

        if (clazz.getRuntime().getClassFromPath(path) != real) {
            throw clazz.getRuntime().newTypeError(path + " can't be referred");
        }
        return path;
    }
    */
    
    static public void marshalRubyType(OutputStream os, RubyType value) throws IOException {
        if (value == null) {
            marshalNil(os);
        } else if (value instanceof RubyBoolean) {
            marshalRubyBoolean(os, (RubyBoolean)value);
        } else if (value instanceof RubyBignum) {
            marshalRubyBignum(os, (RubyBignum)value);
        } else if (value instanceof RubyString) {
            marshalRubyString(os, (RubyString)value);
        } else if (value instanceof RubySymbol) {
            marshalRubySymbol(os, (RubySymbol)value);
        } else if (value instanceof RubyObject) {
            marshalRubyObject(os, (RubyObject)value);
        } else if (value instanceof RubyHash) {
            marshalRubyHash(os, (RubyHash)value);
        } else if (value instanceof RubyArray) {
            marshalRubyArray(os, (RubyArray)value);
        } else if (value instanceof RubyFixnum) {
            marshalRubyFixnum(os, (RubyFixnum)value);
        } else {
            throw new IOException("Unable or unsupported marshal for type " + value.getClass().getCanonicalName());
        }
        
        /**
        case ClassIndex.FIXNUM: {
            RubyFixnum fixnum = (RubyFixnum)value;

            if (isMarshalFixnum(fixnum)) {
                write('i');
                writeInt((int) fixnum.getLongValue());
                return;
            }
            // FIXME: inefficient; constructing a bignum just for dumping?
            value = RubyBignum.newBignum(value.getRuntime(), fixnum.getLongValue());

            // fall through
        }
        case ClassIndex.CLASS:
            if (((RubyClass)value).isSingleton()) throw runtime.newTypeError("singleton class can't be dumped");
            write('c');
            RubyClass.marshalTo((RubyClass)value, this);
            return;
        case ClassIndex.FLOAT:
            write('f');
            RubyFloat.marshalTo((RubyFloat)value, this);
            return;
        case ClassIndex.MODULE:
            write('m');
            RubyModule.marshalTo((RubyModule)value, this);
            return;
        case ClassIndex.REGEXP:
            write('/');
            RubyRegexp.marshalTo((RubyRegexp)value, this);
            return;
        case ClassIndex.STRUCT:
            RubyStruct.marshalTo((RubyStruct)value, this);
            return;


        default:
            throw runtime.newTypeError("can't dump " + value.getMetaClass().getName());
            }
        } else {
            dumpDefaultObjectHeader(value.getMetaClass());
            value.getMetaClass().getRealClass().marshal(value, this);
        }
        */
    }
    
    

    /**
    public void userNewMarshal(IRubyObject value, DynamicMethod method) throws IOException {
        userNewCommon(value, method);
    }

    public void userNewMarshal(IRubyObject value) throws IOException {
        userNewCommon(value, null);
    }

    private void userNewCommon(IRubyObject value, DynamicMethod method) throws IOException {
        registerLinkTarget(value);
        write(TYPE_USRMARSHAL);
        RubyClass metaclass = value.getMetaClass().getRealClass();
        writeAndRegisterSymbol(metaclass.getName());

        IRubyObject marshaled;
        if (method != null) {
            marshaled = method.call(runtime.getCurrentContext(), value, value.getMetaClass(), "marshal_dump");
        } else {
            marshaled = value.callMethod(runtime.getCurrentContext(), "marshal_dump");
        }
        dumpObject(marshaled);
    }

    public void userMarshal(IRubyObject value, DynamicMethod method) throws IOException {
        userCommon(value, method);
    }

    public void userMarshal(IRubyObject value) throws IOException {
        userCommon(value, null);
    }

    private void userCommon(IRubyObject value, DynamicMethod method) throws IOException {
        RubyFixnum depthLimitFixnum = runtime.newFixnum(depthLimit);

        IRubyObject dumpResult;
        if (method != null) {
            dumpResult = method.call(runtime.getCurrentContext(), value, value.getMetaClass(), "_dump", depthLimitFixnum);
        } else {
            dumpResult = value.callMethod(runtime.getCurrentContext(), "_dump", depthLimitFixnum);
        }
        
        if (!(dumpResult instanceof RubyString)) {
            throw runtime.newTypeError(dumpResult, runtime.getString());
        }
        RubyString marshaled = (RubyString)dumpResult;

        boolean hasVars;
        if (hasVars = marshaled.hasVariables()) {
            write(TYPE_IVAR);
        }

        write(TYPE_USERDEF);
        RubyClass metaclass = value.getMetaClass().getRealClass();

        writeAndRegisterSymbol(metaclass.getName());

        writeString(marshaled.getByteList());

        if (hasVars) {
            dumpVariables(marshaled.getVariableList());
        }

        registerLinkTarget(value);
    }
    
    public void writeUserClass(IRubyObject obj, RubyClass type) throws IOException {
        write(TYPE_UCLASS);
        
        // w_unique
        if (type.getName().charAt(0) == '#') {
            throw obj.getRuntime().newTypeError("can't dump anonymous class " + type.getName());
        }
        
        // w_symbol
        writeAndRegisterSymbol(type.getName());
    }
    
    public void dumpVariablesWithEncoding(List<Variable<Object>> vars, IRubyObject obj) throws IOException {
        if (shouldMarshalEncoding(obj)) {
            writeInt(vars.size() + 1); // vars preceded by encoding
            writeEncoding(((EncodingCapable)obj).getEncoding());
        } else {
            writeInt(vars.size());
        }
        
        dumpVariablesShared(vars);
    }
    */
    
    /**
    static public void writeEncoding(Encoding encoding) throws IOException {
        if (encoding == null || encoding == USASCIIEncoding.INSTANCE) {
            writeAndRegisterSymbol(SYMBOL_ENCODING_SPECIAL);
            writeObjectData(runtime.getFalse());
        } else if (encoding == UTF8Encoding.INSTANCE) {
            writeAndRegisterSymbol(SYMBOL_ENCODING_SPECIAL);
            writeObjectData(runtime.getTrue());
        } else {
            writeAndRegisterSymbol(SYMBOL_ENCODING);
            byte[] name = encoding.getName();
            write('"');
            writeString(new ByteList(name, false));
        }
    }
    */

    /** 
     * w_extended
     */
    /**
    private RubyClass dumpExtended(RubyClass type) throws IOException {
        if(type.isSingleton()) {
            if (hasSingletonMethods(type) || type.hasVariables()) { // any ivars, since we don't have __attached__ ivar now
                throw type.getRuntime().newTypeError("singleton can't be dumped");
            }
            type = type.getSuperClass();
        }
        while(type.isIncluded()) {
            write('e');
            writeAndRegisterSymbol(((IncludedModuleWrapper)type).getNonIncludedClass().getName());
            type = type.getSuperClass();
        }
        return type;
    }
    */

    /**
    static public void dumpDefaultObjectHeader(OutputStream os, RubyObject value) throws IOException {
        //dumpDefaultObjectHeader(os, 'o', type.getClassName());
        write(os, 'o');
        // name/symbol of object comes next
        writeSymbol(os, value.getName());
    }
    
    public void writeDirectly(OutputStream os, RubyObject value) throws IOException {
        writeObjectData(os, value);
        if (variables != null) {
            //if (runtime.is1_9()) {
            //    dumpVariablesWithEncoding(variables, value);
            //} else {
                dumpVariables(variables);
            //}
        }
    }
    */
    
    public static void marshalRubyFixnum(OutputStream os, RubyFixnum fixnum) throws IOException {
        writeByte(os, 'i');
        writeInt(os, (int)fixnum.getValue());
        // FIXME: some question on long vs. int here...
    }
    
    public static void marshalNil(OutputStream os) throws IOException {
        writeByte(os, '0');
    }
    
    public static void marshalRubyBoolean(OutputStream os, RubyBoolean bool) throws IOException {
        if (bool.getValue()) {
            writeByte(os, 'T');
        } else {
            writeByte(os, 'F');
        }
    }
   
    public static void marshalRubyBignum(OutputStream os, RubyBignum bignum) throws IOException {
        writeByte(os, 'l');
        writeBigInteger(os, bignum.getValue());
    }
    
    public static void marshalRubyString(OutputStream os, RubyString str) throws IOException {
        writeByte(os, '"');
        writeRubyString(os, str); // just writes the length and bytes
    }
    
    public static void marshalRubyArray(OutputStream os, RubyArray array) throws IOException {
        writeByte(os, '[');
        writeInt(os, array.getArray().size());
        for (RubyType o : array.getArray()) {
            marshalRubyType(os, o);
        }
    }

    public static void marshalRubyHash(OutputStream os, RubyHash hash) throws IOException {
        writeByte(os, '{');
        writeInt(os, hash.getMap().size());
        for (Map.Entry<RubyType,RubyType> entry : hash.getMap().entrySet()) {
            marshalRubyType(os, entry.getKey());
            marshalRubyType(os, entry.getValue());
        }

        // FIXME: ??
        //if (!hash.ifNone.isNil()) output.dumpObject(hash.ifNone);
    }
    
    static public void marshalRubySymbol(OutputStream os, RubySymbol value) throws IOException {
        writeByte(os, ':');
        writeJavaStringAsUTF8Bytes(os, value.getName());
    }
    
    static public void marshalRubyObject(OutputStream os, RubyObject value) throws IOException {
        writeByte(os, 'o');
        // name/symbol of object comes next
        marshalRubySymbol(os, value.getName());
        // variables always comes next
        writeVariables(os, value.getVariables());
    }
    
    static public void writeVariables(OutputStream os, Map<RubySymbol,RubyType> vars) throws IOException {
        writeInt(os, vars.size());
        writeVariablesShared(os, vars);
    }
    
    static private void writeVariablesShared(OutputStream os, Map<RubySymbol,RubyType> vars) throws IOException {
        for (Map.Entry<RubySymbol,RubyType> var : vars.entrySet()) {
            marshalRubySymbol(os, var.getKey());
            marshalRubyType(os, var.getValue());
        }
    }

    static public void writeJavaStringAsUTF8Bytes(OutputStream os, String value) throws IOException {
        byte[] bytes = value.getBytes("UTF-8");
        writeInt(os, bytes.length);
        os.write(bytes);
    }
    
    static public void writeRubyString(OutputStream os, RubyString value) throws IOException {
        writeInt(os, value.getBytes().length);
        os.write(value.getBytes());
    }

    /** FIXME: this seems like its wrong and actually should support up to 8 bytes, not 4 */
    static public void writeInt(OutputStream os, int value) throws IOException {
        if (value == 0) {
            os.write(0);
        } else if (0 < value && value < 123) {
            os.write(value + 5);
        } else if (-124 < value && value < 0) {
            os.write((value - 5) & 0xff);
        } else {
            byte[] buf = new byte[4];
            int i = 0;
            for (; i < buf.length; i++) {
                buf[i] = (byte)(value & 0xff);
                
                value = value >> 8;
                if (value == 0 || value == -1) {
                    break;
                }
            }
            int len = i + 1;
            os.write(value < 0 ? -len : len);
            os.write(buf, 0, i + 1);
        }
    }
    
    public static void writeBigInteger(OutputStream os, BigInteger bigint) throws IOException {
        os.write(bigint.signum() >= 0 ? '+' : '-');
        
        BigInteger absValue = bigint.abs();
        
        byte[] digits = absValue.toByteArray();
        
        boolean oddLengthNonzeroStart = (digits.length % 2 != 0 && digits[0] != 0);
        int shortLength = digits.length / 2;
        if (oddLengthNonzeroStart) {
            shortLength++;
        }
        writeInt(os, shortLength);
        
        for (int i = 1; i <= shortLength * 2 && i <= digits.length; i++) {
            writeByte(os, digits[digits.length - i]);
        }
        
        if (oddLengthNonzeroStart) {
            // Pad with a 0
            writeByte(os, 0);
        }
    }

    static public void writeByte(OutputStream os, int value) throws IOException {
        os.write(value);
    }
    
}
