//
//  Unmarshaller.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

var symcache = [RubySymbol]() //class variables not yet supported

class Unmarshaller {
    private var _input:NSInputStream
    var input:NSInputStream {
        get {
            return _input
        }
    }
    
    init(input: NSInputStream) {
        self._input = input
        _input.open()
    }
}

extension NSInputStream {
    //var symcache = [RubySymbol]()
    
    func readSignedByte() -> Int8 {
        var buffer = [UInt8](count: 1, repeatedValue: 0)
        self.read(&buffer, maxLength: 1)
        //Do real conversion
        return Int8(buffer.first!)
    }
    
    func readUnsignedByte() -> UInt8 {
        var buffer = [UInt8](count: 1, repeatedValue: 0)
        self.read(&buffer, maxLength: 1)
        return buffer.first!
    }
    
    func readInt() -> Int? {
        let c = Int(readSignedByte())
        if (c == 0) {
            return 0
        } else if (5 < c && c < 128) {
            return c - 5
        } else if (-129 < c && c < -5) {
            return c + 5
        } else if (1 <= c && c <= 5) { //indicates longer int
            var result:Int = 0
            if (c > 0) {
                result = 0
                for (var i = 0; i < c; i++) { //BigEndian
                    let byte = Int(readUnsignedByte())
                    result += byte * Int(pow(256, Double(i)))
                }
            } /*else {
            c = -c
            result = -1
            for (int i = 0; i < c; i++) {
            result &= ~((long) 0xff << (8 * i))
            result |= (long) readUnsignedByte(is) << (8 * i)
            }
            }*/
            return Int(result)
        }
        
        return nil
    }
    
    func readType() -> String {
        return toASCII([readSignedByte()])
    }
    
    func toASCII(var arr: [Int8]) -> String {
        let ptr = arr.withUnsafeBufferPointer{$0.baseAddress}
        return NSString(bytes: ptr, length: arr.count, encoding: NSASCIIStringEncoding)!
    }
    
    func toUTF8(var arr: [Int8]) -> String {
        let ptr = arr.withUnsafeBufferPointer{$0.baseAddress}
        return NSString(bytes: ptr, length: arr.count, encoding: NSUTF8StringEncoding)!
    }
    
    func readStringAsBytes() -> [UInt8] {
        let length = readInt()!
        var buffer = [UInt8](count: length, repeatedValue: 0)
        var readLength = 0
        while (readLength < length) {
            let readNow = read(&buffer, maxLength: length - readLength)
            
            if (readNow == -1) {
                NSLog("marshal data too short")
            }
            readLength += readNow
        }
        
        return buffer
    }
    
    func readASCIIString() -> String {
        let bytes = readStringAsBytes().map{Int8($0)}
        return toASCII(bytes)
    }
    
    func readUTF8String() -> String {
        let bytes = readStringAsBytes().map{Int8($0)}
        return toUTF8(bytes)
    }
    
    func unmarshalRubyType() -> RubyType? {
        let type = readType()
        switch type {
        case "I":
            return RubyString(str: readIVar())
        case "\"":
            return RubyString(str: readASCIIString())
        case "0":
            return nil
        case "T":
            return RubyBoolean(value: true)
        case "F":
            return RubyBoolean(value: false)
        case "i":
            return RubyFixnum(value: readInt()!)
        case "{":
            return RubyHash(map: unmarshalRubyHash())
        case ":":
            let sym = RubySymbol(name: readUTF8String())
            symcache.append(sym)
            return sym
        case ";":
            return symcache[readInt()!]
        case "o":
            return unmarshalRubyObject()
        default:
            return nil
        }
    }
    
    func readIVar() -> String { // we standardize it here
        if readType() == "\"" {
            let bytes = readStringAsBytes().map{Int8($0)}
            if readInt()! == 1 { // length of params, should be one for encoding only
                if (unmarshalRubyType() as RubySymbol).name == "E" {
                    if let bool = unmarshalRubyType() as? RubyBoolean {
                        return bool.value ? toUTF8(bytes) : toASCII(bytes)
                    }
                }
            }
        }
        println("Can't read IVar")
        return ""
    }
    
    func unmarshalRubyHash() -> [RubyType:RubyType] {
        var size = readInt()
        var hash = [RubyType:RubyType]()
        //Dictionary<RubyType,RubyType> hash = LinkedHashMap<RubyType,RubyType>()
        //logger.info("size of hash: {}", size)
        
        for (var i = 0; i < size; i++) {
            //result.fastASetCheckNSMutableString(input.getRuntime(), input.unmarshalObject(), input.unmarshalObject())
            let key = unmarshalRubyType()!
            //logger.info("parsed key: {}", key)
            let value = unmarshalRubyType()
            //logger.info("parsed value: {}", value)
            if let value = value {
                hash.updateValue(value, forKey: key)
            } else {
                break
            }
        }
        return hash
    }
    
    func unmarshalRubyObject() -> RubyObject {
        // name of object should be a symbol? (not totally sure)
        /*if (!(objectName instanceof RubySymbol)) {
        throw IOException("Expected symbol for object name but was something else intead")
        }*/
        if let objectName = unmarshalRubyType() as? RubySymbol {
            var ro = RubyObject(name: objectName)
            // unmarshal its variables!
            unmarshalRubyObjectVariables(&ro)
            return ro
        }
        
        return RubyObject(name: RubySymbol(name: ""))
    }
    
    
    func unmarshalRubyObjectVariables(inout ro: RubyObject) {
        var count = readInt()!
        
        for (var i = 0; i < count; i++) {
            // name of variable (should always be a symbol)
            let name = unmarshalRubyType() as RubySymbol
            
            // value of variable
            let value = unmarshalRubyType()
            
            ro.put(name, value: value)
        }
    }
}
