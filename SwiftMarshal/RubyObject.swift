//
//  RubyObject.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class RubyObject: RubyType {
    
    var name:RubySymbol    // class/module name (not sure if it can be anything else and is always a symbol)
    private var variables:Dictionary<RubySymbol,RubyType?>
    public var count:Int {
        get {
            return variables.count
        }
    }
    
    public init(name:RubySymbol) {
        self.name = name;
        self.variables = Dictionary<RubySymbol,RubyType?>()
    }
    
    public func getName() -> RubySymbol {
        return self.name;
    }
    
    public func get(name: RubySymbol) -> RubyType? {
        return self.variables[name]!
    }
    
    public func put(name: RubySymbol, value: RubyType?) {
        self.variables.updateValue(value, forKey: name)
    }
    
    public func getVariables() -> Dictionary<RubySymbol,RubyType?> {
        return variables
    }
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.appendString("Object:");
        sb.appendString(name.name);
        sb.appendString("(");
        var j = 0;
        for (key, value) in self.variables {
            if (j == 0) { sb.appendString("\n"); }
            for (var i = 0; i < indent+1; i++) { sb.appendString(" "); }
            sb.appendString(key.toString() as String);
            sb.appendString(" = ");
            sb.appendString(Ruby.toString(value, indent: indent+1));
            sb.appendString("\n");
            j++;
        }
        for (var i = 0; i < indent; i++) { sb.appendString(" "); }
        sb.appendString(")");
    }
    
    public override var description:String {
        get {
            var str = "\(name.name) {"
            var j = 1
            for (key, value) in self.variables {
                //for (var i = 0; i < indent+1; i++) { sb.appendString(" "); }
                if let value = value {
                    str += "\(key) = \(value)"
                } else {
                    str += "\(key) = nil"
                }
                if j < count {str += "\n"}
                j++
            }
            //for (var i = 0; i < indent; i++) { sb.appendString(" ") }
            str += "}"
            return str
        }
    }
}
