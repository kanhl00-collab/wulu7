# Adding Floating-Point Numbers to P0



## 1. Project Description
### 1.1 Research Scope and Challenge

This project is a requirement for the McMaster University course COMPSCI 4TB3. The aim of the project is to extend the programming language P0 developed by the instructor Dr. Sekerinksi. We are interested in the implementation of P0 types, and for our project we are adding P0 support for floating point arithmetic. 

Our research question is "How can floating point values be implemented in the P0 language?". Support for floating point values are included in most programming and assembly languages, so there are plenty of examples where floating point values have been implemented in compilers. The target language for our P0 compiler is WebAssembly (WASM) which provides support for 64bit and 32bit floating point values by default. All of the operations supported by WASM for integers also support floating point values.

The language of P0 is written in four modules; parser, scanner, symbol table, and code generator. The parser module parses the source text, type-checks it, evaluates constant expressions, and generates the target code. The scanner reads characters from the source text and provides the next symbol to the parser. The symbol table stores all the currently valid declarations. Lastly, the procedures for generating the code for expressions, statements, variable declarations, and procedure declarations, are provided by the code generator. Implementing floating point values will require extensions to the syntax for all four modules. 

Firstly, the parser module specifies the production for numbers 'number → digit {digit}', and will need to recognize the floating point values, such as 'float → digit {digit} "." digit {digit}' Secondly, the scanner will need to identify the "." character to distinguish floats. Next, the symbol table will need to recognize 'float' as a class of allowable types. Lastly, the code generator module will need to specify the size of a float in bytes inside a float generator function. In our floating point implementation we will allow both 32bit and 64bit precisions, float and double. 

We will also implement P0 support for converting from floating point to integer, and vice versa. We will need to implement new syntax for conversions from scratch, as part of the parser and scanner modules. The conversion syntax will be explicit using a new production from the keyword non-terminal of the grammar. We will also implement the unicode floating point operators of square-root, floor, ceiling, and absolute value. We will also implement binary operators, for max and min, and n-ary predicate operators, for sum and average.   

### 1.2 Testing Method and Plan

Our project will use Jupyter Notebook for the implementation and testing. We will utilize the four modules of the initial P0 compiler 
and extend the modules by adding two new type (float, double) and the corresponding expressions.

* **Testing Method**

   Based on the four main modules for the P0 compiler, we will do unit testing for the modules of scanner(SC), symbol table(ST), 
   code generator(SGwat) and parser(P0) respectively. So we will create 
   normal test cases as well as edge cases to test the code we add to the four modules respectively. Finally, we will create a integration testing 
   file to compile some complicated P0 programs. 

   We have six groups of problems based on the research scope and we will implement them step by step, thus we will follow the test-driven development process 
   to add testing cases and code for each problems. 
* **Testing Plan**
 
	| Implementation                                        | Testing Method                         | Some Important Testing Cases        |                                                                                                  
	| -----------------                                     | ------------------------               | ------------------- |
	| Add double type                                       | Symbol Table Unit testing              |   Type with the name = double and value = <class 'ST.Double'> should be added to the symbol table |
    | Add double type                                       | Scanner Unit testing                   |   Raise error for numbers which is not in the range of 64 bits floating point number |	
    | Add double type                                       | Code Generator Unit testing            |   Correct type generation for variables, assignment, operations and relations |
    | Basic arithmetic operations                           | P0 Parsing Unit testing                |   Operands of arithmetic operators with different type - double and integer |		
    | SIMD instructions                                     | P0 Parsing Unit testing                |   Raise error for adding two arrays of double type with different size, check the result of adding two arrays, subtraction between arrays , multiplying/dividing an array with an integer or double number|
	| Add float type                                        | Symbol Table Unit testing              |   Type with the name = float and value = <class 'ST.Float'> should be added to the symbol table |
    | Add float type                                        | Scanner Unit testing                   |   Raise error for number which is not in the range of 32 bits floating point number |	
    | Add float type                                        | Code Generator Unit testing            |   Correct type generation for variables, assignment, operations and relations |
	| Conversions between types of integer, float and double| Scanner, Generator and P0 Parsing Unit testing    |   Raise error for converting larger types to smaller type implicitly; The precision of an operator with implicit conversions |
	| Unicode symbols                                       | Scanner, Generator and P0 Parsing Unit testing    |  At least one testing case for each Unicode symbol |
	| Operator for max and min                              | Scanner, Generator and P0 Parsing Unit testing    |  At least one testing case for max and min  |
	| Quantified expressions                                | Scanner, Generator and P0 Parsing Unit testing    |  At least one testing case for each quantified expression  |	
	| WebAssembly Testing                                   | Integration Testing                               |  Three to Five programs with procedures to test type conversions, operations and relations  |

