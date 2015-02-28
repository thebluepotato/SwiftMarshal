//
//  RubyBool.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class RubyBoolean: RubyType {
    
    private final var _value:Bool
    var value:Bool {
        get {
            return _value
        }
    }
    
    public init(value:Bool) {
        self._value = value
    }
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.append(value ? "true" : "false")
    }
}
