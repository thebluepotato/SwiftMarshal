//
//  Ruby.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class Ruby {
    
    class public func toString(obj:RubyType?, indent:Int = 0) -> String{
        var sb = NSMutableString()
        appendToString(sb, obj: obj, indent: indent)
        return sb as String
    }
    
    class public func appendToString(var sb:NSMutableString, obj:RubyType?, indent:Int) {
        if let obj = obj {
            obj.inspect(&sb, indent: indent)
        } else {
            sb.append("nil")
        }
    }
}

extension NSMutableString {
    func append(string: String) {
        return self.appendString(string)
    }
}

/*public func ==(lhs: RubyType, rhs: RubyType) -> Bool {
return lhs.hashValue == rhs.hashValue
}*/


/*@objc protocol RubyInspect {
optional func inspect(sb: NSMutableString, indentt: NSNumber)
}*/