### 1.3 Documentation
We will keep the initial P0 compiler code without the original documentation. All the documents will be added in the markdown cells. 
In the front of each module file, we will give an overview about the modification that we make. For example, In P0 module, 
we will state the modifications to the grammar and the corresponding changes in procedures. In the module of symbol table,
we will state the new added symbols and the new added classes for the float type and double type. In the module of scanner, we
will show how many new keywords and symbols are added. For the code generator, we will explain how many new procedure are added and how many procedure are
modified by adding the types of f32 and f64.

Furthermore, for each new procedure and modified block, we will document the design decisions and the function details in the markdown cells above the code cells. And also, we will add some 
inline comments for the meaningful modification and the complicated code. 

### 1.4 Insight
Through this project, we hope to have a deep understanding about the compiling process from plain code to executable program. 
In future, we can distinguish clearly that the causes for compiling errors and run-time errors after we implement the scanner and parser with type checking.
Furthermore, we will do some research about floating point numbers which will enhance our understanding about the problems such as comparing floating point nunbers, 
the precision of real numbers and type conversions in different programming languages. 


## 2. Resources

[1] “Python 3.9.2 Documentation.” 3.9.2 Documentation, docs.python.org/3/.

[2] Sekerinski, Emil. “Construction of a Parser.”

[3] “WebAssembly Specification.” WebAssembly Specification - WebAssembly 1.1, webassembly.github.io/spec/core/.



## 3. Division of Work
<table>
    <tr>
        <th>Week</th><th>Work</th><th>Who</th>
    </tr>
    <tr>
        <td rowspan="3">Week of March 8</td><td>research scope and challenges</td><td>Samuel</td>
    </tr>
    <tr>
        <td>testing method and plan, documentation, insight</td><td>Fang</td>
    </tr>
    <tr>
        <td>resources, division of work, weekly schedules</td><td>Hailan</td>
    </tr>
	<tr>
        <td rowspan="3">Week of March 15</td><td>double: scanner, code generator</td><td>Hailan</td>
    </tr>
    <tr>
        <td>double: parser, symbol table</td><td>Fang</td>
    </tr>
    <tr>
        <td>basic operations</td><td>Samuel</td>
    </tr>
	<tr>
        <td rowspan="2">Week of March 22</td><td>float: scanner, parser, code generator, symbol table</td><td>Hailan</td>
    </tr>
    <tr>
        <td>SIMD instructions</td><td>Fang, Samuel</td>
    </tr>
		<tr>
        <td rowspan="3">Week of March 29</td><td>conversion</td><td>Fang</td>
    </tr>
    <tr>
        <td>Unicode symbols</td><td>Samuel</td>
    </tr>
    <tr>
        <td>max and min operators</td><td>Hailan</td>
    </tr>
		<tr>
        <td>Week of April 5</td><td>integration testing</td><td>Fang, Hailan, Samuel</td>
    </tr>
</table>

## 4. Weekly Schedule
   Week            |   Work                 
:----------------: |----------------------- 
March 10           | project plan
Week of March 15 |  implement double type, +, -, *, /, and unit testing
Week of March 22 | implement float type, SIMD instruction, and unit testing
Week of March 29 | implement conversions between integers and floating point values, unicode floating point operators for square-root, floor, ceiling, and absolute value,  min, max, sum, avg operators, and unit testing
Week of April 5 | integration testing
April 12         | complete project and presentation
April 15/16      | project presentation


