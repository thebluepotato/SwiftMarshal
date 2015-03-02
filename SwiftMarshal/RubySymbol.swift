//
//  RubySymbol.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class RubySymbol : RubyType {//, Comparable {
    
    // name of symbol w/o the leading ":" char
    private final var _name:String
    final var name:String {
        get {
            return _name
        }
    }
    
    /**
    * Creates a new symbol.  If in Ruby you'd type ":name", you just use the
    * "name" part here and don't include the leading ':' char.
    * @param name
    */
    public init(name:String) {
        self._name = name;
    }
    
    public func hashCode() -> Int {
        // hash code of a symobl should NOT match a string
        return ":".hashValue + self.name.hashValue
    }
    
    /*public func equals(obj: AnyObject?) -> Bool {
    if (obj == nil) {
    return false;
    }
    
    if (getClass() != obj.getClass()) {
    return false;
    }
    
    let other:RubySymbol = obj as RubySymbol
    if ((self.name == nil) ? (other.name != nil) : !self.name.equals(other.name)) {
    return false;
    }
    return true;
    }
    
    public func compareTo(o: RubySymbol) -> Int {
    return self.name.compareTo(o.name);
    }*/
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.appendString(":");
        sb.appendString(name);
    }
    
    public override var description:String {
        get {
            return "\(name)"
        }
    }
}