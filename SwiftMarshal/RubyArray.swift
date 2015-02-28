//
//  RubyArray.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class RubyArray: RubyType {
    
    private var array:Array<RubyType>
    
    public override init() {
        self.array = [RubyType]()
    }
    
    public init(array: Array<RubyType>) {
        self.array = array;
    }
    
    /*public init(RubyType[] array) {
        this.array = Arrays.asList(array);
    }*/
    
    public func add(element: RubyType) {
        self.array.append(element);
    }
    
    public func get(index: Int) -> RubyType {
        return self.array[index]
    }
    
    public func size() -> Int {
        return self.array.count
    }
    
    public func getArray() -> [RubyType] {
        return array
    }
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.append("Array[");
        var j = 0;
        for o in array {
            if (j == 0) { sb.append("\n"); }
            for (var i = 0; i < indent+1; i++) { sb.append(" "); }
            if (j != 0) { sb.append(", "); }
            sb.append(Ruby.toString(o, indent: indent+1));
            sb.append("\n");
            j++;
        }
        for (var i = 0; i < indent; i++) { sb.append(" "); }
        sb.append("]");
    }
}
