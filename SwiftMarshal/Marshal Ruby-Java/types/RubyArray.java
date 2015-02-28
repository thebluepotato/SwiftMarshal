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

import com.mfizz.ruby.core.Ruby;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mfizz
 */
public class RubyArray extends RubyType {
    
    private List<RubyType> array;
    
    public RubyArray() {
        this.array = new ArrayList<RubyType>();
    }

    public RubyArray(List<RubyType> array) {
        this.array = array;
    }
    
    public RubyArray(RubyType[] array) {
        this.array = Arrays.asList(array);
    }
    
    public void add(RubyType element) {
        this.array.add(element);
    }
    
    public RubyType get(int index) {
        return this.array.get(index);
    }

    public int size() {
        return this.array.size();
    }
    
    public List<? extends RubyType> getArray() {
        return array;
    }

    @Override
    public void inspect(StringBuilder sb, int indent) {
        sb.append("Array[");
        int j = 0;
        for (RubyType o : array) {
            if (j == 0) { sb.append("\n"); }
            for (int i = 0; i < indent+1; i++) { sb.append(" "); }
            if (j != 0) { sb.append(", "); }
            sb.append(Ruby.toString(o, indent+1));
            sb.append("\n");
            j++;
        }
        for (int i = 0; i < indent; i++) { sb.append(" "); }
        sb.append("]");
    }
}
