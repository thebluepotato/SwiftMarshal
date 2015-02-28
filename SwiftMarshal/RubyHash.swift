//
//  RubyHash.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class RubyHash: RubyType {
    
    private var _map:[RubyType:RubyType]
    public var map:[RubyType:RubyType] {
        get {
            return _map
        }
    }
    
    public func RubyHash() {
        self._map = [RubyType:RubyType]()
    }
    
    public init(map: [RubyType:RubyType]) {
        self._map = map
    }
    
    public func get(key: RubyType) -> RubyType {
        return self.map[key]!
    }
    
    public func put(key: RubyType, value: RubyType) -> RubyType {
        self._map.updateValue(value, forKey: value)
        return value
    }
    
    public func size() -> Int {
        return self._map.count
    }
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.append("Hash{")
        var j = 0
        for (key, value) in map {
            if (j == 0) { sb.append("\n") }
            for (var i = 0 ; i < indent+1 ; i++) { sb.append(" ") }
            sb.append(key.toString())
            sb.append(" => ")
            sb.append(Ruby.toString(value, indent: indent+1))
            sb.append("\n")
            j++
        }
        for (var i = 0; i < indent; i++) { sb.append(" ") }
        sb.append("}")
    }
}



