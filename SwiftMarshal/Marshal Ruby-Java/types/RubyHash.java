/*
 * Copyright 2012 mfizz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package com.mfizz.ruby.types

/*
 * #%L
 * mfz-ruby-marshal
 * %%
 * Copyright (C) 2012 mfizz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import com.mfizz.ruby.core.Ruby
import java.util.LinkedHashMap
import java.util.Map

/**
 *
 * @author mfizz
 */
public class RubyHash extends RubyType {
    
    private Map<RubyType,RubyType> map
    
    public RubyHash() {
        this.map = new LinkedHashMap<RubyType,RubyType>()
    }

    public RubyHash(Map<RubyType,RubyType> map) {
        this.map = map
    }
    
    public RubyType get(RubyType key) {
        return this.map.get(key)
    }

    public RubyType put(RubyType key, RubyType value) {
        return this.map.put(key, value)
    }
    
    public int size() {
        return this.map.size()
    }
    
    public Map<RubyType, RubyType> getMap() {
        return map
    }

    @Override
    public void inspect(String sb, int indent) {
        sb.append("Hash{")
        int j = 0
        for (Map.Entry<RubyType,RubyType> entry : map.entrySet()) {
            if (j == 0) { sb.append("\n") }
            for (int i = 0 i < indent+1 i++) { sb.append(" ") }
            sb.append(entry.getKey())
            sb.append(" => ")
            sb.append(Ruby.toString(entry.getValue(), indent+1))
            sb.append("\n")
            j++
        }
        for (int i = 0 i < indent i++) { sb.append(" ") }
        sb.append("}")
    }
}
