//
//  Marshal.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

public class Marshal {
    
    /*class public func dump(obj:RubyType) -> NSdata {
    let output = NSOutputStream()
    
    dump(baos, obj);
    
    return baos.toByteArray();
    }
    
    static public void dump(OutputStream os, RubyType obj) throws IOException {
    // write major and minor version
    Marshaller.writeByte(os, 4);
    Marshaller.writeByte(os, 8);
    
    Marshaller.marshalRubyType(os, obj);
    
    // good practice to flush it
    os.flush();
    }*/
    
    class public func load(data: NSData) -> RubyType {
        let input = NSInputStream(data: data)
        let unmarsh = Unmarshaller(input: input)
        
        // read major and minor version
        let majorVersion = unmarsh.input.readUnsignedByte()
        let minorVersion = unmarsh.input.readUnsignedByte()
        
        if (majorVersion != 4) {
            println("Unable to support Ruby Marshal major version: \(majorVersion)");
        }
        
        if (minorVersion != 8) {
            println("Unable to support Ruby Marshal minor version: \(minorVersion)");
        }
        
        return unmarsh.input.unmarshalRubyType()!
    }
}
