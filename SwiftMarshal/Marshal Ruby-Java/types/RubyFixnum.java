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
package com.mfizz.ruby.types;

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

/**
 * http://www.ruby-doc.org/docs/ProgrammingRuby/html/tut_stdtypes.html
 * 
 * Ruby supports integers and floating point numbers. Integers can be any length
 * (up to a maximum determined by the amount of free memory on your system).
 * Integers within a certain range (normally -230 to 230-1 or -262 to 262-1) are
 * held internally in binary form, and are objects of class Fixnum. Integers
 * outside this range are stored in objects of class Bignum (currently implemented
 * as a variable-length set of short integers). This process is transparent,
 * and Ruby automatically manages the conversion back and forth.
 * 
 * Fixnum 8
 * Fixnum 64
 * Fixnum 4096
 * Fixnum 16777216
 * Fixnum 4294967296 (2^32, or unsigned int which should be a long in Java)
 * Fixnum 1152921504606846976 (2^60)
 * Fixnum 2305843009213693952 (2^61)
 * Fixnum 4611686018427387903 (2^62 - 1) (max Fixnum in Ruby 1.8)
 * Bignum 4611686018427387904 (2^62)
 * 
 * @author mfizz
 */
public class RubyFixnum extends RubyType {
    
    private final long value;

    public RubyFixnum(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public void inspect(StringBuilder sb, int indent) {
        sb.append(value);
    }
    
}