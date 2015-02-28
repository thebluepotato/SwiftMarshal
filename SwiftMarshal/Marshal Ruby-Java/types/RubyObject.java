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
import java.util.LinkedHashMap;
import java.util.Map;

public class RubyObject extends RubyType {
    
    private RubySymbol name;    // class/module name (not sure if it can be anything else and is always a symbol)
    private Map<RubySymbol,RubyType> variables;
    
    public RubyObject(RubySymbol name) {
        this.name = name;
        this.variables = new LinkedHashMap<RubySymbol,RubyType>();
    }
    
    public RubySymbol getName() {
        return this.name;
    }
    
    public RubyType get(RubySymbol name) {
        return this.variables.get(name);
    }
    
    public void put(RubySymbol name, RubyType value) {
        this.variables.put(name, value);
    }
    
    public int count() {
        return this.variables.size();
    }

    public Map<RubySymbol,RubyType> getVariables() {
        return variables;
    }

    @Override
    public void inspect(StringBuilder sb, int indent) {
        sb.append("Object:");
        sb.append(name.getName());
        sb.append("(");
        int j = 0;
        for (Map.Entry<RubySymbol,RubyType> entry : this.variables.entrySet()) {
            if (j == 0) { sb.append("\n"); }
            for (int i = 0; i < indent+1; i++) { sb.append(" "); }
            sb.append(entry.getKey());
            sb.append(" = ");
            sb.append(Ruby.toString(entry.getValue(), indent+1));
            sb.append("\n");
            j++;
        }
        for (int i = 0; i < indent; i++) { sb.append(" "); }
        sb.append(")");
    }
    
    
}
