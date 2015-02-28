//
//  RubyString.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class RubyString: RubyType {
    
    final private var _str:String
    final var str:String {
        get {
            return _str
        }
    }
    
    public init(str: String) {
        self._str = str
    }
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.append("\"")
        sb.append(str)
        sb.append("\"")
    }
    
    /*public func hashCode() -> Int {
    var hash = 3
    hash = 53 * hash + Arrays.hashCode(self.bytes)
    }*/
    
    public override func isEqual(object: AnyObject?) -> Bool {
        if let object:AnyObject = object {
            if object.isKindOfClass(RubyString) {
                return (object as RubyString) == self
            } else {
                return false
            }
        } else {
            return false
        }
    }
    
    /*@Override
    public boolean equals(Object obj) {
    if (obj == null) {
    return false;
    }
    if (getClass() != obj.getClass()) {
    return false;
    }
    final RubyString other = (RubyString) obj;
    if (!Arrays.equals(this.bytes, other.bytes)) {
    return false;
    }
    return true;
    }*/
    
}

func ==(lhs: RubyString, rhs: RubyString) -> Bool {
    return lhs.str == rhs.str
}