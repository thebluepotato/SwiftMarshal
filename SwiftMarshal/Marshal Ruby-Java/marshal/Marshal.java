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

import com.mfizz.ruby.types.RubyType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author mfizz
 */
public class Marshal {
    
    static public byte[] dump(RubyType obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        dump(baos, obj);
        
        return baos.toByteArray();
    }
    
    static public void dump(OutputStream os, RubyType obj) throws IOException {
        // write major and minor version
        Marshaller.writeByte(os, 4);
        Marshaller.writeByte(os, 8);
        
        Marshaller.marshalRubyType(os, obj);
        
        // good practice to flush it
        os.flush();
    }
    
    static public RubyType load(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        
        // read major and minor version
        int majorVersion = Unmarshaller.readUnsignedByte(bais);
        int minorVersion = Unmarshaller.readUnsignedByte(bais);
        
        if (majorVersion != 4) {
            throw new IOException("Unable to support Ruby Marshal major version: " + majorVersion);
        }
        
        if (minorVersion != 8) {
            throw new IOException("Unable to support Ruby Marshal minor version: " + minorVersion);
        }
        
        return Unmarshaller.unmarshalRubyType(bais);
    }
    
}
