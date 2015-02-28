
//
//  RubyBignum.swift
//  SwiftMarshal
//
//  Created by Jonas Zaugg on 25.02.15.
//  Copyright (c) 2015 Jonas Zaugg. All rights reserved.
//

import Foundation

/**
* http://www.ruby-doc.org/docs/ProgrammingRuby/html/tut_stdtypes.html
*
* Ruby supports integers and floating point numbers. Integers can be any length
* (up to a maximum determined by the amount of free memory on your system).
* Integers within a certain range (normally -230 to 230-1 or -262 to 262-1) are
* held internally in binary form, and are objects of class Fixnum. Integers
* outside this range are stored in objects of class Bignum (currently implemented
* as a variable-length set of short integers). This process is transparent,
* and Ruby automatically manages the conversion back and forth.
*
* Fixnum 8
* Fixnum 64
* Fixnum 4096
* Fixnum 16777216
* Fixnum 4294967296 (2^32, or unsigned int which should be a long in Java)
* Fixnum 1152921504606846976 (2^60)
* Fixnum 2305843009213693952 (2^61)
* Fixnum 4611686018427387903 (2^62 - 1) (max Fixnum in Ruby 1.8)
* Bignum 4611686018427387904 (2^62)
* Bignum 79228162514264337593543950336
* Bignum 6277101735386680763835789423207666416102355444464034512896
*
* @author mfizz
*/
public class RubyBignum: RubyType {
    
    private final var value:NSDecimalNumber
    
    public init(value:NSDecimalNumber) {
        self.value = value;
    }
    
    public func getValue() -> NSDecimalNumber {
        return value
    }
    
    public override func inspect(inout sb: NSMutableString, indent: Int) {
        sb.append(value.description)
    }
}