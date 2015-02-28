//
//  RubyType.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//
//

public class RubyType: NSObject {//Hashable, Equatable {
    
    public func toString() -> NSMutableString {
        var sb = NSMutableString()
        inspect(&sb, indent: 0)
        return sb
    }
    
    /*public var hashValue: Int {
    return toString().hashValue
    }*/
    
    public func inspect(inout sb: NSMutableString, indent: Int) {
    }
}
